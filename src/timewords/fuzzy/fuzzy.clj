(ns timewords.fuzzy.fuzzy
  (:require [clj-time.core :refer [date-time]]
            [timewords.fuzzy.en.en :as en]))

(defn to-date
  "Parses string dates into components represented by numeric values:
    1-12 for months
    1-31 for days
    19??-20?? for years."
  [fuzzy-date]
  (if-let [date-parts (en/parse-date fuzzy-date)]
    (apply date-time (map #(Integer/parseInt %) date-parts))))