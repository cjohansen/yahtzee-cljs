(ns ^:figwheel-hooks yahtzee.test-runner
  (:require [yahtzee.core-test]
            [cljs.test :as test]
            [cljs-test-display.core :as display]))

(enable-console-print!)

(defn test-run []
  (test/run-tests
   (display/init! "app-tests")
   'yahtzee.core-test))

(defn ^:after-load render-on-relaod []
  (test-run))

(test-run)
