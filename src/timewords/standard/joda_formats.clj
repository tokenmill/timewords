(ns timewords.standard.joda-formats
  (:require [clojure.string :as str])
  (:import (java.util Locale TimeZone Date)
           (org.joda.time.format ISODateTimeFormat DateTimeFormat DateTimeFormatter)
           (org.joda.time DateTime LocalDateTime)))

(def lt-locale (Locale/forLanguageTag "lt"))
(def en-locale Locale/ENGLISH)

(defn fmt [pattern & [locale default-year]]
  (cond-> (DateTimeFormat/forPattern pattern)
          locale (.withLocale locale)
          default-year (.withDefaultYear default-year)))

(defn lt-formatters []
  [(ISODateTimeFormat/dateTimeParser)
   (fmt "MMMM dd" lt-locale (.getYear (DateTime.)))
   (fmt "MMMM dd 'd.' HH:mm" lt-locale (.getYear (DateTime.)))
   (fmt "yyyy/MM/dd HH:mm" lt-locale)
   (fmt "yyyy MMMM dd" lt-locale)
   (fmt "yyyy 'metų' MMMM dd" lt-locale)
   (fmt "yyyy MMMM dd HH:mm" lt-locale)
   (fmt "yyyy MMMM dd HH:mm:ss" lt-locale)
   (fmt "yyyy 'm.' MMMM dd 'd.' HH:mm" lt-locale)])

(defn en-formatters [] [(ISODateTimeFormat/dateTimeParser)])

(def by-locale
  {en-locale (en-formatters)
   lt-locale (lt-formatters)})

(.withLocale (DateTimeFormat/forPattern "yyyy MMMM dd")
             (Locale/forLanguageTag "lt"))

(defn normalize [text]
  (-> text
      (str/lower-case)
      (str/replace "kovo" "kovas")
      (str/replace "vasario" "vasaris")
      (str/replace "balandžio" "balandis")
      (str/replace "gegužės" "gegužė")
      (str/replace "liepos" "liepa")
      (str/replace "rugpjūčio" "rugpjūtis")
      (str/replace "rugsėjo" "rugsėjis")
      (str/replace "spalio" "spalis")
      (str/replace "lapkričio" "lapkritis")
      (str/replace "gruodžio" "gruodis")))

(defn parse
  ([^String text]
   (parse text Locale/ENGLISH nil))
  ([^String text ^Locale locale]
   (parse text locale nil))
  ([^String text ^Locale locale ^DateTime document-time]
   (let [text (normalize text)
         formatters (get by-locale locale)]
     (for [^DateTimeFormatter formatter formatters
           :let [parsed (try
                          (let [^LocalDateTime pdate (.parseLocalDateTime formatter text)]
                            (when (and (not (nil? document-time))
                                       (not= (.getYear document-time) (.getYear (DateTime.))))
                              #_(.setYear pdate (.getYear document-time)))
                            (.toDate pdate (TimeZone/getTimeZone "GMT")))
                          (catch Exception _ nil))]
           :when parsed] parsed))))
