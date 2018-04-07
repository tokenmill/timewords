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

(defn parse-today-time [s document-time]
  (let [[hh mm] (map #(Integer/parseInt %) (re-seq #"\d{2}" s))]
    (joda/floor
      (-> (joda/floor document-time joda/day)
          (joda/plus (joda/hours hh))
          (joda/plus (joda/minutes mm)))
      joda/minute)))

(defn parse-hours-ago [s document-time]
  (prn s document-time)
  (let [hours (Integer/parseInt (re-find #"\d+" s))]
    (joda/floor
      (joda/minus document-time (joda/hours hours))
      joda/hour)))

(defn parse-weeks-ago [s document-time]
  (let [days (Integer/parseInt (re-find #"\d+" s))]
    (joda/floor
      (joda/minus document-time (joda/days (* 7 days)))
      joda/day)))

(defn parse-relative-date
  [^String s ^DateTime document-time]
  (date-to-str-seq
    (cond
     (re-matches #"prieš \d+ d.?" s) (parse-days-ago s document-time)
     (re-matches #"\d+ d.? prieš" s) (parse-days-ago s document-time)
     (re-matches #"\d+ sav.? prieš" s) (parse-weeks-ago s document-time)
     (re-matches #"\d+ val.? prieš" s) (parse-hours-ago s document-time)
     (re-matches #"šiandien \d{2}:\d{2}" s) (parse-today-time s document-time)
     :else nil)))