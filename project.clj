(defproject lt.tokenmill/timewords "0.5.0"
  :description "Library to parse time strings."

  :dependencies [[clj-time "0.14.4"]]

  :aot [timewords.core]

  :profiles {:dev {:dependencies  []}
             :provided {:dependencies [[org.clojure/clojure "1.8.0"]]}})
