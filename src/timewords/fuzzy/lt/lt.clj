(ns timewords.fuzzy.lt.lt
  (:require [clj-time.core :as joda]
            [timewords.fuzzy.lt.absolute :refer [parse-absolute-date]]
            [timewords.fuzzy.lt.relative :refer [parse-relative-date]]
            [timewords.fuzzy.lt.utils :refer [clean]])
  (:import (org.joda.time DateTime)))

(defn relative-date?
  "No relative date support."
  [s]
  false)

(defn parse-date [^String s & [^DateTime document-time]]
  (let [document-time (or document-time (joda/now))
        s (clean s)]
    (if (relative-date? s)
      (parse-relative-date s document-time)
      (parse-absolute-date s document-time))))
