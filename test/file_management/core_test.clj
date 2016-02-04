(ns file-management.core-test
  (:require [clojure.test :refer :all]
            [file-management.core :as core]))

(deftest build-avatar-key-test
  (is (= "test-avatar" (core/build-avatar-key "test"))))
