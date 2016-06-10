(ns app.web
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [clojure.java.io :as io]
            [clj-http.client :as client]))

(defn index-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Welcome to Clojure API"})

; NOTE: see https://github.com/Giphy/GiphyAPI
(defn data-handler [request]
  (let [query {:q (get-in request [:params :q]) :api_key "dc6zaTOxFJmzC"}
        response (client/get "http://api.giphy.com/v1/gifs/search"
                    {:query-params query :as :json})
        data (get-in response [:body :data 0 :images])]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body data}))

(defn missing-handler [request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (slurp (io/resource "404.html"))})

(def routes [
    {:methods #{:get} :path "/" :handler index-handler}
    {:methods #{:get} :path "/data" :handler data-handler}
  ])

(defn route-match? [request route]
  (and ((:methods route) (:request-method request))
       (= (:path route) (:uri request))))

(defn app [request]
  (let [route (first (filter (partial route-match? request) routes))
        handler (get route :handler missing-handler)]
    (println "app request " (:request-method request) (:uri request) (pr-str route))
    (handler request)))

(defn with-middleware [handler]
  (-> handler
    (wrap-reload)
    (wrap-keyword-params)
    (wrap-json-params {})
    (wrap-params {})
    (wrap-json-response {:pretty true})))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") 5000))]
    (jetty/run-jetty (with-middleware app) {:port port :join? false})))
