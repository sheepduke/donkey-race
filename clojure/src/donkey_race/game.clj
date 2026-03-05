(ns donkey-race.game
  (:require [malli.core :as m]
            [clojure.core.match :refer [match]]
            [clojure.tools.logging :as log]
            [taoensso.truss :refer [have]]
            [donkey-race.util :refer :all]
            [donkey-race.player :as player]
            [donkey-race.feed :as feed]
            [donkey-race.rule :as rule]
            [donkey-race.dice :as dice]
            [donkey-race.action :as action]))

(defn make-game [{:keys [players rules]}]
  {::players (into {} (map (fn [player] [(::player/name player) player]) players))
   ::rules rules})

;; ============================================================
;;  Utility
;; ============================================================

(defn on-vortex? [player rules]
  (boolean (some #{(::player/position player)} (::rule/map-vortexes rules))))

(defn next-vortex [player rules]
  (let [position (::player/position player)
        next-position (first (filter #(> % position) (::rule/map-vortexes rules)))]
    (if next-position
      next-position
      position)))

(defn last-player? [game player]
  (== (apply min (map ::player/position (vals (::players game))))
      (::player/position player)))

;; ============================================================
;;  Actions
;; ============================================================

(declare act)

(defn move-by-value [player feed]
  (player/move player (feed/value feed)))

(defn give-gem-by-vortex [player rules]
  (player/inc-counter-gem player
                        (if (on-vortex? player rules)
                          2 1)))

(defn give-gem-by-price [player feed rules]
  (player/inc-counter-gem player (feed/price feed)))

(defn roll-dice [player rules]
  (let [dice (::rule/dice rules)]
    (case (dice/roll dice)
      )))

(defn roll-dice-by-vortex [player rules]
  (-> player
      (roll-dice rules)
      (roll-dice rules)))

(defn roll-dice-by-vortex [player rules]
  (-> player
      (roll-dice rules)
      (roll-dice rules)))

(defn put-back-feed [player rules]
  (let [return-fn (::player/strategy-return player)
        feed (return-fn player rules)]
    (-> player 
        (player/spew-feed feed)
        (player/add-feeds feed))))

(defn pick-feed [player rules]
  (let [{feed ::feed player ::player} (player/pick-feed player)]
    (-> player
        (move-by-value feed)
        (player/eat-feeds feed)
        (act feed rules))))

(defn jump-to-vortex [player rules]
  (player/move-to player (next-vortex player rules)))

(defn upgrade-grape
  "Removes feed from stomach, upgrade it and add it back."
  [player feed rules]
  {:pre [(have [:el #{::feed/grape1 ::feed/grape2 ::feed/grape4}] feed)]}
  (let [new-feed (case feed
                   ::feed/grape1 ::feed/grape2
                   ::feed/grape2 ::feed/grape4
                   ::feed/grape4 ::feed/radish)]
    (assoc player ::player/stomach
           (concat (remove-last (::player/stomach player) feed)
                   [new-feed]))))

(defn move-by-gem-count [player rules]
  (player/move player (::player/counter-gem player)))

(defn give-feed-by-vortex [player rules]
  (let [choices (if (on-vortex? player rules)
                  (filter #(= 4 (feed/value %)) (::rule/shop rules))
                  (filter #(= 2 (feed/value %)) (::rule/shop rules)))
        ask-fn (::player/strategy-take player)]
    (player/add-feeds player (ask-fn player choices rules))))

(defn add-clover-by-vortex [player rules]
  (player/inc-counter-clover player
                             (if (on-vortex? player rules) 2 1)))

(defn add-clover-by-carrot-count [player rules]
  (let [carrot-count (count (filter #(= (::feed/type %) ::carrot)
                                    (::player/stomach player)))]
    (player/inc-counter-clover player carrot-count)))

(defn sleep [game player rules]
  (if (>= (::player/dream-grass-count player) 3)

    ;; Go shopping when there are 3 dream grasses.
    (-> player
        (player/move (::player/counter-clover player))
        (::player/shopping-fn player))

    ;; Or just increase the count.
    (player/inc-counter-dream-grass player)))

(defn act [player feed rules]
  (case (get (::rule/feed->action rules) (feed/type feed))
    ::action/give-gem-by-vortex (give-gem-by-vortex player rules)
    ::action/give-gem-by-price (give-gem-by-price player feed rules)
    ::action/roll-dice (roll-dice player rules)
    ::action/roll-dice-by-vortex (roll-dice-by-vortex player rules)
    ::action/put-back-feed (put-back-feed player rules)
    ::action/pick-feed (pick-feed player rules)
    ::action/jump-to-vortex (jump-to-vortex player rules)
    ::action/upgrade-grape (upgrade-grape player feed rules)
    ::action/move-by-gem-count (move-by-gem-count player rules)
    ::action/give-feed-by-vortex (give-feed-by-vortex player rules)
    ::action/add-clover-by-vortex (add-clover-by-vortex player rules)
    ::action/add-clover-by-carrot-count (add-clover-by-carrot-count player rules)))

(defn play
  "Make the player move."
  [game player-name]
  {:pre [(have #(contains? (::players game) %) player-name)]}
  (let [player ((::players game) player-name)
        rules (::rules game)
        {:keys [player feed]} (player/pick-feed player)
        player (-> player
                   (player/eat-feeds feed)
                   (act feed rules))]
    (update-in game [::players player-name] player)))
