(ns yahtzee.core
  (:require [clojure.set :as set]))

(defn- dice-result [dice]
  {:dice (or dice []) :score (reduce + 0 dice)})

(defn aces [dice]
  (dice-result (filter #{1} dice)))

(defn twos [dice]
  (dice-result (filter #{2} dice)))

(defn threes [dice]
  (dice-result (filter #{3} dice)))

(defn fours [dice]
  (dice-result (filter #{4} dice)))

(defn fives [dice]
  (dice-result (filter #{5} dice)))

(defn sixes [dice]
  (dice-result (filter #{6} dice)))

(defn pairs [dice]
  (->> (frequencies dice)
       (filter #(<= 2 (second %)))
       (map first)))

(defn pair [dice]
  (dice-result
   (when-let [best-pair (apply max (pairs dice))]
     (repeat 2 best-pair))))

(defn two-pairs [dice]
  (let [pairs (pairs dice)]
    (dice-result
     (when (= 2 (count pairs))
       (sort (concat pairs pairs))))))

(defn- alike [n dice]
  (when-let [alike (->> (frequencies dice)
                        (filter #(<= n (second %)))
                        (ffirst))]
    (repeat n alike)))

(defn three-alike [dice]
  (dice-result (alike 3 dice)))

(defn four-alike [dice]
  (dice-result (alike 4 dice)))

(defn yahtzee [dice]
  (let [result (alike 5 dice)]
    (if (= (count dice) (count result))
      {:dice result :score 50}
      {:dice [] :score 0})))

(defn house [dice]
  (dice-result
   (when (= #{2 3} (set (vals (frequencies dice))))
     (sort dice))))

(defn small-straight [dice]
  (dice-result
   (when (= #{1 2 3 4 5} (set dice))
     (sort dice))))

(defn large-straight [dice]
  (dice-result
   (when (= #{2 3 4 5 6} (set dice))
     (sort dice))))

(defn chance [dice]
  (dice-result (sort dice)))

(def upper-section #{:aces :twos :threes :fours :fives :sixes})

(defn upper-score [scores]
  (->> upper-section
       (select-keys scores)
       vals
       (map :score)
       (reduce + 0)))

(def open-bonus-requirement (+ 3 6 9 12 15 18))
(def forced-bonus-requirement (+ 2 4 6 8 10 12))

(defn upper-bonus [requirement scores]
  (if (<= requirement (upper-score scores))
    50
    0))

(def upper-bonus-open (partial upper-bonus open-bonus-requirement))
(def upper-bonus-forced (partial upper-bonus forced-bonus-requirement))

(def score-sheet
  {:aces aces
   :twos twos
   :threes threes
   :fours fours
   :fives fives
   :sixes sixes
   :pair pair
   :two-pairs two-pairs
   :three-alike three-alike
   :four-alike four-alike
   :house house
   :small-straight small-straight
   :large-straight large-straight
   :chance chance
   :yahtzee yahtzee})

(defn possible-scores [dice & [scores]]
  (->> (keys scores)
       (apply dissoc score-sheet)
       (map (fn [[score f]]
              [score (f dice)]))
       (into {})))

(defn roll-dice [n]
  (map #(inc (rand-int 6)) (range n)))

(defn create-game []
  {:current-roll 0
   :scores {}
   :actions [[:roll]]
   :total-score 0})

(defn available-actions [{:keys [current-roll dice scores held]}]
  (if (= (count score-sheet) (count scores))
    [[:new-game]]
    (cond-> []
      (< current-roll 3) (conj [:roll])
      (< current-roll 3) (concat (map-indexed #(if (contains? held %1)
                                                 [:release %1]
                                                 [:hold %1]) dice))
      (< 0 current-roll) (concat (map (fn [[category result]] [:score category result])
                                      (possible-scores dice scores))))))

(defn update-actions [game]
  (assoc game :actions (available-actions game)))

(defn roll [game]
  (-> game
      (update :current-roll inc)
      (assoc :dice (let [new-dice (roll-dice 5)]
                     (map-indexed #(if (contains? (:held game) %1)
                                     (nth (:dice game) %1)
                                     (nth new-dice %1)) (range 5))))
      update-actions))

(defn hold [game die-idx]
  (-> game
      (update :held (comp set conj) die-idx)
      update-actions))

(defn release [game die-idx]
  (-> game
      (update :held set/difference #{die-idx})
      update-actions))

(defn update-upper-bonus [game]
  (if (and (nil? (:upper-bonus game))
           (= #{} (set/difference upper-section (set (keys (:scores game))))))
    (assoc game :upper-bonus (upper-bonus-open (:scores game)))
    game))

(defn score [game category result]
  (-> game
      (assoc-in [:scores category] result)
      (dissoc :held)
      (assoc :current-roll 0)
      (update :total-score + (:score result))
      update-actions
      update-upper-bonus))
