(ns timewords.java-test
  (:require [clojure.test :refer :all]
            [clj-time.core :refer [date-time]]
            [timewords.core :as c])
  (:import (org.joda.time DateTime)
           (lt.tokenmill.timewords Timewords)
           (java.util Date)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest java-interface-test
  (testing "Satandard date string parse testing"
    (let [^Timewords timewords-parser (Timewords.)]
      (is (= nil (.parse timewords-parser nil)))
      (is (= (date 2010 7  8 0  0  0) (.parse timewords-parser "8th July 2010")))
      (is (= (date 2013 1 24 8 46 54) (.parse timewords-parser "2013-01-24T08:46:54Z")))
      (is (= (date 2013 1 24 8 46 54) (.parse timewords-parser "2013-01-24T08:46:54Z" "en")))
      (is (= (date 2013 1 24 8 46 54) (.parse timewords-parser "2013-01-24T08:46:54Z" "en" (Date.)))))))
