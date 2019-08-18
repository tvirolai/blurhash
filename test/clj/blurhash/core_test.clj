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

; (use-fixtures :once with-temp-file-cleaned)

(deftest file->pixels-test
  (let [blurred-matrix (file->pixels blurred-test-file-name)]
    #_(testing "Pixels in the matrix from the read file match the computed values"
      (is (= [158 169 150] (first (first blurred-matrix))))
      (is (= [159 145 124] (first (last blurred-matrix))))
      (is (= [148 134 138] (last (last blurred-matrix))))
      (is (= [191 180 180] (nth (second blurred-matrix) 200))))
    (testing "Dimensions match"
      (is (= 236 (count blurred-matrix)))
      (is (= 300 (count (first blurred-matrix)))))))

#_(deftest io-test
  (testing "Round-tripping (file->matrix->file)"
    (let [orig (file->pixels test-file-name)
          round-tripped (do
                          (write-matrix-to-image orig temporary-file)
                          (file->pixels temporary-file))]
      (is (= orig round-tripped)))))
