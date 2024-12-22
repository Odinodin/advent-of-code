(ns advent.day-07
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]))

(def input-path "resources/day07/")

(defn all-operator-combinations
  "Returns a lazy sequence of all combinations of the items in operators of length x."
  [operators n]
  (if (zero? n)
    '(())
    (for [rest (all-operator-combinations operators (dec n))
          operator operators]
      (conj rest operator))))


(comment
  (all-operator-combinations 3)
  )

(defn execute [numbers operators]
  (let [math-to-solve-left-to-right (m/interleave-all numbers operators)]
    (reduce (fn [acc [op x]]
              (cond  (= :times op)
                     (* acc x)

                     (= :plus op) (+ acc x)

                     (= :concat op) (Long/parseLong (str acc x))
                     ))
            (first math-to-solve-left-to-right)
            (partition 2 (rest math-to-solve-left-to-right)))))


(defn does-compute? [operators {:keys [desired-result nums]}]
  (let [combinations (all-operator-combinations operators (count nums))]
    (some (fn [combination]
            (let [actual (execute nums combination)]
              (= desired-result actual)))
      combinations)))

(defn star-1 [path]
  (let [lines (util/read-file-into-list path)
        sum-and-input (->> lines
                           (map (fn [line] (let [[sum input] (str/split line #":")]
                                             {:desired-result (Long/parseLong sum)
                                              :nums (mapv Long/parseLong (-> (str/trim input)
                                                                             (str/split #"\s")))}))))]
    (->> sum-and-input
         (filter #(does-compute? [:times :plus] %))
         (map :desired-result)
         (reduce +))))

(defn star-2 [path]
  (let [lines (util/read-file-into-list path)
        sum-and-input (->> lines
                           (map (fn [line] (let [[sum input] (str/split line #":")]
                                             {:desired-result (Long/parseLong sum)
                                              :nums (mapv Long/parseLong (-> (str/trim input)
                                                                             (str/split #"\s")))}))))]
    (->> sum-and-input
         (filter #(does-compute? [:times :plus :concat] %))
         (map :desired-result)
         (reduce +))))

(comment
  (star-1 (str input-path "test-input.txt"))
  (star-1 (str input-path "input.txt"))

  (star-2 (str input-path "test-input.txt"))
  (star-2 (str input-path "input.txt")))