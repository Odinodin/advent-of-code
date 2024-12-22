(ns advent.day-14
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

(def input-path "resources/day14/")

(defn move [[[x y] [dx dy]] bounds]
  [[(mod (+ x dx) (first bounds)) (mod (+ y dy) (second bounds))]
   [dx dy]])

(defn map-iteratively [f times coll]
  (->> coll
       (iterate #(map f %))
       (take times)
       (last)))

(defn inside-quadrant? [[x y] [x-start y-start] [width height]]
  (and (< x (+ x-start width))
       (>= x x-start)
       (< y (+ y-start height))
       (>= y y-start)))

(defn star-1 [path bounds]
  (let [seconds (inc 100)
        robots (->> (util/read-file-into-list path)
                    (map (fn [line]
                           (let [[_ x y vx vy] (re-find #"p=(\d+),(\d+)\sv=(-?\d+),(-?\d+)" line)]
                             (->> [x y vx vy]
                                  (map parse-long)
                                  (partition 2))))))
        xy-after-time-x (->> robots
                             (map-iteratively (fn [bot] (move bot bounds)) seconds)
                             (group-by first)
                             (m/map-vals #(count %)))
        quadrant-bounds [(dec (Math/ceil (/ (first bounds) 2))) (dec (Math/ceil (/ (second bounds) 2)))]
        top-left-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [0 0] quadrant-bounds)) xy-after-time-x)
        top-right-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [(- (first bounds) (first quadrant-bounds)) 0] quadrant-bounds)) xy-after-time-x)
        bottom-left-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [0 (- (second bounds) (second quadrant-bounds))] quadrant-bounds)) xy-after-time-x)
        bottom-right-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [(- (first bounds) (first quadrant-bounds)) (- (second bounds) (second quadrant-bounds))] quadrant-bounds)) xy-after-time-x)]

    (->> [top-left-quadrant top-right-quadrant bottom-left-quadrant bottom-right-quadrant]
         (map vals)
         (map #(reduce + %))
         (reduce *))))



(defn star-2-find [path bounds seconds]
  (let [robots (->> (util/read-file-into-list path)
                    (map (fn [line]
                           (let [[_ x y vx vy] (re-find #"p=(\d+),(\d+)\sv=(-?\d+),(-?\d+)" line)]
                             (->> [x y vx vy]
                                  (map parse-long)
                                  (partition 2))))))
        xy-after-time-x (->> robots
                             (map-iteratively (fn [bot] (move bot bounds)) seconds)
                             (group-by first)
                             (m/map-vals #(count %)))
        quadrant-bounds [(dec (Math/ceil (/ (first bounds) 2))) (dec (Math/ceil (/ (second bounds) 2)))]
        top-left-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [0 0] quadrant-bounds)) xy-after-time-x)
        top-right-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [(- (first bounds) (first quadrant-bounds)) 0] quadrant-bounds)) xy-after-time-x)
        bottom-left-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [0 (- (second bounds) (second quadrant-bounds))] quadrant-bounds)) xy-after-time-x)
        bottom-right-quadrant (m/filter-keys (fn [pos] (inside-quadrant? pos [(- (first bounds) (first quadrant-bounds)) (- (second bounds) (second quadrant-bounds))] quadrant-bounds)) xy-after-time-x)]

    (->> [top-left-quadrant top-right-quadrant bottom-left-quadrant bottom-right-quadrant]
         (map vals)
         (map #(reduce + %))
         (reduce *))))


(defn star-2-print [path bounds seconds]
    (let [robots (->> (util/read-file-into-list path)
                      (map (fn [line]
                             (let [[_ x y vx vy] (re-find #"p=(\d+),(\d+)\sv=(-?\d+),(-?\d+)" line)]
                               (->> [x y vx vy]
                                    (map parse-long)
                                    (partition 2))))))
          xy-after-time-x (->> robots
                               (map-iteratively (fn [bot] (move bot bounds)) seconds)
                               (group-by first)
                               (m/map-vals #(count %)))]

      (->> xy-after-time-x
           (util/reify-map bounds)
           (util/print-map!)
           )
      )
    )


;; Let etter en frame hvor ingen roboter er på samme sted
(defn star-2-find-tree [path bounds]
  (let [robots (->> (util/read-file-into-list path)
                    (map (fn [line]
                           (let [[_ x y vx vy] (re-find #"p=(\d+),(\d+)\sv=(-?\d+),(-?\d+)" line)]
                             (->> [x y vx vy]
                                  (map parse-long)
                                  (partition 2))))))
        xy-after-time-x (->> [0 robots]
                             (iterate (fn [[idx robots]] [(inc idx) (map (fn [bot] (move bot bounds)) robots)]))

                             (m/find-first
                               (fn [[idx robots]]
                                 (prn idx)
                                 (let [result (loop [seen-pos #{}
                                                     remaining robots]
                                                #_(prn "seen: " seen-pos " curr: " (ffirst remaining))
                                                (cond
                                                  (empty? remaining)
                                                  false
                                                  (contains? seen-pos (ffirst remaining))
                                                  true
                                                  :else
                                                  (recur (conj seen-pos (ffirst remaining)) (rest remaining))))]

                                   #_(prn "Idx: " idx " Found? " result "Bots:" (->> robots (map first) frequencies (m/filter-vals #(< 1 %))))
                                   (not result)
                                   )

                                 )
                               ))]
    xy-after-time-x
    #_(take 102 xy-after-time-x)
    #_(->> xy-after-time-x
           (util/reify-map bounds)
           (util/print-map!)
           )
    )
  )

(comment

  (star-2-find-tree (str input-path "input.txt") [101 103])

  (let [input [0
               '(((0 4) (3 -3))
                 ((9 4) (-1 -3))
                 ((10 3) (-1 2))
                 ((2 0) (2 -1))
                 ((0 0) (1 3))
                 ((3 0) (-2 -2))
                 ((7 6) (-1 -3))
                 ((3 0) (-1 -2))
                 ((9 3) (2 3))
                 ((7 3) (-1 2))
                 ((2 4) (2 -3))
                 ((9 5) (-3 -3)))]]
    (let [seconds (first input)
          robots (second input)
          ]
      (prn "IDX" (first input) "bots" robots)
      (not (loop [seen-pos #{}
                  remaining robots]
             (prn "seen: " seen-pos " curr: " (ffirst remaining))
             (cond
               (empty? remaining)
               false
               (contains? seen-pos (ffirst remaining))
               true
               :else
               (recur (conj seen-pos (ffirst remaining)) (rest remaining)))
             )))
    )




  #(star-2-find (str input-path "input-test.txt") [101 103] 10)

  (->>
    (range 6000 6500)
    (m/find-first (fn [seconds]
                    (let [result (star-2-find (str input-path "input.txt") [101 103] seconds)]
                      (when (= result 224357412)
                        seconds)
                      )
                    ))

    )


  (star-1 (str input-path "input-test.txt") [11 7])
  (star-1 (str input-path "input.txt") [101 103])           ;; 224357412

  (star-2-print (str input-path "input-test.txt") [11 7] 7083)
  (star-2-print (str input-path "input.txt") [101 103] 7084) ;; off by one :D

  (doseq [n (range 2000 3000)]
    (prn "")
    (prn n)
    (star-2-print (str input-path "input.txt") [101 103] n)
    )

  )