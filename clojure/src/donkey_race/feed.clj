(ns donkey-race.feed
  (:require [taoensso.truss :refer [have]]))

(def name->feed
  "All the supported feeds."
  {::tomato1 {:type ::tomato, :value 1, :price 1}
   ::tomato2 {:type ::tomato, :value 2, :price 2}
   ::tomato4 {:type ::tomato, :value 4, :price 3}
   ::corn1 {:type ::corn, :value 1, :price 1}
   ::corn2 {:type ::corn, :value 2, :price 2}
   ::corn4 {:type ::corn, :value 4, :price 3}
   ::cabbage1 {:type ::cabbage, :value 1, :price 1}
   ::cabbage2 {:type ::cabbage, :value 2, :price 2}
   ::cabbage4 {:type ::cabbage, :value 4, :price 3}
   ::grape1 {:type ::grape, :value 1, :price 1}
   ::grape2 {:type ::grape, :value 2, :price 2}
   ::grape4 {:type ::grape, :value 4, :price 3}
   ::prune {:type ::prune, :value 5, :price 4}
   ::carrot {:type ::carrot, :value 5, :price 4}
   ::radish {:type ::radish, :value 8, :price 5}
   ::dream-grass {:type ::dream-grass, :value 0, :price 0}})

(def primary-feeds
  "Primary feed set."
  #{::tomato1 ::corn1 ::cabbage1 ::grape1
    ::tomato2 ::corn2 ::cabbage2 ::grape2
    ::tomato4 ::corn4 ::cabbage4 ::grape4
    ::radish})

(def extended-feeds
  "Extended feed set."
  #{::tomato1 ::corn1 ::cabbage1 ::grape1
    ::tomato2 ::corn2 ::cabbage2 ::grape2
    ::tomato4 ::corn4 ::cabbage4 ::grape4
    ::radish ::prune ::carrot})

(defn feed? [feed]
  (contains? name->feed feed))

(defn type [feed]
  (have name->feed feed)
  (get-in name->feed [feed :type]))

(defn value [feed]
  (have name->feed feed)
  (get-in name->feed [feed :value]))

(defn price [feed]
  (have name->feed feed)
  (get-in name->feed [feed :price]))
