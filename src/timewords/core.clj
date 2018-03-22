(ns timewords.core
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as jco]
            [timewords.standard.standard :as standard]
            [timewords.fuzzy.fuzzy :as fuzzy])
  (:import (java.util Date)
           (org.joda.time DateTime))
  (:gen-class
    :name lt.tokenmill.timewords.Timewords
    :methods [[parse [java.lang.String] java.util.Date]
              [parse [java.lang.String java.util.Date] java.util.Date]
              [parse [java.lang.String java.util.Date java.lang.String] java.util.Date]]))

(defn parse
  "Given a string that represents date, returns a java.util.Date object.
  Cases that are not handled return nil.
  Second (optional) parameter must be a language code, e.g. `en`.
  Third (optional) parameter is a document-time which must be nil or java.util.Date.
  `document-time` is used to parse relative timewords like 'yesterday'."
  ^Date [^String date-string & [^Date document-time ^String language]]
  (try
    (when-not (or (nil? document-time) (= (type (Date.)) (type document-time)))
      (throw (Exception. "document-time is not either nil or java.util.Date.")))
    (when-not (or (nil? language) (string? language))
      (throw (Exception. "language parameter is not either nil or java.lang.String.")))
    (let [^String language (or language "en")
          ^DateTime document-time (or (DateTime. document-time) (joda/now))]
      (when (not (s/blank? date-string))
        (jco/to-date
          (or
            (standard/to-date date-string language document-time)
            (fuzzy/to-date date-string language document-time)))))
    (catch Exception e
      (prn (str "Caught exception: '" (.getMessage e) "', while parsing timeword '" date-string
                "' with language '" language "' and with document-time '" document-time "'."))
      nil)))

(defn -parse
  ^Date [_ ^String date-string & [^Date document-time ^String language]]
  (parse date-string document-time language))
