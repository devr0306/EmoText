package com.emotext.chatapp.Models.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

public class ArcSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private Paint basePaint;
    private Paint progressPaint;
    private RectF oval = new RectF(5, 5, 550, 550);
    private int startAngle = 90;
    private int defaultMax = 200;
    private int strokeWidth = 10;

    private int trackColor = Color.GRAY;
    private int progressColor = Color.BLUE;

    public ArcSeekBar(Context context){
        super(context);
        init();
    }

    public ArcSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){

        basePaint = new Paint();
        basePaint.setAntiAlias(true);
        basePaint.setColor(trackColor);
        basePaint.setStrokeWidth(strokeWidth);
        basePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        setMax(defaultMax);
    }

    public void setOval(RectF mOval){
        oval = mOval;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setTrackColor(int trackColor) {
        this.trackColor = trackColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(oval, startAngle, getMax(), false, basePaint);
        canvas.drawArc(oval, startAngle, getProgress(), false, progressPaint);
        invalidate();
        //Log.i("ARC", getProgress()+"/"+getMax());

    }
}
