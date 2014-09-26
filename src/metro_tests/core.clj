(ns metro-tests.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log])
  (:gen-class))

(def input-size 3)

(defn ->rad [x] (* (/ x 180) Math/PI))

(defn ->deg [x] (* (/ x Math/PI) 180))

(defn sq [x] (* x x))

(defn calc-side
  [a b angle]
  (let [cosa (Math/cos (->rad angle))
        cp2 (- (+ (sq a) (sq b)) (* a b cosa))]
    (Math/sqrt cp2)))

(defn calc-angle
  [a b c]
  (let [cosa (/ (- (+ (sq a) (sq b)) (sq c)) (* 2 a b))]
    (Math/acos cosa)))

(defn valid-side?
  [side]
  (pos? side))

(defn valid-angle?
  [angle]
  (and (pos? angle) (<= 180 angle)))

(defn valid-input?
  [a b alpha idx]
  (if-not (and (valid-side? a) (valid-side? b) (valid-angle? alpha))
    (do (log/error "Row" idx "is not valid. Input: " a ";" b ";" alpha))
    true))

(defn parse-line
  [row idx]
  (let [partitioned (str/split row #";")
        len (count partitioned)]
    (if (>= len input-size)
      (do
        (try 
          (->> partitioned
            (map #(Double/parseDouble %))
            (zipmap [:a :b :alpha]))
          (catch NumberFormatException e
            (log/error e))))
      (log/error "String has less than 3 parameters at row" idx ": {" row "}"))))

(defn process-line
  [row idx]
  (try
    (let [data (parse-line row idx)
          a (:a data)
          b (:b data)
          alpha (:alpha data)
          c (calc-side a b alpha)
          beta (calc-angle b c a)
          gamma (calc-angle c a b)]
      { :angles (str a ";" b ";" c "\n") 
        :sides (str alpha ";" beta ";" gamma "\n")})
    (catch Exception e
      (log/error (.getMessage e) "at index" idx))))

(defn process
  [input output]
  (with-open [reader (io/reader input)
              writer (io/writer output)]
    (let [idx (atom 1)]
      (doseq [line (line-seq reader)]
        (when-let [res (process-line line @idx)]
          (.write writer (:sides res))
          (log/info (:angles res)))
        (swap! idx inc)))))

(defn -main
  [& args]
  (if (< (count args) 2)
    (log/error "This application requires at least two arguments")
    (apply process args)))