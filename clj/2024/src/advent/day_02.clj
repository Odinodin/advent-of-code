(ns advent.day-02
  (:require [advent.util :as util]))

(defn adjacent-diff-in-range? [nums]
  (every? (fn [[a b]]
            (let [diff (Math/abs (- a b))]
              (and (>= diff 1) (<= diff 3))))
          (partition 2 1 nums)))

(defn strictly-monotonic? [numbers]
  (or (apply < numbers)
      (apply > numbers)))

(defn is-safe? [numbers]
  ;; strictly increasing or decreasing
  (and (strictly-monotonic? numbers)
       (adjacent-diff-in-range? numbers)))

(defn remove-one-at-a-time [nums]
  (map-indexed
    (fn [idx _]
      (concat (take idx nums) (drop (inc idx) nums)))
    nums))

(defn is-actually-safe? [numbers]
  (some is-safe? (remove-one-at-a-time numbers)))

(defn day-2-star-1 [path]
  (let [lines (->> (util/read-file-into-list-of-lists path)
                   (map (fn [line] (map #(Integer/parseInt %) line))))]
    (->> lines
         (filter is-safe?)
         count)))

(defn day-2-star-2 [path]
  (let [lines (->> (util/read-file-into-list-of-lists path)
                   (map (fn [line] (map #(Integer/parseInt %) line))))
        safe-reports (->> lines (filter is-safe?))
        potentially-safe-reports (->> lines (remove is-safe?) (filter is-actually-safe?))]
    (->> (concat safe-reports potentially-safe-reports)
         count)))

(comment

  (day-2-star-1 "resources/day-2-test-input.txt")
  (day-2-star-2 "resources/day-2-test-input.txt")
  (day-2-star-1 "resources/day-2-input1.txt")
  (day-2-star-2 "resources/day-2-input1.txt")
  )