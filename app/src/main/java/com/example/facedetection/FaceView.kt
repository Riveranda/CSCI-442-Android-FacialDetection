package com.example.facedetection

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.face.Face

class FaceView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var bitMap: Bitmap? = null
    private var faces: MutableList<Face>? = null


    fun setContent(bitmap: Bitmap, faces: MutableList<Face>) {
        bitMap = bitmap
        this.faces = faces
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (bitMap != null && faces != null) {
            val scale = canvas?.let { drawBitmap(it) }
            if (canvas != null && scale != null) {
                drawFaces(canvas, scale.toDouble())
            }
        }
    }

    private fun drawBitmap(canvas: Canvas): Int {
        val viewWidth = canvas.width
        val viewHeight = canvas.height
        val imageWidth = bitMap?.width
        val imageHeight = bitMap?.height
        val scale = (viewWidth / imageWidth!!).coerceAtMost(viewHeight / imageHeight!!)
        val destBounds = Rect(0, 0, (imageWidth * scale), (imageHeight * scale))
        bitMap?.let { canvas.drawBitmap(it, null, destBounds, null) }
        return scale
    }

    private fun drawFaces(canvas: Canvas, scale: Double) {
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        faces?.forEach { face ->
            run {
                val bounds = face.boundingBox
                val left = bounds.left * scale
                val right = bounds.right * scale
                val top = bounds.top * scale
                val bottom = bounds.bottom * scale
                bounds.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
                canvas.drawRect(bounds, paint)
            }
        }
    }


}