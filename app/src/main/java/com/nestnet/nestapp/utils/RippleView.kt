import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.nestnet.nestapp.R

class RippleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var rippleColor: Int = ContextCompat.getColor(context, R.color.rippleColor)
    private var rippleRadius: Float = 0f
    private var rippleAlpha: Int = 90
    private var rippleDuration: Int = 400
    private var rippleLineWidth: Float = 0f

    private var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = rippleColor
        alpha = rippleAlpha
        strokeWidth = rippleLineWidth
    }

    private var currentRippleBounds: RectF = RectF()

    private val animator = ValueAnimator().apply {
        interpolator = DecelerateInterpolator()
        addUpdateListener { animation ->
            rippleRadius = animation.animatedValue as Float
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            startRippleAnimation(event.x, event.y)
        }
        return super.onTouchEvent(event)
    }

    private fun startRippleAnimation(x: Float, y: Float) {
        val startRadius = 0f
        val endRadius = width.coerceAtLeast(height) * 1.5f

        animator.setFloatValues(startRadius, endRadius)
        animator.duration = rippleDuration.toLong()
        animator.start()

        currentRippleBounds.apply {
            left = x - endRadius
            top = y - endRadius
            right = x + endRadius
            bottom = y + endRadius
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, rippleRadius, paint)
    }
}
