(ns rolodex.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [rolodex.generator :as gen]))

(defonce contacts (atom (into {} (map (juxt :id identity)
                                      (map gen/contact gen/names)))))

(defn handler "request -> response"
  [req]
  (if (and (= (:request-method req) :get) (= (:uri req) "/"))
    {:status 200
     :headers {"Content-Type" "application/edn; charset=UTF-8"}
     :body (prn-str (vals @contacts))}))

(defonce server
  (run-jetty #'handler {:port 3000
                        :join? false}))
