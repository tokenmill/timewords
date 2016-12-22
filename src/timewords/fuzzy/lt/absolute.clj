(ns timewords.fuzzy.lt.absolute
  (:require [clojure.string :as s]
            [clj-time.core :as joda])
  (:import (org.joda.time DateTime)))

(def months
  {#"sausi[so]"           "1"
   #"vasari[so]"          "2"
   #"kovas|kovo"          "3"
   #"balandis|balandžio"  "4"
   #"geguž[ės]"           "5"
   #"birželi[so]"         "6"
   #"liepa|liepos"        "7"
   #"rugpjūtis|rugpjūčio" "8"
   #"rugsėjis|rugsėjo"    "9"
   #"spali[so]"           "10"
   #"lapkritis|lapkričio" "11"
   #"gruodis|gruodžio"    "12"})

(defn- re [r s] (let [f (re-find r s)] (if (coll? f) (first f) f)))

(defn month [fuzzy-date]
  (some (fn [[re nr]] (when (re-find re fuzzy-date) nr)) months))

(defn year [fuzzy-date] (first (re-find #"\b(19|20)\d{2}\b" fuzzy-date)))
(defn day [fuzzy-date] (re-find #"\b\d{1,2}\b" fuzzy-date))
(defn hour [fuzzy-date] (second (re-find #"\b(\d{2}):\d{2}\b" fuzzy-date)))
(defn minute [fuzzy-date] (second (re-find #"\b\d{2}:(\d{2})\b" fuzzy-date)))
(defn zecond [fuzzy-date] (second (re-find #"\b\d{2}:\d{2}:(\d{2})\b" fuzzy-date)))

(defn parse-date [^String s & [^DateTime document-time]]
  (let [ls (s/lower-case s)]
    (letfn [(if-conj [coll x] (if x (conj coll x) coll))
            (now-part [time-part] (time-part document-time))
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
          add-seconds))))

(defn parse-absolute-date [^String s & [^DateTime document-time]]
  (parse-date s document-time))
