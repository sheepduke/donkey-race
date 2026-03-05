(ns donkey-race.dice
  (:require [donkey-race.feed :as feed]))

(def white-dice
  [[::move 1]
   [::move 3]
   [::give-feed ::feed/tomato2]
   [::give-feed ::feed/cabbage2]
   [::give-gem 1]
   [::pick-feed]])

(def yellow-dice
  [[::move 0]
   [::move 1]
   [::move 1]
   [::move 2]
   [::move 3]
   [::move 4]])

(defn roll [dice]
  (rand-nth dice))
