(defproject clojure-api "1.0.0"
  :description "A barebones Clojure REST API, for learning purposes."
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-jetty-adapter "1.4.0"]]
  :main app.web
  :min-lein-version "2.0.0"
  :uberjar-name "clojure-api-standalone.jar"
  :profiles {:production {:env {:production true}}})
