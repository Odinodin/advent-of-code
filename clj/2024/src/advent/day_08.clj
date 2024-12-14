(ns advent.day-08
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))


(defn update-map-location [xy-to-char [x y] value]
  (assoc xy-to-char [x y] value))

(defn out-of-bounds? [[width height] [x y]]
  (or (< x 0)
      (< y 0)
      (>= x width)
      (>= y height)))

(defn day-8-star-1 [path]
  (let [xy-char (util/xy-to-char path)
        list-of-lists-with-xy-of-same-char (->> (group-by second xy-char)
                                                (m/remove-keys #(= \. %))
                                                vals
                                                (map (fn [list-of-k-v] (map (fn [x] (first x)) list-of-k-v))))
        pairs-of-same-antenna-coords (->> list-of-lists-with-xy-of-same-char
                                          (mapcat (fn [list-with-xy-of-same-char]
                                                    (combo/combinations list-with-xy-of-same-char 2)
                                                    )))
        [width height] (util/bounds-map xy-char)]
    (let [antinode-coords (->>
                            pairs-of-same-antenna-coords
                            (mapcat (fn [foo]
                                      (let [[x1 y1] (first foo)
                                            [x2 y2] (second foo)
                                            antinode-1-x (+ x1 (- x1 x2))
                                            antinode-1-y (+ y1 (- y1 y2))
                                            antinode-2-x (+ x2 (- x2 x1))
                                            antinode-2-y (+ y2 (- y2 y1))]
                                        [[antinode-1-x antinode-1-y]
                                         [antinode-2-x antinode-2-y]
                                         ])))
                            distinct
                            (remove #(out-of-bounds? [width height] %)))]

      #_(->> xy-char
             util/reify-map
             util/print-map!)

      #_(->> (reduce (fn [acc curr]
                       (update-map-location acc curr \#)
                       ) xy-char antinode-coords)
             (m/remove-keys (fn [[x y]]
                              (or (< x 0)
                                  (< y 0)
                                  (>= x width)
                                  (>= y height)
                                  )))
             (util/reify-map)
             (util/print-map!))
      (count antinode-coords))))

(defn day-8-star-2 [path]

  )

(comment




  (day-8-star-1 "resources/day08/input-test.txt")
  (day-8-star-1 "resources/day08/input.txt")

  (day-8-star-2 "resources/day08/-test-input.txt")
  (day-8-star-2 "resources/day08/-input.txt"))