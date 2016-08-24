(ns rolodex.core
  (:require [clojure.edn :as edn]
            [compojure.core :refer [DELETE GET POST PUT routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [rolodex.generator :as gen])
  (:import java.util.UUID))

(defn random-uuid []
  (java.util.UUID/randomUUID))

(defn uuid [s]
  (UUID/fromString s))

(def contacts (atom (into {} (map (juxt :id identity) gen/contacts))))

(defn remove-contact! [id]
  (swap! contacts dissoc id))

(defn add-contact! [contact]
  (swap! contacts assoc (:id contact) contact)
  contact)

(def handler*
  (routes
   (GET "/" []
     {:status 200 :body (vals @contacts)})
   (POST "/" {body :body}
     {:status 200
      :body (-> body
                (assoc :id (random-uuid))
                add-contact!)})
   (GET "/:id" [id]
     {:status 200
      :body (contacts (uuid id))})
   (PUT "/:id" {body :body}
     {:status 200
      :body (add-contact! body)})
   (DELETE "/:id" [id]
     (remove-contact! (uuid id))
     {:status 200})))

(defn wrap-edn [handler]
  (fn [req]
    (-> (handler (update req :body (comp edn/read-string slurp)))
        (assoc :headers {"Content-Type" "application/edn; charset=UTF-8"})
        (update :body prn-str))))

(def handler (wrap-edn handler*))

(defonce server
  (run-jetty #'handler {:port 3000
                        :join? false}))
