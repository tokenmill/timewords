(ns timewords.fr-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.core :refer [parse]])
  (:import (java.util Date)
           (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest fr-dates-test
  (testing "French month names and common French formats"
    (is (= (date 2018 1 14 7 15) (parse "janvier 14, 2018 à 07:15" nil "fr")))
    (is (= (date 2018 2 10 9 12) (parse "février 10, 2018 à 09:12" nil "fr")))
    (is (= (date 2018 3 4 19 22) (parse "mars 4, 2018 à 19:22" nil "fr")))
    (is (= (date 2018 4 18 7 15) (parse "avril 18, 2018 à 7:15" nil "fr")))
    (is (= (date 2018 5 9 7 15) (parse "mai 09, 2018 à 07:15" nil "fr")))
    (is (= (date 2018 6 7) (parse "juin 7, 2018" nil "fr")))
    (is (= (date 2018 7 5 7 15) (parse "juillet 5, 2018 07:15" nil "fr")))
    (is (= (date 2018 8 25 7 15) (parse "août 25, 2018 07:15" nil "fr")))
    (is (= (date 2018 9 21 7 15) (parse "septembre 21, 2018 - 07:15" nil "fr")))
    (is (= (date 2018 10 19 17 15) (parse "octobre 19, 2018 à 17:15" nil "fr")))
    (is (= (date 2018 11 9) (parse "le 9 novembre, 2018" nil "fr")))
    (is (= (date 2018 12 1) (parse "1 décembre, 2018" nil "fr")))
    (is (= (date 2018 5 9 10 40) (parse "09.05.2018 à 10h40" nil "fr")))
    (is (= (date 2018 5 8 23 01) (parse "le 8 mai à 23h01" nil "fr")))
    (is (= (date 2018 5 9 14 25) (parse "09/05/18 à 14h25" nil "fr")))
    (is (= (date 2018 5 9 14 54) (parse "le 09 mai 2018 à 14h54" nil "fr")))
    (is (= (date 2018 5 9 13 07) (parse "09 mai 2018, 13h07" nil "fr")))
    (is (= (date 2018 5 8 22 29) (parse "Le 08/05 à 22:29" nil "fr")))
    (is (= (date 2018 5 9 15 33) (parse "9 mai 2018 à 15:33" nil "fr")))
    (is (= (date 2018 5 9 12 43) (parse "09 Mai 2018 : 12h43" nil "fr")))
    ;; the parser flips places of day and month
    ;(is (= (date 2018 5 9 7 3) (parse "09/05/2018 à 07:03" nil "fr")))
    ;(is (= (date 2018 5 8) (parse "le 08/05/2018" nil "fr")))
    ;(is (= (date 2018 5 9 14 22) (parse "le 09/05/2018 à 14:22" nil "fr")))
    ;(is (= (date 2018 5 9 7 45) (parse "09/05/2018 à 07:45" nil "fr")))
    ;(is (= (date 2018 5 7 16 11) (parse "07/05/2018 | 16:11" nil, "fr")))
    ))
