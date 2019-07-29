(ns blurhash.core
  (:require [blurhash.encode :as encode]
            [blurhash.decode :as decode]
            [clojure.java.io :as io])
  (:import (java.awt.image BufferedImage)
           (java.io File)
           (java.awt Color)
           (javax.imageio ImageIO)))

(defn file->pixels [path]
  (let [image (-> path io/file ImageIO/read)
        width (.getWidth image)]
    (map vec
         (partition width
                    (for [row-index (range (.getHeight image))
                          column-index (range width)
                          :let [rgb (.getRGB image column-index row-index)
                                rgb-object (new Color rgb)]]
                      (vector (.getRed rgb-object)
                              (.getGreen rgb-object)
                              (.getBlue rgb-object)))))))

(defn write-matrix-to-image [matrix filename]
  (ImageIO/write
    (let [height (count matrix)
          width (count (first matrix))
          output-image (new BufferedImage width height BufferedImage/TYPE_INT_RGB)]
      (doseq [row-index    (range height)
              column-index (range width)]
        (let [[r g b] (nth (nth matrix row-index) column-index)
              color (.getRGB (new Color r g b))]
          (.setRGB output-image column-index row-index (.intValue color))))
      output-image)
    "jpg"
    (new File filename)))

(defn encode-file [path]
  (-> path
      file->pixels
      encode/encode))
