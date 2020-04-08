package ru.skillbranch.devintensive.utils

import android.content.Context
import android.graphics.*
import androidx.annotation.ColorInt
import kotlin.math.min

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts = fullName?.trim()?.split(" ")
        val firstName = parts?.getOrNull(0)?.ifEmpty { null }
        val lastName = parts?.getOrNull(1)?.ifEmpty { null }

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val map = fillTranslitMap()
        val builder = StringBuilder()

        for (char in payload.trim())
            builder.append(getTranslChar(char, map))

        return builder.toString().replace(" ", divider)
    }

    private fun getTranslChar(char: Char, map: HashMap<Char, String>): String {
        val transl = map[char.toLowerCase()] ?: char.toString()

        return if (char.isUpperCase() && transl.isNotEmpty())
            transl.capitalize()
        else transl
    }

    private fun fillTranslitMap(): HashMap<Char, String> {
        val map = hashMapOf<Char, String>()
        map['а'] = "a"
        map['б'] = "b"
        map['в'] = "v"
        map['г'] = "g"
        map['д'] = "d"
        map['е'] = "e"
        map['ё'] = "e"
        map['ж'] = "zh"
        map['з'] = "z"
        map['и'] = "i"
        map['й'] = "i"
        map['к'] = "k"
        map['л'] = "l"
        map['м'] = "m"
        map['н'] = "n"
        map['о'] = "o"
        map['п'] = "p"
        map['р'] = "r"
        map['с'] = "s"
        map['т'] = "t"
        map['у'] = "u"
        map['ф'] = "f"
        map['х'] = "h"
        map['ц'] = "c"
        map['ч'] = "ch"
        map['ш'] = "sh"
        map['щ'] = "sh'"
        map['ъ'] = ""
        map['ы'] = "i"
        map['ь'] = ""
        map['э'] = "e"
        map['ю'] = "yu"
        map['я'] = "ya"

        return map
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val name = firstName.orEmpty().trim().getOrNull(0)?.toUpperCase()
        val surname = lastName.orEmpty().trim().getOrNull(0)?.toUpperCase()
        val firstInit = name?.toString() ?: ""
        val secondInit = surname?.toString() ?: ""
        return "$firstInit$secondInit".ifEmpty { null }
    }

    fun textBitmap(
        width: Int,
        height: Int,
        text: String,
        @ColorInt bgColor: Int = Color.BLACK,
        textSize: Int = (min(width, height) * 0.6f).toInt(),
        @ColorInt textColor: Int = Color.WHITE
    ): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawColor(bgColor)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize.toFloat()
        paint.color = textColor
        paint.textAlign = Paint.Align.CENTER

        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)

        val backgroundBounds = RectF()
        backgroundBounds.set(0f, 0f, width.toFloat(), height.toFloat())

        val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()
        canvas.drawText(text, backgroundBounds.centerX(), textBottom, paint)

        return bitmap
    }

    fun isValidateRepository(repo: String): Boolean = repo.isEmpty() || repo.matches(
        Regex(
            "^(https://){0,1}(www.){0,1}github.com\\/[A-z\\d](?:[A-z\\d]|(_|-)(?=[A-z\\d])){0,256}(/)?\$",
            RegexOption.IGNORE_CASE
        )
    ) &&
            !repo.matches(
                Regex(
                    "^.*(" +
                            "\\/enterprise|" +
                            "\\/features|" +
                            "\\/topics|" +
                            "\\/collections|" +
                            "\\/trending|" +
                            "\\/events|" +
                            "\\/marketplace" +
                            "|\\/pricing|" +
                            "\\/nonprofit|" +
                            "\\/customer-stories|" +
                            "\\/security|" +
                            "\\/login|" +
                            "\\/join)\$", RegexOption.IGNORE_CASE
                )
            )

    fun convertSpToPx(context: Context, sp: Int): Int {
        return sp * context.resources.displayMetrics.scaledDensity.toInt()
    }

    fun convertDpToPx(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun convertPxToDp(context: Context, px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }
}