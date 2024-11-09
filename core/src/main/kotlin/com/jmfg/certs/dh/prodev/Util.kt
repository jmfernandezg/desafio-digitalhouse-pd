package com.jmfg.certs.dh.prodev

object Util {
    fun toCapitalizedString(snakeString: String): String {
        return snakeString.lowercase().split('_').joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}