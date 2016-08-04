(ns rolodex.core
  (:require [ring.adapter.jetty :refer [run-jetty]]))

(defn random-uuid []
  (java.util.UUID/randomUUID))

(def contacts (atom (let [id (random-uuid)]
                      {id {:id id
                           :full-name "Trish Turtle"
                           :skills ["LISP" "Lambda Calculus"]}})))

(defn handler "request -> response"
  [req]
  {:status 200
   :headers {"Content-Type" "text/plain; charset=UTF-8"}
   :body "Babby's first Ring app! More Improved!"})

(defonce server
  (run-jetty #'handler {:port 3000
                        :join? false}))
