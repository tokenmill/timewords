(ns timewords.fuzzy.lt.utils
  (:require [clojure.string :as s]))

(defn clean
  "Remove all language specific patterns which usualy accompany publication date"
  [^String d]
  (-> d
      (s/replace #"(Autorius|Publikuota):" "")
      (s/replace #", atnaujinta 20.*" "")))
