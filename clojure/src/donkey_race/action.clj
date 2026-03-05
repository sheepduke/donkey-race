(ns donkey-race.action
  (:require [donkey-race.feed :as feed]))

(def all-actions
  #{;; Tomato
    ::give-gem-by-vortex
    ::give-gem-by-price

    ;; Corn
    ::roll-dice
    ::roll-dice-by-vortex

    ;; Cabbage
    ::put-back-feed
    ::pick-feed

    ;; Grape
    ::jump-to-vortex
    ::upgrade-grape

    ;; Prune
    ::move-by-gem-count
    ::give-feed-by-vortex

    ;; Carrot
    ::add-clover-by-vortex
    ::add-clover-by-carrot-count})

(def basic-feed->action
  {::feed/tomato ::give-gem-by-vortex
   ::feed/corn ::roll-dice
   ::feed/cabbage ::put-back-feed
   ::feed/grape ::jump-to-vortex
   ::feed/prune ::move-by-gem-count
   ::feed/carrot ::add-clover-by-vortex})

(def advanced-feed->actions
  {::feed/tomato ::give-gem-by-price
   ::feed/corn ::roll-dice-by-vortex
   ::feed/cabbage ::pick-feed
   ::feed/grape ::upgrade-grape
   ::feed/prune ::give-feed-by-vortex
   ::feed/carrot ::add-clover-by-carrot-count})
