(defproject asyncbench "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]

  :node-dependencies [[source-map-support "0.3.2"]
                      [benchmark "1.0.0"]
                      [nanotimer "0.3.10"]]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-npm "0.5.0"]]

  :source-paths ["src"]

  :cljsbuild
  {:builds [{:id "asyncbench"
             :source-paths ["src"]
             :compiler {:main asyncbench.core
                        :output-to "run_cljs_benchmark.js"
                        :output-dir "out"
                        :target :nodejs
                        :static-fns true
                        :optimizations :simple
                        :source-map "run_cljs_benchmark.js.map"}}]})
