package com.github.kalabasa.freeformgesturesample.pattern

import android.content.Context
import android.graphics.*
import com.github.kalabasa.freeformgesturesample.R

class ImagePattern : AbstractRenderPattern {
    val bgPaint = Paint()
    val bitmap: Bitmap

    constructor(context: Context) : super(context) {
        bgPaint.color = 0xff292324.toInt()
        val opts = BitmapFactory.Options()
        opts.inDensity = 1000
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.android, opts)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPaint(bgPaint)
        canvas.matrix = transform
        canvas.translate(canvas.width / 2f, canvas.height / 2f)
        val r = Math.min(canvas.width, canvas.height) / 4f;
        canvas.drawBitmap(bitmap, -bitmap.width / 2f, -bitmap.height / 2f, null)
    }
}
