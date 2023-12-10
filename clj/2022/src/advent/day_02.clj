(ns advent.day-02
  (:require [clojure.string :as str]))

(defn to-sign [char]
  (case char
    \A :rock
    \B :paper
    \C :scissors
    \X :rock
    \Y :paper
    \Z :scissors))

(defn parse-input [raw]
  (let [lines (str/split-lines raw)]
    (map (fn [line-str]
           {:opponent (-> (first line-str) to-sign)
            :you (-> (nth line-str 2) to-sign)})
         lines)))

(defn parse-input-without-you [raw]
  (let [lines (str/split-lines raw)]
    (map (fn [line-str]
           {:opponent (-> (first line-str) to-sign)
            :you (nth line-str 2)})
         lines)))

(defn game-score [a b]
  (cond
    (= a b) 3
    ;; Loss
    (or (= [a b] [:rock :scissors])
        (= [a b] [:paper :rock])
        (= [a b] [:scissors :paper]))
    0

    ;; win
    (or (= [b a] [:rock :scissors])
        (= [b a] [:paper :rock])
        (= [b a] [:scissors :paper]))
    6))

(defn single-round-score [{:keys [opponent you]}]
  (let [shape-score (case you :rock 1 :paper 2 :scissors 3)
        total-score (+ (game-score opponent you) shape-score)]
    total-score))

(defn compute-all-rounds-score [raw-input]
  (->> (parse-input raw-input)
       (map single-round-score)
       (reduce +)))

(def loose
  {:rock :paper
   :paper :scissors
   :scissors :rock})

(def win
  {:rock :scissors
   :paper :rock
   :scissors :paper})

;; X = loose, Y = draw, Z = win
(defn necessary-sign [opponent you]
  (cond
    (= you \X)
    (get win opponent)

    (= you \Y)
    opponent

    (= you \Z)
    (get loose opponent)))

(comment
  (necessary-sign :rock \X))

(defn compute-round2 [raw-input]
  (->> (parse-input-without-you raw-input)
       (map (fn [{:keys [you opponent]}]
              {:opponent opponent
               :you (necessary-sign opponent you)}))
       (map single-round-score)
       (reduce +)))

(comment
  (def example-input "A Y\nB X\nC Z\n")
  (def my-input "A Z\nA Z\nC Y\nA X\nA X\nA Z\nC X\nA X\nC Y\nA Z\nB Y\nB Y\nC X\nC Y\nC X\nB Y\nA Y\nC Y\nB Y\nB Z\nB Y\nC X\nA Z\nC Y\nB Y\nB Y\nA Z\nB Y\nB Y\nB Y\nB Z\nB Y\nA Z\nB Y\nC Y\nB Y\nB Z\nC X\nB Z\nA X\nB Y\nB Z\nC Y\nA Z\nC Y\nA Z\nB Y\nC Y\nA Z\nA Z\nB Y\nC Y\nC X\nA Z\nB Z\nA Z\nB Y\nC X\nB Y\nB Y\nB Y\nA Y\nB Z\nC Y\nC X\nA Z\nB Y\nB Y\nB X\nA Z\nC X\nB Y\nB Y\nA Z\nA Z\nA X\nA X\nB Y\nC X\nA X\nA Z\nA Z\nB Y\nC Y\nB Z\nA X\nB Z\nA Z\nB Y\nA Z\nC Y\nA Z\nA X\nB Y\nC Y\nB Y\nA X\nB Z\nC Y\nB Y\nB Y\nA Z\nA X\nA X\nB Y\nC Y\nB X\nB Z\nC X\nB Y\nA Y\nB Y\nB Y\nB Y\nA X\nC Y\nA Z\nC Y\nC Y\nB Z\nB Y\nA Z\nA Z\nA Z\nB Y\nC Y\nB Z\nC X\nC Y\nB Z\nA X\nA Z\nC Y\nB Y\nC Y\nC Y\nC Y\nB Y\nB X\nB Y\nB Y\nC Y\nA X\nB Z\nB X\nC X\nB X\nC Y\nB Z\nB Y\nA X\nC Y\nC Y\nC X\nB Z\nB Y\nA Y\nB Z\nA Z\nB Y\nB Y\nB Z\nB Z\nC Y\nA Z\nA X\nA X\nC Y\nB Y\nA Z\nB Z\nA X\nA Z\nB Z\nC Y\nC Z\nA X\nA Y\nB Y\nC Y\nB Y\nC Y\nB Z\nB Y\nA Z\nC Y\nB Z\nB Y\nA Z\nA Y\nB Y\nB Y\nC Y\nA Z\nC Y\nB Y\nB Y\nA X\nA Z\nA Z\nB Y\nB Y\nB Z\nC Y\nC X\nB Z\nA Y\nB Z\nA X\nC X\nA Y\nB Z\nB Z\nA X\nC X\nC X\nA Z\nC Y\nA X\nA X\nC X\nA X\nB Z\nC Z\nB Z\nB Y\nB Y\nA Z\nA Z\nB Y\nA Z\nC Y\nC X\nB Y\nA X\nA X\nB Z\nC Y\nC Y\nA X\nB Z\nA X\nC X\nC X\nB Z\nC X\nB Z\nA X\nA Y\nA X\nA Z\nA X\nB Y\nC Y\nA X\nC Y\nA Z\nB Z\nA Z\nC Y\nA X\nA Z\nB Y\nA Z\nB Z\nB Z\nA Z\nB Z\nA X\nB Y\nB Z\nC X\nB Y\nB Y\nB Y\nB Y\nA X\nB Z\nB Z\nA Z\nA Z\nC Y\nB Y\nC X\nA X\nB Y\nB Y\nC X\nB Y\nA X\nC Y\nA Z\nB Y\nA Y\nB Z\nB Y\nA Z\nA X\nB Z\nC Y\nA Z\nC Y\nC X\nB Y\nB Z\nC X\nA Y\nB Z\nA Y\nB Z\nB Z\nB Y\nB Z\nC X\nB Y\nA X\nB Z\nA Z\nB Y\nA Z\nC Y\nC Y\nC X\nC X\nA X\nC X\nA X\nA X\nB Z\nB Y\nB Y\nB Y\nB Y\nC X\nC X\nA Z\nA Z\nC X\nC X\nC Y\nC Y\nC Y\nB Y\nA Z\nC Y\nB Z\nB Z\nB Y\nC X\nC X\nC Y\nC Y\nC X\nB Z\nB Z\nB Y\nC Y\nB Y\nA Z\nC Y\nA X\nB Y\nC Y\nC Y\nC Y\nB Y\nA Z\nB Y\nA X\nB Z\nC X\nA Z\nC X\nA Z\nB Z\nC Y\nC Y\nC Y\nC X\nB Z\nC X\nB Y\nB Z\nC X\nB X\nA Z\nB Z\nB Y\nC Y\nB Z\nC Y\nA X\nB Z\nC Y\nC Y\nB Z\nB Y\nC Y\nB Z\nC Y\nA Z\nC X\nC X\nC Y\nA Z\nC Z\nC Y\nB Y\nC Y\nB Y\nB Y\nB Z\nC X\nC X\nB Z\nB Z\nB Z\nA Z\nB Y\nB Y\nA X\nB Y\nC Y\nB Y\nC X\nC X\nB Y\nB Y\nA Y\nB Z\nB Y\nB Y\nC Y\nA Z\nC X\nA Y\nB Y\nB Z\nB Y\nA Y\nC Y\nA Y\nA Z\nB Y\nA Z\nC Y\nC Y\nB Y\nA Z\nB Y\nB Y\nA Z\nB Z\nB Y\nC X\nC Y\nB Z\nB Y\nB Y\nA Z\nC Z\nA Z\nA X\nC X\nB Z\nB Z\nC X\nC Y\nB Y\nB X\nA X\nC Y\nB Z\nB Y\nC X\nB Y\nA Z\nA Z\nA X\nC Y\nB Z\nB Y\nC X\nC X\nB Y\nA Z\nC Z\nB Y\nA Z\nC Y\nB Y\nC X\nA Z\nA Z\nA X\nC X\nA X\nC X\nC X\nA Z\nC X\nB Y\nA Z\nB Y\nB Y\nC Y\nC X\nA Z\nB Z\nB Y\nB Y\nA Y\nA Z\nB Y\nA Z\nB Z\nB Y\nC Y\nA Z\nB Y\nB Y\nB Y\nC Y\nC X\nA X\nA Z\nB Z\nB Y\nC Y\nB Z\nC X\nB Y\nC X\nA Z\nB Y\nB X\nB Y\nA Y\nA X\nA X\nC X\nC X\nB Y\nB X\nC Y\nA Z\nB Z\nC Y\nC Y\nB Y\nA Z\nC X\nA Z\nC Y\nB Z\nB X\nC X\nB X\nC X\nB Y\nC Y\nC Y\nB Z\nB Y\nB Z\nB Z\nA Z\nB Y\nA Y\nB Z\nC X\nC Y\nC X\nA Z\nB Z\nC Y\nB Y\nB Z\nC X\nB Y\nA Y\nA X\nA X\nC Y\nB X\nC X\nB Z\nA Z\nA Y\nB Z\nC X\nC X\nB Y\nA X\nB Z\nC Y\nB X\nB Z\nC Y\nB Y\nC Y\nB Z\nB Y\nB Z\nB Y\nC X\nB Y\nC X\nB Z\nB Z\nC X\nB Z\nA X\nB X\nB Y\nA X\nB Z\nC X\nB Z\nB Y\nB Y\nC X\nA Y\nB Z\nB Z\nB Y\nB Y\nB Z\nC Y\nB Z\nC Y\nC X\nA Z\nC X\nA X\nC Y\nB Y\nC X\nA Z\nB Y\nA Z\nA Z\nB X\nC Y\nB Z\nC Y\nB Y\nB Z\nA X\nB Z\nA X\nC X\nB Z\nB Y\nC X\nC Y\nC Y\nB X\nC X\nC X\nB Z\nC Y\nB Z\nB Y\nB Z\nC Y\nB Y\nC Y\nC X\nB Y\nB Y\nB Z\nA Z\nC X\nA Z\nB Y\nB Z\nA Z\nC Y\nA Z\nB Y\nA Z\nA Z\nC Y\nA Y\nB Y\nA Y\nC X\nB Y\nC X\nB Z\nC Y\nA Z\nC X\nC X\nB Z\nC Z\nC Z\nB Y\nB Y\nC Y\nC Y\nA Z\nC Y\nC Y\nC Y\nA Z\nB Z\nC Y\nB Y\nC Y\nC X\nC X\nB X\nB Y\nC Y\nA Y\nB Y\nB Z\nB Y\nA Y\nB Y\nB X\nC X\nC Z\nC Y\nC Y\nC Y\nB Z\nB X\nC Y\nB Z\nC X\nC Y\nA Z\nA Y\nB Y\nC X\nA Z\nB Y\nC Y\nB Z\nA Z\nA Z\nB Y\nA Z\nC Y\nA X\nC Y\nB Z\nB Z\nB Z\nB Y\nB Y\nA Z\nA Z\nB Y\nC X\nA Z\nC Y\nB Y\nA Z\nC Y\nA Z\nB Z\nB Z\nA Y\nB Y\nB Y\nB Z\nC Z\nB Y\nB X\nB Y\nC X\nB Z\nC Y\nB Z\nA Y\nB Z\nA Z\nA X\nA Z\nC X\nC X\nB Y\nC X\nC Y\nA Z\nB Z\nB Y\nA Z\nB Z\nB Z\nB Z\nC Y\nB Y\nA X\nC Y\nC Y\nC X\nA X\nC Y\nB Y\nC X\nB Z\nA Y\nB X\nB Y\nA Z\nB Y\nB Y\nB Y\nB Z\nC X\nB Z\nB Z\nC X\nB Y\nA Y\nB Y\nC Y\nA Z\nC Z\nB Y\nB Z\nB Z\nC Y\nA Z\nC Y\nC X\nC Y\nB Y\nA X\nA X\nB Z\nC Y\nB Z\nB Y\nA Y\nC X\nA Z\nB Z\nC Y\nC Z\nC X\nB Y\nC X\nA Y\nA Y\nB Z\nC X\nB X\nC X\nB Y\nB Z\nB Z\nB Y\nC Y\nB Y\nB Y\nA Z\nB Z\nB Y\nC X\nB Z\nB Y\nB Y\nC Y\nC Z\nC X\nB Y\nA X\nC Y\nC X\nB Z\nC Y\nA X\nB Y\nA Y\nB Y\nC X\nC Y\nC X\nA X\nB Z\nC X\nB Y\nA Z\nC X\nB Y\nA Z\nB Y\nB Y\nB Y\nA Z\nA X\nC X\nC Y\nC Y\nB Y\nB Y\nB Z\nA Y\nC X\nC X\nB Y\nB Y\nB Z\nA X\nC X\nC X\nB X\nA X\nB Z\nB Y\nC X\nA X\nB Y\nC Y\nA X\nB Y\nC X\nB Z\nC Z\nB X\nC Y\nB Y\nC Y\nA Z\nC Y\nB Z\nC X\nB Z\nC X\nC Y\nC X\nC Y\nC Z\nC X\nC X\nB Y\nC Y\nC X\nC X\nB Z\nB Z\nA X\nC Y\nC Z\nB Y\nC Z\nC Y\nC X\nC X\nB Y\nC Y\nA Y\nB Y\nA Y\nB Y\nB Y\nB Y\nC Y\nC Z\nB Y\nC X\nC X\nB Z\nB Y\nB Z\nC Y\nB Z\nC X\nB Z\nB Y\nB Y\nA Z\nA X\nB X\nA X\nC X\nC Y\nB Y\nA X\nB Z\nB Y\nB Y\nA X\nC X\nC X\nB Y\nB Y\nC Y\nC Y\nB Z\nB Y\nB Y\nB Y\nC Y\nA X\nB Y\nC Y\nB Z\nB Y\nB Y\nB Z\nB Y\nB Y\nB Z\nB Y\nB Y\nC Y\nA Z\nB Z\nA Y\nB Y\nC Y\nB Z\nB Y\nB Y\nC X\nA X\nC Z\nC X\nB Y\nB Z\nC Y\nC X\nB Z\nA Z\nA Z\nC Y\nA Z\nB Y\nB Y\nB Z\nB Y\nC Y\nB Z\nC Y\nB Z\nB Z\nB Y\nC X\nB Y\nB Z\nA X\nC X\nC X\nB Z\nB Z\nC X\nB Y\nA Z\nB Y\nB Z\nC Y\nB Y\nA Z\nB Z\nB Y\nB Y\nB Y\nC X\nC Y\nA Z\nB Y\nB Y\nA Z\nA Z\nA Z\nA X\nB X\nA Z\nC Y\nC X\nA X\nB Z\nA X\nC X\nB Z\nB Y\nB Y\nC X\nA Z\nC Y\nC X\nA Y\nC X\nA Z\nB Z\nB Z\nB Y\nC Y\nB Z\nA Z\nA Z\nC Y\nB Y\nB Z\nA Z\nA Z\nB Z\nB Y\nA Z\nC Y\nA Z\nA Z\nB Y\nB Z\nB Z\nA X\nA X\nB Y\nB Z\nB Z\nC X\nB Z\nC Z\nB Y\nB Y\nB Y\nB Z\nA X\nA Z\nB Y\nA X\nB Y\nB Y\nC X\nC X\nC Y\nC Y\nA Z\nA X\nB Y\nA Z\nB Z\nB Y\nC X\nB Y\nB X\nC X\nA X\nA Z\nC Y\nC X\nC Y\nB Y\nB Y\nA X\nA Y\nB Y\nA Y\nC Y\nC X\nB Y\nB Y\nB Z\nC X\nC X\nB Y\nC X\nB Z\nC Y\nA X\nA Z\nA Z\nB Y\nA Y\nC Y\nC X\nC Y\nC Y\nB Y\nB Y\nA Z\nC X\nA X\nB Y\nB Y\nB Y\nB Z\nA Z\nB Y\nB Y\nA Z\nB Y\nA Z\nC X\nB Z\nC Y\nB Y\nB Y\nA X\nB Y\nA X\nB Y\nB Z\nC X\nB Z\nB Y\nC X\nC Y\nC Y\nA X\nA X\nB Z\nB Z\nB Y\nC X\nA Z\nC Y\nC Y\nC Y\nB Z\nB Z\nB Y\nA Z\nC Y\nA X\nA Z\nC Y\nB Z\nB Y\nB Y\nA Z\nB Y\nA Z\nC X\nA X\nB Z\nB Y\nB Y\nB Z\nA Z\nC X\nB Y\nC X\nB Z\nC X\nA Z\nB Y\nA Z\nA Z\nB Y\nC Y\nC X\nC Y\nA X\nC Y\nB Y\nB Z\nC X\nC Y\nA X\nB Z\nB Y\nC Y\nA X\nB Z\nA Y\nB Z\nB Y\nB Z\nA X\nA Z\nA X\nB Z\nC Y\nB Y\nA Y\nB Z\nA Z\nC Y\nC X\nB Z\nB Y\nC Y\nB Z\nC X\nC Y\nA X\nB Z\nB Z\nB Z\nB Z\nB Y\nC X\nB Y\nC X\nA Z\nA Z\nC Y\nA Y\nB Y\nC X\nA Z\nB Z\nC X\nC X\nA X\nB Y\nC X\nA Z\nB Y\nB Y\nB Y\nB Y\nC X\nC Y\nC Y\nB Y\nA Z\nB Y\nB Z\nB Z\nB Y\nA X\nB Z\nB Z\nC X\nC X\nC Y\nB Y\nC Y\nB Y\nB Y\nB Z\nB X\nC X\nC Y\nA Z\nB Z\nB Y\nA X\nB Z\nA Z\nB Z\nB Y\nB Y\nA Z\nC Y\nB Y\nC Y\nB Z\nB Y\nC X\nB Z\nB Z\nC Y\nC Y\nB Y\nC Y\nC X\nB X\nB Y\nC Y\nB Y\nB X\nC X\nB Y\nA Y\nA Z\nA Y\nB Z\nA Z\nA Z\nA Z\nB Y\nA Z\nA Z\nC Y\nB Z\nA Z\nA X\nB Y\nC Y\nC X\nC X\nA Z\nB Z\nB X\nB Z\nC X\nB Z\nC Y\nC X\nC Z\nC X\nB Z\nB Z\nB Z\nB Z\nA Z\nA Z\nC Y\nC Z\nC Y\nA X\nB Y\nB Y\nB Y\nA Z\nA Z\nB Z\nC Y\nC X\nB Y\nC X\nB Z\nB Y\nB Y\nB Y\nB Y\nB Y\nB X\nA Z\nA X\nA Y\nC X\nB Z\nB Y\nB Z\nC Y\nA Z\nB Z\nB Z\nC Y\nB Z\nB Z\nC X\nB Y\nC X\nB Z\nC X\nC Y\nC Y\nC X\nA Z\nA Z\nB Z\nB Y\nC X\nC X\nA Y\nA Z\nC X\nA X\nA X\nA Z\nC Y\nB Z\nB Y\nA Z\nB Y\nB Y\nB Z\nB Y\nC Y\nB Z\nA X\nB Y\nA X\nB Z\nA Z\nC X\nA X\nC Z\nA Z\nC Y\nA X\nB Y\nB Z\nB Z\nA X\nC Y\nA Z\nC X\nA Y\nC X\nB Y\nB Z\nA X\nB Y\nB Y\nA Y\nA X\nB Y\nC X\nA Y\nB Y\nB Y\nC Y\nA Z\nB Z\nA Z\nB Y\nA Z\nC Y\nC X\nB Z\nB Y\nB Z\nA X\nC Y\nC Z\nA X\nA X\nC Y\nC Y\nA Z\nC Y\nC X\nA Y\nC Y\nB Y\nB X\nB Y\nA Z\nC X\nC X\nB Y\nA Z\nA X\nB Z\nC X\nB Y\nB Y\nC Z\nA X\nA X\nB Z\nC X\nC Y\nB Z\nC Z\nB Y\nA Y\nA Z\nA Y\nC Y\nC Y\nC Y\nB Z\nB Z\nB Y\nB Y\nB Y\nB Z\nB Z\nC X\nA X\nC X\nB Y\nB Y\nB Y\nC Y\nB Y\nB Y\nC X\nA Y\nB Z\nC X\nB Z\nB Y\nA X\nC Y\nB Y\nC Y\nB Z\nB Y\nC Y\nC Z\nC Y\nA Y\nB Z\nA X\nB Z\nA Z\nA Z\nA Z\nB Y\nA X\nC Y\nB Y\nB Y\nB Y\nC X\nB Y\nA X\nC X\nB Y\nC X\nA Z\nB Z\nB Z\nB Y\nC Y\nB Y\nA Z\nB Y\nC Y\nB Z\nC X\nB Y\nB Y\nB Z\nC X\nC Y\nB Z\nB Z\nC Y\nC X\nC X\nB Y\nB Z\nA X\nC X\nA Z\nA Y\nB Z\nB Y\nB Y\nC Y\nC X\nB Y\nB Z\nC Y\nC Y\nC X\nC X\nB Z\nA Z\nB Z\nB Y\nC X\nA X\nC X\nA Z\nC X\nB Z\nC Z\nB Y\nC Y\nB Y\nC Z\nA Z\nB Y\nC Z\nA X\nB Z\nB X\nC Y\nC X\nB Z\nA Z\nB Y\nA X\nB X\nA Z\nC Y\nA Y\nA Z\nA X\nC X\nA Y\nC X\nB X\nB Y\nC Z\nA Z\nC X\nB Y\nC X\nC X\nB X\nB Y\nA Y\nB Y\nB Y\nA Z\nB Y\nC Y\nC X\nB Y\nB Y\nB Z\nC Y\nB Z\nA Z\nA Z\nA X\nB Y\nC Y\nB Y\nC Y\nC Y\nB Z\nA X\nC Y\nB Z\nC Y\nA Y\nB Y\nB Y\nB Y\nC Y\nA Z\nA Z\nB Z\nA Z\nC Y\nC Y\nB Y\nC X\nB Y\nA Z\nB Z\nA X\nB Z\nB Y\nB Z\nA X\nA X\nC Y\nA Z\nB Z\nC Y\nB Z\nC X\nB Z\nA X\nB Z\nB Z\nC Y\nB Y\nB Y\nC Y\nB Y\nA X\nB Y\nC Y\nA Z\nB Y\nC X\nA Z\nB Z\nA Z\nB Z\nC Y\nB Y\nB Y\nB Y\nC Y\nB Y\nB Y\nA Z\nB Y\nB Z\nC X\nB Z\nC X\nC X\nA X\nB Z\nC Z\nB Z\nA Y\nC X\nC X\nA Z\nA X\nA Y\nA X\nB Y\nB Y\nC X\nC Z\nA Y\nC Y\nB Y\nB Y\nC X\nC X\nB Z\nB Z\nB Y\nC Y\nA Z\nB Y\nC X\nC X\nA X\nA X\nA Y\nB Y\nC X\nA Z\nC Z\nC Y\nC Y\nC Y\nB Y\nC Y\nA Y\nA X\nB X\nB Y\nB Y\nB Y\nB Y\nA Z\nB Z\nB Y\nB Y\nC Y\nA X\nA Z\nA Z\nA Z\nB Y\nA X\nC Y\nC Y\nB Y\nB Y\nC Y\nA Z\nC Y\nA X\nC X\nB Z\nB Y\nC X\nB Y\nA X\nA Z\nC X\nC Y\nC Y\nC X\nC X\nB Z\nB Y\nA Z\nB Y\nB Z\nB Z\nB Y\nB Y\nA X\nC X\nC Y\nC X\nA Z\nB Z\nB Y\nB Y\nC Y\nA X\nA X\nC Y\nC X\nB Z\nA Y\nB Y\nB Z\nB Y\nA X\nC Y\nB Y\nA X\nC X\nB Y\nB Y\nA Z\nA Z\nA Z\nB Y\nB Y\nC X\nB Z\nA Z\nB Z\nA Z\nC X\nB Y\nC X\nB Y\nC X\nC X\nB Y\nB Y\nA Y\nC Y\nC Y\nB Z\nC X\nB Y\nB Y\nA X\nA Z\nB Y\nC Y\nB Y\nC Y\nA X\nC Y\nC Y\nB Z\nC Y\nA X\nC X\nA Z\nC Y\nA Z\nC X\nB Z\nB Y\nB Z\nC Y\nB X\nA Z\nC Y\nC X\nB Y\nC X\nB Z\nA X\nA Z\nC X\nB Z\nC X\nB Y\nA Z\nB Y\nC Y\nB Y\nC X\nC Y\nB X\nB X\nB Y\nB Y\nB Y\nB Z\nB Y\nC X\nA Z\nA Z\nC X\nB Y\nB Y\nB Y\nA Y\nC Y\nB Z\nC X\nC Y\nA X\nC Y\nB Z\nC Y\nB Y\nB Y\nB Z\nA Z\nA Z\nB Y\nA Y\nB Z\nA X\nC Y\nB Y\nA X\nC X\nB Y\nC Y\nB Z\nB Y\nC X\nA Z\nC Y\nA Z\nB Y\nC Y\nB Y\nC X\nA Z\nC Y\nC Y\nA Y\nB Z\nC X\nC X\nA Z\nC Z\nC X\nB Y\nC X\nA X\nC Y\nC X\nC X\nA X\nA Y\nA X\nC X\nA X\nA X\nB X\nB Y\nC Y\nA X\nB Z\nC X\nC Y\nB Y\nB Z\nC Y\nB Z\nB Y\nC X\nA X\nB Y\nB Z\nC Y\nA Z\nC X\nC Z\nA X\nA Z\nA Z\nB Z\nA Z\nB Y\nC Y\nC Y\nB Z\nC Y\nC Y\nC Y\nC X\nA X\nB Y\nA X\nC X\nC Z\nB Y\nA Z\nB Y\nC Y\nC Y\nB Z\nC Y\nC X\nB Z\nC Y\nB Y\nB Z\nA Z\nB Z\nB Z\nB Y\nA Z\nA Y\nC X\nB Y\nA Z\nC Y\nB Z\nA Z\nB Y\nB Y\nC Y\nB Y\nB Y\nB Y\nC Y\nB X\nB Y\nA X\nA X\nB Z\nA X\nC Y\nA Z\nB Y\nB Y\nB Z\nA Z\nC Y\nC X\nB Y\nA Z\nA X\nB Y\nC X\nB Z\nA X\nC X\nB Z\nB Z\nB Z\nB X\nA X\nC Z\nC X\nC Z\nB X\nA Z\nC X\nB Y\nC X\nC Y\nC Y\nB Y\nB Z\nB Y\nC Y\nC X\nA Z\nC Y\nC Y\nC Y\nB Z\nA Z\nA Y\nC X\nC Y\nB Y\nC Y\nC Y\nB Z\nC Y\nB Y\nB Y\nC X\nB Z\nB Y\nA X\nB Y\nA Z\nB Y\nC X\nB Y\nC X\nB Y\nA Z\nC X\nB Y\nC X\nB Z\nC X\nB Y\nA X\nC X\nC X\nA X\nC Y\nA Z\nC Y\nB Z\nA Z\nA X\nC X\nA X\nC Y\nA X\nA X\nB Z\nC X\nC Y\nA Y\nB Z\nB Y\nC Y\nB Y\nA Y\nC Y\nC Y\nB Y\nB Y\nB Z\nB Z\nC X\nB Y\nC X\nA X\nA Z\nC Y\nB Z\nC Y\nB Y\nC X\nC X\nA Z\nB Z\nA Z\nC X\nB Y\nB Z\nC Y\nB Y\nA X\nA X\nB Y\nB Y\nB Y\nA X\nB Y\nB Z\nA Z\nA X\nA X\nB Z\nB Y\nA X\nB Z\nA Z\nB Y\nB Z\nB Z\nB Z\nC Y\nC Y\nA Z\nB Y\nC Y\nA Z\nC X\nB Z\nA X\nB X\nB Y\nB Y\nC Y\nC Y\nB Z\nB Y\nA Z\nB Y\nA X\nB Z\nB Y\nC Z\nB Y\nB Y\nC Y\nB Y\nB Y\nA Y\nC Y\nA Z\nA Y\nB Z\nC X\nB Z\nA X\nA Z\nA X\nC Y\nA Z\nB Y\nB Z\nB Z\nC Y\nC Y\nC Y\nB X\nB Y\nB Z\nA X\nB Y\nC Y\nA X\nA X\nB Y\nC Y\nA Y\nB Z\nB Z\nB Z\nA Z\nB Y\nC X\nA Z\nC Y\nC Y\nB Y\nC Y\nC Y\nC Y\nC Y\nB Y\nB Z\nA Z\nB X\nC Y\nC X\nC X\nC Y\nA Z\nC Y\nC X\nB Z\nB Y\nA Y\nC Z\nB Y\nB Y\nB Y\nB Y\nC Y\nB Y\nC Y\nA Z\nB Y\nC X\nC Y\nB Z\nA X\nC Y\nB Y\nB Y\nB Y\nA Z\nA X\nA X\nC X\nC Z\nB Z\nA X\nA Z\nA X\nA Y\nC Y\nC Y\nA Z\nC X\nC X\nB X\nB Y\nA X\nA Z\nA X\nC Y\nB X\nA X\nA X\nC X\nA Y\nB Z\nA Z\nB Y\nA X\nA Z\nB Y\nA Z\nC Y\nB Z\nA Z\nC Y\nC X\nB Z\nC Y\nB Y\nC X\nA Z\nB Z\nA X\nA X\nC Z\nA X\nC X\nB Y\nB Z\nA X\nA X\nC Y\nA Z\nC Y\nC X\nC Y\nB Z\nB Y\nC X\nC X\nB Z\nB Z\nC Y\nA X\nB X\nC X\nC Y\nA X\nA Z\nB Y\nC X\nA Y\nC X\nC Y\nA Z\nB Y\nB Z\nB Y\nC X\nC Y\nC X\nC X\nB Y\nC X\nC X\nB Y\nC Z\nC Y\nC Y\nB Z\nB Z\nB Y\nA X\nB Y\nA Z\nA Y\nB Y\nC Y\nB Z\nB Z\nA X\nB Y\nA X\nB Y\nA Z\nB Y\nC X\nA Z\nB Z\nC Y\nB Y\nA Y\nA X\nC X\nA X\nB Y\nB Z\nB Z\nC Y\nA X\nB Y\nB Z\nB Y\nB X\nC Z\nB Z\nC X\nC Y\nC X\nB Z\nC Y\nA X\nA X\nC X\nB Y\nB X\nB Y\nA X\nC Y\nA Z\nB Y\nB Y\nB Y\nB Z\nB Y\nC X\nA Z\nC Y\nA Z\nB Z\nC X\nC Y\nC X\nC Y\nA Y\nB Z\nB Y\nB Z\nC Y\nA X\nB Y\nA X\nC X\nB X\nC X\nA Z\nB Z\nB Z\nC Y\nB Z")

  (let [{:keys [opponent you]} {:opponent :rock :you :scissors}
        shape-score (case you :rock 1 :paper 2 :scissors 3)
        total-score (+ (game-score opponent you) shape-score)
        ]
    total-score)

  (compute-all-rounds-score my-input)
  (compute-round2 my-input)

  )