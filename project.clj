(defproject siili-core/blurhash "0.0.2"
  :description "A Clojure(Script) implementation of the blurhash algorithm"
  :url "http://github.com/siili-core/blurhash"
  :license {:name "MIT License"
            :url "https://github.com/siili-core/blurhash/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :aliases {"analyze" ["eastwood" "{:linters [:unused-namespaces]}"]}
  :plugins [[jonase/eastwood "0.3.5"]
            [lein-shell "0.5.0"]]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass
                                    :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["shell" "git" "commit" "-am" "Version ${:version} [ci skip]"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["shell" "git" "commit" "-am" "Version ${:version} [ci skip]"]
                  ["vcs" "push"]]
  :repl-options {:init-ns blurhash.core})
