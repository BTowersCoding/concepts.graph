(ns exercism.concepts.graph
  (:require [dorothy.core :as dot]
            [dorothy.jvm :as dot-jvm]))

(comment
  (-> (dot/digraph [[:a :b :c] [:b :d]])
      dot/dot
      (dot-jvm/save! "out.png" {:format :png}))
  )
