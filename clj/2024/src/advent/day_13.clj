(ns advent.day-13
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

;; This is a simple linear equation with 2 unknowns (number of presses a and b)
;; Realized that there can only be one solution, so no need to try to search for a path
;; Using a binary search would probably also work just fine
;; Freshend up some linear algebra with Cramer's rule

(def input-path "resources/day13/")

(defn parse-movement [input-str]
  (let [[_ x y] (re-find #"X\+(\d+),\sY\+(\d+)" input-str)]
    [(parse-long x) (parse-long y)]))

(defn parse-prize [input-str]
  (let [[_ x y] (re-find #"X=(\d+),\sY=(\d+)" input-str)]
    [(parse-long x) (parse-long y)]))

;; Calculate the determinant of a matrix
;;
;; | x1 y1 |
;; | x2 y2 |
;;
;; (x1 * y2) - (y1 - x2)
;;
(defn determinant [[x1 y1] [x2 y2]]
  (- (* x1 y2)
    (* y1 x2)))


;; 2x2 linear equation with cramer's rule
(defn solve-linear-equation [[a1 b1 target1] [a2 b2 target2]]
  (prn "solve" [a1 b1 target1] [a2 b2 target2])
  (let [det (determinant [a1 b1] [a2 b2])
        det-x (determinant [target1 b1] [target2 b2])
        det-y (determinant [a1 target1] [a2 target2])
        x (/ det-x det)
        y (/ det-y det)]
    [x y]))


(defn legal-number-of-pushes [input]
  (and (pos? input) (not (ratio? input))))

(defn star-1 [path]
  (let [machines (->> (util/read-file-into-list path)
                      (partition-by #(= "" %))
                      (remove #(= [""] %))
                      (map (fn [[button-a button-b prize]]
                             {:a (parse-movement button-a)
                              :b (parse-movement button-b)
                              :prize (parse-prize prize)})))]
    (->> machines
         #_(filter machine-is-viable?)
         (map (fn [{:keys [a b prize]}]
                (let [[x1 y1] a
                      [x2 y2] b
                      [target-x target-y] prize]
                  (solve-linear-equation [x1 x2 target-x] [y1 y2 target-y]))))
         (filter (fn [[a-pushes b-pushes]]
                   (and (legal-number-of-pushes a-pushes)
                        (legal-number-of-pushes b-pushes))))
         (map (fn [[a-pushes b-pushes]]
                (+ (* a-pushes 3) b-pushes)))
         (reduce +))))

(defn star-2 [path]
  (let [machines (->> (util/read-file-into-list path)
                      (partition-by #(= "" %))
                      (remove #(= [""] %))
                      (map (fn [[button-a button-b prize]]
                             {:a (parse-movement button-a)
                              :b (parse-movement button-b)
                              :prize (parse-prize prize)}))
                      (map (fn [input]
                             (update input :prize (fn [[x y]]
                                                    [(+ 10000000000000 x)
                                                     (+ 10000000000000 y)])))))]
    #_machines
    (->> machines
        (map (fn [{:keys [a b prize]}]
                (let [[x1 y1] a
                      [x2 y2] b
                      [target-x target-y] prize]
                  (solve-linear-equation [x1 x2 target-x] [y1 y2 target-y]))))
        (filter (fn [[a-pushes b-pushes]]
                   (and (legal-number-of-pushes a-pushes)
                        (legal-number-of-pushes b-pushes))))
        (map (fn [[a-pushes b-pushes]]
                (prn a-pushes b-pushes)
                (+ (* a-pushes 3) b-pushes)))
        (reduce +)))
  )

(comment

  (star-1 (str input-path "input-test.txt"))                ;; 480
  (star-1 (str input-path "input.txt"))                     ;; 35997

  (star-2 (str input-path "input-test.txt"))
  (star-2 (str input-path "input.txt"))
  )