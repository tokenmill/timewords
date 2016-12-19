(ns timewords.core-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda :refer [date-time]]
            [clj-time.coerce :as tc]
            [timewords.core :refer :all])
  (:import (org.joda.time DateTime)))

(defn date [& xs] (.toDate (apply date-time xs)))

(deftest core-test

  (testing "Edge cases"
    (is (nil? (parse nil)))
    (is (nil? (parse ""))))

  (testing "Standard date parsing"
    (is (= (date 2013 3 12) (parse "        2013-03-12    ")))
    (is (= (date 2012 3 14 16 40) (parse "2012-03-14 16:40")))
    (is (= (date 2016 2 8 9 41) (parse "08/02/16 09:41")))
    (is (= (date 2000 9 14 13 19) (parse "14/09/2000, 13:19")))
    (is (= (date 2000 9 14) (parse "14/09/2000")))
    (is (= (date 2015 7 24 9 38) (parse "2015-07-24, 09:38")))
    (is (= (date 2012 3 14 16 40 10) (parse "2012-03-14 16:40:10")))
    (is (= (date 2014 12 22 22 19 48) (parse "2014-12-22T22:19:48+02:00")))
    ;; to fix
    (is (= (date 1865 04 27 5 0 0) (parse "1865-04-27T00:00:00-05:00")))
    (is (= (date 2009 1 8) (parse "2009/01/08")))
    (is (= (date 2013 3 12) (parse "2013-03-12")))
    (is (= (date 2010 8) (parse "August 2010")))
    (is (= (date 2013) (parse "2013"))))

  (testing "EN date parsing"
    (is (= (date 2015 10 19 21 44) (parse "Mon Oct 19, 2015 5:44pm EDT")))
    (is (= (date 2015 10 19 22 44) (parse "Mon Oct 19, 2015 5:44pm EST")))
    (is (= (date 2015 10 19 21 44) (parse "Mon Oct 19, 2015 5:44PM EDT")))
    (is (= (date 2015 10 19 16 44) (parse "Mon Oct 19, 2015 12:44pm EDT")))
    (is (= (date 2015 10 19 12 30) (parse "Monday 19 October 2015 13.30 BST")))
    (is (= (date 2015 10 19 11 51) (parse "Oct. 19, 2015 11:51 AM ET")))
    (is (= (date 2015 10 20) (parse "20 October 2015")))
    (is (= (date 2013 1 18 13 8 57) (parse "Fri Jan 18 13:08:57 UTC 2013")))
    (is (= (date 2013 2 9) (parse "February 9, 2013")))
    (is (= (date 2013 2 12 14) (parse "Tuesday, February 12, 2013,  2:00 PM")))
    (is (= (date 2013 3 13 13 32) (parse "March 13, 2013 - 1:32PM")))
    (is (= (date 2013 2 9) (parse "February 9, 2013")))
    (is (= (date 2013 2 5 13 2) (parse "February 5, 2013, 1:02 PM")))
    (is (= (date 2013 2 6) (parse "2013-02-06")))
    (is (= (date 2013 2 7) (parse "February 07, 2013")))
    (is (= (date 2012 12 9 17 54 7) (parse "12/09/2012 09:54:07 AM PST")))
    (is (= (date 2013 2 4) (parse "Monday February  4, 2013")))
    (is (= (date 2013 1 30 9 14) (parse "01/30/2013 09:14")))
    (is (= (date 2013 1 28) (parse "2013-01-28")))
    (is (= (date 2013 1 25) (parse "2013-01-25")))
    (is (= (date 2013 1 25 5 58) (parse "2013-01-25 05:58:00.0")))
    (is (= (date 2013 3 26 9 28) (parse "2013-03-26T09:28-05")))
    (is (= (date 2013 1 24) (parse "2013/01/24")))
    (is (= (date 2013 1 24 8 46 54) (parse "2013-01-24T08:46:54Z")))
    (is (= (date 2013 1 24 1 24 31) (parse "2013/01/24 01:24:31")))
    (is (= (date 2013 1 22 1 31 5) (parse "2013/01/22 01:31:05")))
    (is (= (date 2013 1 23) (parse "2013-01-23T00:00:00+00:00")))
    (is (= (date 2013 1 20 0 1) (parse "Sun Jan 20 2013 00:01 UTC+0000")))
    (is (= (date 2013 1 20) (parse "Sunday 20 January 2013 00:00")))
    (is (= (date 2013 1 16 13 11 37) (parse "2013-01-16 08:11:37 EST")))
    (is (= (date 2013 1 17 17 17 23) (parse "2013-01-17T17:17:23Z")))
    (is (= (date 2013 1 18 12 21 41) (parse "2013-01-18 12:21:41 UTC")))
    (is (= (date 2013 1 18) (parse "Jan. 18, 2013")))
    (is (= (date 2013 1 18 13 8 57) (parse "Fri Jan 18 13:08:57 UTC 2013")))
    (is (= (date 2013 1 18) (parse "January 18, 2013")))
    (is (= (date 2013 1 12) (parse "2013/01/12")))
    (is (= (date 2015 6 1 23 53) (parse "June 1, 2015 11:53 pm")))
    (is (= (date 2013 1 18 4 57 51) (parse "2013-01-18T04:57:51.0000000Z")))
    (is (= (date 2015 11 16 20 13) (parse "Nov 16, 2015 3:13 p.m. ET")))
    (is (= (date 2015 11 13) (parse "13 Nov 2015")))
    (is (= (date 2015 11 16) (parse "Houston (Platts)--16 Nov 2015 517 pm EST/2217 GMT")))
    (is (= (date 2015 11 14 5 0) (parse "2015-11-14T00:00:00-05:00")))
    (is (= (date 2015 11 17 10 17) (parse "2015-11-17T05:17:00-05:00")))
    (is (= (date 2015 11 18) (parse "Wednesday, November 18, 2015")))
    (is (= (date 2015 11 18) (parse "11/18/2015")))
    (is (= (date 2015 11 20 10 33 36) (parse "2015-11-20T10:33:36")))
    (is (= (date 2015 11 12 13 35) (parse "November 12, 2015 1:35pm")))
    (is (= (date 2015 11 19) (parse "19 November 2015")))
    (is (= (date 2016 1 12 5 53) (parse "2016-01-12T05:53:00.000Z")))
    (is (= (date 2016 1 12 11 20) (parse "2016-01-12T11:20Z")))
    (is (= (date 2015 11 19 9 23) (parse "Nov 19, 2015 09:23 GMT")))
    (is (= (date 2015 11 17 0 10) (parse "17 Nov 2015 00:10")))
    (is (= (date 2015 11 17 0 10) (parse "17 Nov 2015 00:10")))
    (is (= (date 2015 11 17 16 1 37) (parse "2015-11-17T16:01:37+0000")))
    (is (= (date 2016 2 24 0 1) (parse "Wed Feb 24 2016 00:01 UTC+1201")))

    (let [delta 1000]
      ; now
      (is (> delta (- (tc/to-long (joda/now)) (tc/to-long (parse "now")))))
      (is (> delta (- (tc/to-long (joda/now)) (tc/to-long (parse "today")))))
      ; past
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/seconds 1))) (tc/to-long (parse "a sec ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/seconds 1))) (tc/to-long (parse "1 sec ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/seconds 1))) (tc/to-long (parse "1 second ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/seconds 16))) (tc/to-long (parse "16 secs ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/seconds 16))) (tc/to-long (parse "16 seconds ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/seconds 16))) (tc/to-long (parse "16s ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/minutes 1))) (tc/to-long (parse "a min ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/minutes 1))) (tc/to-long (parse "1 min ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/minutes 32))) (tc/to-long (parse "32 mins ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/minutes 1))) (tc/to-long (parse "a minute ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/minutes 1))) (tc/to-long (parse "1 minute ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/minutes 32))) (tc/to-long (parse "32m ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/minutes 32))) (tc/to-long (parse "32 minutes ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/hours 1))) (tc/to-long (parse "a hour ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/hours 1))) (tc/to-long (parse "an hour ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/hours 1))) (tc/to-long (parse "1h ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/hours 1))) (tc/to-long (parse "1 hour ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/hours 2))) (tc/to-long (parse "2 hours ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 1))) (tc/to-long (parse "yesterday")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 1))) (tc/to-long (parse "a day ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 1))) (tc/to-long (parse "1d ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 1))) (tc/to-long (parse "1 day ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 2))) (tc/to-long (parse "2 days ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 7))) (tc/to-long (parse "a week ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 7))) (tc/to-long (parse "1 week ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/days 21))) (tc/to-long (parse "3 weeks ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/months 1))) (tc/to-long (parse "a month ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/months 1))) (tc/to-long (parse "1 month ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/months 2))) (tc/to-long (parse "2 months ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/years 1))) (tc/to-long (parse "a year ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/years 1))) (tc/to-long (parse "1 year ago")))))
      (is (> delta (- (tc/to-long (joda/minus (joda/now) (joda/years 2))) (tc/to-long (parse "2 years ago")))))
      ; future
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/seconds 1))) (tc/to-long (parse "1 sec from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/seconds 1))) (tc/to-long (parse "1 second from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/seconds 16))) (tc/to-long (parse "16 secs from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/seconds 16))) (tc/to-long (parse "16 seconds from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/seconds 16))) (tc/to-long (parse "16s from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/minutes 1))) (tc/to-long (parse "a min from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/minutes 1))) (tc/to-long (parse "1 min from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/minutes 32))) (tc/to-long (parse "32 mins from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/minutes 1))) (tc/to-long (parse "a minute from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/minutes 1))) (tc/to-long (parse "1 minute from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/minutes 32))) (tc/to-long (parse "32m from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/minutes 32))) (tc/to-long (parse "32 minutes from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/hours 1))) (tc/to-long (parse "a hour from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/hours 1))) (tc/to-long (parse "an hour from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/hours 1))) (tc/to-long (parse "1h from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/hours 1))) (tc/to-long (parse "1 hour from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/hours 2))) (tc/to-long (parse "2 hours from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 1))) (tc/to-long (parse "tomorrow")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 1))) (tc/to-long (parse "a day from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 1))) (tc/to-long (parse "1d from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 1))) (tc/to-long (parse "1 day from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 2))) (tc/to-long (parse "2 days from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 7))) (tc/to-long (parse "a week from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 7))) (tc/to-long (parse "1 week from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/days 21))) (tc/to-long (parse "3 weeks from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/months 1))) (tc/to-long (parse "a month from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/months 1))) (tc/to-long (parse "1 month from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/months 2))) (tc/to-long (parse "2 months from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/years 1))) (tc/to-long (parse "a year from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/years 1))) (tc/to-long (parse "1 year from now")))))
      (is (> delta (- (tc/to-long (joda/plus (joda/now) (joda/years 2))) (tc/to-long (parse "2 years from now"))))))
    ; dealing with weekdays
    (is (= 1 (joda/day-of-week (tc/to-date-time (parse "last monday")))))
    (is (= 2 (joda/day-of-week (tc/to-date-time (parse "last tuesday")))))
    (is (= 3 (joda/day-of-week (tc/to-date-time (parse "last wednesday")))))
    (is (= 4 (joda/day-of-week (tc/to-date-time (parse "last thursday")))))
    (is (= 5 (joda/day-of-week (tc/to-date-time (parse "last friday")))))
    (is (= 6 (joda/day-of-week (tc/to-date-time (parse "last saturday")))))
    (is (= 7 (joda/day-of-week (tc/to-date-time (parse "last sunday")))))
    (is (= nil (parse "next monday")))
    (is (= nil (parse "next tuesday")))
    (is (= nil (parse "next wednesday")))
    (is (= nil (parse "next thursday")))
    (is (= nil (parse "next friday")))
    (is (= nil (parse "next saturday")))
    (is (= nil (parse "next sunday")))

    ; nonsenses should return nil
    (is (= nil (parse "makes no sense")))
    (is (= nil (parse "2013-40-12")))

    (is (= nil (parse "last year")))
    (is (= nil (parse "previous year")))
    (is (= nil (parse "next year")))))