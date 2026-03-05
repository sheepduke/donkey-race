(ns donkey-race.rule-test
  (:require [clojure.test :refer :all]
            [donkey-race.rule :as rule]))

(deftest test-make-rules
  (testing "vortexes should be sorted"
    (let [rules (rule/make-rules (merge {::rule/map-length 10
                                         ::rule/map-vortexes [6 4 2]}
                                        rule/default-player-initial-feeds
                                        rule/default-primary-feeds
                                        rule/default-basic-feed->action))]
      (is (= [2 4 6] (::rule/map-vortexes rules))))))
