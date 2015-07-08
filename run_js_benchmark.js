var summator = require('./summator')
var Benchmark = require('benchmark')

var countTo = parseInt(process.argv[2], 10)
var delay = process.argv.length > 3 ? process.argv[3] : null

var fn = function(done) {
    var i = 0
    var expectedSum = countTo * (countTo + 1) / 2
    var add = summator(delay)

    var loop = function() {
        i += 1
        add(i, function(sum) {
            if (i == countTo) {
                if (sum !== expectedSum) {
                    (console.log(sum, expectedSum))
                    throw new Error("Wrong sum")
                }
                done()
            } else {
                loop()
            }
        })
    }
    loop()
}

var suite = new Benchmark.Suite

suite.add('JavaScript', {
    defer: true,
    fn: function(defered) { fn(function() { defered.resolve() }) }
})
suite.on('cycle', function(event) { console.log(event.target.toString()) })

suite.run()
