(ns file-management.core-test
  (:require [clojure.test :refer :all]
            [file-management.core :as core]))

(deftest build-avatar-key-test
  (is (= "test-avatar" (core/build-avatar-key "test"))))

(deftest get-content-type-test
  (is (= "image/png"
         (core/get-content-type "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQCAY")))
  (is (= "application/pdf"
         (core/get-content-type "data:application/pdf;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQCAY")))
  (is (= "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
         (core/get-content-type "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQCAY"))))

(deftest get-file-parsed-test
  (let [content "iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQCAY"]
    (is (= content
           (core/get-file-parsed (str "data:image/png;base64," content))))
    (is (= content
           (core/get-file-parsed (str "data:application/pdf;base64," content))))
    (is (= content
           (core/get-file-parsed (str "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64," content))))))
