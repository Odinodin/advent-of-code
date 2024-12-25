(ns advent.day-16
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

(def input-path "resources/day16/")

(defn vacant? [x] #(= \. x))
(defn wall? [x] (= \# x))
(defn end? [x] #(= \E x))
(defn scoring [previous curr]
  (if (= previous curr)
    1
    1001))

(defn star-1 [path]
  (let [xy-to-char (util/xy-to-char path)
        start (util/find-in-xy-to-char xy-to-char \S)
        result (loop [active-paths [[{:coord start :score 0 :weight 1 :value \>}]]
                      success-paths []
                      visited {}]

                 (if (empty? active-paths)                  ;; Terminition condition
                   success-paths

                   ;; Keep looking
                   (let [path (first active-paths)
                         {:keys [coord score weight value]} (last path)]
                     #_(prn "LOOKING AT " (get xy-to-char coord))
                     (if (end? (get xy-to-char coord))
                       (do
                         #_(prn "FOUND END")
                         (recur (rest active-paths) (conj success-paths path) visited)) ;; Do the end need to be added to visited? think not
                       (let [next-coords (filter (fn [{:keys [coord]}]
                                                   (and (or (end? (get xy-to-char coord))
                                                            (vacant? (get xy-to-char coord)))
                                                        (< score (:score (get visited coord) Integer/MAX_VALUE))))
                                           [{:coord (util/coord-in-dir coord \^) :score (+ score (scoring value \^)) :weight (inc weight) :value \^}
                                            {:coord (util/coord-in-dir coord \v) :score (+ score (scoring value \v)) :weight (inc weight) :value \v}
                                            {:coord (util/coord-in-dir coord \<) :score (+ score (scoring value \<)) :weight (inc weight) :value \<}
                                            {:coord (util/coord-in-dir coord \>) :score (+ score (scoring value \>)) :weight (inc weight) :value \>}])]
                         #_(prn "NEXT" next-coords)
                         (recur
                           (concat (rest active-paths) (map #(conj path %) next-coords))
                           success-paths
                           (reduce (fn [acc curr]
                                     (assoc acc (:coord curr) curr)) visited next-coords))))
                     )))]

    #_(util/print-map! (util/reify-map xy-to-char))

    (let [cheapest-path (first (sort-by (fn [path] (-> path last :score)) result))]
      (prn cheapest-path)

      (util/print-map! (util/reify-map (reduce (fn [acc {:keys [coord value]}] (assoc acc coord value)) xy-to-char cheapest-path))))

    result))

(defn star-2 [path])

(comment

  (star-1 (str input-path "input-test.txt"))                ;; 7036
  (star-1 (str input-path "input.txt"))                     ;; 79404

  (star-2 (str input-path "input-test.txt"))
  (star-2 (str input-path "input.txt"))

  )