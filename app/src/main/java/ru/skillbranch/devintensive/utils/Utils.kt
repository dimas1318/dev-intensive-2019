package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
//        return Pair(firstName, lastName)
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val map = transliterationMap()
        val stringBuilder = StringBuilder()
        for (letter in payload.trim()) {
            stringBuilder.append(getTranslitSymbol(letter, map))
        }
        return stringBuilder.toString().replace(" ", divider)
    }

    private fun getTranslitSymbol(char: Char, map: Map<String, String>): String {
        val translate = map[char.toLowerCase().toString()] ?: char.toString()
        return if (char.isUpperCase()) {
            translate.capitalize()
        } else {
            translate
        }
    }

    private fun transliterationMap(): Map<String, String> {
        val map: MutableMap<String, String> = mutableMapOf()
        map["а"] = "a"
        map["б"] = "b"
        map["в"] = "v"
        map["г"] = "g"
        map["д"] = "d"
        map["е"] = "e"
        map["ё"] = "e"
        map["ж"] = "zh"
        map["з"] = "z"
        map["и"] = "i"
        map["й"] = "i"
        map["к"] = "k"
        map["л"] = "l"
        map["м"] = "m"
        map["н"] = "n"
        map["о"] = "o"
        map["п"] = "p"
        map["р"] = "r"
        map["с"] = "s"
        map["т"] = "t"
        map["у"] = "u"
        map["ф"] = "f"
        map["х"] = "h"
        map["ц"] = "c"
        map["ч"] = "ch"
        map["ш"] = "sh"
        map["щ"] = "sh'"
        map["ъ"] = ""
        map["ы"] = "i"
        map["ь"] = ""
        map["э"] = "e"
        map["ю"] = "yu"
        map["я"] = "ya"
        return map
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val first = firstName?.trim()?.firstOrNull()?.toUpperCase()
        val second = lastName?.trim()?.firstOrNull()?.toUpperCase()
        return "${first.toString() ?: ""}${second.toString() ?: ""}".ifEmpty { null }
    }
}