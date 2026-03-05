(ns donkey-race.player-test
  (:require [clojure.test :refer :all]
            [donkey-race.feed :as feed]
            [donkey-race.player :as player]
            [donkey-race.rule :as rule]))

(def rules
  (rule/make-rules (merge rule/default-short-map
                          rule/default-primary-feeds
                          rule/default-basic-feed->action)))

(def ai-player
  (player/make-normal-ai-player rules {::player/name ::player/sheep}))

(deftest test-pick-feed
  (let [player {::player/feed-bag (repeat 3 ::feed/tomato1)}
        player' (player/pick-feed player)]
    (is (= ::feed/tomato1 (:feed player')))
    (is (= (repeat 2 ::feed/tomato1) (::player/feed-bag (:player player'))))))

(deftest test-add-feeds
  (let [player {::player/feed-bag [::feed/tomato1]}
        player' (player/add-feeds player ::feed/tomato4)]
    (is (= [::feed/tomato1 ::feed/tomato4] (::player/feed-bag player')))))

(deftest test-eat-feeds
  (let [player {::player/feed-bag [::feed/tomato1]}
        player' (player/eat-feeds player ::feed/tomato4)]
    (is (= [::feed/tomato1] (::player/feed-bag player')))
    (is (= [::feed/tomato4] (::player/stomach player')))))

(deftest test-spwe-feed
  (let [player {::player/stomach [::feed/tomato1]}
        player' (player/spew-feed player ::feed/tomato1)]
    (is (= [] (::player/stomach player')))))

(deftest test-inc-counter-gem
  (let [player (player/inc-counter-gem ai-player 10)]
    (is (= 10 (::player/counter-gem player)))))

(deftest test-inc-counter-clover
  (let [player (player/inc-counter-clover ai-player 10)]
    (is (= 10 (::player/counter-clover player)))))

(deftest test-inc-counter-dream-grass
  (let [player (player/inc-counter-dream-grass ai-player)]
    (is (= 1 (::player/counter-dream-grass player)))))

(deftest test-move
  (let [player (-> ai-player
                   (player/move 10)
                   (player/move 20))]
    (is (= 30 (::player/position player)))))

(deftest test-move-to
  (let [player (-> ai-player
                   (player/move 20)
                   (player/move-to 10))]
    (is (= 10 (::player/position player)))))

(deftest test-ai-return
  (let [player (-> ai-player
                   (player/eat-feeds ::feed/cabbage1)
                   (player/eat-feeds ::feed/tomato2)
                   (player/eat-feeds ::feed/grape4))]
    (is (= ::feed/grape4
           (player/return-most-valuable player rules)))))

(deftest test-ai-take
  (= ::feed/tomato4
     (player/take-most-valuable ai-player
                                rules
                                [::feed/cabbage1 ::feed/grape2 ::feed/tomato4])))

(deftest test-ai-buy
  (testing "basic feeds"
    (let [player (player/inc-counter-gem ai-player 11)
          feeds (player/buy-most-valuable player rules)]
      (is (= [8 4 4] (map feed/value feeds)))))

  (testing "extended feeds"
    (let [rules (merge rules rule/default-extended-feeds)
          player (player/inc-counter-gem ai-player 11)
          feeds (player/buy-most-valuable player rules)]
      (is [8 5 2] (map feed/value feeds)))))
