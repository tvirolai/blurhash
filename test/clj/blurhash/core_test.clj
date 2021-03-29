(ns blurhash.core-test
  (:require [blurhash.core :refer [file->pixels image->pixels pixels->file]]
            [clojure.java.io :as io]
            [clojure.test :refer :all])
  (:import (javax.imageio ImageIO)))

(def test-file-name
  "./resources/example.jpg")

(def blurred-test-file-name
  "./resources/example-blurred.jpg")

(def temporary-file
  "./resources/test-file.jpg")

(defn with-temp-file-cleaned [f]
  (f)
  (let [temp-file (io/file temporary-file)]
    (when (.isFile temp-file)
      (io/delete-file temp-file))))

(use-fixtures :once with-temp-file-cleaned)

(deftest image->pixels-test
  (let [blurred-image  (-> blurred-test-file-name io/file ImageIO/read)
        blurred-matrix (image->pixels blurred-image)]
    (testing "Dimensions match"
      (is (= 236 (count blurred-matrix)))
      (is (= 300 (count (first blurred-matrix)))))))

(deftest file->pixels-test
  (let [blurred-matrix (file->pixels blurred-test-file-name)]
    (testing "Dimensions match"
      (is (= 236 (count blurred-matrix)))
      (is (= 300 (count (first blurred-matrix)))))))

(deftest pixel->file-test
  (let [pixels (file->pixels blurred-test-file-name)
        saved-pixels (do
                       (pixels->file pixels temporary-file)
                       (file->pixels temporary-file))]
    (testing "Dimensions match"
      (is (= (count pixels) (count saved-pixels)))
      (is (= (count (first pixels)) (count (first saved-pixels)))))
    (testing "The pixels are identical"
      (is (= pixels saved-pixels)))))
