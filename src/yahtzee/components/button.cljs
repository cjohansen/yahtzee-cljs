(ns yahtzee.components.button
  (:require [dumdom.core :refer [defcomponent]]))

(defcomponent Button [{:keys [text on-click disabled?]}]
  [:div {:style {:display "inline-block"
                 :text-align "center"
                 :vertical-align "middle"
                 :line-height "50px"
                 :white-space "nowrap"
                 :cursor "pointer"
                 :border-radius "4px"
                 :width "100%"
                 :min-height "50px"
                 :text-decoration "none"
                 :background "#09f"
                 :font-weight "bold"
                 :color "#fff"
                 :opacity (if disabled? "0.3" "1")}
         :onClick on-click}
   text])
