package util

private fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}

fun lcmOfList(numbers: List<Long>): Long {
    var result = 1L
    for (number in numbers) {
        result = lcm(result, number)
    }
    return result
}