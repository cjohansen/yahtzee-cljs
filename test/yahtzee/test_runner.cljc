(ns ^:figwheel-hooks yahtzee.test-runner
  (:require [yahtzee.game-test]
            [yahtzee.prep-test]
            [cljs.test :as test]
            [cljs-test-display.core :as display]))

(enable-console-print!)

(defn test-run []
  (test/run-tests
   (display/init! "app-tests")
   'yahtzee.game-test
   'yahtzee.prep-test))

(defn ^:after-load render-on-relaod []
  (test-run))

(test-run)
