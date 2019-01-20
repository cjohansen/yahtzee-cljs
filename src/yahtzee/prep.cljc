(ns yahtzee.prep
  (:require [yahtzee.game :as yahtzee]))

(def score-sheet
  [{:label "Aces" :path [:scores :aces :score]}
   {:label "Twos" :path [:scores :twos :score]}
   {:label "Threes" :path [:scores :threes :score]}
   {:label "Fours" :path [:scores :fours :score]}
   {:label "Fives" :path [:scores :fives :score]}
   {:label "Sixes" :path [:scores :sixes :score]}
   {:label "Sum" :f yahtzee/upper-score}
   {:label "Bonus" :path [:upper-bonus]}
   {:label "Pair" :path [:scores :pair :score]}
   {:label "Two pairs" :path [:scores :two-pairs :score]}
   {:label "Three alike" :path [:scores :three-alike :score]}
   {:label "Four alike" :path [:scores :four-alike :score]}
   {:label "House" :path [:scores :house :score]}
   {:label "Small straight" :path [:scores :small-straight :score]}
   {:label "Large straight" :path [:scores :large-straight :score]}
   {:label "Chance" :path [:scores :chance :score]}
   {:label "Yahtzee" :path [:scores :yahtzee :score]}
   {:label "Sum" :path [:total-score]}])

(defn score-action-for [{:keys [actions]} category]
  (first (filter #(and (= :score (first %))
                       (= category (second %))) actions)))

(defn dash [v]
  (if (= 0 v) "-" v))

(defn prep-score [game {:keys [label path f]}]
  (let [v (if path
            (get-in game path)
            (f (:scores game)))
        action (score-action-for game (second path))]
    (cond-> {:label label}
      (not (nil? v)) (assoc :value (dash v))
      action (merge {:dice-suggestion (-> action (nth 2) :dice)
                     :score-suggestion (dash (-> action (nth 2) :score))
                     :action action}))))

(defn prep-die [game idx die]
  (if (< 0 (:current-roll game))
    (let [held? (contains? (:held game) idx)]
      {:value die
       :action (first (filter #(and (#{:hold :release} (first %)) (= idx (second %))) (:actions game)))
       :background (when held? "#f9f3f6")
       :color (when held? "#c06")})
    {:value die
     :opacity "0.3"}))

(defn prep [game]
  {:dice (map-indexed #(prep-die game %1 %2) (or (:dice game) [1 2 3 4 5]))
   :button (let [action (first (filter #(#{:new-game :roll} (first %)) (:actions game)))]
             {:action action
              :text (if (= :new-game (first action)) "Play again!" "Roll dice")
              :disabled? (nil? action)})
   :scores (map #(prep-score game %) score-sheet)})
