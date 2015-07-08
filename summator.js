var NanoTimer = require('nanotimer')
var timer = new NanoTimer()

function summator(delay) {
    var sum = 0

    add = function(amount, callback) {
        afterDelay = function() {
            sum += amount
            callback(sum)
        }

        if (delay === null) {
            process.nextTick(afterDelay)
        } else {
            timer.setTimeout(afterDelay, null, delay)
        }
    }

    return add
}

module.exports = summator
