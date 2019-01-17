(ns yahtzee.core)

(defn- dice-result [dice]
  {:dice (or dice []) :score (reduce + 0 dice)})

(defn ones [dice]
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

(defn upper-score [scores]
  (->> [:ones :twos :threes :fours :fives :sixes]
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

(defn lower-bonus [requirement scores]
  (if (<= requirement (upper-score scores))
    50
    0))

(def score-sheet
  {:ones ones
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

(defn roll [n]
  (map #(inc (rand-int 6)) (range n)))
