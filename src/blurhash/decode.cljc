(ns blurhash.decode
  (:require [blurhash.base83 :as base83]
            [blurhash.util :as util]))

(defn decode-dc [value]
  (mapv util/srgb->linear
        (vector (bit-shift-right value 16)
                (bit-and (bit-shift-right value 8) 255)
                (bit-and value 255))))

(defn decode-ac [blurhash value maxvalue]
	(let [ac-component (base83/decode (subs blurhash
                                          (+ 4 (* 2 value))
                                          (+ 4 (* 2 (inc value)))))]
    [(* maxvalue (util/sign-pow (/ (- (int (/ ac-component (* 19 19))) 9.0) 9.0) 2.0))
     (* maxvalue (util/sign-pow (/ (- (float (mod (int (/ ac-component 19)) 19)) 9.0) 9.0) 2.0))
     (* maxvalue (util/sign-pow (/ (- (mod ac-component 19) 9.0) 9.0) 2.0))]))

(defn ->basis [x y i j width height]
  (* (Math/cos (/ (* Math/PI x i) width))
     (Math/cos (/ (* Math/PI y j) height))))

(defn decode-components [blurhash]
  (let [size-info (base83/decode (str (first blurhash)))
        size-x (inc (int (/ size-info 9)))
        size-y (inc (mod size-info 9))]
    (if (= (count blurhash)
           (+ 4 (* 2 size-x size-y)))
      {:size-x size-x
       :size-y size-y}
      (throw (Exception. "Invalid blurhash length")))))

(defn get-real-maxval [blurhash punch]
  (let [quant-max-val (base83/decode (str (second blurhash)))]
    (* punch (/ (float (inc quant-max-val)) 166.0))))

(defn get-colors [blurhash size-x size-y max-val]
  (let [dc (decode-dc (base83/decode (subs blurhash 2 6)))
        ac (for [value (range 1 (* size-x size-y))]
             (decode-ac blurhash value max-val))]
    (conj ac dc)))

(defn decode-pixel [x y size-x size-y colors width height linear]
  (let [res (apply mapv +
                   (for [j (range size-y)
                         i (range size-x)
                         :let [basis (->basis x y i j width height)
                               color (nth colors (+ i (* j size-x)))]]
                     (mapv (partial * basis) color)))]
    (if-not linear
      (mapv util/linear->srgb res)
      res)))

(defn decode [blurhash w h & [punch linear]]
  (let [punch (or punch 1.0)
        linear (or linear false)
        {:keys [size-x size-y]} (decode-components blurhash)
        dc-val (base83/decode (subs blurhash 2 6))
        colors (get-colors blurhash size-x size-y (get-real-maxval blurhash punch))]
    (for [y (range h)]
      (vec
        (for [x (range w)
              :let [pixel (decode-pixel x y size-x size-y colors w h linear)]]
          pixel)))))
