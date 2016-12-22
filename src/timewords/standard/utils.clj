(ns timewords.standard.utils
  (:require [clojure.string :as s]))

(defn- clean-with-regex [^String date]
  (reduce #(s/replace %1 %2 "") date [#"[|>;]" #"^[,;\.]"]))

(defn clean [^String date]
  (-> date
      ;(remove-day-names)
      (clean-with-regex)
      (s/trim)))