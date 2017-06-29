package com.example.kotlinhorizontal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.TintTypedArray
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.w3c.dom.Text

/**
 * Created by dubo on 2017/6/25.
 * author:dubo
 * e-mail:dubo_java@163.com
 * time:2017/06/25
 * desc:
 */

class HorizontalScrollView @JvmOverloads constructor(private var context1: Context, var attrs: AttributeSet? = null, var defStyleAttr: Int = 0) : View(context1, attrs, defStyleAttr) {

    var rowCount: Int = 5
    var numRow: Int = 0
    var selectSize: Float = 0f
    var noSelectSize: Float = 0.toFloat()
    var selecColor: Int = 0
    var noSelectColor: Int = 0
    var selectPaint: Paint? = null
    var noSelectPaint: Paint? = null
    var list: List<String> = ArrayList<String>()
    var rect: Rect = Rect()
    var everyWidth: Int = 0
    var width1: Int = 0
    var height1: Int = 0
    var startX: Float = 0f
    var anOffset: Float = 0.toFloat()
    var selectNum: SelectList? = null

    init {
        setWillNotDraw(false)
        isClickable = true
        initAttrs(attrs)//属性
        initPaint()//画笔
    }

    private fun initPaint() {
        selectPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        selectPaint!!.textSize = selectSize
        selectPaint!!.color = selecColor
        noSelectPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        noSelectPaint!!.textSize = noSelectSize
        noSelectPaint!!.color = noSelectColor
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val attr = TintTypedArray.obtainStyledAttributes(context1, attrs, R.styleable.HorizontalScrollView)
        selectSize = attr.getFloat(R.styleable.HorizontalScrollView_selectSize, 16f)
        noSelectSize = attr.getFloat(R.styleable.HorizontalScrollView_noSelectSize, 16f)
        selecColor = attr.getColor(R.styleable.HorizontalScrollView_selectColor, context1.resources.getColor(android.R.color.black))
        noSelectColor = attr.getColor(R.styleable.HorizontalScrollView_noSelectColor, context1.resources.getColor(android.R.color.darker_gray))
        rowCount = attr.getInteger(R.styleable.HorizontalScrollView_integerMode, 5)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        everyWidth = width / rowCount
        if (numRow >= 0 && numRow <= list.size) {
            val text = list.get(numRow)
            selectPaint!!.getTextBounds(text, 0, text.length, rect)
            val rectWidth = rect.width()
            val rectHeight = rect.height()
            canvas!!.drawText(list.get(numRow), width / 2 - rectWidth / 2 + anOffset, (height / 2 + rectHeight / 2).toFloat(), selectPaint)
            for (i in list.indices) {
                if (numRow >= 0 && numRow <= list.size) {
                    noSelectPaint!!.getTextBounds(list.get(numRow), 0, list.get(numRow).length, rect)
                    width1 = rect.width()
                    height1 = rect.height()
                }
                if (i != numRow) {
                    canvas.drawText(list.get(i), (i - numRow) * everyWidth + width / 2 - width1 / 2 + anOffset, (height / 2 + height1 / 2).toFloat(), noSelectPaint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> startX = event.x
            MotionEvent.ACTION_MOVE -> {
                val scrollX = event.x
                if (numRow != 0 && numRow != list.size - 1) {
                    anOffset = scrollX - startX
                } else {
                    anOffset = ((scrollX - startX) / 1.5).toFloat() // 添加阻力
                }
                if (scrollX > startX) {
                    if (scrollX - startX >= everyWidth) {
                        if (numRow > 0) {
                            anOffset = 0f
                            numRow = numRow - 1
                            startX = scrollX
                        }
                    }
                } else {
                    if (startX - scrollX >= everyWidth) {
                        if (numRow < list.size - 1) {
                            anOffset = 0f
                            numRow = numRow + 1
                            startX = scrollX
                        }
                    }
                }
                //selectNum!!.select(list.get(numRow))
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                anOffset = 0f
                invalidate()
            }
            else -> {

            }
        }
        return super.onTouchEvent(event)
    }

    fun dates(list: List<String>) {
        this.list = list
        numRow = list.size / 2
        invalidate()
    }

    interface SelectList {
        fun select(text: String)
    }

    fun onSelectList(listener: SelectList) {
        this.selectNum = listener
    }
}
