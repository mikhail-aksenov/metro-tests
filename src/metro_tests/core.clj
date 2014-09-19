(ns metro-tests.core
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(defn ->rad [x] (* (/ x 180) Math/PI))

(defn ->deg [x] (* (/ x Math/PI) 180))

(defn- calc-side
  "Calculates side using given sides and angle.
  
  c^2 = a^2+b^2 - 2ab * cos(alpha)
  c = sqrt(a^2+b^2 - 2ab * cos(alpha))"
  [a b angle-gr]
  (let [sq (fn [x] (* x x))
        cosa (Math/cos (->rad angle-gr))
        cp2 (- (+ (sq a) (sq b)) (* a b cosa))]
    (Math/sqrt cp2)))

(defn- calc-angle
  "Calculates angle between last two sides of triangle
  
  c^2 = a^2+b^2 - 2ab * cos(alpha)
  alpha = arccos((a^2 + b^2 - c^2) / 2ab)
  "
  [a b c]
  (let []))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
