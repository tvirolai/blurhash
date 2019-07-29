(ns blurhash.base83
  (:require [clojure.string :as s]))

(def alphabet
  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#$%*+,-.:;=?@[]^_{|}~")

(defn decode [string]
  (let [indices (map (partial s/index-of alphabet) string)]
    (reduce #(+ (* 83 %1) %2) indices)))

(defn encode [value len]
  (->> (range)
       rest
       (map-indexed (fn [idx itm]
                      (let [div (Math/pow 83 (- len (inc idx)))
                            digit (mod (Math/floor (/ value div)) 83)]
                        (nth alphabet digit))))
       (take len)
       s/join))
