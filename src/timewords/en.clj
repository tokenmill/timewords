(ns timewords.en
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc]))

(def months
  {#"(january)|(jan.)"    "1"
   #"(fabruary)|(feb.)"   "2"
   #"(march)|(mar.)"      "3"
   #"(april)|(apr.)"      "4"
   #"(may)|(may.)"        "5"
   #"(june)|(jun.)"       "6"
   #"(july)|(jul.)"       "7"
   #"(august)|(aug.)"     "8"
   #"(september)|(sep.)"  "9"
   #"(october)|(oct.)"    "10"
   #"(november)|(nov.)"   "11"
   #"(december)|(dec.)"   "12"})

(defn- re [r s] (let [f (re-find r s)] (if (coll? f) (first f) f)))

(defn is-pm?
  "Checks if fuzzy date represents a PM time."
  [fuzzy-date]
  (if (re-find #"(?i)\b(\d{1,2}):\d{2}[\s]?pm" fuzzy-date)
    true
    false))

(defn fix-am-hours
  "Convert civil am hours to military hours."
  [^String am-hour]
  (if am-hour
    (str (let [hour (Integer/parseInt am-hour)]
           (if (= 12 hour)
             0
             hour)))
    nil))

(defn fix-pm-hours
  "Convert civil pm hours to military hours."
  [^long pm-hour]
  (if (< pm-hour 12)
    (+ 12 pm-hour)
    pm-hour))

(defn safe-parse [fuzzy-date] (if fuzzy-date (Integer/parseInt fuzzy-date) 0))

(defn year [fuzzy-date] (first (re-find #"\b(19|20)\d{2}\b" fuzzy-date)))

(defn month [fuzzy-date]
  (if (re-matches #"\b\d{2}/\d{2}/\d{4}\b.*" fuzzy-date)
    (let [date-part (re-find #"\b\d{2}/\d{2}/\d{4}\b" fuzzy-date)
          month-and-day (->> (s/split date-part #"/")
                             (map #(Integer/parseInt %))
                             (filter #(>= 31 %)))]
      (str
        (if (seq (filter #(> 12 %) month-and-day))
          (first (filter #(> 12 %) month-and-day))
          (first month-and-day))))
    (some (fn [[re nr]] (when (re-find re fuzzy-date) nr)) months)))

(defn day [fuzzy-date] (re-find #"\b\d{1,2}\b" fuzzy-date))
(defn hour [fuzzy-date] (if (is-pm? fuzzy-date)
                          (-> (second (re-find #"\b(\d{1,2}):\d{2}[\\b]?" fuzzy-date))
                              (safe-parse)
                              (fix-pm-hours)
                              str)
                          (-> (second (re-find #"\b(\d{1,2})[:\.]\d{2}[\\b]?" fuzzy-date))
                              (fix-am-hours))))
(defn minute [fuzzy-date] (second (re-find #"\b\d{1,2}[:|\.](\d{2})[\\b]?" fuzzy-date)))
(defn zecond [fuzzy-date] (second (re-find #"\b\d{2}:\d{2}:(\d{2})\b" fuzzy-date)))

(defn date-to-str-seq [joda-datetime]
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
  ([s relative-to]
   (let [cleaned-timeword (-> s (s/replace "ago" "") (s/trim))]
     (if (re-find #"\d+" s)
       (let [amount (Integer/parseInt (re-find #"\d+" s))]
         ; normal cases
         (cond
           (re-find #"\d+s$|sec|secs|second|seconds" cleaned-timeword) (joda/minus relative-to (joda/millis (* amount 1000)))
           (re-find #"\d+m$|min|mins|minute|minutes" cleaned-timeword) (joda/minus relative-to (joda/minutes amount))
           (re-find #"\d+h$|hour|hours" cleaned-timeword) (joda/minus relative-to (joda/hours amount))
           (re-find #"\d+d$|day|days" cleaned-timeword) (joda/minus relative-to (joda/days amount))
           (re-find #"\d+w$|week|weeks" cleaned-timeword) (joda/minus relative-to (joda/days (* 7 amount)))
           (re-find #"month|months" cleaned-timeword) (joda/minus relative-to (joda/months amount))
           (re-find #"year|years" cleaned-timeword) (joda/minus relative-to (joda/years amount))
           :else nil))
       (cond
         ; special cases
         (= "a sec" cleaned-timeword) (joda/minus relative-to (joda/millis 1000))
         (= "a second" cleaned-timeword) (joda/minus relative-to (joda/millis 1000))
         (= "a min" cleaned-timeword) (joda/minus relative-to (joda/minutes 1))
         (= "a minute" cleaned-timeword) (joda/minus relative-to (joda/minutes 1))
         (= "a hour" cleaned-timeword) (joda/minus relative-to (joda/hours 1))
         (= "an hour" cleaned-timeword) (joda/minus relative-to (joda/hours 1))
         (= "a day" cleaned-timeword) (joda/minus relative-to (joda/days 1))
         (= "a week" cleaned-timeword) (joda/minus relative-to (joda/days 7))
         (= "a month" cleaned-timeword) (joda/minus relative-to (joda/months 1))
         (= "a year" cleaned-timeword) (joda/minus relative-to (joda/years 1))
         :else nil)))))

(defn parse-some-time-from-now
  "Handle strings like '32 mins from now'."
  ([s] (parse-some-time-from-now s (joda/now)))
  ([s relative-to]
   (let [cleaned-timeword (-> s (s/replace "from now" "") (s/trim))]
     (if (re-find #"\d+" s)
       (let [amount (Integer/parseInt (re-find #"\d+" s))]
         ; normal cases
         (cond
           (re-find #"\d+s$|sec|secs|second|seconds" cleaned-timeword) (joda/plus relative-to (joda/millis (* amount 1000)))
           (re-find #"\d+m$|min|mins|minute|minutes" cleaned-timeword) (joda/plus relative-to (joda/minutes amount))
           (re-find #"\d+h$|hour|hours" cleaned-timeword) (joda/plus relative-to (joda/hours amount))
           (re-find #"\d+d$|day|days" cleaned-timeword) (joda/plus relative-to (joda/days amount))
           (re-find #"\d+w$|week|weeks" cleaned-timeword) (joda/plus relative-to (joda/days (* 7 amount)))
           (re-find #"month|months" cleaned-timeword) (joda/plus relative-to (joda/months amount))
           (re-find #"year|years" cleaned-timeword) (joda/plus relative-to (joda/years amount))
           :else nil))
       (cond
         ; special cases
         (= "a sec" cleaned-timeword) (joda/plus relative-to (joda/millis 1000))
         (= "a second" cleaned-timeword) (joda/plus relative-to (joda/millis 1000))
         (= "a min" cleaned-timeword) (joda/plus relative-to (joda/minutes 1))
         (= "a minute" cleaned-timeword) (joda/plus relative-to (joda/minutes 1))
         (= "a hour" cleaned-timeword) (joda/plus relative-to (joda/hours 1))
         (= "an hour" cleaned-timeword) (joda/plus relative-to (joda/hours 1))
         (= "a day" cleaned-timeword) (joda/plus relative-to (joda/days 1))
         (= "a week" cleaned-timeword) (joda/plus relative-to (joda/days 7))
         (= "a month" cleaned-timeword) (joda/plus relative-to (joda/months 1))
         (= "a year" cleaned-timeword) (joda/plus relative-to (joda/years 1))
         :else nil)))))

(defn parse-last-weekday
  ([^String s] (parse-last-weekday s (joda/now)))
  ([s relative-to]
   (let [required-weekday (cond
                            (re-find #"monday" s) 1
                            (re-find #"tuesday" s) 2
                            (re-find #"wednesday" s) 3
                            (re-find #"thursday" s) 4
                            (re-find #"friday" s) 5
                            (re-find #"saturday" s) 6
                            (re-find #"sunday" s) 7)]
     (if (= required-weekday (joda/day-of-week relative-to))
       (joda/minus relative-to (joda/days 7))
       (loop [datetime relative-to]
         (if (= required-weekday (joda/day-of-week datetime))
           datetime
           (recur (joda/minus datetime (joda/days 1)))))))))

(defn parse-last-month
  ([^String s] (parse-last-month s (joda/now)))
  ([s relative-to]
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
     (if (= required-month (joda/month relative-to))
       (joda/minus relative-to (joda/months 12))
       (loop [datetime relative-to]
         (if (= required-month (joda/month datetime))
           datetime
           (recur (joda/minus datetime (joda/months 1)))))))))

(defn parse-last-season
  ([^String s] (parse-last-season s (joda/now)))
  ([s relative-to]
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
     (if (= required-season (month->season (joda/month relative-to)))
       (joda/minus relative-to (joda/months 12))
       (loop [datetime relative-to]
         (if (= required-season (month->season (joda/month datetime)))
           datetime
           (recur (joda/minus datetime (joda/months 1)))))))))

(defn parse-relative-date
  ([^String s] (parse-relative-date s (joda/now)))
  ([^String s relative-to]
   (cond
     (= "now" s) (-> relative-to (date-to-str-seq))
     (= "today" s) (-> relative-to (date-to-str-seq))
     (= "yesterday" s) (-> relative-to (joda/minus (joda/days 1)) (date-to-str-seq))
     (= "tomorrow" s) (-> relative-to (joda/plus (joda/days 1)) (date-to-str-seq))
     (re-find #"ago" s) (-> s (parse-some-time-ago) (date-to-str-seq))
     (re-find #"from now" s) (-> s (parse-some-time-from-now) (date-to-str-seq))
     (= "last month" s) (-> relative-to (joda/minus (joda/months 1)) (date-to-str-seq))
     (re-find #"last (monday|tuesday|wednesday|thursday|friday|saturday|sunday)" s) (-> s (parse-last-weekday relative-to) (date-to-str-seq))
     (re-find #"last (january|february|march|april|may|june|july|august|september|october|november|december)" s) (-> s (parse-last-month relative-to) (date-to-str-seq))
     (re-find #"last (spring|summer|autumn|fall|winter)" s) (-> s (parse-last-season relative-to) (date-to-str-seq))
     :else nil)))

(defn is-relative-date? [s]
  ;; Here list of words for relative time is not extensive
  (if (or (not (re-find #"\d" s))
          (re-find #"ago|yesterday|tomorrow|last|previous|next|now" s))
    true
    false))

(defn parse-date [s]
  (let [ls (s/lower-case s)
        relative-to (joda/now)]
    (if (is-relative-date? ls)
      (parse-relative-date ls relative-to)
      (letfn [(if-conj [coll x] (if x (conj coll x) coll))
              (now-part [time-part] (time-part (joda/now)))
              (add-years [date-parts]
                (conj date-parts
                      (if-let [y (year ls)]
                        y
                        (str (now-part joda/year)))))
              (add-months [date-parts] (if-conj date-parts (month ls)))
              (add-days [date-parts] (if-conj date-parts (day ls)))
              (add-hours [date-parts] (if-conj date-parts (hour ls)))
              (add-minutes [date-parts] (if-conj date-parts (minute ls)))
              (add-seconds [date-parts] (if-conj date-parts (zecond ls)))]
        (-> []
            add-years
            add-months
            add-days
            add-hours
            add-minutes
            add-seconds)))))
