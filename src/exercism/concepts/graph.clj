(ns web-dev.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [response content-type]]
            [clojure.pprint     :as pprint]
            [web-dev.graph :as graph]
            [clojure.data.json :as json]
            [com.phronemophobic.clj-graphviz :refer [render-graph]]
            [hiccup.page :refer [html5]]))

(defn exercises [track]
  (let [url (str "https://raw.githubusercontent.com/exercism/"
                 track "/main/config.json")]
    (-> url
        slurp
        (json/read-str :key-fn keyword)
        :exercises
        :concept)))

(defn concept
  "Returns a sequence of the exercises that teach a concept
  or have it as a prerequisite, according to key provided."
  [track c k]
  (map :name (filter #(contains? (set (k %)) c) (exercises track))))

(defn nodes [track c]
  (mapv vector (cycle (concept track c :concepts))
       (concept track c :prerequisites)))

(defn render [track edges]
  (render-graph {:edges edges}
                {:filename (str "resources/public/" track ".png")}))

(defn page [name]
  (str "<html><body>"
       (if name
         (let [img (render name (vec (mapcat #(nodes name %) (set (mapcat :concepts (exercises name))))))]
           (str "<div> <h1> " name " syllabus</h1>
            <img src=" name ".png" ">"))
           (str "<form>"
                "Track name (e.g. javascript): <input name='name' type='text'>"
                "<input type='submit'>"
                "</form>"))
         "</body></html>"))

(page "clojure")

(defn handler [{{name "name"} :params}]
  (-> (response (page name))
      (content-type "text/html")))

(def app
  (-> handler
      (wrap-resource "public")
      wrap-params))

(run-jetty app {:port 80})
