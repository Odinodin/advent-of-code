(ns advent.day-05
  (:require [clojure.string :as str]
            [medley.core :refer [map-vals]]))

;;     [D]
;; [N] [C]
;; [Z] [M] [P]
;;  1   2   3
;;
;; move 1 from 2 to 1
;; move 3 from 1 to 3
;; move 2 from 2 to 1
;; move 1 from 1 to 2


(defn column-ascii-line-to-mapping [column-ascii-line]
  (keep-indexed (fn [idx char]
                  (when-not (str/blank? (str char))
                    {:ascii-index idx
                     :column (Integer/parseInt (str char))}))
    column-ascii-line))

;; Returns {col-id {:column col-id :boxes [a b c]}}
(defn tower-ascii-lines-to-mapping [tower-ascii-lines columns-mapping ]
  (let [columns-with-boxes (for [{:keys [ascii-index column]} columns-mapping]
                             (let [reversed-tower-lines (reverse tower-ascii-lines)]

                               {:column column
                                :boxes (keep
                                         (fn [ascii-line]
                                           (let [char (nth ascii-line ascii-index)]
                                             (when-not (str/blank? (str char)) char)))
                                         reversed-tower-lines)}
                               ))]
    (->> columns-with-boxes
         (group-by :column)
         (map-vals #(-> % first :boxes)))))

(comment
  (let [input [{:column 1, :boxes [\Z \N]} {:column 2, :boxes [\M \C \D]} {:column 3, :boxes [\P]}]]
    (group-by :column input))
  )

;; move 1 from 2 to 1
(defn execute-move [column->boxes move]
  (let [[_ num-moves-str from-str to-str] (re-matches #"move\s(\d+)\sfrom\s(\d+)\sto\s(\d+)" move)
        num-moves (Integer/parseInt num-moves-str)
        from (Integer/parseInt from-str)
        to (Integer/parseInt to-str)]
    (reduce (fn [acc _]
              (let [box-to-move (last (get acc from))]
                (-> acc
                    (update from drop-last)
                    (update to #(concat % [box-to-move])))))
            column->boxes
            (range num-moves))))

(defn execute-cratemover-move [column->boxes move]
  (let [[_ num-moves-str from-str to-str] (re-matches #"move\s(\d+)\sfrom\s(\d+)\sto\s(\d+)" move)
        num-moves (Integer/parseInt num-moves-str)
        from (Integer/parseInt from-str)
        to (Integer/parseInt to-str)]

    (let [boxes-to-move (take-last num-moves (get column->boxes from))]
      (-> column->boxes
          (update from #(drop-last num-moves %))
          (update to #(concat % boxes-to-move))))))


(comment
  (execute-cratemover-move {1 '("a" "b") 2 '("c" "d")} "move 2 from 1 to 2")
  )


(defn answer [raw]
  (let [lines (str/split-lines raw)
        [tower-ascii-all-lines _ moves] (partition-by #(= % "") lines)
        tower-ascii-lines (butlast tower-ascii-all-lines)
        tower-ascii-column-line (last tower-ascii-all-lines)
        columns-mapping (column-ascii-line-to-mapping tower-ascii-column-line)
        column->boxes (tower-ascii-lines-to-mapping tower-ascii-lines columns-mapping)]

    (->> (reduce (fn [acc curr-move]
                   (execute-move acc curr-move))
                 column->boxes
                 moves)
         (sort-by first)
         (map last)
         (map last)
         (str/join ""))))

(defn answer-part-2 [raw]
  (let [lines (str/split-lines raw)
        [tower-ascii-all-lines _ moves] (partition-by #(= % "") lines)
        tower-ascii-lines (butlast tower-ascii-all-lines)
        tower-ascii-column-line (last tower-ascii-all-lines)
        columns-mapping (column-ascii-line-to-mapping tower-ascii-column-line)
        column->boxes (tower-ascii-lines-to-mapping tower-ascii-lines columns-mapping)]

    (->> (reduce (fn [acc curr-move]
                   (execute-cratemover-move acc curr-move))
                 column->boxes
                 moves)
         (sort-by first)
         (map last)
         (map last)
         (str/join ""))))



(comment
  (update {1 '("a" "b")} 1 drop-last)

  (re-matches #"move\s(\d+)\sfrom\s(\d+)\sto\s(\d+)" "move 1 from 2 to 1")

  (def example-input "    [D]    \n[N] [C]    \n[Z] [M] [P]\n 1   2   3 \n\nmove 1 from 2 to 1\nmove 3 from 1 to 3\nmove 2 from 2 to 1\nmove 1 from 1 to 2")
  (def my-input "        [H]         [S]         [D]\n    [S] [C]         [C]     [Q] [L]\n    [C] [R] [Z]     [R]     [H] [Z]\n    [G] [N] [H] [S] [B]     [R] [F]\n[D] [T] [Q] [F] [Q] [Z]     [Z] [N]\n[Z] [W] [F] [N] [F] [W] [J] [V] [G]\n[T] [R] [B] [C] [L] [P] [F] [L] [H]\n[H] [Q] [P] [L] [G] [V] [Z] [D] [B]\n 1   2   3   4   5   6   7   8   9 \n\nmove 2 from 7 to 2\nmove 1 from 4 to 8\nmove 2 from 1 to 9\nmove 4 from 6 to 5\nmove 1 from 7 to 6\nmove 2 from 1 to 4\nmove 7 from 8 to 9\nmove 7 from 4 to 5\nmove 4 from 2 to 4\nmove 1 from 5 to 9\nmove 14 from 5 to 4\nmove 1 from 3 to 8\nmove 5 from 4 to 8\nmove 1 from 2 to 5\nmove 2 from 4 to 1\nmove 6 from 8 to 1\nmove 1 from 8 to 6\nmove 1 from 2 to 5\nmove 5 from 3 to 7\nmove 2 from 6 to 3\nmove 2 from 4 to 7\nmove 3 from 3 to 9\nmove 7 from 4 to 1\nmove 1 from 6 to 9\nmove 2 from 6 to 1\nmove 3 from 5 to 2\nmove 1 from 1 to 8\nmove 21 from 9 to 1\nmove 1 from 4 to 2\nmove 7 from 7 to 2\nmove 1 from 4 to 2\nmove 23 from 1 to 5\nmove 5 from 5 to 1\nmove 1 from 3 to 6\nmove 1 from 6 to 3\nmove 12 from 1 to 6\nmove 1 from 3 to 6\nmove 2 from 1 to 8\nmove 1 from 9 to 3\nmove 2 from 8 to 1\nmove 2 from 1 to 8\nmove 1 from 1 to 3\nmove 2 from 3 to 1\nmove 2 from 8 to 1\nmove 3 from 6 to 1\nmove 1 from 8 to 7\nmove 4 from 6 to 2\nmove 3 from 6 to 9\nmove 2 from 5 to 7\nmove 2 from 7 to 8\nmove 1 from 7 to 9\nmove 9 from 1 to 5\nmove 12 from 5 to 9\nmove 1 from 8 to 6\nmove 1 from 6 to 9\nmove 1 from 6 to 9\nmove 7 from 9 to 4\nmove 10 from 2 to 1\nmove 12 from 5 to 4\nmove 7 from 4 to 9\nmove 7 from 4 to 7\nmove 1 from 5 to 4\nmove 7 from 7 to 8\nmove 1 from 6 to 3\nmove 1 from 3 to 1\nmove 3 from 2 to 4\nmove 1 from 6 to 8\nmove 7 from 1 to 2\nmove 1 from 6 to 7\nmove 12 from 9 to 4\nmove 3 from 8 to 5\nmove 1 from 7 to 3\nmove 6 from 9 to 1\nmove 10 from 1 to 9\nmove 7 from 9 to 5\nmove 3 from 9 to 5\nmove 1 from 3 to 4\nmove 2 from 2 to 1\nmove 1 from 5 to 1\nmove 9 from 4 to 3\nmove 1 from 1 to 3\nmove 8 from 4 to 7\nmove 7 from 5 to 3\nmove 2 from 7 to 2\nmove 8 from 3 to 9\nmove 1 from 1 to 8\nmove 10 from 2 to 3\nmove 4 from 8 to 7\nmove 12 from 3 to 4\nmove 9 from 7 to 2\nmove 2 from 1 to 3\nmove 1 from 9 to 6\nmove 2 from 4 to 9\nmove 1 from 7 to 6\nmove 5 from 5 to 9\nmove 8 from 3 to 1\nmove 2 from 6 to 3\nmove 14 from 4 to 3\nmove 15 from 3 to 9\nmove 1 from 3 to 1\nmove 3 from 9 to 8\nmove 1 from 8 to 1\nmove 1 from 3 to 2\nmove 5 from 2 to 8\nmove 1 from 4 to 2\nmove 2 from 1 to 3\nmove 2 from 3 to 9\nmove 3 from 2 to 4\nmove 6 from 1 to 8\nmove 2 from 2 to 6\nmove 1 from 6 to 4\nmove 2 from 4 to 7\nmove 5 from 8 to 5\nmove 1 from 6 to 9\nmove 7 from 9 to 6\nmove 1 from 5 to 3\nmove 2 from 7 to 8\nmove 2 from 2 to 4\nmove 3 from 5 to 6\nmove 1 from 3 to 8\nmove 1 from 5 to 6\nmove 2 from 4 to 1\nmove 3 from 1 to 6\nmove 21 from 9 to 5\nmove 1 from 4 to 3\nmove 1 from 4 to 9\nmove 2 from 9 to 2\nmove 1 from 3 to 9\nmove 4 from 2 to 3\nmove 3 from 8 to 1\nmove 14 from 5 to 9\nmove 7 from 5 to 4\nmove 3 from 8 to 4\nmove 4 from 3 to 2\nmove 3 from 8 to 5\nmove 1 from 2 to 3\nmove 1 from 5 to 1\nmove 2 from 5 to 4\nmove 3 from 2 to 9\nmove 11 from 4 to 1\nmove 17 from 9 to 2\nmove 17 from 2 to 9\nmove 10 from 9 to 2\nmove 2 from 8 to 2\nmove 3 from 8 to 3\nmove 8 from 9 to 7\nmove 4 from 7 to 3\nmove 2 from 3 to 2\nmove 3 from 2 to 3\nmove 9 from 3 to 5\nmove 1 from 1 to 9\nmove 8 from 5 to 1\nmove 2 from 7 to 9\nmove 24 from 1 to 3\nmove 24 from 3 to 6\nmove 1 from 5 to 3\nmove 10 from 2 to 1\nmove 1 from 4 to 5\nmove 3 from 9 to 1\nmove 1 from 3 to 5\nmove 17 from 6 to 5\nmove 1 from 7 to 4\nmove 13 from 5 to 4\nmove 3 from 5 to 8\nmove 1 from 7 to 9\nmove 3 from 6 to 9\nmove 8 from 6 to 4\nmove 1 from 9 to 6\nmove 11 from 1 to 8\nmove 1 from 5 to 6\nmove 12 from 4 to 9\nmove 2 from 5 to 1\nmove 1 from 1 to 7\nmove 5 from 9 to 2\nmove 1 from 7 to 9\nmove 3 from 1 to 5\nmove 3 from 5 to 9\nmove 7 from 9 to 3\nmove 4 from 9 to 6\nmove 3 from 6 to 8\nmove 5 from 4 to 3\nmove 2 from 2 to 6\nmove 3 from 9 to 3\nmove 3 from 6 to 4\nmove 4 from 2 to 6\nmove 11 from 3 to 5\nmove 11 from 6 to 9\nmove 2 from 3 to 5\nmove 1 from 5 to 8\nmove 3 from 6 to 2\nmove 7 from 9 to 2\nmove 8 from 5 to 7\nmove 6 from 4 to 5\nmove 2 from 4 to 3\nmove 1 from 8 to 6\nmove 4 from 8 to 3\nmove 13 from 8 to 3\nmove 1 from 9 to 5\nmove 6 from 7 to 2\nmove 1 from 7 to 6\nmove 1 from 6 to 5\nmove 2 from 6 to 7\nmove 13 from 3 to 5\nmove 6 from 2 to 7\nmove 1 from 6 to 1\nmove 1 from 2 to 8\nmove 2 from 7 to 8\nmove 14 from 5 to 8\nmove 1 from 1 to 4\nmove 9 from 2 to 1\nmove 14 from 8 to 7\nmove 3 from 3 to 9\nmove 11 from 5 to 3\nmove 1 from 4 to 5\nmove 4 from 9 to 8\nmove 4 from 8 to 7\nmove 5 from 3 to 9\nmove 11 from 7 to 8\nmove 9 from 1 to 3\nmove 4 from 3 to 2\nmove 6 from 8 to 4\nmove 2 from 8 to 2\nmove 13 from 3 to 6\nmove 1 from 4 to 1\nmove 5 from 4 to 2\nmove 10 from 2 to 6\nmove 4 from 9 to 1\nmove 8 from 7 to 8\nmove 10 from 8 to 5\nmove 2 from 3 to 2\nmove 2 from 8 to 6\nmove 1 from 7 to 1\nmove 2 from 7 to 6\nmove 2 from 2 to 9\nmove 2 from 8 to 6\nmove 6 from 1 to 7\nmove 5 from 9 to 1\nmove 4 from 7 to 8\nmove 1 from 7 to 2\nmove 2 from 1 to 7\nmove 1 from 3 to 8\nmove 1 from 1 to 6\nmove 2 from 2 to 6\nmove 1 from 7 to 8\nmove 1 from 1 to 9\nmove 8 from 5 to 7\nmove 2 from 7 to 9\nmove 9 from 6 to 3\nmove 13 from 6 to 8\nmove 3 from 9 to 1\nmove 5 from 6 to 1\nmove 3 from 8 to 1\nmove 3 from 3 to 4\nmove 1 from 4 to 3\nmove 1 from 4 to 8\nmove 4 from 6 to 3\nmove 11 from 8 to 2\nmove 1 from 6 to 9\nmove 8 from 3 to 9\nmove 3 from 5 to 8\nmove 4 from 1 to 2\nmove 6 from 8 to 5\nmove 6 from 5 to 1\nmove 5 from 1 to 3\nmove 3 from 3 to 4\nmove 3 from 8 to 4\nmove 2 from 4 to 5\nmove 10 from 7 to 8\nmove 5 from 9 to 2\nmove 1 from 7 to 5\nmove 3 from 5 to 2\nmove 4 from 9 to 3\nmove 4 from 1 to 5\nmove 1 from 3 to 2\nmove 3 from 5 to 2\nmove 6 from 2 to 5\nmove 10 from 8 to 3\nmove 4 from 4 to 5\nmove 4 from 2 to 8\nmove 12 from 3 to 8\nmove 1 from 1 to 3\nmove 9 from 8 to 6\nmove 1 from 4 to 1\nmove 6 from 8 to 7\nmove 3 from 1 to 7\nmove 9 from 5 to 7\nmove 11 from 7 to 2\nmove 2 from 7 to 3\nmove 9 from 2 to 7\nmove 1 from 8 to 7\nmove 1 from 5 to 2\nmove 2 from 6 to 2\nmove 2 from 1 to 2\nmove 6 from 3 to 5\nmove 2 from 3 to 6\nmove 4 from 7 to 3\nmove 3 from 3 to 1\nmove 2 from 1 to 5\nmove 7 from 7 to 6\nmove 1 from 1 to 5\nmove 3 from 2 to 4\nmove 1 from 3 to 2\nmove 18 from 2 to 1\nmove 4 from 2 to 7\nmove 6 from 5 to 9\nmove 1 from 4 to 8\nmove 2 from 6 to 1\nmove 19 from 1 to 2\nmove 4 from 9 to 5\nmove 5 from 7 to 2\nmove 1 from 8 to 7\nmove 1 from 1 to 2\nmove 6 from 5 to 7\nmove 1 from 3 to 8\nmove 6 from 7 to 6\nmove 1 from 4 to 1\nmove 4 from 7 to 9\nmove 1 from 1 to 3\nmove 1 from 2 to 5\nmove 1 from 4 to 8\nmove 1 from 3 to 4\nmove 3 from 5 to 4\nmove 2 from 8 to 9\nmove 9 from 2 to 4\nmove 19 from 6 to 4\nmove 1 from 4 to 7\nmove 5 from 9 to 5\nmove 10 from 2 to 9\nmove 2 from 5 to 4\nmove 14 from 4 to 7\nmove 2 from 2 to 1\nmove 3 from 9 to 1\nmove 1 from 1 to 3\nmove 13 from 7 to 6\nmove 1 from 5 to 9\nmove 1 from 6 to 9\nmove 1 from 7 to 2\nmove 5 from 9 to 7\nmove 1 from 5 to 2\nmove 3 from 7 to 3\nmove 3 from 4 to 9\nmove 1 from 5 to 2\nmove 4 from 4 to 2\nmove 2 from 7 to 3\nmove 4 from 1 to 6\nmove 1 from 7 to 9\nmove 11 from 9 to 5\nmove 8 from 2 to 9\nmove 6 from 9 to 6\nmove 8 from 4 to 5\nmove 14 from 5 to 6\nmove 1 from 5 to 4\nmove 3 from 5 to 1\nmove 1 from 5 to 2\nmove 2 from 6 to 4\nmove 2 from 4 to 2\nmove 1 from 9 to 2\nmove 1 from 2 to 3\nmove 1 from 9 to 3\nmove 3 from 2 to 7\nmove 7 from 6 to 7\nmove 5 from 4 to 3\nmove 23 from 6 to 1\nmove 5 from 7 to 2\nmove 22 from 1 to 6\nmove 6 from 6 to 3\nmove 6 from 2 to 4\nmove 6 from 4 to 1\nmove 3 from 7 to 8\nmove 3 from 1 to 8\nmove 4 from 3 to 2\nmove 1 from 1 to 3\nmove 3 from 3 to 1\nmove 1 from 7 to 5\nmove 1 from 6 to 5\nmove 1 from 7 to 4\nmove 4 from 6 to 9\nmove 5 from 3 to 6\nmove 2 from 2 to 1\nmove 3 from 9 to 4\nmove 11 from 1 to 9\nmove 2 from 4 to 7\nmove 4 from 6 to 1\nmove 1 from 5 to 4\nmove 5 from 8 to 9\nmove 1 from 7 to 1\nmove 3 from 2 to 7\nmove 4 from 1 to 2\nmove 3 from 4 to 2\nmove 1 from 8 to 5\nmove 1 from 5 to 4\nmove 1 from 5 to 4\nmove 5 from 6 to 1\nmove 3 from 7 to 6\nmove 5 from 2 to 8\nmove 15 from 9 to 2\nmove 1 from 3 to 9\nmove 10 from 6 to 8\nmove 1 from 4 to 9\nmove 1 from 8 to 3\nmove 1 from 4 to 6\nmove 4 from 6 to 3\nmove 2 from 9 to 7\nmove 1 from 7 to 6\nmove 1 from 1 to 6\nmove 3 from 3 to 8\nmove 2 from 7 to 8\nmove 3 from 8 to 4\nmove 12 from 2 to 9\nmove 14 from 9 to 5\nmove 12 from 8 to 2\nmove 1 from 6 to 7\nmove 8 from 3 to 1\nmove 2 from 4 to 6\nmove 1 from 3 to 6\nmove 5 from 6 to 1\nmove 17 from 1 to 2\nmove 29 from 2 to 1\nmove 1 from 8 to 5\nmove 1 from 4 to 3\nmove 1 from 8 to 5\nmove 1 from 8 to 7\nmove 5 from 2 to 1\nmove 1 from 3 to 5\nmove 1 from 6 to 4\nmove 6 from 5 to 8\nmove 1 from 4 to 9\nmove 1 from 7 to 2\nmove 1 from 2 to 6\nmove 7 from 8 to 7\nmove 1 from 6 to 9\nmove 2 from 9 to 2\nmove 2 from 2 to 8\nmove 15 from 1 to 2\nmove 2 from 8 to 3\nmove 9 from 1 to 2\nmove 24 from 2 to 7\nmove 11 from 1 to 2\nmove 1 from 3 to 1\nmove 22 from 7 to 6\nmove 6 from 5 to 2\nmove 2 from 6 to 5\nmove 1 from 1 to 9\nmove 1 from 9 to 6\nmove 6 from 5 to 1\nmove 12 from 6 to 2\nmove 3 from 1 to 5\nmove 1 from 3 to 2\nmove 25 from 2 to 6\nmove 4 from 7 to 5\nmove 8 from 5 to 4\nmove 4 from 4 to 8\nmove 1 from 1 to 8\nmove 5 from 8 to 4\nmove 4 from 4 to 1\nmove 2 from 1 to 9\nmove 20 from 6 to 8\nmove 4 from 2 to 6\nmove 19 from 8 to 7\nmove 2 from 9 to 3\nmove 1 from 8 to 2\nmove 11 from 6 to 7\nmove 3 from 1 to 2\nmove 5 from 4 to 3\nmove 1 from 1 to 3\nmove 1 from 3 to 5\nmove 2 from 2 to 8\nmove 33 from 7 to 3\nmove 1 from 5 to 3\nmove 1 from 8 to 7\nmove 1 from 7 to 4\nmove 5 from 6 to 8\nmove 2 from 7 to 6\nmove 2 from 2 to 3\nmove 1 from 2 to 5\nmove 1 from 7 to 9\nmove 1 from 5 to 7\nmove 1 from 8 to 2\nmove 1 from 4 to 3\nmove 43 from 3 to 7\nmove 1 from 3 to 8\nmove 1 from 6 to 8\nmove 8 from 7 to 5\nmove 3 from 5 to 3\nmove 1 from 6 to 4\nmove 2 from 6 to 7\nmove 4 from 8 to 7\nmove 3 from 3 to 2\nmove 1 from 9 to 6\nmove 3 from 8 to 3\nmove 1 from 6 to 7\nmove 1 from 4 to 6\nmove 1 from 3 to 7\nmove 1 from 3 to 2\nmove 5 from 2 to 5\nmove 1 from 6 to 1\nmove 1 from 3 to 2\nmove 42 from 7 to 5\nmove 44 from 5 to 4\nmove 2 from 5 to 8\nmove 1 from 7 to 3\nmove 16 from 4 to 6\nmove 3 from 5 to 9")


  (answer-part-2 my-input)

  ;; PRFBFFSWN

  (sort-by first {1 "a" 2 "b"})

  (map (fn [[k v]] v) {1 "a" 2 "b"})

  (conj '("a") "b")

  (str/blank? "")
  (when (empty? "") :x)
  (empty? (str \space))

  (str \space)
  )