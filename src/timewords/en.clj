(ns timewords.en
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc]
            [clj-time.predicates :as tp]))

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
  [s]
  (let [now (joda/now)
        cleaned-timeword (-> s (s/replace "ago" "") (s/trim))]
    (if (re-find #"\d+" s)
      (let [amount (Integer/parseInt (re-find #"\d+" s))]
        ; normal cases
        (cond
          (re-find #"\d+s$|sec|secs|second|seconds" cleaned-timeword) (joda/minus now (joda/millis (* amount 1000)))
          (re-find #"\d+m$|min|mins|minute|minutes" cleaned-timeword) (joda/minus now (joda/minutes amount))
          (re-find #"\d+h$|hour|hours" cleaned-timeword) (joda/minus now (joda/hours amount))
          (re-find #"\d+d$|day|days" cleaned-timeword) (joda/minus now (joda/days amount))
          (re-find #"\d+w$|week|weeks" cleaned-timeword) (joda/minus now (joda/days (* 7 amount)))
          (re-find #"month|months" cleaned-timeword) (joda/minus now (joda/months amount))
          (re-find #"year|years" cleaned-timeword) (joda/minus now (joda/years amount))
          :else nil))
      (cond
        ; special cases
        (= "a sec" cleaned-timeword) (joda/minus now (joda/millis 1000))
        (= "a second" cleaned-timeword) (joda/minus now (joda/millis 1000))
        (= "a min" cleaned-timeword) (joda/minus now (joda/minutes 1))
        (= "a minute" cleaned-timeword) (joda/minus now (joda/minutes 1))
        (= "a hour" cleaned-timeword) (joda/minus now (joda/hours 1))
        (= "an hour" cleaned-timeword) (joda/minus now (joda/hours 1))
        (= "a day" cleaned-timeword) (joda/minus now (joda/days 1))
        (= "a week" cleaned-timeword) (joda/minus now (joda/days 7))
        (= "a month" cleaned-timeword) (joda/minus now (joda/months 1))
        (= "a year" cleaned-timeword) (joda/minus now (joda/years 1))
        :else nil))))

(defn parse-some-time-from-now
  "Handle strings like '32 mins from now'."
  [s]
  (let [now (joda/now)
        cleaned-timeword (-> s (s/replace "from now" "") (s/trim))]
    (if (re-find #"\d+" s)
      (let [amount (Integer/parseInt (re-find #"\d+" s))]
        ; normal cases
        (cond
          (re-find #"\d+s$|sec|secs|second|seconds" cleaned-timeword) (joda/plus now (joda/millis (* amount 1000)))
          (re-find #"\d+m$|min|mins|minute|minutes" cleaned-timeword) (joda/plus now (joda/minutes amount))
          (re-find #"\d+h$|hour|hours" cleaned-timeword) (joda/plus now (joda/hours amount))
          (re-find #"\d+d$|day|days" cleaned-timeword) (joda/plus now (joda/days amount))
          (re-find #"\d+w$|week|weeks" cleaned-timeword) (joda/plus now (joda/days (* 7 amount)))
          (re-find #"month|months" cleaned-timeword) (joda/plus now (joda/months amount))
          (re-find #"year|years" cleaned-timeword) (joda/plus now (joda/years amount))
          :else nil))
      (cond
        ; special cases
        (= "a sec" cleaned-timeword) (joda/plus now (joda/millis 1000))
        (= "a second" cleaned-timeword) (joda/plus now (joda/millis 1000))
        (= "a min" cleaned-timeword) (joda/plus now (joda/minutes 1))
        (= "a minute" cleaned-timeword) (joda/plus now (joda/minutes 1))
        (= "a hour" cleaned-timeword) (joda/plus now (joda/hours 1))
        (= "an hour" cleaned-timeword) (joda/plus now (joda/hours 1))
        (= "a day" cleaned-timeword) (joda/plus now (joda/days 1))
        (= "a week" cleaned-timeword) (joda/plus now (joda/days 7))
        (= "a month" cleaned-timeword) (joda/plus now (joda/months 1))
        (= "a year" cleaned-timeword) (joda/plus now (joda/years 1))
        :else nil))))

(defn parse-last-weekday
  [^String s]
  (let [reference-datetime (joda/now)
        is-required-weekday? (cond
                            (re-find #"monday" s) tp/monday?
                            (re-find #"tuesday" s) tp/tuesday?
                            (re-find #"wednesday" s) tp/wednesday?
                            (re-find #"thursday" s) tp/thursday?
                            (re-find #"friday" s) tp/friday?
                            (re-find #"saturday" s) tp/saturday?
                            (re-find #"sunday" s) tp/sunday?)]
    (if (is-required-weekday? reference-datetime)
      (joda/minus reference-datetime (joda/days 7))
      (loop [datetime reference-datetime]
        (if (is-required-weekday? datetime)
          datetime
          (recur (joda/minus datetime (joda/days 1))))))))

(defn parse-relative-date [s]
  ;; TODO: implement parsing of such timeword as "32 mins ago".
  (cond
    (= "now" s) (-> (joda/now) (date-to-str-seq))
    (= "yesterday" s) (-> (joda/now) (joda/minus (joda/days 1)) (date-to-str-seq))
    (= "tomorrow" s) (-> (joda/now) (joda/plus (joda/days 1)) (date-to-str-seq))
    (re-find #"ago" s) (-> s (parse-some-time-ago) (date-to-str-seq))
    (re-find #"from now" s) (-> s (parse-some-time-from-now) (date-to-str-seq))
    (re-find #"last (monday|tuesday|wednesday|thursday|friday|saturday|sunday)" s) (-> s (parse-last-weekday) (date-to-str-seq))
    :else nil))

(defn is-relative-date? [s]
  ;; Here list of words for relative time is not extensive
  (if (or (not (re-find #"\d" s)) (re-find #"ago|yesterday|tomorrow|last|previous|next|now" s))
    true
    false))

(defn parse-date [s]
  (if (is-relative-date? s)
    (parse-relative-date s)
    (let [ls (s/lower-case s)]
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
