(ns blurhash.encode
  (:require [blurhash.core :as bh]))

(defn ->basis [x y i j width height norm-factor]
  (* norm-factor
     (Math/cos (/ (* Math/PI x i) width))
     (Math/cos (/ (* Math/PI y j) height))))

(defn encode-component [i j height width norm-factor image-linear]
  (let [comp-data (for [y (range height)
                        x (range width)
                        :let [basis (->basis x y i j width height norm-factor)]]
                    (mapv (partial * basis) (nth (nth image-linear x) y)))]
    (->> comp-data
         (apply map +)
         (mapv #(/ % (* height width))))))

(defn encode-components [comp-x comp-y height width image-linear]
  (vec
    (for [j (range comp-y)
          i (range comp-x)
          :let [norm-factor (if (every? zero? (list i j))
                              1.0
                              2.0)]]
      (encode-component i j height width norm-factor image-linear))))

(defn encode
  ([image]
   (encode image 4 4 false))
  ([image comp-x comp-y linear]
   (let [height (count image)
         width (count (first image))
         image-linear (if-not linear
                        (mapv (fn [row] (mapv bh/srgb->linear row)) image)
                        image)
         components (encode-components comp-x comp-y height width image-linear)
         ])))
