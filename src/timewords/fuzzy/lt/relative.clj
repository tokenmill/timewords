(ns timewords.fuzzy.lt.relative
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc])
  (:import (org.joda.time DateTime)))

(defn get-seconds [joda-datetime]
  (-> joda-datetime (tc/to-long) (mod 60000) (quot 1000)))

(defn date-to-str-seq
  [joda-datetime]
  (when (not (nil? joda-datetime))
    (map str [(joda/year joda-datetime)
              (joda/month joda-datetime)
              (joda/day joda-datetime)
              (joda/hour joda-datetime)
              (joda/minute joda-datetime)
              (get-seconds joda-datetime) ;to avoid using joda/seconds
              ])))

(defn parse-days-ago [s document-time]
  (let [days (Integer/parseInt (re-find #"\d+" s))]
    (joda/floor
      (joda/minus document-time (joda/days days))
      joda/day)))

(defn parse-relative-date
  [^String s ^DateTime document-time]
  (date-to-str-seq
    (cond
     (re-matches #"prieš \d+ d." s) (parse-days-ago s document-time)
     :else nil)))