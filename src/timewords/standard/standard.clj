(ns timewords.standard.standard
  (:require [clojure.string :as s]
            [clj-time.coerce :refer [from-date]]
            [timewords.standard.joda-formats :as formats]
            [timewords.standard.utils :as utils])
  (:import (org.joda.time DateTime)
           (java.util Locale)))

(def date-part-normalizations
  {#"(?i)p\.m\." "PM"
   #"(?i)a\.m\." "AM"
   #"([ \d])ET$" "$1 EST"})

(defn normalize-date-parts
  [^String date]
  (reduce
    (fn [date [match replacement]]
      (s/replace date match replacement))
    date
    date-part-normalizations))

(defn clean-date-string [^String date]
  (-> date
      s/trim
      (s/replace #"\s+" " ")))

(defn multi-format-parse [^String date ^String language ^DateTime document-time]
  (let [locale (Locale/forLanguageTag language)]
    (->> (formats/parse date locale document-time)
         (map from-date)
         ; for cases where multiple patterns match
         (sort)
         (reverse)
         (first))))

(defn to-date
  [^String date & [^String language ^DateTime document-time]]
  (when-not (empty? date)
    (-> date
        utils/clean
        clean-date-string
        normalize-date-parts
        (multi-format-parse language document-time))))
