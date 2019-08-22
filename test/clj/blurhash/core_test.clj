(ns blurhash.core-test
  (:require [clojure.test :refer :all]
            [blurhash.core :refer :all]
            [clojure.java.io :as io]))

(def test-file-name
  "./resources/example.jpg")

(def blurred-test-file-name
  "./resources/example-blurred.jpg")

(def temporary-file
  "./resources/test-file.png")

(defn with-temp-file-cleaned [f]
  (f)
  (let [temp-file (io/file temporary-file)]
    (when (.isFile temp-file)
      (io/delete-file temp-file))))

(use-fixtures :once with-temp-file-cleaned)

(deftest file->pixels-test
  (let [blurred-matrix (file->pixels blurred-test-file-name)]
    (testing "Dimensions match"
      (is (= 236 (count blurred-matrix)))
      (is (= 300 (count (first blurred-matrix)))))))

#_(deftest io-test
  (testing "Round-tripping (file->matrix->file)"
    (let [orig (file->pixels test-file-name)
          round-tripped (do
                          (pixels->file orig temporary-file)
                          (file->pixels temporary-file))]
      (is (= orig round-tripped)))))
