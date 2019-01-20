(ns yahtzee.prep-test
  (:require [yahtzee.prep :as sut]
            #?(:clj [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer [deftest testing is]])))

(deftest prep-test
  (is (= {:dice [{:value 1 :opacity "0.3"}
                 {:value 2 :opacity "0.3"}
                 {:value 3 :opacity "0.3"}
                 {:value 4 :opacity "0.3"}
                 {:value 5 :opacity "0.3"}]
          :button {:action [:roll]
                   :text "Roll dice"
                   :disabled? false}
          :scores [{:label "Aces"}
                   {:label "Twos"}
                   {:label "Threes"}
                   {:label "Fours"}
                   {:label "Fives"}
                   {:label "Sixes"}
                   {:label "Sum" :value "-"}
                   {:label "Bonus"}
                   {:label "Pair"}
                   {:label "Two pairs"}
                   {:label "Three alike"}
                   {:label "Four alike"}
                   {:label "House"}
                   {:label "Small straight"}
                   {:label "Large straight"}
                   {:label "Chance"}
                   {:label "Yahtzee"}
                   {:label "Sum" :value "-"}]}
         (sut/prep {:current-roll 0
                    :total-score 0
                    :actions [[:roll]]})))

  (is (= [{:value 1 :opacity "0.3"}
          {:value 2 :opacity "0.3"}
          {:value 3 :opacity "0.3"}
          {:value 4 :opacity "0.3"}
          {:value 5 :opacity "0.3"}]
         (:dice (sut/prep {:current-roll 0
                           :total-score 0
                           :actions [[:roll]]
                           :dice [1 2 3 4 5]}))))

  (is (= {:dice [{:value 1
                  :color nil
                  :action [:hold 0]}
                 {:value 2
                  :color nil
                  :action [:hold 1]}
                 {:value 3
                  :color "#c06"
                  :action [:release 2]}
                 {:value 4
                  :color nil
                  :action [:hold 3]}
                 {:value 5
                  :color "#c06"
                  :action [:release 4]}]
          :button {:action [:roll]
                   :text "Roll dice"
                   :disabled? false}
          :scores [{:label "Aces"}
                   {:label "Twos"}
                   {:label "Threes"}
                   {:label "Fours"}
                   {:label "Fives"}
                   {:label "Sixes"}
                   {:label "Sum" :value "-"}
                   {:label "Bonus"}
                   {:label "Pair"}
                   {:label "Two pairs"}
                   {:label "Three alike"}
                   {:label "Four alike"}
                   {:label "House"}
                   {:label "Small straight"}
                   {:label "Large straight"}
                   {:label "Chance"}
                   {:label "Yahtzee"}
                   {:label "Sum" :value "-"}]}
         (sut/prep {:current-roll 1
                    :total-score 0
                    :dice [1 2 3 4 5]
                    :held #{2 4}
                    :actions [[:roll]
                              [:hold 0]
                              [:hold 1]
                              [:release 2]
                              [:hold 3]
                              [:release 4]]})))

  (is (= [{:label "Aces"
           :action [:score :aces {:dice [1] :score 1}]
           :dice-suggestion [1]
           :score-suggestion 1}
          {:label "Twos"}
          {:label "Threes"}
          {:label "Fours"}
          {:label "Fives"}
          {:label "Sixes"}
          {:label "Sum" :value "-"}
          {:label "Bonus"}
          {:label "Pair"}
          {:label "Two pairs"}
          {:label "Three alike"}
          {:label "Four alike"}
          {:label "House"}
          {:label "Small straight"}
          {:label "Large straight"}
          {:label "Chance"}
          {:label "Yahtzee"}
          {:label "Sum" :value "-"}]
         (-> (sut/prep {:current-roll 1
                        :total-score 0
                        :dice [1 2 3 4 5]
                        :held #{2 4}
                        :actions [[:score :aces {:dice [1] :score 1}]]})
             :scores)))

  (is (= {:text "Play again!"
          :action [:new-game]
          :disabled? false}
         (-> (sut/prep {:current-roll 1
                        :total-score 0
                        :dice [1 2 3 4 5]
                        :held #{2 4}
                        :actions [[:new-game]]})
             :button))))
