(ns timewords.fuzzy.en.relative
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc])
  (:import (org.joda.time DateTime)))

(defn date-to-str-seq
  [joda-datetime]
  (when (not (nil? joda-datetime))
    (map str [(joda/year joda-datetime)
              (joda/month joda-datetime)
              (joda/day joda-datetime)
              (joda/hour joda-datetime)
              (joda/minute joda-datetime)
              (-> joda-datetime (tc/to-long) (mod 60000) (quot 1000)) ;to avoid using joda/seconds
              ])))

(defn parse-some-time-ago
  "Handle strings like '32 mins ago'."
  ([s] (parse-some-time-ago s (joda/now)))
  ([s document-time]
   (let [cleaned-timeword (-> s (s/replace "ago" "") (s/trim))]
     (if (re-find #"\d+" s)
       (let [amount (Integer/parseInt (re-find #"\d+" s))]
         ; normal cases
         (cond
           (re-find #"\d+s$|sec|secs|second|seconds" cleaned-timeword) (joda/minus document-time (joda/millis (* amount 1000)))
           (re-find #"\d+m$|min|mins|minute|minutes" cleaned-timeword) (joda/minus document-time (joda/minutes amount))
           (re-find #"\d+h$|hour|hours" cleaned-timeword) (joda/minus document-time (joda/hours amount))
           (re-find #"\d+d$|day|days" cleaned-timeword) (joda/minus document-time (joda/days amount))
           (re-find #"\d+w$|week|weeks" cleaned-timeword) (joda/minus document-time (joda/days (* 7 amount)))
           (re-find #"month|months" cleaned-timeword) (joda/minus document-time (joda/months amount))
           (re-find #"year|years" cleaned-timeword) (joda/minus document-time (joda/years amount))
           :else nil))
       (cond
         ; special cases
         (= "a sec" cleaned-timeword) (joda/minus document-time (joda/millis 1000))
         (= "a second" cleaned-timeword) (joda/minus document-time (joda/millis 1000))
         (= "a min" cleaned-timeword) (joda/minus document-time (joda/minutes 1))
         (= "a minute" cleaned-timeword) (joda/minus document-time (joda/minutes 1))
         (= "a hour" cleaned-timeword) (joda/minus document-time (joda/hours 1))
         (= "an hour" cleaned-timeword) (joda/minus document-time (joda/hours 1))
         (= "a day" cleaned-timeword) (joda/minus document-time (joda/days 1))
         (= "a week" cleaned-timeword) (joda/minus document-time (joda/days 7))
         (= "a month" cleaned-timeword) (joda/minus document-time (joda/months 1))
         (= "a year" cleaned-timeword) (joda/minus document-time (joda/years 1))
         :else nil)))))

(defn parse-some-time-from-now
  "Handle strings like '32 mins from now'."
  ([s] (parse-some-time-from-now s (joda/now)))
  ([^String s ^DateTime document-time]
   (let [cleaned-timeword (-> s (s/replace "from now" "") (s/trim))]
     (if (re-find #"\d+" s)
       (let [amount (Integer/parseInt (re-find #"\d+" s))]
         ; normal cases
         (cond
           (re-find #"\d+s$|sec|secs|second|seconds" cleaned-timeword) (joda/plus document-time (joda/millis (* amount 1000)))
           (re-find #"\d+m$|min|mins|minute|minutes" cleaned-timeword) (joda/plus document-time (joda/minutes amount))
           (re-find #"\d+h$|hour|hours" cleaned-timeword) (joda/plus document-time (joda/hours amount))
           (re-find #"\d+d$|day|days" cleaned-timeword) (joda/plus document-time (joda/days amount))
           (re-find #"\d+w$|week|weeks" cleaned-timeword) (joda/plus document-time (joda/days (* 7 amount)))
           (re-find #"month|months" cleaned-timeword) (joda/plus document-time (joda/months amount))
           (re-find #"year|years" cleaned-timeword) (joda/plus document-time (joda/years amount))
           :else nil))
       (cond
         ; special cases
         (= "a sec" cleaned-timeword) (joda/plus document-time (joda/millis 1000))
         (= "a second" cleaned-timeword) (joda/plus document-time (joda/millis 1000))
         (= "a min" cleaned-timeword) (joda/plus document-time (joda/minutes 1))
         (= "a minute" cleaned-timeword) (joda/plus document-time (joda/minutes 1))
         (= "a hour" cleaned-timeword) (joda/plus document-time (joda/hours 1))
         (= "an hour" cleaned-timeword) (joda/plus document-time (joda/hours 1))
         (= "a day" cleaned-timeword) (joda/plus document-time (joda/days 1))
         (= "a week" cleaned-timeword) (joda/plus document-time (joda/days 7))
         (= "a month" cleaned-timeword) (joda/plus document-time (joda/months 1))
         (= "a year" cleaned-timeword) (joda/plus document-time (joda/years 1))
         :else nil)))))

(defn parse-last-weekday
  ([^String s] (parse-last-weekday s (joda/now)))
  ([^String s ^DateTime document-time]
   (let [required-weekday (cond
                            (re-find #"monday" s) 1
                            (re-find #"tuesday" s) 2
                            (re-find #"wednesday" s) 3
                            (re-find #"thursday" s) 4
                            (re-find #"friday" s) 5
                            (re-find #"saturday" s) 6
                            (re-find #"sunday" s) 7)]
     (joda/with-time-at-start-of-day
       (if (= required-weekday (joda/day-of-week document-time))
         (joda/minus document-time (joda/days 7))
         (loop [datetime document-time]
           (if (= required-weekday (joda/day-of-week datetime))
             datetime
             (recur (joda/minus datetime (joda/days 1))))))))))

(defn parse-next-weekday
  ([^String s] (parse-next-weekday s (joda/now)))
  ([^String s ^DateTime document-time]
   (let [required-weekday (cond
                            (re-find #"monday" s) 1
                            (re-find #"tuesday" s) 2
                            (re-find #"wednesday" s) 3
                            (re-find #"thursday" s) 4
                            (re-find #"friday" s) 5
                            (re-find #"saturday" s) 6
                            (re-find #"sunday" s) 7)]
     (joda/with-time-at-start-of-day
       (if (= required-weekday (joda/day-of-week document-time))
         (joda/plus document-time (joda/days 7))
         (loop [datetime document-time]
           (if (= required-weekday (joda/day-of-week datetime))
             datetime
             (recur (joda/plus datetime (joda/days 1))))))))))

(defn parse-last-month
  ([^String s] (parse-last-month s (joda/now)))
  ([^String s ^DateTime document-time]
   (let [required-month (cond
                          (re-find #"january" s) 1
                          (re-find #"february" s) 2
                          (re-find #"march" s) 3
                          (re-find #"april" s) 4
                          (re-find #"may" s) 5
                          (re-find #"june" s) 6
                          (re-find #"july" s) 7
                          (re-find #"august" s) 8
                          (re-find #"september" s) 9
                          (re-find #"october" s) 10
                          (re-find #"november" s) 11
                          (re-find #"december" s) 12)]
     (if (= required-month (joda/month document-time))
       (joda/minus document-time (joda/months 12))
       (loop [datetime document-time]
         (if (= required-month (joda/month datetime))
           datetime
           (recur (joda/minus datetime (joda/months 1)))))))))

(defn parse-last-season
  ([^String s] (parse-last-season s (joda/now)))
  ([^String s ^DateTime document-time]
   (let [required-season (cond
                           (re-find #"spring" s) 1
                           (re-find #"summer" s) 2
                           (re-find #"autumn" s) 3
                           (re-find #"fall" s) 3
                           (re-find #"winter" s) 4)
         month->season (fn [month]
                         (cond
                           (<= 3 month 5) 1
                           (<= 6 month 8) 2
                           (<= 9 month 11) 3
                           (<= 1 month 2) 4
                           (= 12 month) 4))]
     (if (= required-season (month->season (joda/month document-time)))
       (joda/minus document-time (joda/months 12))
       (loop [datetime document-time]
         (if (= required-season (month->season (joda/month datetime)))
           datetime
           (recur (joda/minus datetime (joda/months 1)))))))))

(defn parse-relative-date
  ([^String s] (parse-relative-date s (joda/now)))
  ([^String s ^DateTime document-time]
   (date-to-str-seq
     (cond
       (= "now" s) (-> document-time )
       (= "today" s) (-> document-time )
       (= "yesterday" s) (-> document-time (joda/minus (joda/days 1)))
       (= "tomorrow" s) (-> document-time (joda/plus (joda/days 1)))
       (re-find #"ago" s) (-> s (parse-some-time-ago))
       (re-find #"from now" s) (-> s (parse-some-time-from-now))
       (= "last month" s) (-> document-time (joda/minus (joda/months 1)))
       (re-find #"last (monday|tuesday|wednesday|thursday|friday|saturday|sunday)" s) (-> s (parse-last-weekday document-time))
       (re-find #"next (monday|tuesday|wednesday|thursday|friday|saturday|sunday)" s) (-> s (parse-next-weekday document-time))
       (re-find #"last (january|february|march|april|may|june|july|august|september|october|november|december)" s) (-> s (parse-last-month document-time) )
       (re-find #"last (spring|summer|autumn|fall|winter)" s) (-> s (parse-last-season document-time))
       :else nil))))