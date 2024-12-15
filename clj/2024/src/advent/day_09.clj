(ns advent.day-09
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

(comment

  (let [input (slurp "resources/day09/input-test.txt")
        list-of-block-info (->> input
                                seq
                                (map (fn [x] (Integer/parseInt (str x))))
                                (partition-all 2)
                                (map-indexed (fn [index [block-count free-space-count]]
                                               {:id index :block-count block-count :free-space-count free-space-count})))

        id-to-block-info (reduce (fn [acc curr] (assoc acc (:id curr) curr)) {} list-of-block-info)
        reified-blocks (->> list-of-block-info
                            (mapcat (fn [{:keys [id block-count free-space-count]}]
                                      (concat (for [_ (range (or block-count 0))]
                                                id)
                                              (for [_ (range (or free-space-count 0))]
                                                nil)))))
        ]

    compacted-blocks

    #_(->>
      compacted-blocks
      (map-indexed (fn [idx curr]
                     (* idx (or curr 0))))
      (reduce +))

    )


  (defn day-9-star-1 [path]
    (let [input (slurp path)
          list-of-block-info (->> input
                                  seq
                                  (map (fn [x] (Integer/parseInt (str x))))
                                  (partition-all 2)
                                  (map-indexed (fn [index [block-count free-space-count]]
                                                 {:id index :block-count block-count :free-space-count free-space-count})))

          reified-blocks (->> list-of-block-info
                              (mapcat (fn [{:keys [id block-count free-space-count]}]
                                        (concat (for [_ (range (or block-count 0))]
                                                  id)
                                                (for [_ (range (or free-space-count 0))]
                                                  nil)))))
          compacted-blocks (loop [remaining-blocks reified-blocks
                                  result []]
                             (let [curr (first remaining-blocks)]
                               #_(prn "CURR: " curr " Remaining: " remaining-blocks " Result: " result)
                               (cond
                                 (empty? remaining-blocks)
                                 result                     ;; Termination condition

                                 (some? curr)               ;; There is a block here already, let's just add that
                                 (recur (rest remaining-blocks) (conj result curr))

                                 ;; No block, need to fetch the last block from remaining
                                 (nil? curr)
                                 ;; Extremely inefficient ..
                                 (let [seen-from-back (reverse (drop-while nil? (reverse remaining-blocks)))]
                                   (recur (butlast (rest seen-from-back)) (conj result (last seen-from-back)))))))]

      (->>
        compacted-blocks
        (map-indexed (fn [idx curr]
                       (* idx (or curr 0))))
        (reduce +)))))


(defn day-9-star-2 [path]
  )

(comment

  (day-9-star-1 "resources/day09/input-test.txt")
  (day-9-star-1 "resources/day09/input.txt")

  (day-9-star-2 "resources/day09/input-test.txt")
  (day-9-star-2 "resources/day09/input.txt")
  )