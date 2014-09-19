(ns metro-tests.core-test
  (:require [clojure.test :refer :all]
            [metro-tests.core :refer :all]))

(def eps 0.00001)

(defn cmp [x y] (< (Math/abs (- x y)) eps))

(deftest conversion-test
  (testing "Deg->rad cases."
    (let [case1 [[180 Math/PI]
                 [90 (/ Math/PI 2)]
                 [60 (/ Math/PI 3)]]
          deg->rad-cmp (fn [x] (cmp (->rad (first x)) (second x)))]
      (is (every? deg->rad-cmp case1))))
  (testing "Rad->deg cases."
    (let [case1 [[Math/PI 180]
                 [(/ Math/PI 2) 90]
                 [(/ Math/PI 3) 60]]
          rad->deg-cmp (fn [x] (cmp (->deg (first x)) (second x)))]
      (is (every? rad->deg-cmp case1)))))
