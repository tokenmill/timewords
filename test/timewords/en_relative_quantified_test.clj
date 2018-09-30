(ns timewords.en-relative-quantified-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as joda]
            [clj-time.coerce :as tc]
            [timewords.core :refer :all]))

(deftest en-relative-quantified
  (testing "some time ago"
    (let [parsed-datetime (tc/to-date-time (parse "now" (tc/to-date (joda/now))))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 1000))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 1000)))))
    ; past
    (let [parsed-datetime (tc/to-date-time (parse "a sec ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 2000))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/millis 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 sec ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 2000))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/millis 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a second ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 2000))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/millis 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 second ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 2000))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/millis 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "16 secs ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 17000))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/millis 15000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "16 seconds ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 17000))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/millis 15000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "16s ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/millis 17000))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/millis 15000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a min ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/minutes 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/minutes 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 min ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/minutes 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/minutes 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a minute ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/minutes 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/minutes 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 minute ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/minutes 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/minutes 0))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "32 mins ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/minutes 33))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/minutes 31))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "32m ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/minutes 33))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/minutes 31))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "32 minutes ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/minutes 33))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/minutes 31))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a hour ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/hours 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/hours 0))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "an hour ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/hours 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/hours 0))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1h ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/hours 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/hours 0))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 hour ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/hours 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/hours 0))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 hours ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/hours 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/hours 1))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "3 hrs ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/hours 4))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/hours 1))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "yesterday"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 0))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a day ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 0))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1d ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 0))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 day ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 0))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 days ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 1))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a week ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 8))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 6))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 week ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 8))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 6))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "3 weeks ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/days 22))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/days 20))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "last month"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/months 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/months 1))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "previous month"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/months 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/months 1))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a month ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/months 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/months 1))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 month ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/months 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/months 1))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 months ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/months 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/months 1))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "last year"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/years 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/years 0))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "previous year"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/years 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/years 0))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a year ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/years 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/years 0))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 year ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/years 2))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/years 0))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 years ago"))]
      (is (joda/after? parsed-datetime (joda/minus (joda/now) (joda/years 3))))
      (is (joda/before? parsed-datetime (joda/minus (joda/now) (joda/years 1))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime)))))

  (testing "some time from now"
    (let [parsed-datetime (tc/to-date-time (parse "a sec from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/millis 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 2000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 sec from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/millis 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 2000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a second from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/millis 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 2000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 second from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/millis 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 2000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "16 secs from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/millis 15000))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 17000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "16 seconds from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/millis 15000))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 17000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "16s from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/millis 15000))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/millis 17000))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a min from now"))]
      (is (joda/after? parsed-datetime (joda/now)))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 2))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 min from now"))]
      (is (joda/after? parsed-datetime (joda/now)))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 2))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a minute from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/minutes 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 2))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 minute from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/minutes 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 2))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "32 mins from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/minutes 31))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 33))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "32 minutes from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/minutes 31))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 33))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "32m from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/minutes 31))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 33))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "32 minutes from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/minutes 31))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/minutes 33))))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a hour from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/hours 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/hours 2))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "an hour from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/hours 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/hours 2))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1h from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/hours 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/hours 2))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 hour from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/hours 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/hours 2))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 hours from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/hours 1))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/hours 3))))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "tomorrow"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 2))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a day from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 2))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1d from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 2))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 day from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 2))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 days from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 1))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 3))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a week from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 6))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 8))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 week from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 6))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 8))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "3 weeks from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/days 20))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/days 22))))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "next month"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/months 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/months 2))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a month from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/months 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/months 2))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 month from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/months 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/months 2))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 months from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/months 1))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/months 3))))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "a year from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/years 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/years 2))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "1 year from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/years 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/years 2))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "next year"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/years 0))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/years 2))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))
    (let [parsed-datetime (tc/to-date-time (parse "2 years from now"))]
      (is (joda/after? parsed-datetime (joda/plus (joda/now) (joda/years 1))))
      (is (joda/before? parsed-datetime (joda/plus (joda/now) (joda/years 3))))
      (is (= 1 (joda/month parsed-datetime)))
      (is (= 1 (joda/day parsed-datetime)))
      (is (= 0 (joda/hour parsed-datetime)))
      (is (= 0 (joda/minute parsed-datetime)))
      (is (= 0 (joda/milli parsed-datetime))))))
