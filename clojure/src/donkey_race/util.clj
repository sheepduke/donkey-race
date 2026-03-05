(ns donkey-race.util)

(defn remove-first [coll x]
  (let [head (take-while #(not= % x) coll)
        tail (rest (drop-while #(not= % x) coll))]
    (concat head tail)))

(defn remove-last [coll x]
  (reverse (remove-first (reverse coll) x)))
