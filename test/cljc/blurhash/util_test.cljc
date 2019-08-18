(ns blurhash.util-test
  #?(:cljs (:require-macros [blurhash.util :as util]))
  (:require [clojure.test :refer [deftest testing is]]
            [blurhash.util :as util]))

; Testing that the functions work as expected by using some inputs
; from from other BH implementations.

(deftest srgb->linear-test
  (is (= 0.8631572134541023 (util/srgb->linear 239)))
  (is (= 0.003035269835488375 (util/srgb->linear 10))))

(deftest linear->srgb-test
  (is (= 170 (util/linear->srgb 0.4)))
  (is (= 239 (util/linear->srgb (util/srgb->linear 239)))))

(deftest signed->unsigned-test
  (is (= 227 (util/signed->unsigned -29)))
  (is (= 100 (util/signed->unsigned 100))))

(deftest forv-macro
  (testing "The forv macro works should work in all environments"
    (is (= (vec (for [i (range 5)] i))
           (util/forv [i (range 5)] i)))))
