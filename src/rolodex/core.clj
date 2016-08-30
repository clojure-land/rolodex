(ns rolodex.core
  (:require [clojure.edn :as edn]
            [ring.adapter.jetty :refer [run-jetty]]
            [rolodex.generator :as gen]))

(defonce contacts (atom (into {} (map (juxt :id identity)
                                      (map gen/contact gen/names)))))

(defn- get-all-contacts []
  {:status 200
   :headers {"Content-Type" "application/edn; charset=UTF-8"}
   :body (prn-str (vals @contacts))})

(defn- create-new-contact [req]
  (if-let [instream (:body req)]
    (let [body (slurp instream)
          contact (edn/read-string body)]
      (swap! contacts assoc (:id contact) contact)

      {:status 201
       :headers {"Content-Type" "application/edn; charset=UTF-8"}
       :body (prn-str contact)})))

(defn handler "request -> response"
  [req]
  (if (= (:uri req) "/")
    (case  (:request-method req)
      :get (get-all-contacts)
      :post (create-new-contact req))))

(defonce server
  (run-jetty #'handler {:port 3000
                        :join? false}))
