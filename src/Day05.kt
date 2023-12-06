import java.lang.Long.max
import java.lang.Long.min

fun main() {
    fun parseSeeds(input: String): List<Long> {
        return input.substring(input.indexOf(':') + 1)
            .trim()
            .split(' ')
            .map { str -> str.trim() }
            .filter { str -> str.isNotEmpty() }
            .map { str -> str.toLong() }
            .toList()
    }

    fun getMapIndex(mapName: String, input: List<String>): Int {
        for (i in input.indices) {
            if (input[i] == mapName) {
                return i
            }
        }
        return -1
    }

    fun getNextNewLineIndex(input: List<String>, startIdx: Int): Int {
        for (i in startIdx until input.size) {
            if (input[i].isEmpty()) {
                return i
            }
        }
        return -1
    }

    data class Interval(val start: Long, val end: Long)
    data class ConversionResult(val result: List<Interval>, val toBeConverted: List<Interval>)
    data class Conversion(private val list: List<Long>) {
        val destStart = list[0]
        val srcStart = list[1]
        val range = list[2]

        fun convert(src: Long): Long? {
            val diff = src - srcStart
            if (diff in 0 until range) {
                return destStart + diff
            }
            return null
        }

        fun convertInterval(interval: Interval): ConversionResult {
            val result = mutableListOf<Interval>()
            val toBeConverted = mutableListOf<Interval>()

//            println("interval: $interval")
            val srcEnd = srcStart + range - 1
//            println("srcStart: $srcStart, srcEnd: $srcEnd")
            val destEnd = destStart + range - 1
//            println("destStart: $destStart, destEnd: $destEnd")

            val intervalStart = interval.start
            val intervalEnd = interval.end

            // Completely out of range.
            if (intervalEnd < srcStart || intervalStart > srcEnd) {
                toBeConverted.add(interval)
                return ConversionResult(result, toBeConverted)
            }

            // Before the overlap with range of conversion.
            if (intervalStart < srcStart) {
                toBeConverted.add(Interval(intervalStart, srcStart - 1))
            }

            // Overlap. Starting at srcStart.
            val beforeConversionStart = max(intervalStart, srcStart)
//            println("beforeConversionStart: $beforeConversionStart")
            val afterConversionStart = beforeConversionStart - srcStart + destStart
//            println("afterConversionStart: $afterConversionStart")
            if (intervalEnd <= srcEnd) {
                val beforeConversionEnd = min(intervalEnd, srcEnd)
                result.add(Interval(afterConversionStart, beforeConversionEnd - srcEnd + destEnd))
            } else {
                // After the overlap with range of conversion.
                result.add(Interval(afterConversionStart, destEnd))
                toBeConverted.add(Interval(srcEnd + 1, intervalEnd))
            }

//            println("result: $result, toBeConverted: $toBeConverted")
            return ConversionResult(result, toBeConverted)
        }
    }

    data class ConversionMap(val conversions: List<Conversion>) {
        fun convert(src: Long): Long {
            var result: Long? = null
            for (conversion in conversions) {
                if (conversion.convert(src) != null) {
                    result = conversion.convert(src)!!
                    break
                }
            }
            return result ?: src
        }

        fun convertInterval(interval: Interval): ConversionResult {
            val result = mutableListOf<Interval>()

            var toBeConverted = listOf(interval)
            var newToBeConverted = listOf<Interval>()
            for (conversion in conversions) {
                var conversionResult = ConversionResult(listOf(), listOf())
                for (original in toBeConverted) {
                    conversionResult = conversion.convertInterval(original)
                    result.addAll(conversionResult.result)
                    newToBeConverted = conversionResult.toBeConverted
                }
                toBeConverted = newToBeConverted
            }
            return ConversionResult(result, toBeConverted)
        }

        fun convertIntervals(intervals: List<Interval>): List<Interval> {
            val result = mutableListOf<Interval>()
            for (interval in intervals) {
                val conversionResult = convertInterval(interval)
                result.addAll(conversionResult.result)
                result.addAll(conversionResult.toBeConverted)
            }
            return result
        }
    }

    fun parseMapRow(input: String): List<Long> {
        return input.trim()
            .split(' ')
            .map { str -> str.trim() }
            .filter { str -> str.isNotEmpty() }
            .map { str -> str.toLong() }
            .toList()
    }

    fun parseMap(input: List<String>): ConversionMap {
        val map = mutableListOf<Conversion>()
        input.forEach {
            map.add(Conversion(parseMapRow(it)))
        }
        return ConversionMap(map)
    }

    fun convertSeedToLocation(seed: Long, maps: List<ConversionMap>): Long {
        var convert = seed
        for (map in maps) {
            convert = map.convert(convert)
        }
        return convert
    }

    fun getLowestLocationV2(seeds: List<Interval>, maps: List<ConversionMap>): Long {
        var lowest = Long.MAX_VALUE

        for (seed in seeds) {
            var intervals = listOf(seed)
            for (map in maps) {
                intervals = map.convertIntervals(intervals)
            }

            for (interval in intervals) {
                if (interval.start < lowest) {
                    lowest = interval.start
                }
            }
        }

        return lowest
    }

    fun parseSeedsIntoIntervals(input: List<Long>): List<Interval> {
        val result = mutableListOf<Interval>()
        input.chunked(2).forEach {
            result.add(Interval(it[0], it[0] + it[1] - 1))
        }
        return result
    }

    fun getMaps(input: List<String>): List<ConversionMap> {
        val maps = mutableListOf<ConversionMap>()

        val seedToSoilIdx = getMapIndex("seed-to-soil map:", input)
        maps.add(parseMap(
            input.subList(
                seedToSoilIdx + 1,
                getNextNewLineIndex(input, seedToSoilIdx))))

        val soilToFertilizerIdx = getMapIndex("soil-to-fertilizer map:", input)
        maps.add(parseMap(
            input.subList(
                soilToFertilizerIdx + 1,
                getNextNewLineIndex(input, soilToFertilizerIdx))))

        val fertilizerToWaterIdx = getMapIndex("fertilizer-to-water map:", input)
        maps.add(parseMap(
            input.subList(
                fertilizerToWaterIdx + 1,
                getNextNewLineIndex(input, fertilizerToWaterIdx))))

        val waterToLightIdx = getMapIndex("water-to-light map:", input)
        maps.add(parseMap(
            input.subList(
                waterToLightIdx + 1,
                getNextNewLineIndex(input, waterToLightIdx))))

        val lightToTempIdx = getMapIndex("light-to-temperature map:", input)
        maps.add(parseMap(
            input.subList(
                lightToTempIdx + 1,
                getNextNewLineIndex(input, lightToTempIdx))))

        val tempToHumIdx = getMapIndex("temperature-to-humidity map:", input)
        maps.add(parseMap(
            input.subList(
                tempToHumIdx + 1,
                getNextNewLineIndex(input, tempToHumIdx))))

        val humToLocationIdx = getMapIndex("humidity-to-location map:", input)
        maps.add(parseMap(
            input.subList(
                humToLocationIdx + 1,
                input.size)))

        return maps
    }

    fun part1(input: List<String>): Long {
        val seeds = parseSeeds(input[0])
        val maps = getMaps(input)

        var result = Long.MAX_VALUE
        for (seed in seeds) {
            val location = convertSeedToLocation(seed, maps)
            if (location < result) {
                result = location
            }
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val seedIntervals = parseSeedsIntoIntervals(parseSeeds(input[0]))
        val maps = getMaps(input)

        return getLowestLocationV2(seedIntervals, maps)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
//    println("=== test output ===")
//    part2(testInput).println()

    val input = readInput("Day05")
//    println("=== test output: part1 ===")
//    part1(input).println()
    println("=== test output: part2 ===")
    part2(input).println()
}

