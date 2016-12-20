(ns timewords.cleaner-test
  (:require [clojure.test :refer :all]
            [timewords.utils.cleaner :refer [clean]]))

(deftest cleaner-test
  (testing "Dirty dates"
    (is (= "2015-02-02" (clean "2015-02-02")))
    (is (= "2015-02-02, 12:13" (clean "2015-02-02, 12:13")))
    (is (= "2015-02-02, 12:13" (clean ",2015-02-02, 12:13")))))
