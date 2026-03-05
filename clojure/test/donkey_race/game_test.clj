(ns donkey-race.game-test
  (:require [clojure.test :refer :all]
            [donkey-race.player :as player]
            [donkey-race.game :as game]
            [donkey-race.feed :as feed]
            [donkey-race.rule :as rule]
            [donkey-race.action :as action]))

(def basic-rules
  (rule/make-rules (merge rule/default-short-map
                          rule/default-extended-feeds
                          rule/default-basic-feed->action)))

(def advanced-rules
  (rule/make-rules (merge rule/default-short-map
                          rule/default-extended-feeds
                          rule/default-advanced-feed->action)))

(def player
  (player/make-normal-ai-player basic-rules {::player/name ::player/sheep}))

(deftest test-on-vortex?
  (is (game/on-vortex? (player/move-to player 2) basic-rules))
  (is (not (game/on-vortex? (player/move-to player 3) basic-rules))))

(deftest test-next-vortex
  (is (= 2 (game/next-vortex (player/move-to player 1) basic-rules)))
  (is (= 4 (game/next-vortex (player/move-to player 2) basic-rules)))
  (is (= 7 (game/next-vortex (player/move-to player 4) basic-rules)))
  (is (= 7 (game/next-vortex (player/move-to player 6) basic-rules))))

(deftest test-last-player?
  (let [player-ox (-> (player/make-normal-ai-player
                       basic-rules
                       {::player/name ::player/ox})
                      (player/move-to 4))
        player-sheep (-> (player/make-normal-ai-player
                          basic-rules
                          {::player/name ::player/sheep})
                         (player/move-to 4))
        player-pig (-> (player/make-normal-ai-player
                          basic-rules
                          {::player/name ::player/pig})
                         (player/move-to 6))
        game (game/make-game
              {::game/players [player-ox player-sheep player-pig]
               ::game/rules basic-rules})]
    (is (game/last-player? game player-ox))
    (is (game/last-player? game player-sheep))
    (is (not (game/last-player? game player-pig)))))

(deftest test-act
  (testing "tomato - basic"
    (testing "tomato1 and on vortex gives 2"
      (is (= 2 (-> player
                   (player/move-to 2)
                   (game/act ::feed/tomato1 basic-rules)
                   (::player/counter-gem player)))))

    (testing "tomato1 and not on vortex gives 1"
      (is (= 1 (-> player
                   (player/move-to 1)
                   (game/act ::feed/tomato2 basic-rules)
                   (::player/counter-gem player))))))

  (testing "tomato - advanced"
    (testing "tomato1 and not on vortex gives 1"
      (is (= 1 (-> player
                   (player/move-to 1)
                   (game/act ::feed/tomato1 advanced-rules)
                   (::player/counter-gem player)))))

    (testing "tomato1 and on vortex gives 1"
      (is (= 1 (-> player
                   (player/move-to 2)
                   (game/act ::feed/tomato1 advanced-rules)
                   (::player/counter-gem player)))))

    (testing "tomato4 and on vortex gives 3"
      (is (= 3 (-> player
                   (player/move-to 2)
                   (game/act ::feed/tomato4 advanced-rules)
                   (::player/counter-gem player))))))

  (testing "corn - basic"
    "TODO")

  (testing "cabbage - basic"
    (is (= (-> player
               (player/add-feeds ::feed/tomato1 ::feed/tomato2))
           (-> player
               (player/add-feeds ::feed/tomato1)
               (player/eat-feeds ::feed/tomato2)
               (game/put-back-feed basic-rules)))))

  (testing "cabbage - advanced"
    "TODO")

  (testing "grape - basic"
    (is (= (player/move-to player 2)
           (game/act player ::feed/grape1 basic-rules)))
    (is (= (player/move-to player 4)
           (game/act (player/move-to player 2) ::feed/grape2 basic-rules))))

  (testing "grape - advanced"
    (is (= (player/eat-feeds player ::feed/grape2)
           (game/act player ::feed/grape1 advanced-rules)))
    (is (= (player/eat-feeds player ::feed/grape4)
           (game/act player ::feed/grape2 advanced-rules)))
    (is (= (player/eat-feeds player ::feed/radish)
           (game/act player ::feed/grape4 advanced-rules))))

  (testing "prune - basic"
    (is (= (-> player
               (player/inc-counter-gem 9)
               (player/move-to 9))
           (-> player
               (player/inc-counter-gem 9)
               (game/act ::feed/prune basic-rules)))))

  (testing "prune - advanced"
    (testing "not on vortex"
      (is (contains? (set (map #(player/add-feeds player %)
                               [::feed/tomato2 ::feed/corn2
                                ::feed/grape2 ::feed/cabbage2]))
                     (-> player
                         (game/act ::feed/prune advanced-rules)))))
    (testing "on vortex"
      (let [player (player/move-to player 4)]
        (is (contains? (set (map #(player/add-feeds player %)
                                 [::feed/tomato4 ::feed/corn4
                                  ::feed/grape4 ::feed/cabbage4]))
                       (game/act player ::feed/prune advanced-rules))))))

  (testing "carrot - basic"
    (testing "not on vortex"
      (is (= (player/inc-counter-clover player 1)
             (game/act player ::feed/carrot basic-rules))))
    (testing "on vortex"
      (let [player (player/move-to player 4)]
        (is (= (player/inc-counter-clover player 2)
               (game/act player ::feed/carrot basic-rules))))))

  (testing "carrot - advanced"
    ))

(def rules
  (merge basic-rules
         {::rule/player-initial-feeds rule/default-player-initial-feeds}))

(def player
  (player/make-normal-ai-player {:name ::player/sheep
                                 :rules rules}))

(def game
  (game/make-game {:players [player] :rules rules}))

(game/play game ::player/sheep)
