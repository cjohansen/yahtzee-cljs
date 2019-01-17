(ns ^:figwheel-hooks yahtzee.cards
  (:require [devcards.core]
            [yahtzee.cards.dice-cards]))

(enable-console-print!)

(defn render []
  (devcards.core/start-devcard-ui!))

(defn ^:after-load render-on-relaod []
  (render))

(render)
