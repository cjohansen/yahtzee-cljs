(ns yahtzee.cards.dice-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [yahtzee.components.die :as die]))

(defcard dice
  [:div {:style {:display "grid"
                 :grid-template-columns "1fr 1fr 1fr"}}
   [:div {:className "mod"} (die/die {:value 1})]
   [:div {:className "mod"} (die/die {:value 2})]
   [:div {:className "mod"} (die/die {:value 3})]
   [:div {:className "mod"} (die/die {:value 4})]
   [:div {:className "mod"} (die/die {:value 5})]
   [:div {:className "mod"} (die/die {:value 6})]])

(defcard colored-dice
  [:div {:style {:display "grid"
                 :grid-template-columns "1fr 1fr 1fr"}}
   [:div {:className "mod"} (die/die {:color "red" :value 1})]
   [:div {:className "mod"} (die/die {:color "red" :value 2})]
   [:div {:className "mod"} (die/die {:color "red" :value 3})]
   [:div {:className "mod"} (die/die {:color "red" :value 4})]
   [:div {:className "mod"} (die/die {:color "red" :value 5})]
   [:div {:className "mod"} (die/die {:color "red" :value 6})]])
