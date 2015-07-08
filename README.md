##Simplistic nodejs core.async benchmark

This benchmark sums natural numbers from 1 to N, invoking each summation step
via a callback after a delay. If unspecified, the delay is just
`process.nextTick`, otherwise `setTimeout` of
[nanotimer](https://www.npmjs.com/package/nanotimer) is used.

The benchmark is implemented in three variants: pure JavaScript, ClojureScript + callbacks,
ClojureScript + core.async.


###How to build:

`lein npm install`

`lein cljsbuild once`


###How to run

`node run_js_benchmark.js N [delay]` --- for the JavaScript version.

`node run_cljs_benchmark.js N [delay]` --- for two ClojureScript versions.

`delay` is specified as a number with letter suffix, ex '150n', '200u', '35m', '10s'.


###Results on my machine


####`SetTimeout` results

```
$ node run_js_benchmark.js 2000 100u
JavaScript x 4.79 ops/sec ±0.14% (28 runs sampled)

$ node run_cljs_benchmark.js 2000 100u
ClojureScript callbacks x 4.71 ops/sec ±0.68% (28 runs sampled)
ClojureScript async x 4.36 ops/sec ±0.60% (26 runs sampled)
```


####`process.nextTick(f)` results

```
$ node run_js_benchmark.js 2000
JavaScript x 443 ops/sec ±0.80% (82 runs sampled)

$ node run_cljs_benchmark.js 2000
ClojureScript callbacks x 316 ops/sec ±0.79% (83 runs sampled)
ClojureScript async x 37.79 ops/sec ±2.10% (68 runs sampled)
```


####tiny delays make async faster!

```
$ node run_cljs_benchmark.js 1000 10n
ClojureScript callbacks x 119 ops/sec ±1.79% (75 runs sampled)
ClojureScript async x 142 ops/sec ±1.59% (80 runs sampled)


$ node run_cljs_benchmark.js 1000
ClojureScript callbacks x 441 ops/sec ±0.66% (88 runs sampled)
ClojureScript async x 67.54 ops/sec ±1.89% (82 runs sampled)
```
