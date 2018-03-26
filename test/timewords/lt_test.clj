(ns timewords.lt-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.core :refer [parse]])
  (:import (java.util Date)
           (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest lt-dates-test

  (testing "lithuanian month names"
    (is (= (date 2000 1 3) (parse "2000 sausio 3" nil "lt")))
    (is (= (date 2000 2 3) (parse "2000 vasario 3" nil "lt")))
    (is (= (date 2000 3 3) (parse "2000 kovo 3" nil "lt")))
    (is (= (date 2000 4 3) (parse "2000 balandžio 3" nil "lt")))
    (is (= (date 2000 5 3) (parse "2000 gegužės 3" nil "lt")))
    (is (= (date 2000 6 3) (parse "2000 birželis 3" nil "lt")))
    (is (= (date 2000 7 3) (parse "2000 liepos 3" nil "lt")))
    (is (= (date 2000 8 3) (parse "2000 rugpjūčio 3" nil "lt")))
    (is (= (date 2000 9 3) (parse "2000 rugsėjo 3" nil "lt")))
    (is (= (date 2000 10 3) (parse "2000 spalio 3" nil "lt")))
    (is (= (date 2000 11 3) (parse "2000 lapkričio 3" nil "lt")))
    (is (= (date 2000 12 3) (parse "2000 gruodžio 3" nil "lt")))
    (is (= (date 2013 12 2 8 14) (parse " 2013/12/02 8:14" nil "lt")))
    (is (= (date (.getYear (DateTime.)) 3 16 15 17) (parse "Kovo 16 d. 15:17" (Date.) "lt")))
    (is (= (date 2016 12 22 11 10) (parse "2016 m. gruodžio 22 d. 11:10" nil "lt")))
    (is (= (date 2000 1 3 12 13) (parse "2000 sausio 3 12:13" nil "lt")))
    (is (= (date 2000 1 3 12 13 14) (parse "2000 sausio 3 12:13:14" nil "lt")))
    (is (= (date 1999 3 13) (parse "1999 metų kovo 13" nil "lt")))
    (is (= (date (-> (joda/now) (joda/year)) 3 13) (parse "kovo 13" nil "lt")))
    (is (= (date 2005 3 13) (parse "kovo 13" (.toDate (joda/date-time 2005)) "lt")))
    (is (= (date 2018 3 20 9 40) (parse "2018 03 20 9:40" nil "lt")))
    (is (= (date 2018 03 22 18 30) (parse "2018-03-22 / 18:30" nil "lt")))
    (is (= (date 2018 3 22 21 5 43) (parse "2018-03-22T21:05:43+02:00" nil "lt")))
    (is (= (date 2018 3 22 7 30 44) (parse "2018 kovo mėn. 22 d. 07:30:44" nil "lt")))
    (is (= (date 2018 3 22 16 1) (parse "16:01 2018.03.22" nil "lt")))
    (is (= (date 2018 3 20 18 57) (parse "2018 kovo 20d. 18:57" nil "lt")))
    (is (= (date 2018 3 12 14 4) (parse "Pirmadienis, 12 Kovas 2018 14:04" nil "lt")))
    (is (= (date 2018 3 26) (parse "2018 kovo 26" nil "lt")))
    (is (= (date 2018 3 24 10 9) (parse "2018 kovo 24 d. 10:09" nil "lt")))
    (is (= (date 2018 3 25 12) (parse "2018.03.25 12:00" nil "lt")))
    (is (= (date 2018 3 26) (parse "Kov 26, 2018" nil "lt")))
    ))