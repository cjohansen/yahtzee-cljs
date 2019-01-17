(ns yahtzee.core-test
  (:require [yahtzee.core :as sut]
            #?(:clj [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer [deftest testing is]])))

(deftest ones-test
  (is (= {:dice [] :score 0} (sut/ones [2 2 3 4 5])))
  (is (= {:dice [1] :score 1} (sut/ones [2 1 3 4 5])))
  (is (= {:dice [1 1] :score 2} (sut/ones [2 1 3 1 5])))
  (is (= {:dice [1 1 1 1] :score 4} (sut/ones [2 1 1 1 1]))))

(deftest pair-test
  (is (= {:dice [] :score 0} (sut/pair [1 2 3 4 5])))
  (is (= {:dice [2 2] :score 4} (sut/pair [1 2 3 2 5])))
  (is (= {:dice [5 5] :score 10} (sut/pair [5 2 3 2 5]))))

(deftest two-pairs-test
  (is (= {:dice [] :score 0} (sut/two-pairs [1 2 3 4 5])))
  (is (= {:dice [] :score 0} (sut/two-pairs [1 2 3 2 5])))
  (is (= {:dice [2 2 5 5] :score 14} (sut/two-pairs [5 2 3 2 5]))))

(deftest three-alike-test
  (is (= {:dice [] :score 0} (sut/three-alike [1 2 3 4 5])))
  (is (= {:dice [] :score 0} (sut/three-alike [1 2 3 2 5])))
  (is (= {:dice [2 2 2] :score 6} (sut/three-alike [1 2 3 2 2])))
  (is (= {:dice [5 5 5] :score 15} (sut/three-alike [5 5 5 5 5]))))

(deftest four-alike-test
  (is (= {:dice [] :score 0} (sut/four-alike [1 2 3 4 5])))
  (is (= {:dice [] :score 0} (sut/four-alike [1 2 3 2 5])))
  (is (= {:dice [] :score 0} (sut/four-alike [1 2 3 2 2])))
  (is (= {:dice [2 2 2 2] :score 8} (sut/four-alike [2 2 3 2 2])))
  (is (= {:dice [5 5 5 5] :score 20} (sut/four-alike [5 5 5 5 5]))))

(deftest yahtzee-test
  (is (= {:dice [] :score 0} (sut/yahtzee [1 2 3 4 5])))
  (is (= {:dice [] :score 0} (sut/yahtzee [1 2 3 2 5])))
  (is (= {:dice [] :score 0} (sut/yahtzee [1 2 3 2 2])))
  (is (= {:dice [] :score 0} (sut/yahtzee [2 2 3 2 2])))
  (is (= {:dice [5 5 5 5 5] :score 50} (sut/yahtzee [5 5 5 5 5])))
  (is (= {:dice [3 3 3 3 3] :score 50} (sut/yahtzee [3 3 3 3 3]))))

(deftest house-test
  (is (= {:dice [] :score 0} (sut/house [1 2 3 4 5])))
  (is (= {:dice [] :score 0} (sut/house [1 2 3 2 5])))
  (is (= {:dice [] :score 0} (sut/house [1 2 3 2 2])))
  (is (= {:dice [] :score 0} (sut/house [2 2 3 2 2])))
  (is (= {:dice [] :score 0} (sut/house [2 2 2 2 2])))
  (is (= {:dice [2 2 5 5 5] :score 19} (sut/house [2 5 5 2 5]))))

(deftest small-straight-test
  (is (= {:dice [1 2 3 4 5] :score 15} (sut/small-straight [1 2 3 4 5])))
  (is (= {:dice [1 2 3 4 5] :score 15} (sut/small-straight [3 2 1 5 4])))
  (is (= {:dice [] :score 0} (sut/small-straight [1 2 3 2 2])))
  (is (= {:dice [] :score 0} (sut/small-straight [2 3 4 5 6])))
  (is (= {:dice [] :score 0} (sut/small-straight [5 5 5 5 5]))))

(deftest large-straight-test
  (is (= {:dice [2 3 4 5 6] :score 20} (sut/large-straight [2 3 4 5 6])))
  (is (= {:dice [2 3 4 5 6] :score 20} (sut/large-straight [3 2 6 5 4])))
  (is (= {:dice [] :score 0} (sut/large-straight [1 2 3 2 2])))
  (is (= {:dice [] :score 0} (sut/large-straight [2 3 4 5 1])))
  (is (= {:dice [] :score 0} (sut/large-straight [5 5 5 5 5]))))

(deftest chance-test
  (is (= {:dice [2 3 4 5 6] :score 20} (sut/chance [6 5 2 3 4])))
  (is (= {:dice [5 5 6 6 6] :score 28} (sut/chance [6 5 6 5 6]))))

(defn- upper [ones twos threes fours fives sixes]
  {:ones {:dice ones :score (reduce + 0 ones)}
   :twos {:dice twos :score (reduce + 0 twos)}
   :threes {:dice threes :score (reduce + 0 threes)}
   :fours {:dice fours :score (reduce + 0 fours)}
   :fives {:dice fives :score (reduce + 0 fives)}
   :sixes {:dice sixes :score (reduce + 0 sixes)}})

(deftest upper-bonus-open-test
  (is (= 0 (sut/upper-bonus-open (upper [1 1] [2 2] [] [] [] []))))
  (is (= 50 (sut/upper-bonus-open (upper [1 1 1] [2 2 2] [3 3 3] [4 4 4] [5 5 5] [6 6 6]))))
  (is (= 0 (sut/upper-bonus-open (upper [1 1 1] [2 2 2] [3 3 3] [4 4 4] [5 5 5] [6 6]))))
  (is (= 50 (sut/upper-bonus-open (upper [1 1 1] [2 2 2] [3 3 3 3 3] [4 4 4] [5 5 5] [6 6]))))
  (is (= 0 (sut/upper-bonus-open (upper [1 1] [2 2 2] [3 3 3] [4 4 4] [5 5 5] [6 6 6]))))
  (is (= 50 (sut/upper-bonus-open (upper [1 1] [2 2 2 2] [3 3 3] [4 4 4] [5 5 5] [6 6 6])))))

(deftest upper-bonus-forced-test
  (is (= 0 (sut/upper-bonus-forced (upper [1 1] [2 2] [] [] [] []))))
  (is (= 50 (sut/upper-bonus-forced (upper [1 1] [2 2] [3 3] [4 4] [5 5] [6 6]))))
  (is (= 0 (sut/upper-bonus-forced (upper [1 1] [2 2] [3 3] [4 4] [5 5] [6]))))
  (is (= 50 (sut/upper-bonus-forced (upper [1 1] [2 2] [3 3 3 3] [4 4] [5 5] [6]))))
  (is (= 0 (sut/upper-bonus-forced (upper [1] [2 2] [3 3] [4 4] [5 5] [6 6]))))
  (is (= 50 (sut/upper-bonus-forced (upper [1] [2 2 2] [3 3] [4 4] [5 5] [6 6])))))

(deftest possible-scores
  (is (= {:ones {:dice [1] :score 1}
          :twos {:dice [2] :score 2}
          :threes {:dice [3] :score 3}
          :fours {:dice [4] :score 4}
          :fives {:dice [5] :score 5}
          :sixes {:dice [] :score 0}
          :pair {:dice [] :score 0}
          :two-pairs {:dice [] :score 0}
          :three-alike {:dice [] :score 0}
          :four-alike {:dice [] :score 0}
          :house {:dice [] :score 0}
          :small-straight {:dice [1 2 3 4 5] :score 15}
          :large-straight {:dice [] :score 0}
          :chance {:dice [1 2 3 4 5] :score 15}
          :yahtzee {:dice [] :score 0}}
         (sut/possible-scores [1 2 3 4 5])))

  (is (= {:ones {:dice [] :score 0}
          :twos {:dice [2 2] :score 4}
          :threes {:dice [] :score 0}
          :fours {:dice [4 4 4] :score 12}
          :fives {:dice [] :score 0}
          :sixes {:dice [] :score 0}
          :pair {:dice [4 4] :score 8}
          :two-pairs {:dice [2 2 4 4] :score 12}
          :three-alike {:dice [4 4 4] :score 12}
          :four-alike {:dice [] :score 0}
          :house {:dice [2 2 4 4 4] :score 16}
          :small-straight {:dice [] :score 0}
          :large-straight {:dice [] :score 0}
          :chance {:dice [2 2 4 4 4] :score 16}
          :yahtzee {:dice [] :score 0}}
         (sut/possible-scores [2 2 4 4 4])))

  (is (= {:ones {:dice [] :score 0}
          :twos {:dice [] :score 0}
          :threes {:dice [] :score 0}
          :fours {:dice [] :score 0}
          :fives {:dice [] :score 0}
          :sixes {:dice [6 6 6 6 6] :score 30}
          :pair {:dice [6 6] :score 12}
          :two-pairs {:dice [] :score 0}
          :three-alike {:dice [6 6 6] :score 18}
          :four-alike {:dice [6 6 6 6] :score 24}
          :house {:dice [] :score 0}
          :small-straight {:dice [] :score 0}
          :large-straight {:dice [] :score 0}
          :chance {:dice [6 6 6 6 6] :score 30}
          :yahtzee {:dice [6 6 6 6 6] :score 50}}
         (sut/possible-scores [6 6 6 6 6])))

  (is (= {:twos {:dice [] :score 0}
          :threes {:dice [] :score 0}
          :fours {:dice [] :score 0}
          :fives {:dice [] :score 0}
          :pair {:dice [6 6] :score 12}
          :two-pairs {:dice [] :score 0}
          :four-alike {:dice [6 6 6 6] :score 24}
          :house {:dice [] :score 0}
          :small-straight {:dice [] :score 0}
          :large-straight {:dice [] :score 0}
          :chance {:dice [6 6 6 6 6] :score 30}
          :yahtzee {:dice [6 6 6 6 6] :score 50}}
         (sut/possible-scores [6 6 6 6 6] {:ones [1 1]
                                           :sixes [6 6 6]
                                           :three-alike [5 5 5]}))))
