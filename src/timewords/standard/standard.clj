(ns timewords.standard.standard
  (:require [clojure.string :as s]
            [clj-time.core :as t]
            [clj-time.coerce :refer [from-date]]
            [timewords.standard.formats :as formats]))

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

(defn multi-format-parse [^String date]
  (->> date
       (formats/parse)
       (map from-date)
       ; for cases where multiple patterns match
       (sort)
       (reverse)
       (first)))

(defn to-date [^String date]
  (when-not (empty? date)
    (-> date
        clean-date-string
        normalize-date-parts
        multi-format-parse)))
