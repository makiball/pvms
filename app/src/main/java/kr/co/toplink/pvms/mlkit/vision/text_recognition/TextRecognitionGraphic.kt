package kr.co.toplink.pvms.mlkit.vision.text_recognition

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.google.mlkit.vision.text.Text
import kr.co.toplink.pvms.camerax.GraphicOverlay

class TextRecognitionGraphic(
    overlay: GraphicOverlay,
    private val element: Text.TextBlock,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private val rectPaint = Paint().apply {
        color = TEXT_COLOR
        style = Paint.Style.FILL
        alpha = 50
    }

    private val textPaint = Paint().apply {
        color = TEXT_COLOR
        textSize = TEXT_SIZE
    }

    override fun draw(canvas: Canvas?) {
        element.boundingBox?.let { box ->
            val rect = calculateRect(imageRect.height().toFloat(), imageRect.width().toFloat(), box)
            canvas?.drawRoundRect(rect, ROUND_RECT_CORNER, ROUND_RECT_CORNER, rectPaint)
            canvas?.drawText(element.text, rect.left, rect.bottom, textPaint)
        }
    }

    companion object {
        private const val TEXT_COLOR = Color.RED
        private const val TEXT_SIZE = 54.0f
        private const val ROUND_RECT_CORNER = 8F
    }
}