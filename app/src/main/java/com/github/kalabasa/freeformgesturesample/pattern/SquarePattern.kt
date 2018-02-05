package com.github.kalabasa.freeformgesturesample.pattern

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class SquarePattern : AbstractRenderPattern {
    val bgPaint = Paint()
    val fgPaint = Paint()

    constructor(context: Context) : super(context) {
        bgPaint.color = 0xff2C333B.toInt()
        fgPaint.color = 0xffE5DDA0.toInt()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPaint(bgPaint)
        canvas.matrix = transform
        canvas.translate(canvas.width / 2f, canvas.height / 2f)
        val r = Math.min(canvas.width, canvas.height) / 4f;
        canvas.drawRect(-r, -r, r, r, fgPaint)
    }
}
