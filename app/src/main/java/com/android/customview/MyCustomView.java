package com.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyCustomView extends View {

    private Paint mpaint;
    private Rect mrect;
    private static int mPadding = 0;

    public MyCustomView(Context context) {
        super(context);
        init(null);
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mrect = new Rect();
        if (set == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.MyCustomView);
        int mSquareColor = ta.getColor(R.styleable.MyCustomView_square_color, Color.GREEN);
        mpaint.setColor(mSquareColor);
        ta.recycle();
    }

    public void swapColor(){
        mpaint.setColor(Color.YELLOW);
        postInvalidate();
    }

    public void customPaddingUp(int padding){
        mPadding = mPadding + padding;
        postInvalidate();
    }

    public void customPaddingDown(int padding){
        mPadding = mPadding -  padding;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mrect.left = mPadding;

        mrect.right = getWidth() - mPadding;
        mrect.top = mPadding;
        mrect.bottom = getHeight() - mPadding;

        canvas.drawRect(mrect, mpaint);
    }
}
