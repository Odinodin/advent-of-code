(ns advent.day-05
  (:require [advent.util :as util]
            [medley.core :as m]
            [clojure.set :refer [difference union intersection]]
            [clojure.string :as str]))

(def input-path "resources/day05/")

(defn is-valid-print-job? [pages-to-print page->before-other-pages]
  (let [page->idx (->> (map-indexed (fn [idx item] [item idx]) pages-to-print)
                       (into {}))]
    (every? (fn [page]
              (let [idx-of-current-page (page->idx page)
                    page-must-come-before-pages (page->before-other-pages page)]
                (every? (fn [before-p]
                          (< idx-of-current-page (or (page->idx before-p) Integer/MAX_VALUE))) page-must-come-before-pages))) pages-to-print)))

(defn middle-item
  [lst]
  (let [n (count lst)
        mid (quot n 2)]
    (when (pos? n)
      (nth lst mid))))

(defn star-1 [path]
  (let [all-lines (util/read-file-into-list path)

        split (split-with (fn [x] (not= x "")) all-lines)
        rules (->> (first split)
                   (map (fn [p-pipe-before-p] (str/split p-pipe-before-p #"\|")))
                   (map (fn [p-p]
                          (map #(Integer/parseInt %) p-p))))
        print-jobs (->> (rest (second split))
                        (map (fn [item] (str/split item #"\,")))
                        (map (fn [items] (map #(Integer/parseInt %) items))))

        page-to-before-other-pages (->> (group-by first rules) (m/map-vals (fn [items] (map second items))))]

    (->> (filter (fn [pages-to-print] (is-valid-print-job? pages-to-print page-to-before-other-pages)) print-jobs)
         (map middle-item)
         (reduce +)
         )))


(defn without
  "Returns set s with x removed."
  [s x] (difference s #{x}))

(defn take-1
  "Returns the pair [element, s'] where s' is set s with element removed."
  [s] {:pre [(not (empty? s))]}
  (let [item (first s)]
    [item (without s item)]))

(defn no-incoming
  "Returns the set of nodes in graph g for which there are no incoming
  edges, where g is a map of nodes to sets of nodes."
  [g]
  (let [nodes (set (keys g))
        have-incoming (apply union (vals g))]
    (difference nodes have-incoming)))

(defn normalize
  "Returns g with empty outgoing edges added for nodes with incoming
  edges only.  Example: {:a #{:b}} => {:a #{:b}, :b #{}}"
  [g]
  (let [have-incoming (apply union (vals g))]
    (reduce #(if (get % %2) % (assoc % %2 #{})) g have-incoming)))

(defn kahn-sort
  "Proposes a topological sort for directed graph g using Kahn's
   algorithm, where g is a map of nodes to sets of nodes. If g is
   cyclic, returns nil."
  ([g]
   (kahn-sort (normalize g) [] (no-incoming g)))
  ([g l s]
   (if (empty? s)
     (when (every? empty? (vals g)) l)
     (let [[n s'] (take-1 s)
           m (g n)
           g' (reduce #(update-in % [n] without %2) g m)]
       (recur g' (conj l n) (union s' (intersection (no-incoming g') m)))))))


(defn star-2 [path]
  (let [all-lines (util/read-file-into-list path)

        split (split-with (fn [x] (not= x "")) all-lines)
        rules (->> (first split)
                   (map (fn [p-pipe-before-p] (str/split p-pipe-before-p #"\|")))
                   (map (fn [p-p]
                          (map #(Integer/parseInt %) p-p))))
        print-jobs (->> (rest (second split))
                        (map (fn [item] (str/split item #"\,")))
                        (map (fn [items] (map #(Integer/parseInt %) items))))

        page-to-before-other-pages (->> (group-by first rules) (m/map-vals (fn [items] (->> (map second items) (into #{})))))
        incorrectly-ordered-pages (->> (remove (fn [pages-to-print] (is-valid-print-job? pages-to-print page-to-before-other-pages)) print-jobs))]
    (->> incorrectly-ordered-pages
         (map (fn [pages]
                (let [pages-set (set pages)
                      filtered-graph (->> page-to-before-other-pages
                                          (m/filter-keys (fn [k] (contains? pages-set k)))
                                          (m/map-vals (fn [before-pages] (->> before-pages
                                                                              (filter #(contains? pages-set %))
                                                                              (into #{}))))
                                          )]
                  [pages filtered-graph]
                  (kahn-sort filtered-graph))))
         (map middle-item)
         (reduce +))))

(comment

  (def numbers [1 2 3 4 5])
  (def rules [[5 1] [2 4] [5 3]])
  (kahn-sort numbers rules)

  (let [path (str input-path "test-input.txt")
        all-lines (util/read-file-into-list path)

        split (split-with (fn [x] (not= x "")) all-lines)
        rules (->> (first split)
                   (map (fn [p-pipe-before-p] (str/split p-pipe-before-p #"\|")))
                   (map (fn [p-p]
                          (map #(Integer/parseInt %) p-p))))
        print-jobs (->> (rest (second split))
                        (map (fn [item] (str/split item #"\,")))
                        (map (fn [items] (map #(Integer/parseInt %) items))))

        page-to-before-other-pages (->> (group-by first rules) (m/map-vals (fn [items] (->> (map second items) (into #{})))))
        incorrectly-ordered-pages (->> (remove (fn [pages-to-print] (is-valid-print-job? pages-to-print page-to-before-other-pages)) print-jobs))]
    (->> incorrectly-ordered-pages
         (map (fn [pages]
                (let [pages-set (set pages)
                      filtered-graph (->> page-to-before-other-pages
                                          (m/filter-keys (fn [k] (contains? pages-set k)))
                                          (m/map-vals (fn [before-pages] (->> before-pages
                                                                              (filter #(contains? pages-set %))
                                                                              (into #{}))))
                                          )]
                  [pages filtered-graph]
                  (kahn-sort filtered-graph))))
         (map middle-item)
         (reduce +)))

  (star-1 (str input-path "test-input.txt"))
  (star-1 (str input-path "input.txt"))

  (star-2 (str input-path "test-input.txt"))
  (star-2 (str input-path "input.txt"))
  )