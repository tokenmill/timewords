(ns timewords.de-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.core :refer [parse]])
  (:import (java.util Date)
           (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest lt-dates-test
  (testing "German month names"
    (is (= (date 2018 1 28 16 1) (parse "28. Januar 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 2 28 16 1) (parse "28. Februar 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 3 28 16 1) (parse "28. März 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 4 28 16 1) (parse "28. April 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 5 28 16 1) (parse "28. Mai 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 6 28 16 1) (parse "28. Juni 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 7 28 16 1) (parse "28. Juli 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 8 28 16 1) (parse "28. August 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 9 28 16 1) (parse "28. September 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 10 28 16 1) (parse "28. Oktober 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 11 28 16 1) (parse "28. November 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 12 28 16 1) (parse "28. Dezember 2018, 16:01 Uhr" nil "de")))
    (is (= (date 2018 3 29 12 58) (parse "Donnerstag, 29.03.2018, 12:58" nil "de")))
    (is (= (date 2011 10 5) (parse "5.10.2011" nil "de")))
    (is (= (date 2011 10 5) (parse "5. Oktober 2011" nil "de")))
    (is (= (date 2011 10 4) (parse "Dienstag, 4. Oktober 2011" nil "de")))))
