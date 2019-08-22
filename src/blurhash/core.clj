(ns blurhash.core
  (:require [clojure.java.io :as io])
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


(defn pixels->file [pixels ^String filename]
  (let [height (count pixels)
        width (count (first pixels))
        output-image (new BufferedImage width height BufferedImage/TYPE_INT_RGB)
        _ (doseq [row-index    (range height)
                  column-index (range width)]
            (let [[^Integer r ^Integer g ^Integer b] (nth (nth pixels row-index) column-index)
                  color (.getRGB (new Color r g b))]
              (.setRGB output-image column-index row-index color)))]
    (ImageIO/write
      output-image
      "jpg"
      (new File filename))))
