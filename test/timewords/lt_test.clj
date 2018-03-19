(ns timewords.lt-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.core :refer [parse]])
  (:import (java.util Date)
           (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest lt-dates-test

  (testing "Some dates"
    (is (= (date 2016 12 22 11 10) (parse "2016 m. gruodžio 22 d. 11:10" nil "lt")))
    (is (= (date 2013 12 2 8 14) (parse " 2013/12/02 8:14" nil "lt")))

    (is (= (date 2000 1 3) (parse "2000 sausio 3" nil "lt")))
    (is (= (date 2000 1 3 12 13) (parse "2000 sausio 3 12:13" nil "lt")))
    (is (= (date 2000 1 3 12 13 14) (parse "2000 sausio 3 12:13:14" nil "lt")))
    (is (= (date 1999 3 13) (parse "1999 metų kovo 13" nil "lt")))
    (is (= (date (-> (joda/now) (joda/year)) 3 13) (parse "kovo 13" nil "lt")))
    (is (= (date 2005 3 13) (parse "kovo 13" (.toDate (joda/date-time 2005)) "lt")))
    ))