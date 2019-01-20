(ns yahtzee.ui
  (:require [dumdom.core :as dumdom :refer [defcomponent]]
            [yahtzee.components.button :refer [Button]]
            [yahtzee.components.die :refer [die]]
            [yahtzee.game :as yahtzee]
            [yahtzee.prep :as prep]))

(defonce game (atom nil))

(defn trigger-action [action & args]
  (case action
    :roll (apply swap! game yahtzee/roll args)
    :hold (apply swap! game yahtzee/hold args)
    :release (apply swap! game yahtzee/release args)
    :score (apply swap! game yahtzee/score args)
    :new-game (reset! game (yahtzee/create-game))))

(defn action-fn [action]
  (when action
    (fn [e]
      (.preventDefault e)
      (apply trigger-action action))))

(defn score [{:keys [label value dice-suggestion score-suggestion action]}]
  (list [:div {:style {:background "#fff"}} [:p {:style {:margin "1vw 2vw"}} [:strong label]]]
        [:div {:style {:background "#fff"}} [:p {:style {:margin "1vw 6vw"}} value]]
        [:div {:style {:background "#fff"}}
         [:p {:style {:margin "1vw 6vw"
                      :opacity "0.5"}
              :onClick (action-fn action)}
          score-suggestion]]
        [:div {:style {:background "#fff"}}
         ]))

(defcomponent die-box [props]
  [:div {:onClick (action-fn (:action props))
         :style {:opacity (:opacity props)}}
   [die props]])

(defcomponent GameComponent [{:keys [dice button scores]}]
  [:div {:className "mod"}
   (when (seq dice)
     [:div {:className "mod"
            :style {:display "grid"
                    :grid-template-columns "18% 18% 18% 18% 18%"
                    :justify-content "space-between"}}
      (map die-box dice)])
   (when button
     [:div {:className "mod"}
      [Button (assoc button :on-click (action-fn (:action button)))]])
   [:div {:className "mod"
          :style {:display "grid"
                  :grid-gap "1px"
                  :background "#999"
                  :grid-template-columns "auto 4em 4em 1fr"}}
    (mapcat score scores)]])

(defcomponent Game [game]
  (GameComponent (prep/prep game)))

(defn render [game]
  (dumdom/render (Game game) (js/document.getElementById "app")))

(defn start! []
  (add-watch game :render #(render %4))
  (reset! game (yahtzee/create-game))
  nil)
