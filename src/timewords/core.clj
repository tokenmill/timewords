(ns timewords.core
  (:require [clojure.string :as s]
            [clj-time.core :refer [date-time year month day]]
            [clj-time.coerce :as jco]
            [timewords.standard.standard :as standard]
            [timewords.fuzzy.fuzzy :as fuzzy]
            [timewords.utils.cleaner :refer [clean]])
  (:import (java.util Date))
  (:gen-class
    :name lt.tokenmill.timewords.Timewords
    :methods [[parse [String] java.util.Date]]))

(defn parse
  "Given a string that represents date, returns a java.util.Date object.
  Cases that are not handled return nil."
  ^Date [^String date-string]
  (try
    (when (not (s/blank? date-string))
      (let [clean-date-string (clean date-string)]
        (jco/to-date
          (or
            (standard/to-date clean-date-string)
            (fuzzy/to-date clean-date-string)))))
    (catch Exception e
      (prn (str "Caught exception: '" (.getMessage e) "', while parsing timeword '" date-string "'."))
      nil)))

(defn -parse [_ date-string] (parse date-string))
