(ns timewords.ru-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [timewords.core :refer [parse]])
  (:import (java.util Date)
           (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest es-dates-test
  (testing "spanish month names"
    (is (= (date 2018 4 7 19 19) (parse "7 April 2018, 19:19" nil "en")))
    (is (= (date 2018 4 7 19 19) (parse "7 апреля 2018, 19:19" nil "ru")))
    (is (= (date 2018 4 7 22 46) (parse "22:46, 7 апреля 2018" nil "ru")))
    (is (= (date 2018 4 6 16 20) (parse "6 апреля 16:20" nil "ru")))
    (is (= (date 2018 3 28 16 32) (parse "28 марта 16:32" nil "ru")))
    (is (= (date 2018 4 7 20 55) (parse "7 апреля 2018 20:55" nil "ru")))
    (is (= (date 2018 4 7 22 22) (parse "7 апреля, 22:22" nil "ru")))
    (is (= (date 2018 4 7 21 55) (parse "07.04.2018, 21:55" nil "ru")))
    (is (= (date 2018 3 1 22 06) (parse "01.03.2018 в 22:06" nil "ru")))
    (is (= (date 2018 4 8 0 01) (parse "08.04.2018 00:01" nil "ru")))
    (is (= (date 2018 4 6 20 10) (parse "06.04.2018 - 20:10" nil "ru")))
    (is (= (date 2018 4 7 19 0) (parse "19:00 07/04/2018" nil "ru")))
    (is (= (date 2018 4 7 17 31) (parse "7 апреля 2018 г., 17:31" nil "ru")))
    (is (= (date 2018 4 8 0 38) (parse "00:38 08.04.2018" nil "ru")))
    (is (= (date 2018 4 7 23 56) (parse "07 Апрель 2018, 23:56" nil "ru")))
    (is (= (date 2018 3 31 17 56) (parse "31 Март 2018, 17:56" nil "ru")))
    (is (= (date 2018 2 27 10 0) (parse "27 Февраль 2018, 10:00" nil "ru")))
    (is (= (date 2017 11 22 1 42) (parse "22 Ноябрь 2017, 01:42" nil "ru")))
    (is (= (date 2017 10 23 10 41) (parse "23 Октябрь 2017, 10:41" nil "ru")))
    (is (= (date 2017 9 26 16 33) (parse "26 Сентябрь 2017, 16:33" nil "ru")))
    (is (= (date 2017 8 30 19 5) (parse "30 Август 2017, 19:05" nil "ru")))
    (is (= (date 2017 7 26 10 0) (parse "26 Июль 2017, 10:00" nil "ru")))
    (is (= (date 2017 6 29 10 0) (parse "29 Июнь 2017, 10:00" nil "ru")))
    (is (= (date 2017 5 30 10 16) (parse "30 Май 2017, 10:16" nil "ru")))
    (is (= (date 2018 1 31 13 8) (parse "31 Январь 2018, 13:08" nil "ru")))
    (is (= (date 2017 12 29 19 31) (parse "29 Декабрь 2017, 19:31" nil "ru")))
    ;сегодня15:23
    ; вчера в 22:06
    ))
