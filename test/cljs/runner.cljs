(ns blurhash.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            blurhash.base83-test
            blurhash.decode-test
            blurhash.util-test))

(enable-console-print!)

(doo-tests 'blurhash.base83-test
           'blurhash.decode-test
           'blurhash.util-test)
