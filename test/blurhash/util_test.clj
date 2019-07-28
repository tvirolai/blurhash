(ns blurhash.util-test
  (:require [clojure.test :refer :all]
            [blurhash.util :refer :all]))

; Testing that the functions work as expected by using some inputs
; from from other BH implementations.

(deftest srgb->linear-test
  (is (= 0.8631572134541023 (srgb->linear 239)))
  (is (= 0.003035269835488375 (srgb->linear 10))))

(deftest linear->srgb-test
  (is (= 170 (linear->srgb 0.4)))
  (is (= 239 (linear->srgb (srgb->linear 239)))))

(deftest signed->unsigned-test
  (is (= 227 (signed->unsigned -29)))
  (is (= 100 (signed->unsigned 100))))

(run-tests)
