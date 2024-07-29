package shopping.common.util

fun String.toSnakeCase(): String =
    this.replace(Regex("([a-z])([A-Z]+)")) {
        it.groupValues[1] + "_" + it.groupValues[2]
    }
