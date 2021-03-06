(ns advent2019.day-03
  (:require [clojure.math.numeric-tower :refer [abs]]
            [clojure.string :as str]))

(defn parse [input]
  (map #(map (fn [[direction & length]]
               {:direction (case direction \L :left \R :right \U :up \D :down)
                :length (Integer/parseInt (apply str length))}) %)
       (map #(str/split % #",") (str/split-lines input))))

(defn move [point path]
  (let [[x y] point
        {:keys [direction length]} path]
    (case direction
      :up [x (+ y length)]
      :down [x (- y length)]
      :right [(+ x length) y]
      :left [(- x length) y])))

(defn- acc-path-to-lines [{lines :lines [x y] :position steps :steps}
                         path]
  (let [{:keys [direction length]} path
        [x' y'] (move [x y] path)]
    {:position [x' y']
     :steps (+ steps length)
     :lines (case direction
              :up (cons {:vertical true :x x
                         :bottom y :top y'
                         :direction :up :steps steps} lines)
              :down (cons {:vertical true :x x
                           :bottom y' :top y
                           :direction :down :steps steps} lines)
              :right (cons {:horizontal true :y y
                            :left x :right x'
                            :direction :right :steps steps} lines)
              :left (cons {:horizontal true :y y
                           :left x' :right x
                           :direction :left :steps steps} lines))}))

(defn paths-to-lines [paths]
  (:lines (reduce acc-path-to-lines {:lines () :position [0 0] :steps 0} paths)))

(defn between [low x high] (or (and (< low x) (< x high))
                               (and (< high x) (< x low))))

(defn distance [a b] (abs (- a b)))

(defn steps-to-intersection-of-line-from-position [line point]
  (let [{:keys [direction steps]} line
        [x y] point]
    (case direction
      :up (+ steps (distance y (:bottom line)) (distance x (:x line)))
      :down (+ steps (distance y (:top line)) (distance x (:x line)))
      :right (+ steps (distance x (:left line)) (distance y (:y line)))
      :left (+ steps (distance x (:right line)) (distance y (:y line))))))

(defn line-intersects-path-from-position? [line path point]
  (let [[x y] point
        {:keys [direction]} path
        [x' y'] (move point path)]
    (cond
      (or (= direction :up)
          (= direction :down)) (and (:horizontal line)
                                    (between (:left line) x (:right line))
                                    (between y (:y line) y'))
      (or (= direction :right)
          (= direction :left)) (and (:vertical line)
                                    (between (:bottom line) y (:top line))
                                    (between x (:x line) x')))))

(defn line-intersection-with-path-from-position [line path point path-steps]
  (let [{:keys [direction]} path
        [x y] point]
    (cond
      (or (= direction :up)
          (= direction :down)) {:x x :y (:y line)
                                :steps (+ path-steps (steps-to-intersection-of-line-from-position line [x y]))}
      (or (= direction :right)
          (= direction :left)) {:x (:x line) :y y
                                :steps (+ path-steps (steps-to-intersection-of-line-from-position line [x y]))})))

(defn- acc-intersections-of-path [{:keys [lines position steps intersections]} path]
  {:lines lines
   :position (move position path)
   :steps (+ steps (:length path))
   :intersections (concat intersections
                          (->> lines
                               (filter #(line-intersects-path-from-position? % path position))
                               (map #(line-intersection-with-path-from-position % path position steps))))})

(defn intersections-of-lines-and-paths [lines paths]
  (:intersections (reduce acc-intersections-of-path {:lines lines :position [0 0] :steps 0 :intersections ()} paths)))

(defn manhattan [{:keys [x y]}] (+ (abs x) (abs y)))

(defn min-intersection [f wire-1 wire-2]
  (let [wire-1-lines (paths-to-lines wire-1)
        intersections (intersections-of-lines-and-paths wire-1-lines wire-2)]
    (apply min-key f intersections)))

(defn load-file! [file] (parse (slurp file)))

(defn solve! [file]
  (let [[wire-1 wire-2] (load-file! file)
        closest (min-intersection manhattan wire-1 wire-2)
        fewest-steps (min-intersection :steps wire-1 wire-2)]
    (println "Closest intersection:" closest "Distance:" (manhattan closest))
    (println "Fewest step intersection:" fewest-steps "Steps:" (:steps fewest-steps))))
