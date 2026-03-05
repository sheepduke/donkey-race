(ns donkey-race.player
  (:require [sc.api :as sc]
            [medley.core :refer [find-first]]
            [donkey-race.util :refer :all]
            [taoensso.truss :refer [have]]
            [donkey-race.feed :as feed]
            [donkey-race.rule :as rule]))

(def player-names
  #{::donkey
    ::ox
    ::sheep
    ::pig})

(defn make-player
  "Creates a player.
  Argument type:
  `::strategy-take` [player feeds rules] -> feed
  `::strategy-return` [player rules] -> feed
  `::strategy-buy` [player rules] -> [feed]"
  [{:keys [name rules strategy-take strategy-return strategy-buy]}]
  ;; {:pre [(have name)
  ;;        (have rules)
  ;;        (have strategy-take)
  ;;        (have strategy-return)
  ;;        (have strategy-buy)]}
  {::name name
   ::feed-bag (::rule/player-initial-feeds rules)
   ::stomach []
   ::counter-gem 0
   ::counter-dream-grass 0
   ::counter-clover 0
   ::position 0
   ::strategy-take strategy-take
   ::strategy-return strategy-return
   ::strategy-buy strategy-buy})

;; ============================================================
;;  Operations
;; ============================================================

(defn pick-feed
  "Randomly picks a feed from player's feed bag. Returns {:player :feed}."
  [player]
  {:pre [(have (::feed-bag player))]}
  (let [feed-bag (::feed-bag player)
        feed (rand-nth feed-bag)]
    {:feed feed
     :player (assoc player ::feed-bag (remove-first feed-bag feed))}))

(defn add-feeds
  "Put the feed to the feed bag."
  [player & feeds]
  {:pre [(have #(every? feed/feed? %) feeds)]}
  (have #(every? feed/feed? %) feeds)
  (update player ::feed-bag #(into % feeds)))

(defn eat-feeds
  "Put the feed to player's stomach."
  [player & feeds]
  (update player ::stomach #(into % feeds)))

(defn spew-feed
  "Remove the feed from player's stomach."
  [player feed]
  {:pre [(have (contains? (set (::stomach player)) feed))]}
  (update player ::stomach #(remove-first % feed)))

(defn inc-counter-gem
  "Increase gem count."
  [player count]
  (update player ::counter-gem (partial + count)))

(defn inc-counter-dream-grass [player]
  (update player ::counter-dream-grass inc))

(defn inc-counter-clover [player count]
  (update player ::counter-clover (partial + count)))

(defn move [player count]
  (update player ::position #(+ % count)))

(defn move-to [player position]
  (assoc player ::position position))

;; ============================================================
;;  AI
;; ============================================================

(defn return-most-valuable [player rules]
  {:pre [(have (::stomach player))]}
  (last (sort-by feed/value (::stomach player))))

(defn take-most-valuable [player feeds rules]
  (let [value->feeds (group-by feed/value feeds)
        max-value (apply max (keys value->feeds))]
    (rand-nth (value->feeds max-value))))

(defn buy-most-valuable
  "Always try to buy the most valuable feed, then less-valuable and so on.
  For each level of value, randomly pick one."
  [player rules]
  ;; FIXME enhance it
  (loop [gem-count (::counter-gem player)
         shop (::rule/shop rules)
         feeds []]
    (let [affordable-feeds (->> shop
                                (filter #(<= (feed/price %) gem-count))
                                (group-by feed/value))]
      (if (empty? affordable-feeds)
        feeds
        (let [max-value (apply max (keys affordable-feeds))
              feed (rand-nth (affordable-feeds max-value))]
          (recur (- gem-count (feed/price feed))
                 (filter #(not= (feed/type feed) (feed/type %)) shop)
                 (conj feeds feed)))))))

(defn make-normal-ai-player [{:keys [name rules] :as opts}]
  "See `make-player` for details of `opts`."
  (make-player {:name name
                :rules rules
                :strategy-take take-most-valuable
                :strategy-return return-most-valuable
                :strategy-buy buy-most-valuable}))
