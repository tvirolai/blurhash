(ns blurhash.decode
  #?(:cljs (:require-macros [blurhash.util :as util]))
  (:require
    [blurhash.util :as util]
    [blurhash.base83 :as base83]))

(defn decode-dc [value]
  (mapv util/srgb->linear
        (vector (bit-shift-right value 16)
                (bit-and (bit-shift-right value 8) 255)
                (bit-and value 255))))

(defn decode-ac [blurhash value maxvalue]
	(let [ac-component (base83/decode (subs blurhash
                                          (+ 4 (* 2 value))
                                          (+ 4 (* 2 (inc value)))))]
    (mapv (fn [v]
            (-> v
                (- 9.0)
                (/ 9.0)
                (util/sign-pow 2.0)
                (* maxvalue)))
          (list (int (/ ac-component (* 19 19)))
                (float (mod (int (/ ac-component 19)) 19))
                (mod ac-component 19)))))

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

(defn- decode-pixel
  "A helper function that decodes a single pixel."
  [x y size-x size-y colors width height linear]
  (let [res (apply mapv +
                   (for [j (range size-y)
                         i (range size-x)
                         :let [basis (util/->basis x y i j width height)
                               color (nth colors (+ i (* j size-x)))]]
                     (mapv (partial * basis) color)))]
    (if-not linear
      (mapv util/linear->srgb res)
      res)))

(defn decode
  "The function takes a blurhash, along with some parameters, and returns an
  image as a pixel matrix. Along with the blurhash string, you need to provide
  width and height (w, h) values of the image you want to decode.

  The optional `punch` tunes the contrast of the image.
  If you want the image to be decoded into a matrix of linear floating-point
  values, set the `linear` parameter to `true`."
  [blurhash w h & [punch linear]]
  (let [punch (or punch 1.0)
        linear (or linear false)
        {:keys [size-x size-y]} (decode-components blurhash)
        colors (get-colors blurhash size-x size-y (get-real-maxval blurhash punch))]
    (for [y (range h)]
      (for [x (range w)]
        (decode-pixel x y size-x size-y colors w h linear)))))
