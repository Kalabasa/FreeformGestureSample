package com.github.kalabasa.freeformgesturesample.pattern

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix

abstract class AbstractRenderPattern(val context: Context) {
    var transform = Matrix()
    abstract fun draw(canvas: Canvas)
}
