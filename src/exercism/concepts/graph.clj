(ns exercism.concepts.graph
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :refer [wrap-resource]]
            [com.phronemophobic.clj-graphviz :refer [render-graph]]
            [hiccup.page :refer [html5]]
            [clojure.data.json :as json]))

(def exercises (:concept (:exercises (json/read-str (slurp "https://raw.githubusercontent.com/exercism/clojure/main/config.json") :key-fn keyword))))

(defn concept
  "Returns a sequence of the exercises that teach a concept
  or have it as a prerequisite, according to key provided."
  [c k]
  (map :name (filter #(contains? (set (k %)) c) exercises)))

(defn nodes [c]
  (mapv vector (cycle (concept c :concepts))
       (concept c :prerequisites)))

(render-graph {:edges (vec (mapcat nodes (set (mapcat :concepts exercises))))}
               {:filename "resources/public/graph.png"})

(def page
  (html5 {:lang "en"}
    [:body [:div [:h1 "Clojure syllabus"]
                 [:img {:src "graph.png"}]]]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body page})

(defn -main []
  (jetty/run-jetty (wrap-resource handler "public") {:port 80}))