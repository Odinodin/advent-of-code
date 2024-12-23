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

(defn big-barrel? [input]
  (or (= input \[)
      (= input \])))

(defn left-part-of-big-barrel? [input]
  (= input \[))

(defn other-big-barrel-coord [[x y] part]
  (if (= part \[)
    [(inc x) y]
    [(dec x) y]))

(defn movable? [input]
  (or (barrel? input)
      (big-barrel? input)
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
         (reverse)                                          ;; apply moves to coords that CAN move in reverse sequence to only move when they can
         (reduce (fn [acc coord-being-moved]
                   (let [item-at-coord (get acc coord-being-moved)
                         coord-in-direction-of-force (coord-in-dir-fn coord-being-moved force-direction)]
                     (if (vacant? (get acc coord-in-direction-of-force))
                       (-> acc
                           (assoc coord-in-direction-of-force item-at-coord)
                           (assoc coord-being-moved \.)     ;; Leave empty spot
                           )
                       acc
                       ))
                   ) xy-to-char))))

(defn score-barrel [[x y]]
  (+ x (* 100 y)))

(comment

  (with-redefs [prn (fn [& _] nil)]
    (let [input-lists (util/read-file-into-list (str input-path "input.txt"))
          [map-as-lists move-list] (->> input-lists
                                        (partition-by #(= "" %))
                                        (remove #(= [""] %)))
          ;; Should ignore line breaks in move-input, this got me stumped a while on the second test input
          moves (str/join move-list)
          supersize-map (fn [map-as-lists]
                          (->> map-as-lists
                               (map (fn [line]
                                      (->> line
                                           (mapv (fn [char]
                                                   (case char
                                                     \. (str "..")
                                                     \# (str "##")
                                                     \O (str "[]")
                                                     \@ (str "@."))))
                                           (str/join))))))
          supersized-map-as-lists (supersize-map map-as-lists)
          xy-to-char (util/lines->xy-to-char supersized-map-as-lists)

          bounds [(count (first map-as-lists)) (count map-as-lists)]

          execute-move-2-fn (fn [xy-to-char bounds force-direction]
                              ;; Apply force
                              (let [alpha-pos (find-alpha xy-to-char)]
                                #_(prn "Start " alpha-pos)

                                (->>
                                  (loop [remaining (list alpha-pos)
                                         result []
                                         seen #{}]
                                    (let [coord (first remaining)]
                                      #_(prn "Coord" coord "Remaining" remaining "Result " result)

                                      (if (empty? remaining) ;; Terminating condition
                                        (do
                                          #_(prn "DONE! " result)
                                          result)

                                        (let [next-coords-to-check (if (and (big-barrel? (get xy-to-char coord))
                                                                            (or (= force-direction \^)
                                                                                (= force-direction \v)))
                                                                     (do
                                                                       #_(prn "BIG BARREL")
                                                                       [(coord-in-dir-fn coord force-direction)
                                                                        (other-big-barrel-coord coord (get xy-to-char coord))])
                                                                     [(coord-in-dir-fn coord force-direction)])]
                                          (if (and
                                                (some #(movable? (get xy-to-char %)) next-coords-to-check)
                                                (some #(blocking? (get xy-to-char %)) next-coords-to-check))
                                            (do
                                              #_(prn "DONE! nothing can move")
                                              [])           ;; DOnE! nothing can move!

                                            (recur
                                              ;; Updated remaining to check
                                              (if (and (not (get seen coord))
                                                       (movable? (get xy-to-char coord)))
                                                (into (rest remaining) next-coords-to-check)
                                                (rest remaining))

                                              ;; Updated result
                                              (if (movable? (get xy-to-char coord))
                                                (conj result coord)
                                                result)

                                              ;; To avoid double processing!
                                              (conj seen coord)))))))

                                  (sort-by (fn [[x y]]
                                             (case force-direction
                                               \< x
                                               \> (- x)
                                               \^ y
                                               \v (- y))
                                             ))             ;; apply moves to coords that CAN move in reverse sequence to only move when they can
                                  (reduce (fn [acc coord-being-moved]
                                            (let [item-at-coord (get acc coord-being-moved)
                                                  _ (prn "MOVING " item-at-coord " " coord-being-moved)
                                                  coord-in-direction-of-force (coord-in-dir-fn coord-being-moved force-direction)
                                                  result (cond (and (big-barrel? item-at-coord) (or (= force-direction \^)
                                                                                                    (= force-direction \v)))
                                                               (let [coord2-being-moved (other-big-barrel-coord coord-being-moved item-at-coord)
                                                                     coord2-in-direction-of-force (coord-in-dir-fn coord2-being-moved force-direction)
                                                                     item2-at-coord (get acc coord2-being-moved)
                                                                     #_#__ (prn "Big barrel! ")
                                                                     ]
                                                                 (if (and (vacant? (get acc coord-in-direction-of-force))
                                                                          (vacant? (get acc coord2-in-direction-of-force)))
                                                                   (do
                                                                     #_(prn "Moving big barrel")
                                                                     (-> acc
                                                                         (assoc coord-in-direction-of-force item-at-coord)
                                                                         (assoc coord2-in-direction-of-force item2-at-coord)
                                                                         (assoc coord-being-moved \.) ;; Leave empty spots
                                                                         (assoc coord2-being-moved \.)))
                                                                   acc))

                                                               :else
                                                               (do
                                                                 #_(prn "Else ..")
                                                                 (if (vacant? (get acc coord-in-direction-of-force))
                                                                   (-> acc
                                                                       (assoc coord-in-direction-of-force item-at-coord)
                                                                       (assoc coord-being-moved \.) ;; Leave empty spot
                                                                       )
                                                                   acc
                                                                   )))
                                                  ]
                                              result
                                              )) xy-to-char))))

          final-xy-to-char (->> (seq moves)
                                (reduce (fn [acc move-direction]
                                          #_(prn "")
                                          (prn "MOVE: " move-direction)

                                          (let [result (execute-move-2-fn acc bounds move-direction)]
                                            (prn (-> (util/reify-map result) (util/print-map!)))
                                            result
                                            )) xy-to-char))
          ]

      (->> final-xy-to-char
           (util/reify-map)
           (util/print-map!)
           )

      (->> final-xy-to-char
           (filter (fn [[k v]] (left-part-of-big-barrel? v)))
           (map first)
           (map score-barrel)

           #_(map score-barrel)
           #_(map (fn [coord] [coord (other-big-barrel-coord cord \[)]))
           (reduce +)
           )

      ))                                                    ;;; 1509751 too low, 1527746 too high, 1532020 too high
  ;; Correct 1511259



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