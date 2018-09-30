(ns timewords.fuzzy.en.relative
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc])
  (:import (org.joda.time DateTime)))

(def special-to-normal {"a sec" "1 second"
                        "a second" "1 second"
                        "a min" "1 minute"
                        "a minute" "1 minute"
                        "a hour" "1 hour"
                        "an hour" "1 hour"
                        "a day" "1 day"
                        "a week" "7 days"
                        "a month" "1 month"
                        "a year" "1 year"
                        "yesterday" "1 day ago"
                        "tomorrow" "1 day from now"
                        "this week" "last monday"
                        "last month" "1 month ago"
                        "previous month" "1 month ago"
                        "next month" "1 month from now"
                        "this month" "0 months ago"
                        "last year" "1 year ago"
                        "next year" "1 year from now"
                        "this year" "0 years ago"
                        "previous year" "1 year ago"
                        "now" "0 seconds ago"
                        "today" "0 days ago"})

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


(defn ^DateTime floor
  "Floors the given date-time dt to the given time unit dt-fn,
  e.g. (floor (now) hour) returns (now) for all units
  up to and including the hour"
  [^DateTime dt dt-fn]
  (let [dt-fns [joda/year joda/month joda/day joda/hour joda/minute get-seconds joda/milli]]
    (apply joda/date-time
           (map apply
                (concat (take-while (partial not= dt-fn) dt-fns) [dt-fn])
                (repeat [dt])))))

(defn parse-relative-time [cleaned-timeword document-time plus-or-minus]
  (let [amount (Integer/parseInt (re-find #"\d+" cleaned-timeword))]
    (cond
      (re-find #"\d+s$|sec|secs|second|seconds" cleaned-timeword) (-> (plus-or-minus document-time (joda/millis (* amount 1000))) (floor get-seconds))
      (re-find #"\d+m$|min|mins|minute|minutes" cleaned-timeword) (-> (plus-or-minus document-time (joda/minutes amount)) (floor joda/minute))
      (re-find #"\d+h$|hour|hr|hours|hrs" cleaned-timeword) (-> (plus-or-minus document-time (joda/hours amount)) (floor joda/hour))
      (re-find #"\d+d$|day|days" cleaned-timeword) (-> (plus-or-minus document-time (joda/days amount)) (floor joda/day))
      (re-find #"\d+w$|week|weeks" cleaned-timeword) (-> (plus-or-minus document-time (joda/days (* 7 amount))) (floor joda/day))
      (re-find #"month|months" cleaned-timeword) (-> (plus-or-minus document-time (joda/months amount)) (floor joda/month))
      (re-find #"year|years" cleaned-timeword) (-> (plus-or-minus document-time (joda/years amount)) (floor joda/year))
      :else nil)))

(defn parse-some-time-ago
  "Handle strings like '32 mins ago'."
  ([s] (parse-some-time-ago s (joda/now)))
  ([s document-time]
   (let [cleaned-timeword (-> s (s/replace "ago" "") (s/trim))]
     (if (re-find #"\d+" s)
       (parse-relative-time cleaned-timeword document-time joda/minus)
       (when-let [normalized (get special-to-normal cleaned-timeword)]
         (parse-some-time-ago (str normalized " ago") document-time))))))

(defn parse-some-time-from-now
  "Handle strings like '32 mins from now'."
  ([s] (parse-some-time-from-now s (joda/now)))
  ([^String s ^DateTime document-time]
   (let [cleaned-timeword (-> s (s/replace "from now" "") (s/trim))]
     (if (re-find #"\d+" s)
       (parse-relative-time cleaned-timeword document-time joda/plus)
       (when-let [normalized (get special-to-normal cleaned-timeword)]
         (parse-some-time-from-now (str normalized " from now") document-time))))))

(defn parse-relative-weekday
  [^String s ^DateTime document-time plus-or-minus]
  (let [required-weekday (cond
                           (re-find #"monday" s) 1
                           (re-find #"tuesday" s) 2
                           (re-find #"wednesday" s) 3
                           (re-find #"thursday" s) 4
                           (re-find #"friday" s) 5
                           (re-find #"saturday" s) 6
                           (re-find #"sunday" s) 7)
        in-required-weekday (if (= required-weekday (joda/day-of-week document-time))
                              (plus-or-minus document-time (joda/days 7))
                              (loop [datetime document-time]
                                (if (= required-weekday (joda/day-of-week datetime))
                                  datetime
                                  (recur (plus-or-minus datetime (joda/days 1))))))]
    (floor in-required-weekday joda/day)))

(defn parse-last-weekday [s document-time]
  (parse-relative-weekday s document-time joda/minus))

(defn parse-next-weekday [s document-time]
  (parse-relative-weekday s document-time joda/plus))

(defn parse-relative-month [s document-time joda-minus-or-plus]
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
                         (re-find #"december" s) 12)
        datetime-with-adjusted-month (if (= required-month (joda/month document-time))
                                       (joda-minus-or-plus document-time (joda/months 12))
                                       (loop [datetime document-time]
                                         (if (= required-month (joda/month datetime))
                                           datetime
                                           (recur (joda-minus-or-plus datetime (joda/months 1))))))]
    (floor datetime-with-adjusted-month joda/month)))

(defn parse-last-month [s document-time]
  (parse-relative-month s document-time joda/minus))

(defn parse-next-month [s document-time]
  (parse-relative-month s document-time joda/plus))

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
   (let [s (or (get special-to-normal s) s)]
     (date-to-str-seq
       (cond
         (re-find #"ago" s) (-> s (parse-some-time-ago))
         (re-find #"from now" s) (-> s (parse-some-time-from-now))
         (re-find #"^(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$" s) (-> s (parse-last-weekday document-time))
         (re-find #"last (monday|tuesday|wednesday|thursday|friday|saturday|sunday)" s) (-> s (parse-last-weekday document-time))
         (re-find #"this (monday|tuesday|wednesday|thursday|friday|saturday|sunday)" s) (-> s (parse-next-weekday document-time))
         (re-find #"next (monday|tuesday|wednesday|thursday|friday|saturday|sunday)" s) (-> s (parse-next-weekday document-time))
         (re-find #"last (january|february|march|april|may|june|july|august|september|october|november|december)" s) (-> s (parse-last-month document-time))
         (re-find #"^in\s(early|late)?\s?(january|february|march|april|may|june|july|august|september|october|november|december)$" s) (-> s (parse-last-month document-time))
         (re-find #"this (january|february|march|april|may|june|july|august|september|october|november|december)" s) (-> s (parse-next-month document-time))
         (re-find #"next (january|february|march|april|may|june|july|august|september|october|november|december)" s) (-> s (parse-next-month document-time))
         (re-find #"last (spring|summer|autumn|fall|winter)" s) (-> s (parse-last-season document-time))
         :else nil)))))