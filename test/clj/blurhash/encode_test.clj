(ns blurhash.encode-test
  (:require [blurhash.encode :as e]
            [blurhash.core :as bh]
            [clojure.test :refer [deftest testing is]]))

(def test-hash
  "UIGuXeS@x[xX_MORbuoy?uNGM{nTNHMzIVnn")

(def example-file-path
  "./resources/example.jpg")

(deftest encode-image
  (testing "The generated hash matches one created by another implementation"
    (is (= test-hash (->> example-file-path bh/file->pixels e/encode)))))
