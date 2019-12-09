(ns advent2019.day-09
  (:require [advent2019.lib.intcode :refer [run-program
                                            load-program!
                                            gather-outputs
                                            resume-program-with-input]]))

(defn solve! [file]
  (let [program (load-program! file)
        continuation (run-program program)
        test-mode (resume-program-with-input continuation 1)]
    (println "BOOST test mode outputs:" (:outputs (gather-outputs test-mode)))))
