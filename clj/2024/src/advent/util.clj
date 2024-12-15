(ns advent.util
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn read-file-into-list-of-lists [filepath]
  (with-open [rdr (io/reader filepath)]
    (doall
      (map
        #(str/split % #"\s+")
        (line-seq rdr)))))

(defn read-file-into-list [filepath]
  (with-open [rdr (io/reader filepath)]
    (doall
      (line-seq rdr))))



(defn indexed [items]
  (map-indexed vector items))

(defn xy-to-char [path]
  (into {}
        (for [[y line] (indexed (str/split-lines (slurp path)))
              [x c] (indexed line)]
          [[x y] c])))

(defn bounds-map [xy-to-char]
  (let [width (->> xy-to-char keys (map first) (apply max) inc) ;; inc because 0-based coordinates
        height (->> xy-to-char keys (map second) (apply max) inc)]
    [width height]))

(defn out-of-bounds? [[width height] [x y]]
  (or (< x 0)
      (< y 0)
      (>= x width)
      (>= y height)))

(defn reify-map [xy-to-char]
  (let [[width height] (bounds-map xy-to-char)]
    (->> (for [col (range width)
               row (range height)]
           (str (get xy-to-char [row col])))
         (partition width))))

(defn print-map! [mappy]
  (doseq [row mappy]
    (println (str/join "" row))))

