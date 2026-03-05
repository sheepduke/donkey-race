(ns donkey-race.rule
  (:require [clojure.set :as set]
            [taoensso.truss :refer [have]]
            [donkey-race.dice :as dice]
            [donkey-race.feed :as feed]
            [donkey-race.action :as action]))

(defn make-rules
  "Returns:
  {`::map-length` int
   `::map-vortexes` [vec int]
   `::player-initial-feeds` [vec feed]
   `::shop` [set feed]
   `::dice` [vec dice-def]
   `::feed->action` [map feed -> action]}"
  [{:keys [::map-length ::map-vortexes
           ::shop ::player-initial-feeds
           ::dice ::feed->action]}]
  {:pre [(have pos-int? map-length)
         (have #(<= % map-length) (last map-vortexes))]}
  {::map-length map-length
   ::map-vortexes (vec (sort map-vortexes))
   ::player-initial-feeds (vec player-initial-feeds)
   ::shop (set shop)
   ::dice (vec dice)
   ::feed->action feed->action})

(def default-short-map
  {::map-length 50
   ::map-vortexes [2 4 7 11 14 17 21 24 27 30 34 36 39 42 44 47]})

(def default-long-map
  (ex-info "TODO" {}))

(def default-player-initial-feeds
  {::player-initial-feeds
   (concat [::feed/tomato1 ::feed/tomato2 ::feed/tomato4 ::feed/corn1]
           (repeat 4 ::feed/dream-grass))})

(def default-primary-feeds
  {::shop feed/primary-feeds})

(def default-extended-feeds
  {::shop feed/extended-feeds})

(def default-basic-feed->action
  {::dice dice/white-dice
   ::feed->action action/basic-feed->action})

(def default-advanced-feed->action
  {::dice dice/yellow-dice
   ::feed->action action/advanced-feed->actions})
