(ns advent.day-16
  (:require [advent.util :as util]
            [clojure.string :as str]
            [medley.core :as m]
            [clojure.math.combinatorics :as combo]))

(def input-path "resources/day16/")

(defn star-1 [path]
  (let [vacant? #(= \. %)
        end? #(= \E %)
        xy-to-char (util/xy-to-char path)
        start (util/find-in-xy-to-char xy-to-char \S)
        scoring (fn [previous curr]
                  (if (= previous curr)
                    1
                    1001))
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
                       (let [next-coords (filter (fn [{:keys [coord weight]}]
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
                     )
                   )

                 )
        ]

    (let [cheapest-path (first (sort-by (fn [path] (-> path last :score)) result))]
      (prn cheapest-path)
      (util/print-map! (util/reify-map (reduce (fn [acc {:keys [coord value]}]
                                                 (assoc acc coord value)
                                                 ) xy-to-char cheapest-path))))
    result))

(defn star-2 [path]
  (let [vacant? #(= \. %)
        wall? #(= \# %)
        end? #(= \E %)
        opposite {\^ \v
                  \v \^
                  \< \>
                  \> \<}
        xy-to-char (util/xy-to-char path)
        start (util/find-in-xy-to-char xy-to-char \S)
        end (util/find-in-xy-to-char xy-to-char \E)
        scoring (fn [previous curr]
                  (if (= previous curr)
                    1
                    1001))
        result (loop [active-paths [[{:coord start :score 0 :weight 1 :value \>}]]
                      success-paths []
                      visited {}]

                 (if (empty? active-paths)                  ;; Terminition condition
                   success-paths

                   ;; Keep looking
                   (let [path (first active-paths)
                         curr (last path)
                         {:keys [coord score weight value]} curr]
                     #_(prn "LOOKING AT " (get xy-to-char coord))
                     (if (end? (get xy-to-char coord))
                       (do
                         #_(prn "FOUND END")
                         (recur (rest active-paths) (conj success-paths path) (conj visited curr))) ;; Do the end need to be added to visited? think not
                       (let [next-coords (filter (fn [{:keys [coord weight value]}]
                                                   (when (and (= coord [3 9]) (= [3 10] (:coord (last path))))
                                                     #_(prn "whee" (map :coord path))
                                                     #_(prn "cost" (map :score path) "visited " (:score (get visited coord)))
                                                     )
                                                   (and (or (end? (get xy-to-char coord))
                                                            (vacant? (get xy-to-char coord)))
                                                        (<= (+ score (if (= value (:value (get visited coord)))
                                                                       0
                                                                       -1000)) (:score (get visited coord) Integer/MAX_VALUE))))
                                           ;; Not necessary to check in the direction you came
                                           (remove
                                             (fn [item] (= (:value curr) (opposite (:value item))))
                                             [{:coord (util/coord-in-dir coord \^) :score (+ score (scoring value \^)) :weight (inc weight) :value \^}
                                              {:coord (util/coord-in-dir coord \v) :score (+ score (scoring value \v)) :weight (inc weight) :value \v}
                                              {:coord (util/coord-in-dir coord \<) :score (+ score (scoring value \<)) :weight (inc weight) :value \<}
                                              {:coord (util/coord-in-dir coord \>) :score (+ score (scoring value \>)) :weight (inc weight) :value \>}]))]
                         #_(prn "NEXT" next-coords)
                         (recur
                           (concat (rest active-paths) (map #(conj path %) next-coords))
                           success-paths
                           (reduce (fn [acc curr]
                                     (if (<= (:score curr) (:score (get acc (:coord curr)) Integer/MAX_VALUE))
                                       (assoc acc (:coord curr) curr)
                                       acc
                                       )) visited next-coords)))))))]

    (let [cheapest-path (first (sort-by (fn [path] (-> path last :score)) result))
          shortest (:score (last cheapest-path))
          shortest-paths (filter #(-> % last :score (= shortest)) result)]
      (->> shortest-paths
           (mapcat (fn [path] (map #(:coord %) path)))
           (into #{})
           (count)))))

(comment

  (star-1 (str input-path "input-test.txt"))
  (star-1 (str input-path "input.txt"))                     ;

  (star-2 (str input-path "input-test.txt"))
  (star-2 (str input-path "input.txt"))

  )