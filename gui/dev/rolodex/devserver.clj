(ns rolodex.devserver
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [figwheel-sidecar.system :as figwheel]
            [hawk.core :as hawk]
            [rolodex.ring-proxy :refer [wrap-proxy]]))

(def api-proxy
  (-> (constantly nil)
      (wrap-proxy "/api" "http://localhost:3000/")))

(defn handle-change-events [_ctx event]
  (->> event
       (filter #(= (:kind %) :modify))
       (map (comp :file str))
       (filter #(.endsWith % ".clj"))
       (run! load-file)))

(defn instant-reload-component [watch-paths]
  (hawk/watch!
   [{:paths watch-paths
     :handler handle-change-events}]))

(defn ->system [garden-paths css-paths]
  (component/system-map
   :figwheel-system (figwheel/figwheel-system (-> (figwheel/fetch-config)
                                                  :data
                                                  ;; ring-handler is already configured in project.clj
                                                  ;; but not being correctly picked up
                                                  (assoc-in [:figwheel-options :ring-handler] api-proxy)))
   :css-watcher (figwheel/css-watcher {:watch-paths css-paths})
   :garden-watcher (instant-reload-component garden-paths)))

(defonce system (->system ["src/rolodex/styles.clj"]
                          ["resources/public/css"]))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system component/stop))

(defn restart []
  (stop)
  (start))

(defn -main [& _]
  (start))
