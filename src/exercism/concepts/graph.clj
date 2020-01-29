(ns exercism.concepts.graph
  (:require [dorothy.core :as dot]
            [dorothy.jvm :as dot-jvm]))

(def clojure-concepts
  [["Simple Values" ["Numbers" "Booleans" "Symbols" "Keywords" "Strings" "Characters" "Regular Expressions"]]
   ["Operations" ["Flow Control" "Type Inspection" "Concurrency"]]
   ["Functions" ["General" "Multifunctions" "Macros" "Java Interop" "Proxies"]]
   ["Collections" ["Colls" "Vectors" "Lists" "Maps" "Sets" "Structs" "Sequences" "Transients"]]
   ["Code Structure" ["Variables" "Namespaces" "Hierarchies" "User Defined Types" "Metadata"]]
   ["Environment" ["Require" "Import" "Code" "IO" "REPL"]]])

(defn groups [v]
  (for [[group _] v]
    ["Clojure Concepts" group]))

(defn concepts-graph [v]
  (for [[group concepts] v]
    (for [concept concepts]
      [group concept])))

(comment
(groups clojure-concepts)
  (-> (dot/digraph (concat [{:rankdir :LR}] (into (groups clojure-concepts) (concepts-graph clojure-concepts))))
      dot/dot
      (dot-jvm/save! "clojure-concepts.png" {:format :png})))
  
