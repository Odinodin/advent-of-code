(ns advent.day-01
  (:require [advent.util :as util]))

(defn day-1-star-1 [input-file-name]
  (let [lines (util/read-file-into-list-of-lists input-file-name)
        left-list (->> lines (map first) sort (map #(Integer/parseInt %)))
        right-list (->> lines (map second) sort (map #(Integer/parseInt %)))
        paired (map (fn [a b] [a b]) left-list right-list)
        distances (map (fn [[a b]] (Math/abs (- a b))) paired)
        sum (reduce + distances)]
    sum))

(defn day-1-star-2 [input-file-name]
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
  (read-file-into-list-of-lists "resources/day-1-test-input.txt")
  (day-1-star-1 "resources/day-1-input1.txt")
  (day-1-star-2 "resources/day-1-input1.txt")
  )

