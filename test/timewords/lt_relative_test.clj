(ns timewords.lt-relative-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.core :refer [parse]])
  (:import (java.util Date)
           (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest lt-relative-timewords
  (testing "today variations"
    (let [document-time (date 2018 4 7 12 3)]
      (is (= (date 2018 4 6) (parse "prieš 1 d." document-time "lt")))
      (is (= (date 2018 4 6) (parse "prieš 1 d" document-time "lt")))
      (is (= (date 2018 3 28) (parse "prieš 10 d." document-time "lt"))))
    (let [document-time (date 2018 4 7 12 3)]
      (is (= (date 2018 4 7 13 16) (parse "šiandien 13:16" document-time "lt")))
      (is (= (date 2018 4 7 4 47) (parse "šiandien 04:47" document-time "lt")))
      (is (= (date 2018 4 7 22 00) (parse "šiandien 22:00" document-time "lt"))))
    (let [document-time (date 2018 4 7 12 3)]
      (is (= (date 2018 4 6) (parse "1 d. prieš" document-time "lt")))
      (is (= (date 2018 4 6) (parse "1 d prieš" document-time "lt")))
      (is (= (date 2018 3 31) (parse "1 sav. prieš" document-time "lt")))
      (is (= (date 2018 3 31) (parse "1 sav prieš" document-time "lt")))
      ; use a timezone for document time setup
      #_(is (= (date 2018 4 7 11) (parse "1 val prieš" document-time "lt")))
      #_(is (= (date 2018 4 7 11) (parse "7 val prieš" document-time "lt"))))
    ;(is (= nil (parse "Publikuota: 21:05" (Date.) "lt")))
    ))
