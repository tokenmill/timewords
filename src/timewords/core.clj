(ns timewords.core
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as jco]
            [timewords.standard.standard :as standard]
            [timewords.fuzzy.fuzzy :as fuzzy]
            [timewords.utils.cleaner :refer [clean]])
  (:import (java.util Date)
           (org.joda.time DateTime))
  (:gen-class
    :name lt.tokenmill.timewords.Timewords
    :methods [[parse [java.lang.String] java.util.Date]
              [parse [java.lang.String java.lang.String] java.util.Date]
              [parse [java.lang.String java.lang.String java.util.Date] java.util.Date]]))

(defn parse
  "Given a string that represents date, returns a java.util.Date object.
  Cases that are not handled return nil.
  Second (optional) parameter must be a language code, e.g. `en`.
  Third (optional) parameter is a document-time which must be nil or java.util.Date.
  `document-time` is used to parse relative timewords like 'yesterday'."
  ^Date [^String date-string & [^String language ^Date document-time]]
  (try
    (let [^String language (or language "en")
          ^DateTime document-time (or (jco/from-date document-time) (joda/now))]
      (when (not (s/blank? date-string))
        (let [clean-date-string (clean date-string)]
          (jco/to-date
            (or
              (standard/to-date clean-date-string language document-time)
              (fuzzy/to-date clean-date-string language document-time))))))
    (catch Exception e
      (prn (str "Caught exception: '" (.getMessage e) "', while parsing timeword '" date-string "'."))
      nil)))

(defn -parse
  ^Date [_ ^String date-string & [^String language ^Date document-time]]
  (parse date-string language document-time))
