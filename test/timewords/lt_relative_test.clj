(ns timewords.lt-relative-test
  (:require [clojure.test :refer :all]
            [timewords.core :refer [parse]])
  (:import (java.util Date)))

(deftest lt-relative-timewords
  (testing "today variations"
    ;(is (= nil (parse "šiandien 13:16" (Date.) "lt")))
    ;(is (= nil (parse "prieš 1 d." (Date.) "lt")))
    ; 1 val prieš
    ; 7 val prieš
    ; 1 d. prieš
    ; 1 sav. prieš
    ;(is (= nil (parse "Publikuota: 21:05" (Date.) "lt")))
    ))