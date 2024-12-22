(ns advent.day-15
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

(def input-path "resources/day15/")

(defn coord-in-dir-fn [[x y] direction]
  (case direction
    \< [(dec x) y]
    \> [(inc x) y]
    \^ [x (dec y)]
    \v [x (inc y)]))

(defn barrel? [input]
  (= input \O))

(defn movable? [input]
  (or (barrel? input)
      (= input \@)))

(defn vacant? [input]
  (= input \.))

(defn blocking? [input]
  (or (= input \#)
      (nil? input)))

(defn find-alpha [xy-to-char]
  (first (m/find-first (fn [[k v]] (= v \@)) xy-to-char)))

(defn get-coords-in-force-direction-fn [bounds from direction]
  ;; Want the coords sorted by direction of force
  ;; INCLUDE the from coordinate
  (let [[x y] from
        [width height] bounds]
    (case direction
      \< (for [xn (reverse (range 0 (inc x)))] [xn y])
      \> (for [xn (range x width)] [xn y])
      \^ (for [yn (reverse (range 0 (inc y)))] [x yn])
      \v (for [yn (range y height)] [x yn]))))

(defn execute-move-fn [xy-to-char bounds force-direction]
  (let [alpha-pos (find-alpha xy-to-char)]
    (->> (get-coords-in-force-direction-fn bounds alpha-pos force-direction)
         (take-while (fn [coord]
                       #_(when (= force-direction \>)
                           (prn "Coord" coord (get xy-to-char coord))
                           (prn "Moveable? " (movable? (get xy-to-char coord)))
                           (prn "Blocking? " (not (blocking? (get xy-to-char (coord-in-dir-fn coord force-direction)))))
                           )
                       (and
                         (movable? (get xy-to-char coord))
                         (not (blocking? (get xy-to-char (coord-in-dir-fn coord force-direction)))))))
         (reverse) ;; apply moves to coords that CAN move in reverse sequence to only move when they can
         (reduce (fn [acc coord-being-moved]
                   (let [item-at-coord (get acc coord-being-moved)
                         coord-in-direction-of-force (coord-in-dir-fn coord-being-moved force-direction)]
                     (if (vacant? (get acc coord-in-direction-of-force))
                       (-> acc
                           (assoc coord-in-direction-of-force item-at-coord)
                           (assoc coord-being-moved \.) ;; Leave empty spot
                           )
                       acc
                       ))
                   ) xy-to-char))))

(defn score-barrel [[x y]]
  (+ x (* 100 y)))

(comment

  (let [input-lists (util/read-file-into-list (str input-path "input.txt"))
        [map-as-lists move-list] (->> input-lists
                                  (partition-by #(= "" %))
                                  (remove #(= [""] %)))
        ;; Should ignore line breaks in move-input, this got me stumped a while on the second test input
        moves (str/join move-list)
        xy-to-char (util/lines->xy-to-char map-as-lists)
        bounds [(count (first map-as-lists)) (count map-as-lists)]
        final-xy-to-char (->> (seq moves)
                           (reduce (fn [acc move-direction]
                                     #_(prn "")
                                     #_(prn "MOVE: " move-direction)

                                     (let [result (execute-move-fn acc bounds move-direction)]
                                       #_(prn (-> (util/reify-map result) (util/print-map!)))
                                       result
                                       )) xy-to-char))]

    (->> final-xy-to-char
         (filter (fn [[k v]] (barrel? v)))
         (map first)
         (map score-barrel)
         (reduce +)
         )


    #_(->> final-xy-to-char
         (util/reify-map)
         (util/print-map!)
         )

    #_(->> xy-to-char
         (util/reify-map)
         (util/print-map!))
    )

  )

(defn star-1 [path]
  (let [input-lists (util/read-file-into-list path)
        [map-as-lists move-list] (->> input-lists
                                      (partition-by #(= "" %))
                                      (remove #(= [""] %)))
        ;; Should ignore line breaks in move-input, this got me stumped a while on the second test input
        moves (str/join move-list)
        xy-to-char (util/lines->xy-to-char map-as-lists)
        bounds [(count (first map-as-lists)) (count map-as-lists)]
        final-xy-to-char (->> (seq moves)
                              (reduce (fn [acc move-direction]
                                        #_(prn "")
                                        #_(prn "MOVE: " move-direction)

                                        (let [result (execute-move-fn acc bounds move-direction)]
                                          #_(prn (-> (util/reify-map result) (util/print-map!)))
                                          result
                                          )) xy-to-char))]

    (->> final-xy-to-char
         (filter (fn [[k v]] (barrel? v)))
         (map first)
         (map score-barrel)
         (reduce +)
         )

    #_(->> final-xy-to-char
           (util/reify-map)
           (util/print-map!)
           )
    ))

(defn star-2 [path])



(comment

  (star-1 (str input-path "input-test.txt"))                ;; 2028
  (star-1 (str input-path "input-test2.txt"))               ;; 10092
  (star-1 (str input-path "input.txt"))                     ;; 1465152

  (star-2 (str input-path "input-test.txt"))
  (star-2 (str input-path "input.txt"))

  )