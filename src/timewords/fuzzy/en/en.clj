(ns timewords.fuzzy.en.en
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc]
            [timewords.fuzzy.en.relative.parser :refer [parse-relative-date]]))

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
