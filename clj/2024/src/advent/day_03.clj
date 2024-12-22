(ns advent.day-03
  (:require [advent.util :as util]
            [clojure.string :as str]))

(def input-path "resources/day03/")

(defn find-mul-patterns [s]
  (re-seq #"mul\(\d+,\d+\)" s))

(defn extract-numbers [text]
  (let [[_ num1 num2] (re-find #"mul\((\d+),(\d+)\)" text)]
    [(Integer/parseInt num1) (Integer/parseInt num2)]))

(defn get-before-first-dont [text]
  (if-let [idx (clojure.string/index-of text "don't()")]
    (apply str (take idx text))
    text))

(defn extract-active-parts [text]
  (let [chopped-at-do (clojure.string/split text #"do\(\)")
        to-be-included (map get-before-first-dont chopped-at-do)]
    to-be-included))

(defn star-1 [path]
  (let [lines (->> (util/read-file-into-list path))]
    (->> lines
         (mapcat find-mul-patterns)
         (map extract-numbers)
         (map (fn [[a b]]
                (* a b)))
         (reduce +))))

(defn star-2 [path]
  (let [input-string (slurp path)]
    (->> input-string
         (extract-active-parts)

         (remove nil?)
         (mapcat find-mul-patterns)
         (map extract-numbers)
         (map (fn [[a b]]
                (* a b)))
         (reduce +))))

(comment
  ;;  93729253
  ;; 107991598
  ;; 107991598   -- too high
  ;; 201486483
  ;; 252293910
  ;;  57557767


  (slurp (str input-path "input1.txt"))
  (star-1 (str input-path "test-input.txt"))
  (star-1 (str input-path "input1.txt"))
  (star-2 (str input-path "test-input2.txt"))
  (star-2 (str input-path "input1.txt"))
  )