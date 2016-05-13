(ns app.web
  (:require [ring.adapter.jetty :as jetty]
            [clojure.java.io :as io]))

(defn index-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Welcome to Clojure API"})

(defn missing-handler [request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (slurp (io/resource "404.html"))})

(def routes [
    {:methods #{:get} :path "/" :handler index-handler}
  ])

(defn route-match? [request route]
  (and ((:methods route) (:request-method request))
       (= (:path route) (:uri request))))

(defn app [request]
  (let [route (first (filter (partial route-match? request) routes))
        handler (get route :handler missing-handler)]
    (println "app request " (:request-method request) (:uri request) (pr-str route))
    (handler request)))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") 5000))]
    (jetty/run-jetty app {:port port :join? false})))
