(ns advent.day-11
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

;; Naive solution without caching ...
(defn blink [stones]
  (loop [curr-stones stones
         new-stones []]
    (let [stone (first curr-stones)]
      (cond
        (empty? curr-stones)                                ;; Termination
        new-stones

        (= stone 0)
        (recur (rest curr-stones) (conj new-stones 1))

        (even? (count (str stone)))
        (let [stone-str (str stone)
              number-of-digits (count stone-str)]
          (recur
            (rest curr-stones)
            (conj new-stones
              (parse-long (apply str (take (/ number-of-digits 2) stone-str)))
              (parse-long (apply str (take-last (/ number-of-digits 2) stone-str))))))

        :else
        (recur
          (rest curr-stones)
          (conj new-stones (* 2024 stone)))))))

;; Naive solution
(defn day-11-star-1 [path]
  (let [number-of-blinks 25
        stones (->> (str/split (slurp path) #"\s")
                    (map #(Integer/parseInt (str %))))]
    (->> (loop [idx 0
                curr stones]
           (if (= idx number-of-blinks)
             curr
             (recur (inc idx) (blink curr))))
         (count)
         ))
  )

(comment

  (def blink (fn [stone]
                     (cond
                       (zero? stone)
                       1

                       (even? (count (str stone)))
                       (let [stone-str (str stone)
                             number-of-digits (count stone-str)]
                         [(parse-long (apply str (take (/ number-of-digits 2) stone-str)))
                          (parse-long (apply str (take-last (/ number-of-digits 2) stone-str)))])

                       :else
                       (* 2024 stone))))
  (def blink-count (memoize (fn [depth stone]
                              (if (= depth 0)
                                1

                                (let [result (blink stone)]
                                  (if (number? result)
                                    (blink-count (dec depth) result)
                                    (+ (blink-count (dec depth) (first result))
                                       (blink-count (dec depth) (second result)))))))))
  (let [path "resources/day11/input.txt"
        number-of-blinks 75
        stones (->> (str/split (slurp path) #"\s")
                    (map #(Integer/parseInt (str %))))
        ]

    (reduce + (map #(blink-count number-of-blinks %) stones))

    )

  )

(defn day-11-star-2 [path]

  )




(comment
  (Integer/parseInt (apply str (take (/ 2 2) "12")))

  (day-11-star-1 "resources/day11/input-test.txt")
  (day-11-star-1 "resources/day11/input.txt")

  (day-11-star-2 "resources/day11/input-test.txt")
  (day-11-star-2 "resources/day11/input.txt")
  )