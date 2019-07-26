(ns blurhash.decode-test
  (:require [blurhash.decode :refer :all]
            [clojure.string :refer [join]]
            [clojure.test :refer :all]))

(def test-hash
  "UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn")

(deftest decode-test
  (let [pic (decode test-hash 300 263)]
    (testing "Dimensions are right"
      (is (= 263 (count pic)))
      (is (= 300 (count (first pic)))))
    (testing "Content is right"
      (is (= [158 169 150] (first (first pic)))))))

(deftest helper-tests
  (let [real-max-val (get-real-maxval test-hash 1.0)]
    (is (= 0.1144578313253012 real-max-val))
    (testing "The size info is decoded"
      (is (= {:size-x 4 :size-y 4} (decode-components test-hash))))
    (testing "An exception is thrown if the input is invalid"
      (is (thrown? Exception (decode-components ((comp join rest) test-hash)))))
    (testing "Color decoding"
      (let [colors (get-colors test-hash 4 4 real-max-val)]
        (is (= [0.2831487404299921 0.238397573812271 0.22696587351009836]
               (first colors)))
        (is (= -0.012717536813922355 (first (second colors))))
        (is (= 16 (count colors)))))
    (testing "AC component decoding"
      (is (= [-0.012717536813922355 0.012717536813922355 -0.035326491149784325]
             (decode-ac test-hash 1 real-max-val)))
      (is (= [0.022608954335861964, 0.035326491149784325, 0.022608954335861964]
             (decode-ac test-hash 2 real-max-val))))))
