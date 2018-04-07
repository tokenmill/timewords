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
      (is (= (date 2018 3 28) (parse "prieš 10 d." document-time "lt"))))
    ;(is (= nil (parse "šiandien 13:16" (Date.) "lt")))
    ; šiandien 04:47
    ; 1 val prieš
    ; 7 val prieš
    ; 1 d. prieš
    ; 1 sav. prieš
    ;(is (= nil (parse "Publikuota: 21:05" (Date.) "lt")))
    ))