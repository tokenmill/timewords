(defproject lt.tokenmill/timewords "0.4.0"
  :description "Library to parse time strings."

  :dependencies [[clj-time "0.12.2"]]

  :aot [timewords.core]

  :profiles {:dev {:dependencies  []}
             :provided {:dependencies [[org.clojure/clojure "1.5.1"]]}})
