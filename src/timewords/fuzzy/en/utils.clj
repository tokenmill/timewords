(ns timewords.fuzzy.en.utils
  (:require [clojure.string :as s]))

(defn- remove-day-names
  "Remove all language specific patterns which usualy accompany publication date"
  [date]
  (-> date
      (s/replace #"(sunday|monday|tuesday|wednesday|thursday|friday|saturday)" "")))

(defn- clean-with-regex [^String date]
  (reduce #(s/replace %1 %2 "") date [#"[|>;]" #"^[,;\.]"]))

(defn clean [date]
  (-> date
      ;(remove-day-names)
      (clean-with-regex)
      (s/trim)))