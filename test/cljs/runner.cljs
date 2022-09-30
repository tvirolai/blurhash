(ns runner
  (:require [cljs.test :refer [run-tests]]
            blurhash.base83-test
            blurhash.decode-test
            blurhash.util-test))

(enable-console-print!)

(run-tests 'blurhash.base83-test
           'blurhash.decode-test
           'blurhash.util-test)
