(ns advent.util
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]
            [medley.core :as m]))

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

(defn coord-in-dir [[x y] direction]
  (case direction
    \< [(dec x) y]
    \> [(inc x) y]
    \^ [x (dec y)]
    \v [x (inc y)]))

(defn indexed [items]
  (map-indexed vector items))

(defn xy-to-char [path]
  (into {}
        (for [[y line] (indexed (str/split-lines (slurp path)))
              [x c] (indexed line)]
          [[x y] c])))

(defn lines->xy-to-char [lines]
  (into {}
        (for [[y line] (indexed lines)
              [x c] (indexed line)]
          [[x y] c])))

(defn find-in-xy-to-char [xy-to-char what]
  (first (m/find-first (fn [[k v]] (= v what)) xy-to-char)))

(defn bounds-map [xy-to-char]
  (let [width (->> xy-to-char keys (map first) (apply max) inc) ;; inc because 0-based coordinates
        height (->> xy-to-char keys (map second) (apply max) inc)]
    [width height]))

(defn out-of-bounds? [[width height] [x y]]
  (or (< x 0)
      (< y 0)
      (>= x width)
      (>= y height)))

(defn reify-map
  ([xy-to-char]
   (reify-map (bounds-map xy-to-char) xy-to-char))
  ([bounds xy-to-char]
   (let [[width height] bounds]
     (->> (for [row (range height)
                col (range width)]
            (str (get xy-to-char [col row] ".")))
          (partition width)))))

(defn print-map! [mappy]
  (doseq [row mappy]
    (println (str/join "" row))))


(comment
  (let [width 11 height 7]
    (->> (for [row (range height)
               col (range width)]
           (str [col row])

           )
         (partition width)
         ))

  )
