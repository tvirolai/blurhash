# Blurhash

[![CircleCI](https://circleci.com/gh/tvirolai/blurhash.svg?style=svg)](https://circleci.com/gh/tvirolai/blurhash)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A Clojure implementation of [Blurhash](https://blurha.sh/).

Blurhash is an algorithm by [Dag Ågren](https://github.com/DagAgren) of [Wolt](https://wolt.com) that decodes an image to a very compact (~ 20-30 bytes) ASCII string representation, which can be then decoded into a blurred placeholder image. See the [main repo](https://github.com/woltapp/blurhash) for the rationale and details.

## Latest version

[![Clojars Project](https://img.shields.io/clojars/v/tvirolai/blurhash.svg)](https://clojars.org/tvirolai/blurhash)

Both the encoder and decoder are implemented and work in both Clojure and ClojureScript.

## Usage

The encode->hash->decode cycle looks something like this:

![a cute hedgehog](https://raw.githubusercontent.com/tvirolai/blurhash/master/resources/example.jpg)

→ `UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn` →

![essence of a cute hedgehog](https://raw.githubusercontent.com/tvirolai/blurhash/master/resources/example-blurred.jpg)

The Clojure implementation is written in CLJC files, so that they can be used from Clojure and ClojureScript code alike.

### Encoding

You can encode an image to a blurhash using the function `blurhash.encode/encode`. It takes an image as an RGB matrix, currently represented as nested native vectors. The `blurhash.core` namespace contains (Clojure-specific) functions to convert an image file into a matrix, so you can do something like this:

```clojure
(ns my-namespace
  (:require [blurhash.core :refer [file->pixels]
            [blurhash.encode :refer [encode]]]))
(def image
  (file->pixels "./resources/example.jpg"))

(encode image)
=> "UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn"
```

### Decoding

Here's an example of how to decode a blurhash into a placeholder image:

```clojure
(ns my-namespace
  (:require [blurhash.core :refer [pixels->file]
            [blurhash.decode :refer [decode]]]))

(def blurred-image
  (decode "UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn" 300 236))

(pixels->file blurred-image "blurred-image.jpg")

```

In ClojureScript (Reagent), you can decode a hash and render the placeholder like this:

```clojure
(ns blurhash-example.core
  (:require [reagent.core :as r]
            [blurhash.decode :refer [decode]
            [blurhash.util :as util]]))

(defn draw-canvas-contents [canvas]
  (let [pixels (decode "UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn" 100 100)
        ctx (.getContext canvas "2d")
        imageData (.createImageData ctx 100 100)]
    (do
      (.set (. imageData -data) pixels)
      (. ctx putImageData imageData 0 0))))

(defn div-with-canvas []
  (let [dom-node (r/atom nil)]
    (r/create-class
      {:component-did-update
       (fn [_]
         (draw-canvas-contents (.-firstChild @dom-node)))
       :component-did-mount
       (fn [this]
         (reset! dom-node (r/dom-node this)))
       :reagent-render
       (fn []
         [:div
          [:canvas (if-let [node @dom-node]
                     {:width (.-clientWidth node)
                      :height (.-clientHeight node)})]])})))

(defn home-page []
  [:div [:h2 "Look at this blur!"]
   [div-with-canvas]])

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

```

# MIT License

Copyright (c) 2019-2022 Tuomo Virolainen

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
