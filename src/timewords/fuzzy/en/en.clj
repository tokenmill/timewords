(ns timewords.fuzzy.en.en
  (:require [clojure.string :as s]
            [clj-time.core :as joda]
            [timewords.fuzzy.en.relative :refer [parse-relative-date]]
            [timewords.fuzzy.en.absolute :refer [parse-absolute-date]])
  (:import (org.joda.time DateTime)))

(defn is-relative-date? [s]
  (if (or (not (re-find #"\d" s))
          (re-find #"ago|yesterday|tomorrow|last|previous|next|now" s))
    true
    false))

(defn parse-date [^String s & [^DateTime document-time]]
  (let [ls (s/lower-case s)
        document-time (or document-time (joda/now))]
    (if (is-relative-date? ls)
      (parse-relative-date ls document-time)
      (parse-absolute-date ls document-time))))
