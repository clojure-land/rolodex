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
  (if (and (= (:request-method req) :get) (= (:uri req) "/"))
    {:status 200
     :headers {"Content-Type" "application/edn; charset=UTF-8"}
     :body (prn-str (vals @contacts))}))

(defonce server
  (run-jetty #'handler {:port 3000
                        :join? false}))
