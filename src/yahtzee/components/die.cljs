(ns yahtzee.components.die
  (:require [dumdom.core :refer [defcomponent]]))

(defcomponent die [value]
  [:div {} "Value: " value])
