package com.example.findapotty.utils;
// https://stackoverflow.com/questions/24701762/android-spannablecontent-with-rounded-corners

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.util.DisplayMetrics;

public class RoundedBackgroundSpan extends ReplacementSpan {

    private static final int CORNER_RADIUS = 12;

    private final float PADDING_X;
    private final float PADDING_Y;

    private final float MAGIC_NUMBER;

    private int mBackgroundColor;
    private int mTextColor;
    private float mTextSize;
    private Context context;

    /**
     * @param backgroundColor color value, not res id
     * @param textSize        in pixels
     */
    public RoundedBackgroundSpan(Context context, int backgroundColor, int textColor, float textSize) {
        mBackgroundColor = backgroundColor;
        mTextColor = textColor;
        mTextSize = textSize - 10 <= 0 ? 1 : textSize - 10;
        this.context = context;
        PADDING_X = dpToPx(12);
        PADDING_Y = dpToPx(0);
        MAGIC_NUMBER = dpToPx(1);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        paint = new Paint(paint); // make a copy for not editing the referenced paint
        paint.setTextSize(mTextSize);
        // Draw the rounded background
        paint.setColor(mBackgroundColor);
        float textHeightWrapping = dpToPx(4);
        float tagBottom = top + textHeightWrapping + PADDING_Y + mTextSize + PADDING_Y + textHeightWrapping;
        float tagRight = x + getTagWidth(text, start, end, paint);
        RectF rect = new RectF(x, top, tagRight, tagBottom);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        // Draw the text
        paint.setColor(mTextColor);
        canvas.drawText(text, start, end, x + PADDING_X, tagBottom - PADDING_Y - textHeightWrapping - MAGIC_NUMBER, paint);
    }

    private int getTagWidth(CharSequence text, int start, int end, Paint paint) {
        return Math.round(PADDING_X + paint.measureText(text.subSequence(start, end).toString()) + PADDING_X);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint = new Paint(paint); // make a copy for not editing the referenced paint
        paint.setTextSize(mTextSize);
        return getTagWidth(text, start, end, paint);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) Math.floor(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}