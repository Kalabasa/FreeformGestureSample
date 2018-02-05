package com.github.kalabasa.freeformgesturesample

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.github.kalabasa.freeformgesturesample.pattern.AbstractRenderPattern
import com.github.kalabasa.freeformgesturesample.pattern.ImagePattern
import com.github.kalabasa.freeformgesturesample.pattern.SquarePattern

class SampleRendererView : View {
    private lateinit var pattern: AbstractRenderPattern

    var transform
        get() = pattern.transform
        set(transform) {
            pattern.transform = transform
            invalidate();
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
       pattern = ImagePattern(context)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pattern.draw(canvas!!)
    }
}
