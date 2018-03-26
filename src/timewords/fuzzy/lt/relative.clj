(ns timewords.fuzzy.lt.relative
  (:import (org.joda.time DateTime)))

(defn parse-relative-date
  [^String s ^DateTime document-time]
  (prn ">>" s document-time)
  nil)