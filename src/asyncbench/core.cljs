(ns asyncbench.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :as async :refer [<! >!]]
            [cljs.nodejs :as nodejs]))

(enable-console-print!)

(def summator (js/require "./summator"))
(def Benchmark (js/require "benchmark"))

(defn triangle-number [n]
  (/ (* n (inc n)) 2))

(defn mut [v]
  (array v))

(defn mget! [m]
  (aget m 0))

(defn mset! [m v]
  (aset m 0 v))

(defn make-callback-benchmark [count-to delay]
  (fn [done]
    (let [add (summator delay)
          i (mut 0)
          expected-sum (triangle-number count-to)]

      (letfn [(loop-fn []
                (mset! i (inc (mget! i)))
                (add (mget! i) (fn [sum]
                                 (if (= (mget! i) count-to)
                                   (if (not= sum expected-sum)
                                     (throw (js/Error. "Wrong sum"))
                                     (done))
                                   (loop-fn)))))]
        (loop-fn)))))


(defn make-async-benchmark [count-to delay]
  (fn [done]
    (let [add (summator delay)
          expected-sum (triangle-number count-to)
          <call-add (fn [ch amount]
                      (add amount #(async/put! ch %))
                      ch)
          <sums (async/chan 1)]

      (go-loop [i 1]
        (let  [sum (<! (<call-add <sums i))]
          (if (= i count-to)
            (if (not= sum expected-sum)
              (throw (js/Error. "Wrong sum"))
              (done))

            (recur (inc i))))))))


(defn -main []
  (let [[_ _ count-to-arg delay] (.-argv js/process)
        count-to (js/parseInt count-to-arg 10)
        suite (.Suite Benchmark)
        callback-benchmark (make-callback-benchmark count-to delay)
        async-benchmark (make-async-benchmark count-to delay)]
    (doto suite
      (.add "ClojureScript callbacks",
            (clj->js {:defer true
                      :fn (fn [defered]
                            (callback-benchmark #(.resolve defered)))}))

      (.add "ClojureScript async"
            (clj->js {:defer true
                      :fn (fn [defered]
                            (async-benchmark #(.resolve defered)))}))

      (.on "cycle" (fn [event]
                     (println (.. event -target toString))))
      (.run))))


(set! *main-cli-fn* -main)
