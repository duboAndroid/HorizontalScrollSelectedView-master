package com.example.horizontalscrollselectedview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/21.
 */

public class HorizontalSelectedView extends View {
    private Context context;
    private boolean isFirst = true;
    private int num;
    private List<String> list = new ArrayList<>();
    private int visibleSize = 5;
    private int noSelectColor;
    private float noSelectSize;
    private int selectColor;
    private float selectSize;
    private Paint selectPain;
    private TextPaint noSelctPain;
    private Rect rect = new Rect();
    private float anOffset;
    private int count;
    private float startX;
    private int width;
    private int height;
    private int textWidth;
    private int textHeight;
    private SelectList selects;
    private String Tag = HorizontalSelectedView.class.getSimpleName();

    public HorizontalSelectedView(Context context) {
        this(context, null);
    }

    public HorizontalSelectedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalSelectedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWillNotDraw(false);
        setClickable(true);
        initAttrs(attrs);//布局属性
        initPaint();//画笔
    }

    private void initPaint() {
        selectPain = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        selectPain.setTextSize(selectSize);
        selectPain.setColor(selectColor);
        noSelctPain = new TextPaint();
        noSelctPain.setColor(noSelectColor);
        noSelctPain.setTextSize(noSelectSize);
    }

    private void initAttrs(AttributeSet attrs) {
        TintTypedArray tintType = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.HorizontalSelectedView);
        visibleSize = tintType.getInteger(R.styleable.HorizontalSelectedView_integerMode, 0);
        noSelectColor = tintType.getColor(R.styleable.HorizontalSelectedView_noSelectColor, context.getResources().getColor(R.color.colorAccent));
        noSelectSize = tintType.getFloat(R.styleable.HorizontalSelectedView_noSelectSize, 0);
        selectColor = tintType.getColor(R.styleable.HorizontalSelectedView_selectColor, context.getResources().getColor(R.color.colorPrimary));
        selectSize = tintType.getFloat(R.styleable.HorizontalSelectedView_selectSize, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {//第一次绘制的时候得到控件 宽高；
            width = getWidth();
            height = getHeight();
            count = width / visibleSize;
            isFirst = false;
        }
        if (num >= 0 && num <= list.size() - 1) {
            String text = list.get(num);//得到选中的文字
            selectPain.getTextBounds(text, 0, text.length(), rect);//画出文字
            int rectWidth = rect.width();//得到矩形的宽高
            int rectHeight = rect.height();
            canvas.drawText(list.get(num), getWidth() / 2 - rectWidth / 2 + anOffset, getHeight() / 2 + rectHeight / 2, selectPain);//把矩形画进去
            for (int i = 0; i < list.size(); i++) {
                if (num > 0 && num < list.size() - 1) {
                    noSelctPain.getTextBounds(list.get(num - 1), 0, list.get(num - 1).length(), rect);
                    int width = rect.width();
                    noSelctPain.getTextBounds(list.get(num + 1), 0, list.get(num + 1).length(), rect);
                    int width1 = rect.width();
                    textWidth = (width + width1) / 2;
                }
                if (i == 0) {
                    noSelctPain.getTextBounds(list.get(0), 0, list.get(0).length(), rect);
                    textHeight = rect.height();
                }
                if (i != num) {
                    canvas.drawText(list.get(i), (i - num) * count + getWidth() / 2 - textWidth / 2 + anOffset, getHeight() / 2 + textHeight / 2, noSelctPain);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float scrollX = ev.getX();
                if (num != 0 && num != list.size() - 1) {
                    anOffset = scrollX - startX;
                } else {
                    anOffset = (float) ((scrollX - startX) / 1.5);//添加阻力
                }
                if (scrollX > startX) {//滑动的距离大于按下的距离
                    if (scrollX - startX >= count) {//向左滑动
                        if (num > 0) {
                            anOffset = 0;
                            num = num - 1;
                            startX = scrollX;
                        }
                    }
                } else {
                    if (startX - scrollX >= count) {//向右滑动
                        if (num < list.size() - 1) {
                            anOffset = 0;
                            num = num + 1;
                            startX = scrollX;
                        }
                    }
                }
                selects.select(list.get(num));
                invalidate();//重新绘制
                break;
            case MotionEvent.ACTION_UP:
                anOffset = 0;
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void seeSize(int seeSizes) {
        if (visibleSize > seeSizes) {
            visibleSize = seeSizes;
            invalidate();
        }
    }

    public void scroRight() {
        if (num > 0) {
            num = num - 1;
            invalidate();
        }
    }

    public void scroLeft() {
        if (num < list.size() - 1) {
            num = num + 1;
            invalidate();
        }
    }

    public void dates(List<String> list) {
        this.list = list;
        num = list.size() / 2;
        invalidate();
    }

    public String selectString() {
        if (list.size() != 0) {
            return list.get(num);
        }
        return null;
    }

    public interface SelectList {
        void select(String text);
    }

    public void OnSelectList(SelectList select) {
        this.selects = select;
    }
}