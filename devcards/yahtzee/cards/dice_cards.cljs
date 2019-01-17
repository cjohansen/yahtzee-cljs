(ns yahtzee.cards.dice-cards
  (:require [dumdom.devcards :refer-macros [defcard]]
            [yahtzee.components.die :as die]))

(defcard die-one
  (die/die 1))
