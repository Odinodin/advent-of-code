(ns advent.day-10
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))




(defn day-10-star-1 [path]
  (let [xy-to-num (m/map-vals #(Integer/parseInt (str %)) (util/xy-to-char path))
        trailhead-coords (->> xy-to-num
                              (filter (fn [[k v]] (= v 0)))
                              (map first))
        bounds (util/bounds-map xy-to-num)
        is-uphill? (fn [from to xy-to-num]
                     (= (get xy-to-num from)
                        (dec (get xy-to-num to))))
        next-steps-fn (fn [[x y] bounds xy-to-num]
                        (->> [[x (dec y)]
                              [x (inc y)]
                              [(dec x) y]
                              [(inc x) y]]
                             (remove #(util/out-of-bounds? bounds %))
                             (remove (fn [coord] (not (is-uphill? [x y] coord xy-to-num))))))]
    (->> trailhead-coords
         (map (fn [trailhead]
                (loop [paths-to-follow [trailhead]
                       reached-9s #{}]
                  (if (empty? paths-to-follow)
                    (count reached-9s)                      ;; Terminal condition
                    ;; follow paths!
                    (let [next-up (next-steps-fn (first paths-to-follow) bounds xy-to-num)
                          coords-at-top (filter #(= (get xy-to-num %) 9) next-up)
                          not-yet-top (remove #(= (get xy-to-num %) 9) next-up)]
                      (recur (concat (rest paths-to-follow) not-yet-top) (into reached-9s coords-at-top)))))))
         (reduce +)))
  )

(defn day-10-star-2 [path]

  )


(comment
  (day-10-star-1 "resources/day10/input-test.txt")
  (day-10-star-1 "resources/day10/input.txt")

  (day-10-star-2 "resources/day10/input-test.txt")
  (day-10-star-2 "resources/day10/input.txt")
  )