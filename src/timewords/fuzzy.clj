(ns timewords.fuzzy
  (:require [clj-time.core :refer [date-time]]
            [timewords.en :as en]))

(defn to-date
  "Parses string dates into components represented by numeric values:
    1-12 for months
    1-31 for days
    19??-20?? for years.

  *** TODO this part is not done! ***

  If we get fuzzy date like 'yesterday' 'last year' numeric value is represented
  by date math:
    -/+N
  - if we go to date in past
  + if we go to the future"
  [fuzzy-date]
  (if-let [date-parts (en/parse-date fuzzy-date)]
    (apply date-time (map #(Integer/parseInt %) date-parts))))