# Clojure API

A barebones Clojure REST API, for learning purposes.

For a more extensive Clojure API example see my [Clojure CMS API](https://github.com/peter/clojure-cms-api).

The data endpoint uses the [GiphyAPI](https://github.com/Giphy/GiphyAPI).

## Getting Started

Check your Java version, you need 1.6 or later:

```
java -v
```

Install Clojure by installing the [Leiningen Build Tool](http://leiningen.org/#install)

Clone this repo and start the server:

```
git clone git@github.com:peter/clojure-api.git
cd clojure-api
lein run
```

Test the API:

```
curl http://localhost:5000/data?q=cat
```

## Deploying to Heroku

First install the [Heroku Toolbelt](https://toolbelt.heroku.com/) and create a Heroku account. Then you can create an app and deploy:

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## How to create an API from scratch with Leiningen

```
lein new app clojure-api
cd clojure-api
```

Edit project.clj and add to :dependencies:

```clojure
[ring/ring-jetty-adapter "1.4.0"]
```

Also in project.clj, add these lines:

```clojure
:min-lein-version "2.0.0"
:uberjar-name "clojure-api-standalone.jar"
```

Edit src/clojure_api/core.clj to read:

```clojure
(ns clojure-api.core
  (:require [ring.adapter.jetty :as jetty]))

(defn app [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Welcome to Clojure API"})

(defn -main []
  (jetty/run-jetty app {
    :port (Integer. (get (System/getenv) "PORT" 5000))
    :join? false}))
```

Run the app locally:

```
lein run
```

Visit [localhost:5000](http://localhost:5000) in your browser.

To deploy to Heroku, make sure you have a Heroku account and the Heroku toolbelt installed. Then add a Procfile with this line:

```
web: java $JVM_OPTS -cp target/uberjar/clojure-api-standalone.jar clojure.main -m clojure-api.core
```

Check that your Procfile works locally:

```
heroku local web
```

Add your app to git and deploy:

```
git init .
git add .
git commit -m "first commit"
heroku create
git push heroku master
heroku open
heroku logs --tail
```

## Resources

* [Clojure on Heroku](https://devcenter.heroku.com/categories/clojure)
