(ns timewords.en-test
  (:require [clojure.test :refer :all]
            [timewords.fuzzy.en.en :refer :all]
            [timewords.fuzzy.en.absolute :refer :all]))

(deftest en-test

  (testing "We need to get if it is pm or am for these dates"
    (is (false? (is-pm? "October 16, 2015: 8:18 AM ET")))
    (is (true? (is-pm? "October 16, 2015: 8:18 PM ET")))
    (is (true? (is-pm? "Mon Oct 19, 2015 5:44pm EDT")))
    (is (true? (is-pm? "Mon Oct 19, 2015 12:44pm EDT")))
    (is (false? (is-pm? "Monday 19 October 2015 13.30 BST")))
    (is (false? (is-pm? "Oct. 19, 2015 11:51 AM ET"))))


  (testing "October 16, 2015: 8:18 AM ET is 8 hours"
    (is (= "8" (hour "october 16, 2015: 8:18 am et"))))

  (testing "October 16, 2015: 8:18 PM ET is 20 hours"
    (is (= "20" (hour "october 16, 2015: 8:18 pm et"))))

  (testing "money.cnn.com"
    (is (= ["2015" "10" "16" "8" "18"] (parse-date "October 16, 2015: 8:18 AM ET"))))

  (testing "money.cnn.com"
    (is (= ["2015" "10" "16" "20" "18"] (parse-date "October 16, 2015: 8:18 PM ET"))))

  (testing "bloomberg.com"
    (is (= ["2015" "10" "19" "18" "22"] (parse-date "October 19, 2015 — 6:22 PM EEST"))))

  (testing "reuters.com"
    (is (= ["2015" "10" "19" "5" "44"] (parse-date "Mon Oct 19, 2015 5:44am EDT"))))

  (testing "reuters.com"
    (is (= ["2015" "10" "19" "17" "44"] (parse-date "Mon Oct 19, 2015 5:44pm EDT"))))

  (testing "theguardian.com"
    (is (= ["2015" "10" "19" "13" "30"] (parse-date "Monday 19 October 2015 13.30 BST"))))

  (testing "seekingalpha.com"
    (is (= ["2015" "10" "19" "11" "51"] (parse-date "Oct. 19, 2015 11:51 AM ET"))))

  (testing "bbc.com"
    (is (= ["2015" "10" "20"] (parse-date "20 October 2015")))))
