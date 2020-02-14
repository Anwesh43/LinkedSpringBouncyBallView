package com.anwesh.uiprojects.springbouncyballview

/**
 * Created by anweshmishra on 14/02/20.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.content.Context
import android.app.Activity

val nodes : Int = 5
val parts : Int = 2
val scGap : Float = 0.02f / parts
val strokeFactor : Int = 90
val foreColor : Int = Color.parseColor("#3F51B5")
val backColor : Int = Color.parseColor("#BDBDBD")
val springSizeFactor : Float = 7f
val springXFactor : Float = 5f
val ballSizeFactor : Float = 10f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawSpringMotion(springSize : Float, springCurrX : Float, paint : Paint) {
    save()
    drawLine(0f, 0f, springCurrX, 0f, paint)
    translate(springCurrX, 0F)
    drawLine(0f, 0f, 0f, -springSize, paint)
    restore()
}

fun Canvas.drawBallMotion(ballCurrX : Float, ballR : Float, springX : Float, paint : Paint) {
    save()
    translate(springX, 0f)
    drawCircle(ballCurrX, 0f, ballR, paint)
    restore()
}

fun Canvas.drawBouncySpringBall(w : Float, scale : Float, paint : Paint) {
    val springSize : Float = w / springSizeFactor
    val springX : Float = w / springXFactor
    val ballR : Float = w / ballSizeFactor
    val sf : Float = scale.sinify()
    val sf1 : Float = sf.divideScale(0, 2)
    val sf2 : Float = sf.divideScale(1, 2)
    val ballX : Float = w - springX - 2 * ballR
    val springCurrX : Float = springX * sf1
    val ballCurrX : Float = ballX * sf2
    save()
    drawSpringMotion(springSize, springCurrX, paint)
    drawBallMotion(ballCurrX, ballR, springX, paint)
    restore()
}

fun Canvas.drawSBBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    paint.color = foreColor
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    save()
    translate(gap * (i + 1), h / 2)
    drawBouncySpringBall(w, scale, paint)
    restore()
}

class SpringBouncyBallView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}
