(ns yahtzee.components.die
  (:require [dumdom.core :refer [defcomponent]]))

(defn die-styles [color x y]
  {:background color
   :border-radius "50%"
   :width "22%"
   :padding-bottom "22%"
   :position "absolute"
   :left (str x "%")
   :top (str y "%")
   :transform "translate(-50%, -50%)"})

(def x 22)
(def y 22)

(defcomponent die [{:keys [color background value]}]
  (let [color (or color "#000")]
    [:div {:style {:border (str "0.8vw solid " color)
                   :border-radius "10%"
                   :background (or background "#fff")}}
     [:div {:style {:position "relative"
                    :padding-bottom "100%"}}
      (when (#{1 3 5} value)
        [:div {:style (die-styles color 50 50)}])
      (when (#{2 3 4 5 6} value)
        [:div {:style (die-styles color x y)}])
      (when (#{2 3 4 5 6} value)
        [:div {:style (die-styles color (- 100 x) (- 100 y))}])
      (when (#{4 5 6} value)
        [:div {:style (die-styles color (- 100 x) y)}])
      (when (#{4 5 6} value)
        [:div {:style (die-styles color x (- 100 y))}])
      (when (= 6 value)
        [:div {:style (die-styles color x 50)}])
      (when (= 6 value)
        [:div {:style (die-styles color (- 100 x) 50)}])]]))
