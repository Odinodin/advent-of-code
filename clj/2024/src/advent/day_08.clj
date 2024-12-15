(ns advent.day-08
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))


(defn update-map-location [xy-to-char [x y] value]
  (assoc xy-to-char [x y] value))

(defn day-8-star-1 [path]
  (let [xy-char (util/xy-to-char path)
        list-of-lists-with-xy-of-same-char (->> (group-by second xy-char)
                                                (m/remove-keys #(= \. %))
                                                vals
                                                (map (fn [list-of-k-v] (map (fn [x] (first x)) list-of-k-v))))
        pairs-of-same-antenna-coords (->> list-of-lists-with-xy-of-same-char
                                          (mapcat (fn [list-with-xy-of-same-char]
                                                    (combo/combinations list-with-xy-of-same-char 2))))
        [width height] (util/bounds-map xy-char)]
    (let [antinode-coords (->>
                            pairs-of-same-antenna-coords
                            (mapcat (fn [two-pairs]
                                      (let [[x1 y1] (first two-pairs)
                                            [x2 y2] (second two-pairs)
                                            antinode-1-x (+ x1 (- x1 x2))
                                            antinode-1-y (+ y1 (- y1 y2))
                                            antinode-2-x (+ x2 (- x2 x1))
                                            antinode-2-y (+ y2 (- y2 y1))]
                                        [[antinode-1-x antinode-1-y]
                                         [antinode-2-x antinode-2-y]])))
                            distinct
                            (remove #(util/out-of-bounds? [width height] %)))]

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

(defn antinode-next-to [[x y] delta-x delta-y]
  [(+ x delta-x) (+ y delta-y)])

(defn day-8-star-2 [path]
  (let [xy-char (util/xy-to-char path)
        list-of-lists-with-xy-of-same-char (->> (group-by second xy-char)
                                                (m/remove-keys #(= \. %))
                                                vals
                                                (map (fn [list-of-k-v] (map (fn [x] (first x)) list-of-k-v))))
        pairs-of-same-antenna-coords (->> list-of-lists-with-xy-of-same-char
                                          (mapcat (fn [list-with-xy-of-same-char]
                                                    (combo/combinations list-with-xy-of-same-char 2))))
        [width height] (util/bounds-map xy-char)]
    (let [antinode-coords (->>
                            pairs-of-same-antenna-coords
                            (mapcat (fn [two-pairs]
                                      (let [[x1 y1] (first two-pairs)
                                            [x2 y2] (second two-pairs)]
                                        (concat (->> (iterate inc 0)
                                                     (map (fn [i]
                                                            (antinode-next-to [x1 y1] (* i (- x1 x2)) (* i (- y1 y2)))))
                                                     (take-while (fn [coord]
                                                                   (not (util/out-of-bounds? [width height] coord)))))
                                                (->> (iterate inc 0)
                                                     (map (fn [i]
                                                            (antinode-next-to [x2 y2] (* i (- x2 x1)) (* i (- y2 y1)))))
                                                     (take-while (fn [coord]
                                                                   (not (util/out-of-bounds? [width height] coord)))))))))
                            distinct
                            (remove #(out-of-bounds? [width height] %)))]
      (count antinode-coords))))

(comment

  (day-8-star-1 "resources/day08/input-test.txt")
  (day-8-star-1 "resources/day08/input.txt")

  (day-8-star-2 "resources/day08/input-test.txt")
  (day-8-star-2 "resources/day08/input.txt")
  )