(ns timewords.special-cases-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.standard.joda-formats :as fmts])
  (:import (java.util Date Locale)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest special-cases
  (let [document-time (apply date-time [2018 05 24])]
    (is (= (date 2018 5 24 4 57) (fmts/special-cases "4:57AM EDT" Locale/ENGLISH document-time)))
    (is (= (date 2018 5 24 15 4) (fmts/special-cases "15:04" Locale/ENGLISH document-time)))))
