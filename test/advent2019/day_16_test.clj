(ns advent2019.day-16-test
  (:require [clojure.test :refer [deftest is testing]]
            [advent2019.day-16 :refer [phases
                                       parse
                                       decode]]))

(deftest fake-fourier-transform
  (testing "phases"
    (is (= (phases [1 2 3 4 5 6 7 8] 1) [4 8 2 2 6 1 5 8]))
    (is (= (phases [1 2 3 4 5 6 7 8] 2) [3 4 0 4 0 4 3 8]))
    (is (= (phases [1 2 3 4 5 6 7 8] 3) [0 3 4 1 5 5 1 8]))
    (is (= (phases [1 2 3 4 5 6 7 8] 4) [0 1 0 2 9 4 9 8]))

    (is (= (take 8 (phases (parse "80871224585914546619083218645595") 100))
           [2 4 1 7 6 1 7 6]))
    (is (= (take 8 (phases (parse "19617804207202209144916044189917") 100))
           [7 3 7 4 5 4 1 8]))
    (is (= (take 8 (phases (parse "69317163492948606335995924319873") 100))
           [5 2 4 3 2 1 3 3])))
  (testing "decoding"
    (is (= (decode (parse "03036732577212944063491565474664"))
           [8 4 4 6 2 0 2 6]))
    (is (= (decode (parse "02935109699940807407585447034323"))
           [7 8 7 2 5 2 7 0]))
    (is (= (decode (parse "03081770884921959731165446850517"))
           [5 3 5 5 3 7 3 1]))))
