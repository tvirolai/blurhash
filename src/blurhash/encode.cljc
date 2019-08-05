(ns blurhash.encode
  (:require
    #?(:cljs [blurhash.util :as util :refer-macros true]
       :clj  [blurhash.util :as util])
    [blurhash.util :as util]
    [blurhash.base83 :as base83]
    [clojure.string :as s]))

(defn encode-component [i j height width norm-factor image-linear]
  (let [comp-data (for [y (range height)
                        x (range width)
                        :let [basis (util/->basis x y i j width height norm-factor)]]
                    (mapv (partial * basis) (nth (nth image-linear y) x)))]
    (->> comp-data
         (apply map +)
         (mapv #(/ % (* height width))))))

(defn encode-components [comp-x comp-y height width image-linear]
  (util/forv [j (range comp-y)
              i (range comp-x)
              :let [norm-factor (if (every? zero? (list i j))
                                  1.0
                                  2.0)]]
    (encode-component i j height width norm-factor image-linear)))

(defn convert-to-linear [image]
  (util/forv [row image]
    (util/forv [pix row]
      (mapv util/srgb->linear pix))))

(defn encode-dc [component]
  (let [[r g b] (map util/linear->srgb component)]
    (+ (bit-shift-left r 16)
       (bit-shift-left g 8)
       b)))

(defn encode-ac-values [components ac-component-norm-factor]
  (for [c (rest components)
        :let [[r g b] (map #(-> %
                                (/ ac-component-norm-factor)
                                (util/sign-pow 0.5)
                                (* 9.0)
                                (+ 9.5)
                                Math/floor
                                (min 18.0)
                                (max 0.0)
                                int)
                           c)]]
    (+ (* r 19 19)
       (* g 19)
       b)))

(defn encode
  "Encode the pixel matrix into a blurhash.
  Optional args:
    comp-x and comp-y - x and y component counts. Defaults to 4 for both.
    linear - whether the image is encoded in linear format (0.0-1.0 floating point
    values). Defaults to false."
  ([image]
   (encode image 4 4 false))
  ([image comp-x comp-y]
   (encode image comp-x comp-y false))
  ([image comp-x comp-y linear]
   (let [height (count image)
         width (count (first image))
         image-linear (if-not linear
                        (convert-to-linear image)
                        image)
         components (encode-components comp-x comp-y height width image-linear)
         max-ac-comp (->> components
                          rest
                          flatten
                          (map #(Math/abs %))
                          (apply max))
         dc-value (encode-dc (first components))
         quant-max-ac-comp (max 0
                                (min 82
                                     (Math/floor (- (* max-ac-comp 166) 0.5))))
         ac-component-norm-factor (/ (float (inc quant-max-ac-comp)) 166.0)
         ac-values (encode-ac-values components ac-component-norm-factor)]
     (str (base83/encode (+ (dec comp-x) (* 9 (dec comp-y))) 1)
          (base83/encode quant-max-ac-comp 1)
          (base83/encode dc-value 4)
          (s/join
            (map #(base83/encode % 2) ac-values))))))
