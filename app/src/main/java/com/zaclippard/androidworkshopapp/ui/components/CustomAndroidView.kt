package com.zaclippard.androidworkshopapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.zaclippard.androidworkshopapp.R

class CustomAndroidView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val customTextView: TextView
    private val paint = Paint()

    fun setText(text: String) {
        customTextView.text = text
    }

    init {
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.custom_android_view, this, true)

        customTextView = findViewById(R.id.custom_text_view)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, 50f, paint)
    }

}
