package com.example.plank;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImage extends androidx.appcompat.widget.AppCompatImageView
{
    public SquareImage(Context context) {
        super(context);
    }

    public SquareImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

//        width = Math.min(width, height);
//        height = width;

        width = height;

        setMeasuredDimension(width, height);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
