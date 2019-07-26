(ns blurhash.decode
  (:require [blurhash.base83 :as base83]
            [blurhash.core :refer [srgb->linear sign-pow]]))

(defn decode-dc [value]
  (mapv srgb->linear
        (vector (bit-shift-right value 16)
                (bit-and (bit-shift-right value 8) 255)
                (bit-and value 255))))

(def tila
  (atom {}))

(defn decode-ac [value maxvalue]
  (let [quantr (Math/floor (/ value (* 19 19)))
        quantg (mod (Math/floor (/ value 19)) 19)
        quantb (mod value 19)
        _ (swap! tila assoc :quantr quantr
                 :quantg quantg
                 :quantb quantb
                 :maxvalue maxvalue)]
    (mapv #(* (sign-pow (/ (- (float %) 9) 9) 2) maxvalue)
          (list quantr quantg quantb))))

(defn ->basis [x y i j width height]
  (* (Math/cos (/ (* Math/PI x i) width))
     (Math/cos (/ (* Math/PI y j) height))))


(defn decode [blurhash w h & [punch?]]
  (let [sizeflag (base83/decode (str (first blurhash)))
        num-y (inc (/ sizeflag 9))
        num-x (inc (mod sizeflag 9))
        quant-maxval (base83/decode (str (second blurhash)))
        maxval (/ (inc quant-maxval) 166.0)
        punch? (or punch? 1.0)
        dc-value (base83/decode (subs blurhash 2 6))
        _ (swap! tila assoc :dc-value dc-value :punch punch? :maxval maxval)
        colors (into [(decode-dc dc-value)]
                     (mapv (fn [i]
                             (let [value (base83/decode
                                           (subs blurhash
                                                 (+ 4 (* i 2))
                                                 (+ 6 (* i 2))))
                                   _ (println value)]
                               (decode-ac value (* maxval punch?))))
                           (range 1 (* h w))))]
    colors))
