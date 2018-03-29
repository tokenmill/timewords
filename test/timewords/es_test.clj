(ns timewords.es-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.core :refer [parse]])
  (:import (java.util Date)
           (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest es-dates-test
  (testing "spanish month names"
    (is (= (date 2018 3 29) (parse "29/03/2018" nil "es")))
    (is (= (date 2018 3 29 13 50) (parse "29 MAR 2018 - 13:50 CEST" nil "es")))
    (is (= (date 2018 3 29 13) (parse "29 MAR. 2018 13:00" nil "es")))
    (is (= (date 2018 3 29 13 21) (parse "29 marzo 2018 13:21h CEST" nil "es")))
    (is (= (date 2018 3 29) (parse "29 marzo 2018" nil "es")))
    (is (= (date 2018 3 29 13 51) (parse "29/03/2018 13:51h" nil "es")))
    (is (= (date 2018 3 29 10 55) (parse "29.03.2018 - 10:55h" nil "es")))
    (is (= (date 2018 3 29 11 55) (parse "29/03/2018 11:55" nil "es")))
    (is (= (date 2018 3 29 11 52) (parse "29/03/2018 - 11:52" nil "es")))
    (is (= (date 2018 3 29 12 41) (parse "12:41 - 29/03/18" nil "es")))
    (is (= (date 2018 3 29 11 40) (parse "29.03.2018 – 11:40 H." nil "es")))
    (is (= (date 2018 3 29 14 21 40) (parse "2018-03-29 14:21:40 H" nil "es")))
    (is (= (date 2018 3 29 12 10 50) (parse "29/03/2018 12:10:50 CET" nil "es")))
    (is (= (date 2018 3 29 12 10 50) (parse "29/03/2018 12:10:50" nil "es")))
    (is (= (date 2018 3 29 13 39) (parse "29 mar. 2018 - 13:39" nil "es")))
    (is (= (date 2018 3 3 4 30) (parse "sábado, 03 marzo 2018, 04:30" nil "es")))
    (is (= (date 2018 3 29 14 36) (parse "29/03/2018 14:36 h" nil "es")))
    (is (= (date 2018 3 29) (parse "Jueves, 29/03/2018" nil "es")))
    (is (= (date 2018 3 29) (parse "jueves 29 marzo 2018" nil "es")))
    (is (= (date 2018 3 28 15 42) (parse "28 de marzo de 2018. 15:42h" nil "es")))
    (is (= (date 2018 3 28) (parse "28 de marzo de 2018" nil "es")))))
