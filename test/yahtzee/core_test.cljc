(ns yahtzee.core-test
  (:require [yahtzee.core :as sut]
            #?(:clj [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer [deftest testing is]])))

(deftest aces-test
  (is (= {:dice [] :score 0} (sut/aces [2 2 3 4 5])))
  (is (= {:dice [1] :score 1} (sut/aces [2 1 3 4 5])))
  (is (= {:dice [1 1] :score 2} (sut/aces [2 1 3 1 5])))
  (is (= {:dice [1 1 1 1] :score 4} (sut/aces [2 1 1 1 1]))))

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

(defn- upper [aces twos threes fours fives sixes]
  {:aces {:dice aces :score (reduce + 0 aces)}
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
  (is (= {:aces {:dice [1] :score 1}
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

  (is (= {:aces {:dice [] :score 0}
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

  (is (= {:aces {:dice [] :score 0}
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
         (sut/possible-scores [6 6 6 6 6] {:aces [1 1]
                                           :sixes [6 6 6]
                                           :three-alike [5 5 5]}))))

(deftest create-game-test
  (is (= {:current-roll 0
          :scores {}
          :actions [[:roll]]
          :total-score 0}
         (sut/create-game))))

(deftest roll-test
  (testing "Updates current roll"
    (is (= 1 (:current-roll (sut/roll {:current-roll 0})))))

  (testing "Randomizes dice"
    (is (< 75 (->> (range 100)
                   (map #(sut/roll {:current-roll 0}))
                   (map :dice)
                   set
                   count))))

  (testing "Can hold dice after rolling"
    (is (= [[:hold 0]
            [:hold 1]
            [:hold 2]
            [:hold 3]
            [:hold 4]]
           (->> (sut/roll {:current-roll 0
                           :actions [[:roll]]})
                :actions
                (filter #(= :hold (first %)))))))

  (testing "Can release held dice and hold released dice"
    (is (= [[:hold 0]
            [:release 1]
            [:hold 2]
            [:release 3]
            [:hold 4]]
           (->> (sut/roll {:current-roll 1
                           :dice [1 6 2 6 3]
                           :held #{1 3}
                           :actions [[:roll]]})
                :actions
                (filter #(#{:hold :release} (first %)))))))

  (testing "Does not randomize held dice"
    (is (= 1 (->> (range 100)
                  (map #(sut/roll {:current-roll 1
                                   :dice [1 1 6 6 6]
                                   :held #{2 3 4}}))
                  (map :dice)
                  (map sort)
                  (map #(drop 2 %))
                  set
                  count))))

  (testing "Randomizes dice not held"
    (is (= 6 (->> (range 100)
                  (map #(sut/roll {:current-roll 1
                                   :dice [1 6 6 6 6]
                                   :held #{1 2 3 4}}))
                  (map :dice)
                  (map sort)
                  (map #(take 1 %))
                  set
                  count))))

  (testing "Keeps held dice indices"
    (is (= #{[2 4]}
           (->> (range 100)
                (map #(let [dice (:dice (sut/roll {:dice [1 2 3 4 5]
                                                   :held #{1 3}}))]
                        [(nth dice 1) (nth dice 3)]))
                set))))

  (testing "Can roll again after two rolls"
    (is (= [[:roll]]
           (->> (sut/roll {:current-roll 1
                           :actions [[:roll]]})
                :actions
                (filter #(= :roll (first %)))))))

  (testing "Cannot roll more than thrice"
    (is (empty? (->> (sut/roll {:current-roll 2
                                :actions [[:roll]]})
                     :actions
                     (filter #(= :roll (first %)))))))

  (testing "Cannot hold or release dice after three rolls"
    (is (empty? (->> (sut/roll {:current-roll 2
                                :actions [[:roll]]})
                     :actions
                     (filter #(#{:hold :release} (first %)))))))

  (testing "Can place score after three rolls"
    (is (= 15
           (->> (sut/roll {:current-roll 2
                           :actions [[:roll]]})
                :actions
                (filter #(#{:score} (first %)))
                count))))

  (testing "Can place score after first roll"
    (is (= 15
           (->> (sut/roll {:current-roll 0
                           :actions [[:roll]]})
                :actions
                (filter #(#{:score} (first %)))
                count))))

  (testing "Can only place unused scores"
    (is (= 13
           (->> (sut/roll {:current-roll 2
                           :actions [[:roll]]
                           :scores {:aces {:dice [1 1] :score 2}
                                    :twos {:dice [2 2 2 2] :score 8}}})
                :actions
                (filter #(#{:score} (first %)))
                count)))))

(deftest hold-test
  (testing "Holds die"
    (is (= {:dice [1 2 3 4 5]
            :held #{1}}
           (-> (sut/hold {:dice [1 2 3 4 5]} 1)
               (dissoc :actions)))))

  (testing "Allows held die to be released"
    (is (= [[:hold 0]
            [:release 1]
            [:hold 2]
            [:hold 3]
            [:hold 4]]
           (->> (sut/hold {:dice [1 2 3 4 5]} 1)
                :actions
                (filter #(#{:hold :release} (first %)))))))

  (testing "Holds another die"
    (is (= {:dice [1 2 3 4 5]
            :held #{1 4}}
           (-> (sut/hold {:dice [1 2 3 4 5]
                          :held #{1}} 4)
               (dissoc :actions))))))

(deftest release-test
  (testing "Releases die"
    (is (= {:dice [1 2 3 4 5]
            :held #{}}
           (-> (sut/release {:dice [1 2 3 4 5]
                             :held #{1}} 1)
               (dissoc :actions)))))

  (testing "Allows to re-hold die"
    (is (= [[:hold 0]
            [:hold 1]
            [:hold 2]
            [:hold 3]
            [:release 4]]
           (->> (sut/release {:dice [1 2 3 4 5]
                              :held #{1 4}} 1)
                :actions
                (filter #(#{:hold :release} (first %)))))))

  (testing "Releases one of multiple held dice"
    (is (= {:dice [1 2 3 4 5]
            :held #{1 4}}
           (-> (sut/release {:dice [1 2 3 4 5]
                             :held #{1 4 2}} 2)
               (dissoc :actions))))))

(deftest score-test
  (testing "Scores a category"
    (is (= {:current-roll 0
            :scores {:aces {:dice [1 1] :score 2}}
            :total-score 2}
           (-> (sut/score {} :aces {:dice [1 1] :score 2})
               (dissoc :actions)))))

  (testing "Updates total score"
    (is (= {:current-roll 0
            :scores {:aces {:dice [1 1] :score 2}
                     :twos {:dice [2 2 2 2] :score 8}}
            :total-score 10}
           (-> (sut/score {:scores {:aces {:dice [1 1] :score 2}}
                           :total-score 2}
                          :twos {:dice [2 2 2 2] :score 8})
               (dissoc :actions)))))

  (testing "Sets bonus if upper section is complete"
    (is (= 0
           (-> (sut/score {:scores {:aces {:dice [1 1] :score 2}
                                    :twos {:dice [2 2] :score 4}
                                    :threes {:dice [3 3] :score 6}
                                    :fours {:dice [4 4] :score 8}
                                    :fives {:dice [5 5] :score 10}}
                           :total-score 30}
                          :sixes {:dice [6 6] :score 12})
               :upper-bonus))))

  (testing "Awards bonus if upper section is complete and qualifies"
    (is (= 50
           (-> (sut/score {:scores {:aces {:dice [1 1 1] :score 3}
                                    :twos {:dice [2 2 2] :score 6}
                                    :threes {:dice [3 3 3] :score 9}
                                    :fours {:dice [4 4 4] :score 12}
                                    :fives {:dice [5 5 5] :score 15}}
                           :total-score 45}
                          :sixes {:dice [6 6 6] :score 18})
               :upper-bonus))))

  (testing "Can start rolling again after scoring"
    (is (< 0
           (->> (sut/score {} :aces {:dice [1 1] :score 2})
                :actions
                count))))

  (testing "Clears hold status after scoring"
    (is (nil? (:held (sut/score {:held #{1 2}} :aces {:dice [1 1] :score 2})))))

  (testing "Resets roll counter after scoring"
    (is (= 0
           (->> (sut/score {} :aces {:dice [1 1] :score 2})
                :current-roll))))

  (testing "Can start new game when game is over"
    (is (= [[:new-game]]
           (-> (sut/score {:scores {:aces {:dice [1] :score 1}
                                    :twos {:dice [2] :score 2}
                                    :threes {:dice [3] :score 3}
                                    :fours {:dice [4] :score 4}
                                    :fives {:dice [5] :score 5}
                                    :sixes {:dice [6] :score 6}
                                    :pair {:dice [2 2] :score 4}
                                    :two-pairs {:dice [2 2 5 5] :score 14}
                                    :three-alike {:dice [4 4 4] :score 12}
                                    :four-alike {:dice [5 5 5 5] :score 20}
                                    :house {:dice [] :score 0}
                                    :small-straight {:dice [] :score 0}
                                    :large-straight {:dice [] :score 0}
                                    :chance {:dice [2 3 2 6 5] :score 18}}}
                          :yahtzee {:dice [6 6 6 6 6] :score 50})
               :actions)))))
