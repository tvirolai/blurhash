(ns blurhash.encode
  (:require [blurhash.util :as util]))

(defn encode-component [i j height width norm-factor image-linear]
  (let [comp-data (for [y (range height)
                        x (range width)
                        :let [basis (util/->basis x y i j width height norm-factor)]]
                    (mapv (partial * basis) (nth (nth image-linear y) x)))]
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

(defn convert-to-linear [image]
  (vec (for [row image]
         (vec (for [pix row]
                (mapv util/srgb->linear pix))))))

(defn encode
  ([image]
   (encode image 4 4 false))
  ([image comp-x comp-y linear]
   (let [height (count image)
         width (count (first image))
         image-linear (if-not linear
                        (convert-to-linear image)
                        image)
         components (encode-components comp-x comp-y height width image-linear)
         ]
     components)))
