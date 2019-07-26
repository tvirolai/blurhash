(ns blurhash.core
  (:require [clojure.java.io :as io]
            [clojure.set :refer [subset?]]
            [blurhash.base83 :refer [alphabet]]
            [clojure.spec.alpha :as s]
            [mikera.image.core :as img]
            [clojure.core.matrix :as mat])
  (:import (java.awt.image BufferedImage)
           (java.awt Color)
           (javax.imageio ImageIO)))

(s/def ::blurhash
  (s/and string?
         #(> (count %) 6)
         #(subset? (set %) (set alphabet))))

(defn srgb->linear
  "Linear floating point 0.0-1.0 -> srgb integer (0-255)"
  [value]
  (let [fval (/ value 255.0)]
    (if (<= fval 0.04045)
      (/ fval 12.92)
      (Math/pow (/ (+ 0.055 fval) 1.055) 2.4))))

(defn linear->srgb
  "Srgb integer (0-255) -> linear floating point (0.0-1.0)"
  [value]
  (let [v (max 0.0 (min 1.0 value))]
    (if (<= v 0.0031308)
      (int (+ (* v 12.92 255) 0.5))
      (int (+ (* (- (* 1.055 (Math/pow v (/ 1 2.4))) 0.055) 255) 0.5)))))

(defn signed->unsigned [v]
  (if (neg? v) (+ 256 v) v))

(defn sign-pow
  "Sign-preserving exponentiation."
  [v exp]
  (* (if (neg? v) -1 1)
     (Math/pow (Math/abs v) exp)))

(defrecord Pixel [r g b])

(defn file->pixels [path]
  (let [image (-> path io/file ImageIO/read)
        data (-> image .getRaster .getDataBuffer .getData)
        width (.getWidth image)
        height (.getHeight image)]
    (partition width
               (for [x (range width)
                     y (range height)
                     :let [rgb (.getRGB image x y)
                           rgb-object (new Color rgb)]]
                 (vector (.getRed rgb-object)
                         (.getGreen rgb-object)
                         (.getBlue rgb-object))))))
