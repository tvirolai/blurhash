(defproject siili/blurhash "0.0.1"
  :description "A Clojure(Script) implementation of the blurhash algorithm"
  :url "http://github.com/siili-core/blurhash"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :aliases {"analyze" ["eastwood" "{:linters [:unused-namespaces]}"]}
  :plugins [[jonase/eastwood "0.3.5"]]
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env
                                     :password :env
                                     :sign-releases false}]]
  :repl-options {:init-ns blurhash.core})
