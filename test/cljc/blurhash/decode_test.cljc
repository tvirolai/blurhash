(ns blurhash.decode-test
  (:require [blurhash.decode :as d]
            [clojure.string :refer [join]]
            [clojure.test :refer [deftest testing is]]))

(def test-hash
  "UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn")

(deftest decode-test
  (let [pic (d/decode test-hash 300 236)]
    (testing "Dimensions are right"
      (is (= 236 (count pic)))
      (is (= 300 (count (first pic)))))
    (testing "Content looks right"
      (is (= [158 169 150] (first (first pic)))))))

(deftest helper-tests
  (let [real-max-val (d/get-real-maxval test-hash 1.0)]
    (is (= 0.1144578313253012 real-max-val))
    (testing "The size info is decoded"
      (is (= {:size-x 4 :size-y 4} (d/decode-components test-hash))))
    #?(:clj (testing "An exception is thrown if the input is invalid"
      (is (thrown? Exception (d/decode-components ((comp join rest) test-hash))))))
    (testing "Color decoding"
      (let [colors (d/get-colors test-hash 4 4 real-max-val)]
        (is (= [0.2831487404299921 0.238397573812271 0.22696587351009836]
               (first colors)))
        (is (= -0.012717536813922355 (first (second colors))))
        (is (= 16 (count colors)))))
    (testing "AC component decoding"
      (is (= [-0.012717536813922355 0.012717536813922355 -0.035326491149784325]
             (d/decode-ac test-hash 1 real-max-val)))
      (is (= [0.022608954335861964, 0.035326491149784325, 0.022608954335861964]
             (d/decode-ac test-hash 2 real-max-val))))))
