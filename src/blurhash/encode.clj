(ns blurhash.encode)

(defn encode-component [i j height width image]
  (let [norm-factor (if (every? zero? (list i j))
                      1.0
                      2.0)
        wtimesh (* width height)
        means (vals
                (apply merge-with +
                       (for [y (range height)
                             x (range width)
                             :let [basis (* norm-factor
                                            (Math/cos (/ (* Math/PI (float i) (float x)) width))
                                            (Math/cos (/ (* Math/PI (float j) (float y)) height)))]]
                         [{:0 (* basis image[y][x][0])
                           :1 (* basis image[y][x][1])
                           :2 (* basis image[y][x][2])}])))]
  (mapv #(-> %1 (/ wtimesh) float))))

(defn encode-components [comp-x comp-y height width image]
  (vec
    (for [j (range comp-y)
          i (range comp-x)]
      (encode-component i j height width image))))

(defn encode
  ([image]
   (encode image 4 4))
  ([image comp-x comp-y]
   (let [height (count image)
         width (count (first image))
         components (encode-components comp-x comp-y height width image)
         ])))
