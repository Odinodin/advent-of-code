(ns advent.day-06
  (:require [advent.util :as util]
            [medley.core :as m]
            [clojure.set :refer [difference union intersection]]
            [clojure.string :as str]))

(defn mark-visited [xy-to-char [x y] direction]
  (assoc xy-to-char [x y] direction))

(defn within-bounds? [width height [x y]]
  (and (< x width)
       (< y height)
       (>= x 0)
       (>= y 0)))

(defn rotate [direction]
  (get {:up :right
        :right :down
        :down :left
        :left :up} direction))

(defn next-pos [[x y] direction]
  (case direction
    :up [x (dec y)]
    :right [(inc x) y]
    :down [x (inc y)]
    :left [(dec x) y]))

(defn move [xy-to-char curr-pos curr-direction]
  (let [next-xy (next-pos curr-pos curr-direction)
        is-blocked? (= (get xy-to-char next-xy) \#)]
    (if is-blocked?
      [curr-pos (rotate curr-direction)]
      [next-xy curr-direction])))

(defn perform-movement [xy-to-char]
  (let [start-pos (m/find-first (fn [[x y]]
                                  (= (xy-to-char [x y])
                                     \^))
                                (keys xy-to-char))
        [width height] (util/bounds-map xy-to-char)]
    (loop [curr-pos start-pos
           curr-map xy-to-char
           curr-direction :up]
      (if (within-bounds? width height curr-pos)            ;; Recursion base case, are we done?
        (let [updated-map (mark-visited curr-map curr-pos curr-direction)
              [updated-pos updated-direction] (move curr-map curr-pos curr-direction)]
          (recur updated-pos updated-map updated-direction))
        ;; Done!
        (mark-visited curr-map curr-pos curr-direction)))))


(defn count-visited-locations [xy-to-char]
  (-> (count (filter (fn [[_ v]] (keyword? v)) xy-to-char))
      ;; Do not count first pos
       (dec)))

(defn day-6-star-1 [path]
  (let [xy-to-char (util/xy-to-char path)]
    (->> (perform-movement xy-to-char)
         (count-visited-locations))))

(defn make-all-combinations [initial-xy-to-char]
  (let [[width height] (util/bounds-map initial-xy-to-char)]
    ;; TODO continue
    (->> (for [col (range width)
               row (range height)]
           (str (get util/xy-to-char [row col]))))

    )
  )

(defn day-6-star-2 [path]
  (let [initial-xy-to-char (util/xy-to-char path)
        all-combinations (make-all-combinations initial-xy-to-char)]

    (->> (perform-movement xy-to-char)
         (count-visited-locations)))

  )


(comment
  (let [xy-to-char (util/xy-to-char "resources/day-6-input.txt")]
    (->> (perform-movement xy-to-char)
         (count-visited-locations)
         )
    )


  (filter #(= (first %) 1) {1 "a" 2 "b"})

  (day-6-star-1 "resources/day-6-test-input.txt")
  (day-6-star-1 "resources/day-6-input.txt")

  (day-6-star-2 "resources/day-6-test-input.txt")
  (day-6-star-2 "resources/day-6-input.txt")
  )