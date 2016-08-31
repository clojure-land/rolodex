(ns rolodex.core
  (:require [clojure.edn :as edn]
            [ring.adapter.jetty :refer [run-jetty]]
            [rolodex.generator :as gen])
  (:import java.util.UUID))

(defonce contacts (atom (into {} (map (juxt :id identity)
                                      (map gen/contact gen/names)))))

(defn- get-all-contacts []
  {:status 200
   :body (vals @contacts)})

(defn- create-new-contact [req]
  (let [contact (:params req)]
    (swap! contacts assoc (:id contact) contact)
    {:status 201
     :body contact}))

(defn- parse-params [req]
  (if-let [instream (:body req)]
    (assoc req :params (-> instream slurp edn/read-string))
    req))

(defn wrap-edn "handler -> handler"
  [handler]
  (fn [req]
    (let [req (parse-params req)
          res (handler req)]
      (if res
        (-> res
            (assoc-in [:headers "Content-Type"] "application/edn; charset=UTF-8")
            (update :body prn-str))))))

(def uuid-uri-pattern #"\A/(\p{XDigit}{8}-\p{XDigit}{4}-\p{XDigit}{4}-\p{XDigit}{4}-\p{XDigit}{12})\z")

(defn- get-contact [uuid]
  {:status 200
   :body (@contacts uuid)})

(defn handler "request -> response"
  [req]
  (let [uri (:uri req)
        method (:request-method req)]
    (if (= uri "/")
      (case method
        :get (get-all-contacts)
        :post (create-new-contact req))
      (if-let [uuid (second (re-find uuid-uri-pattern uri))]
        (let [uuid (UUID/fromString uuid)]
          (case method
            :get (get-contact uuid)))))))

(defonce server
  (run-jetty (wrap-edn #'handler) {:port 3000
                                   :join? false}))
