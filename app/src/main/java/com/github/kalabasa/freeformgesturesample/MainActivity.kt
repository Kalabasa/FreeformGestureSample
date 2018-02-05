package com.github.kalabasa.freeformgesturesample

import android.app.Activity
import android.graphics.Matrix
import android.os.Bundle
import com.github.kalabasa.freeformgesturedetector.FreeformGestureDetector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var detector: FreeformGestureDetector

    private lateinit var matrix: Matrix

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        matrix = Matrix()

        detector = FreeformGestureDetector(this, { ev, transform ->
            render_view.transform.postConcat(transform)
            render_view.invalidate()
            true
        })

        render_view.setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
            true
        }
    }
}
