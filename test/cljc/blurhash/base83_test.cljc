(ns blurhash.base83-test
  (:require [clojure.test :refer [deftest testing is]]
            [blurhash.base83 :as base83]))

(deftest decoding
  (testing "Base 83 decoding works as expected"
    (is (= 1779490937 (base83/decode "bfD2:")))))

(deftest encoding
  (testing "Round-tripping from string representation"
    (is (= "bfD2:" (base83/encode 1779490937 5)))
    (let [h "ZkdD9;{"]
      (is (= h (-> h
                   base83/decode
                   (base83/encode (count h))))))))
