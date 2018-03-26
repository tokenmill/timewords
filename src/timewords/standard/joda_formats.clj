(ns timewords.standard.joda-formats
  (:require [clojure.string :as str])
  (:import (java.util Locale TimeZone Date)
           (org.joda.time.format ISODateTimeFormat DateTimeFormat DateTimeFormatter)
           (org.joda.time DateTime LocalDateTime)
           (org.joda.time.chrono LenientChronology ISOChronology)))

(def lt-locale (Locale/forLanguageTag "lt"))
(def en-locale Locale/ENGLISH)

(def lenient-chronology (LenientChronology/getInstance (ISOChronology/getInstance)))

(defn fmt [pattern & [locale default-year lenient]]
  (cond-> (DateTimeFormat/forPattern pattern)
          locale (.withLocale locale)
          default-year (.withDefaultYear default-year)
          lenient (.withChronology lenient-chronology)))

(defn lt-formatters []
  [(ISODateTimeFormat/dateTimeParser)
   (fmt "MMMM dd" lt-locale (.getYear (DateTime.)))
   (fmt "MMMM dd 'd.' HH:mm" lt-locale (.getYear (DateTime.)))
   (fmt "yyyy/MM/dd HH:mm" lt-locale)
   (fmt "yyyy MM dd HH:mm" lt-locale)
   (fmt "yyyy MMMM dd" lt-locale)
   (fmt "yyyy 'metų' MMMM dd" lt-locale)
   (fmt "yyyy MMMM dd HH:mm" lt-locale)
   (fmt "yyyy MMMM dd HH:mm:ss" lt-locale)
   (fmt "yyyy 'm.' MMMM dd 'd.' HH:mm" lt-locale)
   (fmt "yyyy MMMM dd'd.' HH:mm" lt-locale)
   (fmt "yyyy-MM-dd '/' HH:mm" lt-locale)
   (fmt "yyyy.MM.dd HH:mm" lt-locale)
   (fmt "HH:mm yyyy.MM.dd" lt-locale)
   (fmt "MMMM dd, yyyy" lt-locale)])

(defn en-formatters []
  [(ISODateTimeFormat/dateTimeParser)
   (fmt "yyyy-MM-dd HH:mm")
   (fmt "yyyy-MM-dd HH:mm:ss")
   (fmt "yyyy-MM-dd, HH:mm")
   (fmt "dd/MM/yy HH:mm")
   (fmt "yyyy/MM/dd")
   (fmt "dd/MM/yyyy, HH:mm")
   (fmt "dd/MM/yyyy")
   (fmt "MM/dd/yyyy")
   (fmt "MMMM yyyy")
   (fmt "dd'st' MMMM yyyy")
   (fmt "dd'nd' MMMM yyyy")
   (fmt "dd'rd' MMMM yyyy")
   (fmt "dd'th' MMMM yyyy")
   (fmt "EEE, dd'st' MMMM yyyy")
   (fmt "EEE, dd'nd' MMMM yyyy")
   (fmt "EEE, dd'rd' MMMM yyyy")
   (fmt "EEE, dd'th' MMMM yyyy")
   (fmt "EEE MMMM dd, yyyy")
   (fmt "EEE MMMM dd, yyyy h:mma" en-locale)
   (fmt "EEE MMMM dd, yyyy h:mma z" en-locale)
   (fmt "EEE MMMM dd yyyy HH:mm 'UTC'Z" en-locale)
   (fmt "dd MMM yyyy HH:mm")
   (fmt "MMM dd, yyyy HH:mm z")
   (fmt "dd MMM yyyy")
   (fmt "MMMM dd, yyyy h:mma" en-locale)
   (fmt "EEE, MMMM dd, yyyy" en-locale)
   (fmt "yyyy-MM-dd'T'HH:mm:ssZ")
   (fmt "MMMM dd, yyyy h:mm a")
   (fmt "MMMM dd, yyyy")
   (fmt "MMMM. dd, yyyy")
   (fmt "EEE MMMM dd HH:mm:ss z yyyy")
   (fmt "yyyy-MM-dd HH:mm:ss z")
   (fmt "EEE dd MMMM yyyy HH:mm")
   (fmt "yyyy/MM/dd HH:mm:ss")
   (fmt "yyyy-MM-dd HH:mm:ss.S")
   (fmt "MM/dd/yyyy HH:mm")
   (fmt "MM/dd/yyyy HH:mm:ss a z")
   (fmt "MMMM dd, yyyy, h:mm a")
   (fmt "MMMM dd, yyyy - h:mma")
   (fmt "EEE, MMMM dd, yyyy, h:mm a")
   (fmt "EEEE dd MMMM yyyy HH.mm z")
   (fmt "yyyy-MM-dd '/' HH:mm")
   (fmt "yyyy.MM.dd HH:mm" lt-locale)
   (fmt "HH:mm yyyy.MM.dd")])

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
      (str/replace "gruodžio" "gruodis")
      (str/replace "pst" "PST")
      (str/replace "edt" "EDT")
      (str/replace "bst" "GMT")
      (str/replace " gmt" " GMT")
      (str/replace "p.m." "pm")
      (str/replace "utc" "UTC")
      (str/replace "est" "EST")))

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
                            (.toDate (if (and (not (nil? document-time))
                                              (not= (.getYear document-time) (.getYear (DateTime.))))
                                       (.minusYears pdate (- (.getYear pdate) (.getYear document-time)))
                                       pdate)
                                     (TimeZone/getTimeZone "GMT")))
                          (catch Exception _ nil))]
           :when parsed] parsed))))
