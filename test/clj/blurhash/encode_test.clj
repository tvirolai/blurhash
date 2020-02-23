(ns blurhash.encode-test
  (:require [blurhash.encode :refer [encode]]
            [blurhash.core :as bh]
            [clojure.test :refer [deftest testing is]]))

(def test-hash
  "UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn")

(def example-file-path
  "./resources/example.jpg")

(deftest encode-image
  (testing "The generated hash matches one created by another implementation"
    (is (= test-hash (->> example-file-path bh/file->pixels encode))))
  (testing "Encoding works with other components counts than the default 4"
    (let [res (-> example-file-path
                  bh/file->pixels
                  (encode 2 3))]
      (is (= "JIGuXeS@_MOR?uNG" res))))
  (testing "Encoding works with pixels in the linear format too"
    (let [pixels [[[0.2 0.3 0.4]
                   [1.0 0.9 0.2]
                   [0.0 0.8 0.23]]
                  [[0.2 0.3 0.4]
                   [1.0 0.9 0.2]
                   [0.0 0.8 0.23]]
                  [[0.2 0.3 0.4]
                   [1.0 0.9 0.2]
                   [0.0 0.8 0.23]]]
          encoded (encode pixels 4 4 true)]
      (is (= "U~JmE4}[Da3X?s%2RWKPfQfQfQfQ?s%2RWKP" encoded)))))
