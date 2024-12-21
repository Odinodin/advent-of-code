(ns advent.day-01
  (:require [advent.util :as util]))

(def input-path "resources/day01/")

(defn star-1 [input-file-name]
  (let [lines (util/read-file-into-list-of-lists input-file-name)
        left-list (->> lines (map first) sort (map #(Integer/parseInt %)))
        right-list (->> lines (map second) sort (map #(Integer/parseInt %)))
        paired (map (fn [a b] [a b]) left-list right-list)
        distances (map (fn [[a b]] (Math/abs (- a b))) paired)
        sum (reduce + distances)]
    sum))

(defn star-2 [input-file-name]
  (let [lines (util/read-file-into-list-of-lists input-file-name)
        left-list (->> lines (map first) sort (map #(Integer/parseInt %)))
        right-list (->> lines (map second) sort (map #(Integer/parseInt %)))
        times-in-list-fn (fn [item list-of-items]
                           (reduce (fn [acc curr]
                                     (if (= item curr)
                                       (inc acc)
                                       acc)
                                     ) 0 list-of-items))

        times-in-list-mult (->> left-list (map (fn [item] (* (times-in-list-fn item right-list)
                                                            item))))]
    (reduce + times-in-list-mult)))

(comment
  (star-1 (str input-path "input.txt"))
  (star-2 (str input-path "input.txt")))

