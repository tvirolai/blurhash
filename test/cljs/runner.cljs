(ns runner
  (:require [doo.runner :refer-macros [doo-all-tests]]
            [blurhash.base83-test]
            [blurhash.decode-test]
            [blurhash.util-test]))

(enable-console-print!)

(doo-all-tests #"blurhash.*-test")
