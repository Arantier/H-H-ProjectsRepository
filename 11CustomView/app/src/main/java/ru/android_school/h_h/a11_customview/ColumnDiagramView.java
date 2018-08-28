package ru.android_school.h_h.a11_customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ColumnDiagramView extends View {

    ArrayList<DateWithValue> listOfDates;
    ArrayList<DateWithValue> listOfSavedDates;
    int maximumValue;

    int viewWidth;
    int viewHeight;
    int spacing;

    int dateTextColor;
    int columnTextColor;

    //Различные параметры для размещения всякого
    int divideLineY;
    int dateTextLineY;
    int textSize;
    int columnTextSize;
    int maximumHeight;
    int textVerticalPadding;

    public void setData(ArrayList<DateWithValue> listOfDates) {
        this.listOfDates = listOfDates;
        //Будем исходить из того, что значения положительны
        maximumValue = 0;
        if (listOfDates!=null) {
            if (this.listOfDates.size()>9){
                Log.d("DiagramData","Too big size, array will be reduced to 9 last elements");
                for (int i=0;i<listOfDates.size()-9;i++){
                    this.listOfDates.remove(i);
                }
            }
            Collections.sort(this.listOfDates);
            for (DateWithValue d : listOfDates) {
                if (d.value > maximumValue) {
                    maximumValue = d.value;
                }
            }
            listOfSavedDates = (ArrayList<DateWithValue>) listOfDates.clone();
        }
        invalidate();
        requestLayout();
    }

    public void startMyAnimation() {
        for (int i=0;i<listOfDates.size();i++){
            animateColumn(i);
        }
    }

    public ColumnDiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColumnDiagramView,
                0, 0
        );
        try {
            dateTextColor = array.getColor(R.styleable.ColumnDiagramView_dateTextColor, Color.BLACK);
            columnTextColor = array.getColor(R.styleable.ColumnDiagramView_columnColor, Color.BLACK);
        } finally {
            array.recycle();
        }
    }

    private int measureDimension(int desiredSize, int measuredSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measuredSpec);
        int specSize = MeasureSpec.getSize(measuredSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        if (result < desiredSize) {
            Log.e("ChartView", "The view is too small, the content might get cut");
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v("[ColDiagr] onMeasure w", MeasureSpec.toString(widthMeasureSpec));
        Log.v("[ColDiagr] onMeasure h", MeasureSpec.toString(heightMeasureSpec));

        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

        viewWidth = measureDimension(desiredWidth, widthMeasureSpec);
        viewHeight = measureDimension(desiredHeight, heightMeasureSpec);

        int restOfHeight = viewHeight/10;
        maximumHeight = viewHeight*8/10;
        textSize = restOfHeight*8/10;
        textVerticalPadding = restOfHeight/10;
        divideLineY = viewHeight*9/10;
        dateTextLineY = divideLineY+ textSize +textVerticalPadding;

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (listOfDates!=null){
            for (int i=0;i<listOfDates.size();i++){
                drawColumn(i,canvas);
            }
        }
    }

    public void drawColumn(int position, Canvas canvas){
        int x = viewWidth*(position+1)/(listOfDates.size()+1);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(dateTextColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        canvas.drawText(listOfDates.get(position).getFancyDate(),x,dateTextLineY,textPaint);
        Paint columnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        columnPaint.setColor(columnTextColor);
        columnPaint.setStyle(Paint.Style.FILL);
        int columnHeight = maximumHeight*listOfDates.get(position).value/maximumValue;
        int columnWidth = viewWidth/(listOfDates.size()+1)/15;
        Log.d("ColDiagr","Height of "+position+" column:"+columnHeight);
        Log.d("ColDiagr","Width of "+position+" column:"+columnWidth);
        RectF column = new RectF(x-columnWidth/2,divideLineY-columnHeight,x+columnWidth/2,divideLineY);
        canvas.drawRoundRect(column,10,10,columnPaint);
        columnPaint.setTextAlign(Paint.Align.CENTER);
        columnPaint.setTextSize(textSize);
        canvas.drawText(listOfDates.get(position).value+"",x,divideLineY-columnHeight-textVerticalPadding,columnPaint);
    }

    public void animateColumn(int position){
        ValueAnimator animator = ValueAnimator.ofInt(listOfSavedDates.get(position).value,listOfDates.get(position).value);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.d("Animation",valueAnimator.getAnimatedValue()+"");
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMyAnimation();
        return true;
    }
}
