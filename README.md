# Blurhash

A Clojure implementation of [Blurhash](https://blurha.sh/).

Blurhash is an algorithm by [Dag Ågren](https://github.com/DagAgren) of [Wolt](https://wolt.com) that decodes an image to a very compact (~ 20-30 bytes) ASCII string representation, which can be then decoded into a blurred placeholder image. See the [main repo](https://github.com/woltapp/blurhash) for the rationale and details. 

## Usage

The encode->hash->decode cycle looks something like this:

![a cute hedgehog](https://raw.githubusercontent.com/siili-core/blurhash/master/resources/example.jpg)

→ `UIGuXhS@x[xX_MORbuoy?uNGM{nTNHMzIVnn` →
![essence of a cute hedgehog](https://raw.githubusercontent.com/siili-core/blurhash/master/resources/example-blurred.jpg)

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
=> "UIGuXhS@x[xX_MORbuoy?uNGM{nTNHMzIVnn"
```

### Decoding

Here's an example of how to decode a blurhash into a placeholder image:

```clojure
(ns my-namespace
  (:require [blurhash.core :refer [write-matrix-to-image]
            [blurhash.decode :refer [decode]]]))

(def blurred-image
  (decode "UIGuXhS@x[xX_MORbuoy?uNGM{nTNHMzIVnn" 300 236))

(write-matrix-to-image blurred-image "blurred-image.jpg")

```

# TODO

Publish in Clojars.


# License
Released under the MIT license.
