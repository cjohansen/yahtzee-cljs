(ns yahtzee.actions
  (:require [yahtzee.core :as yahtzee]))

(defn trigger-action [game action & args]
  (case action
    :roll (apply swap! game yahtzee/roll args)
    :hold (apply swap! game yahtzee/hold args)
    :release (apply swap! game yahtzee/release args)
    :score (apply swap! game yahtzee/score args)))
