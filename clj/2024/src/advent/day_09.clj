(ns advent.day-09
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

(defn split-at-n-nils
  [n v]
  (let [idx (->> (partition n 1 v)
                 (keep-indexed (fn [i group]
                                 (when (every? nil? group)
                                   i)))
                 (first))]
    (if idx
      ;; idx is where the n-nil sequence starts. Split there.
      [(subvec v 0 idx)
       (subvec v (+ idx n))]
      [v []])))

(defn calculate-score [filesystem]
  (->> filesystem
       (map-indexed (fn [idx curr]
                      (* idx (or curr 0))))
       (reduce +)))

(defn attempt-to-move [curr-filesystem id id-to-block-info]
  (let [start-time (. System (nanoTime))
        [before-block with-block] (split-with #(not= id %) curr-filesystem)
        block-length (:block-count (get id-to-block-info id))
        [before-space-for-block after-block] (split-at-n-nils block-length (vec before-block))
        result (if (= before-space-for-block before-block)           ;; No space found!
                 curr-filesystem
                 (let [result (concat before-space-for-block (repeat block-length id) after-block (replace {id nil} with-block))]
                   result))
        time-spent-ms (/ (double (- (. System (nanoTime)) start-time)) 1000000.0)]

    (when (> time-spent-ms 1000)
      (prn "Slow! " time-spent-ms " ms " {:curr-filesystem curr-filesystem :id id :id-to-block-info id-to-block-info}))
    result

    ))

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
      (reduce +))))

(defn day-9-star-2 [path]
  (let [input (slurp path)
        list-of-block-info (->> input
                                seq
                                (map (fn [x] (Integer/parseInt (str x))))
                                (partition-all 2)
                                (map-indexed (fn [index [block-count free-space-count]]
                                               {:id index :block-count block-count :free-space-count free-space-count})))

        id-to-block-info (reduce (fn [acc curr] (assoc acc (:id curr) curr)) {} list-of-block-info)
        filesystem (->> list-of-block-info
                        (mapcat (fn [{:keys [id block-count free-space-count]}]
                                  (concat (for [_ (range (or block-count 0))]
                                            id)
                                          (for [_ (range (or free-space-count 0))]
                                            nil)))))]


    (->> (loop [curr-filesystem filesystem
                remaining-ids (sort (keys id-to-block-info))]
           (when (= 0 (mod (count remaining-ids) 10))
             (prn "Status: " (count remaining-ids)))
           (if (empty? remaining-ids)
             curr-filesystem                              ;; Terminal condition
             (recur (attempt-to-move curr-filesystem (last remaining-ids) id-to-block-info) (butlast remaining-ids))))
         calculate-score
         ))

  )


(comment
  (day-9-star-1 "resources/day09/input-test.txt")
  (day-9-star-1 "resources/day09/input.txt")

  (day-9-star-2 "resources/day09/input-test.txt")
  (day-9-star-2 "resources/day09/input.txt")
  )