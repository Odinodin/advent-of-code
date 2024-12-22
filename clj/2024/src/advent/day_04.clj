(ns advent.day-04
  (:require [advent.util :as util]))

(def input-path "resources/day04/")

(defn make-4-letter-word [xy-to-char [x y] [dx dy]]
  (str (get xy-to-char [x y])
       (get xy-to-char [(+ x dx) (+ y dy)])
       (get xy-to-char [(+ x dx dx) (+ y dy dy)])
       (get xy-to-char [(+ x dx dx dx) (+ y dy dy dy)]))
  )

(defn make-MAS-words-around-x [xy-to-char [x y]]
  (let [center-letter (get xy-to-char [x y])]
    (if (= center-letter \A)
      (let [word-1 (str (get xy-to-char [(- x 1) (- y 1)])
                        center-letter
                        (get xy-to-char [(+ x 1) (+ y 1)]))
            word-2 (str  (get xy-to-char [(- x 1) (+ y 1)])
                         center-letter
                         (get xy-to-char [(+ x 1) (- y 1)])
                         )]
        [word-1 word-2])
      [])))

(defn star-1 [path]
  (let [xy-char (util/xy-to-char path)]
    (->> (for [coordinate (keys xy-char)
               dx [-1 0 1]
               dy [-1 0 1]
               :when (not= 0 dx dy)]
           (make-4-letter-word xy-char coordinate [dx dy]))
         (filter #(= % "XMAS"))
         count)))

(defn is-mas-or-sam? [input]
  (or (= input "MAS") (= input "SAM")))

(defn star-2 [path]
  (let [xy-char (util/xy-to-char path)]
    (->> (for [coordinate (keys xy-char)]
           (make-MAS-words-around-x xy-char coordinate))
         (remove empty?)
         (filter (fn [[a b]] (and (is-mas-or-sam? a) (is-mas-or-sam? b))))
         count)))

(comment

  (star-1 (str input-path "input1.txt"))
  (star-2 (str input-path "input1.txt"))

  (slurp (str input-path "input1.txt"))
  (star-1 (str input-path "test-input.txt"))
  (star-1 (str input-path "input1.txt"))

  (star-2 (str input-path "input2.txt"))
  (star-2 (str input-path "input1.txt"))
  )