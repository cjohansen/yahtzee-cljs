(ns ^:figwheel-hooks yahtzee.dev
  (:require [yahtzee.ui :as ui]))

(enable-console-print!)

(defn ^:after-load render-on-relaod []
  (swap! ui/game assoc :now (js/Date.)))

(defonce started (do (ui/start!) true))
