package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import kotlin.math.min
import kotlin.math.roundToInt

private const val TAG = "CircleImageView"

class CircleImageView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(ctx, attrs, defStyleAttr) {

    private var borderWidth = 2.px

    @ColorInt
    var color = Color.WHITE

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        try {
            borderWidth =
                ta.getDimension(R.styleable.CircleImageView_cv_borderWidth, borderWidth.toFloat())
                    .roundToInt()
            color = ta.getColor(R.styleable.CircleImageView_cv_borderColor, color)
        } finally {
            ta.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let { _ ->
            if (width == 0 || height == 0) return
            var bitmap = getBitmapFromDrawable() ?: return

            val smallestSide = min(width, height)

            bitmap = getScaledBitmap(bitmap, smallestSide)
            bitmap = getCenterCroppedBitmap(bitmap, smallestSide)
            bitmap = getCircleBitmap(bitmap)

            if (borderWidth > 0)
                bitmap = getStrokedBitmap(bitmap, borderWidth, color)

            canvas.drawBitmap(bitmap, 0F, 0F, null)
        }
    }

    @Dimension
    fun getBorderWidth(): Int = borderWidth.dp

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp.px
        invalidate()
    }

    fun getBorderColor(): Int {
        return color
    }

    fun setBorderColor(hex: String) {
        color = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        color = ContextCompat.getColor(App.applicationContext(), colorId)
        invalidate()
    }

    private fun getStrokedBitmap(bitmap: Bitmap, strokeWidth: Int, color: Int): Bitmap {
        val inCircle = RectF()
        val strokeStart = strokeWidth / 2F
        val strokeEnd = bitmap.width - strokeWidth / 2F

        inCircle.set(strokeStart, strokeStart, strokeEnd, strokeEnd)

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        strokePaint.color = color
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth.toFloat()

        val canvas = Canvas(bitmap)
        canvas.drawOval(inCircle, strokePaint)

        return bitmap
    }

    private fun getCenterCroppedBitmap(bitmap: Bitmap, size: Int): Bitmap {
        val cropStartX = (bitmap.width - size) / 2
        val cropStartY = (bitmap.height - size) / 2

        return Bitmap.createBitmap(bitmap, cropStartX, cropStartY, size, size)
    }

    private fun getScaledBitmap(bitmap: Bitmap, minSide: Int): Bitmap {
        return if (bitmap.width != minSide || bitmap.height != minSide) {
            val smallest = min(bitmap.width, bitmap.height).toFloat()
            val factor = smallest / minSide
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width / factor).toInt(),
                (bitmap.height / factor).toInt(),
                false
            )
        } else bitmap
    }

    private fun getBitmapFromDrawable(): Bitmap? {
        if (drawable == null)
            return null

        if (drawable is BitmapDrawable)
            return (drawable as BitmapDrawable).bitmap

        return drawable.toBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val smallest = min(bitmap.width, bitmap.height)
        val outputBmp = Bitmap.createBitmap(smallest, smallest, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBmp)

        val paint = Paint()

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawCircle(smallest / 2F, smallest / 2F, smallest / 2F, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0F, 0F, paint)

        return outputBmp
    }

    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).roundToInt()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

}