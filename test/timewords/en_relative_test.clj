(ns timewords.en-relative-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc]
            [timewords.core :refer :all]))

(deftest en-relative
  (testing "relative weekdays"
    ; dealing with weekdays
    (let [parsed-datetime (tc/to-date-time (parse "last monday"))]
      (is (joda/before? parsed-datetime (joda/now)))
      (is (= 1 (joda/day-of-week parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (is (= 2 (joda/day-of-week (tc/to-date-time (parse "last tuesday")))))
    (is (= 3 (joda/day-of-week (tc/to-date-time (parse "last wednesday")))))
    (is (= 4 (joda/day-of-week (tc/to-date-time (parse "last thursday")))))
    (is (= 5 (joda/day-of-week (tc/to-date-time (parse "last friday")))))
    (is (= 6 (joda/day-of-week (tc/to-date-time (parse "last saturday")))))
    (is (= 7 (joda/day-of-week (tc/to-date-time (parse "last sunday")))))
    (let [parsed-datetime (tc/to-date-time (parse "last Sunday"))]
      (is (= 7 (joda/day-of-week parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))

    (let [parsed-datetime (tc/to-date-time (parse "next monday"))]
      (is (joda/after? parsed-datetime (joda/now)))
      (is (= 1 (joda/day-of-week parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (is (= 2 (joda/day-of-week (tc/to-date-time (parse "next tuesday")))))
    (is (= 3 (joda/day-of-week (tc/to-date-time (parse "next wednesday")))))
    (is (= 4 (joda/day-of-week (tc/to-date-time (parse "next thursday")))))
    (is (= 5 (joda/day-of-week (tc/to-date-time (parse "next friday")))))
    (is (= 6 (joda/day-of-week (tc/to-date-time (parse "next saturday")))))
    (is (= 7 (joda/day-of-week (tc/to-date-time (parse "next sunday")))))

    ; dealing with months
    (is (= 1 (-> (parse "last january") (tc/to-date-time) (joda/month))))
    (is (= 2 (-> (parse "last february") (tc/to-date-time) (joda/month))))
    (is (= 3 (-> (parse "last march") (tc/to-date-time) (joda/month))))
    (is (= 4 (-> (parse "last april") (tc/to-date-time) (joda/month))))
    (is (= 5 (-> (parse "last may") (tc/to-date-time) (joda/month))))
    (is (= 6 (-> (parse "last june") (tc/to-date-time) (joda/month))))
    (is (= 7 (-> (parse "last july") (tc/to-date-time) (joda/month))))
    (is (= 8 (-> (parse "last august") (tc/to-date-time) (joda/month))))
    (is (= 9 (-> (parse "last september") (tc/to-date-time) (joda/month))))
    (is (= 10 (-> (parse "last october") (tc/to-date-time) (joda/month))))
    (is (= 11 (-> (parse "last november") (tc/to-date-time) (joda/month))))
    (is (= 12 (-> (parse "last december") (tc/to-date-time) (joda/month))))
    (is (= nil (parse "next january")))
    (is (= nil (parse "next february")))
    (is (= nil (parse "next march")))
    (is (= nil (parse "next april")))
    (is (= nil (parse "next may")))
    (is (= nil (parse "next june")))
    (is (= nil (parse "next july")))
    (is (= nil (parse "next august")))
    (is (= nil (parse "next september")))
    (is (= nil (parse "next october")))
    (is (= nil (parse "next november")))
    (is (= nil (parse "next december")))

    ; seasons
    (is (<= 3 (-> (parse "last spring") (tc/to-date-time) (joda/month)) 5))
    (is (<= 6 (-> (parse "last summer") (tc/to-date-time) (joda/month)) 8))
    (is (<= 9 (-> (parse "last autumn") (tc/to-date-time) (joda/month)) 11))
    (is (<= 9 (-> (parse "last fall") (tc/to-date-time) (joda/month)) 11))
    (is (or (<= 1 (-> (parse "last winter") (tc/to-date-time) (joda/month)) 2)
            (= 12 (-> (parse "last winter") (tc/to-date-time) (joda/month)))))
    (is (= nil (parse "next spring")))
    (is (= nil (parse "next summer")))
    (is (= nil (parse "next autumn")))
    (is (= nil (parse "next fall")))
    (is (= nil (parse "next winter")))))
