package com.emotext.chatapp.Models.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Timer;

public class FocusCircle extends View {

    private Paint pen;
    private Paint drawPen;

    private float x, y;
    Timer timer;

    private Canvas canvas;

    public FocusCircle(Context context) {
        super(context);

        timer = new Timer();

        pen = new Paint();
        pen.setColor(Color.LTGRAY);
        pen.setAlpha(80);
        pen.setStrokeWidth(8F);
        pen.setStyle(Paint.Style.FILL);

        drawPen = new Paint();
        drawPen.setColor(Color.WHITE);
        drawPen.setStrokeWidth(5f);
        drawPen.setStyle(Paint.Style.STROKE);
    }

    public void setXY(float xPos, float yPos){

        x = xPos;
        y = yPos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.canvas = canvas;
        canvas.drawCircle(x, y, 70f, pen);
        canvas.drawCircle(x, y, 74f, drawPen);
    }
}
