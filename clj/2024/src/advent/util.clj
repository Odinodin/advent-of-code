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