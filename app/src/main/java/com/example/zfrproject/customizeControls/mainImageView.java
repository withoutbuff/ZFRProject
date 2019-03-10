package com.example.zfrproject.customizeControls;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


public class mainImageView extends android.support.v7.widget.AppCompatImageView{
    public mainImageView(Context context) {
        super(context);
    }

    public mainImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();

        if(d!=null){
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            //计算使图片充满屏幕
            int height = (int) Math.ceil((float) width
                    * (float) d.getIntrinsicHeight()
                    / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
