(ns timewords.formats
  (:import (java.text SimpleDateFormat)
           (java.util TimeZone)))

(defn- pattern->formatter [pattern-def]
  (let [pattern (if (string? pattern-def) pattern-def (first pattern-def))
        lenient (if (string? pattern-def) false (second pattern-def))]
    (doto (SimpleDateFormat. pattern)
      (.setTimeZone (TimeZone/getTimeZone "GMT"))
      (.setLenient lenient))))

(def supported-patterns
  ["yyyy-MM-dd'T'HH:mm:ss.sssssssZ"
   "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
   "yyyy-MM-dd'T'HH:mm:ss.SSS"
   "yyyy-MM-dd'T'HH:mm-ss:SS"
   "yyyy-MM-dd'T'HH:mm:ssX"
   "yyyy-MM-dd'T'HH:mm:ssZZ"
   "yyyy-MM-dd'T'HH:mm:ss'Z'"
   "yyyy-MM-dd'T'HH:mm:ss"
   "yyyy-MM-dd'T'HH:mmZ"
   "yyyy-MM-dd'T'HH:mmz"
   "yyyy-MM-dd'T'HH:mm"
   "yyyy-MM-dd HH:mm:ss ZZZ"
   "yyyy-MM-dd HH:mm:ss.S"
   "yyyy-MM-dd HH:mm:ss"
   "yyyy-MM-dd HH:mm"
   "yyyy-MM-dd, HH:mm"
   "yyyy-MM-dd"
   "MMMM dd, yyyy - h:mm a z"
   "MMMM dd, yyyy - h:mma"
   "MMMM dd, yyyy, hh:mm:ss a z"
   "MMMM dd, yyyy, hh:mm a"
   "MMMM dd, yyyy hh:mm:ss a z"
   "MMMM dd, yyyy hh:mm a z"
   "MMMM dd, yyyy HH:mm z"
   "MMMM dd, yyyy hh:mm a"
   "MMMM dd, yyyy h:mma"
   "MMMM dd, yyyy"
   "MMMM dd yyyy"
   "EEEE, MMMM d, yyyy h:mma z"
   "EEEE, MMMM d, yyyy, h:mm a"
   "EEEE, dd MMMM yyyy HH:mm:ss Z"
   "EEEE MMMM dd HH:mm:ss z yyyy"
   ["EEEE MMMM dd yy HH:mm z" true]
   "EEEE MMMM dd yyyy HH:mm 'UTC'Z"
   "EEEE MMMM dd, yyyy h:mma z"
   "EEEE dd MMMM yyyy - h:mma"
   "EEEE dd MMMM yyyy h:mma z"
   "EEEE dd MMMM yyyy HH.mm z"
   "EEEE dd MMMM yyyy HH:mm"
   "EEEE dd MMMM yyyy"
   "yyyyMMdd'T'HHmmss.SSSZ"
   "yyyyMMdd'T'HHmmssZ"
   "yyyyMMddHH"
   "yyyyMMdd"
   "yyyy/MM/dd hh:mm:ss a"
   "yyyy/MM/dd HH:mm:ss"
   "yyyy/MM/dd"
   "yyyy/MMM/dd"
   "yyyy/MMdd"
   "dd.MM.yyyy"
   "dd MMMM, yyyy, HH:mm"
   "dd'th' MMMM yyyy"
   "dd'st' MMMM yyyy"
   "dd'rd' MMMM yyyy"
   "dd MMMM yyyy HH:mm"
   "dd MMMM yyyy"
   "dd-MM-yyyy"
   "yyyy.MM.dd, HH:mm"
   "yyyy.MM.dd HH:mm"
   "yyyy.MM.dd"
   "MM/dd/yyyy hh:mm:ss a ZZZ"
   "MM/dd/yyyy hh:mm:ss a z"
   "MM/dd/yyyy hh:mm a z"
   "MM/dd/yyyy HH:mm"
   ["dd/MM/yyyy, hh:mm" true]
   "dd/MM/yy HH:mm"
   "MM/dd/yyyy"
   "dd/MM/yyyy"
   "h:mm a z 'on' MMMM dd, yyyy"
   "HH:mm MMMM dd, yyyy"])

(def formatters
  (map pattern->formatter supported-patterns))

(defn parse [^String text]
  (for [formatter formatters
        :let [parsed (try
                       (.parse formatter text)
                       (catch Exception _ nil))]
        :when parsed] parsed))
