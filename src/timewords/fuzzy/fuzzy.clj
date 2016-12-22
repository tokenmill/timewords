(ns timewords.fuzzy.fuzzy
  (:require [clj-time.core :refer [date-time]]
            [timewords.fuzzy.en.en :as en]
            [timewords.fuzzy.lt.lt :as lt])
  (:import (org.joda.time DateTime)))

(defn to-date
  "Parses string dates into components represented by numeric values:
    1-12 for months
    1-31 for days
    19??-20?? for years.

    Dispatches parsing by language. Default language is en."
  ^DateTime [^String fuzzy-date & [^String language ^DateTime document-time]]
  (cond
    (= language "en") (if-let [date-parts (en/parse-date fuzzy-date document-time)]
                        (apply date-time (map #(Integer/parseInt %) date-parts)))
    (= language "lt") (if-let [date-parts (lt/parse-date fuzzy-date document-time)]
                        (apply date-time (map #(Integer/parseInt %) date-parts)))
    :else nil))
