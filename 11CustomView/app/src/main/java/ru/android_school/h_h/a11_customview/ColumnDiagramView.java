package ru.android_school.h_h.a11_customview;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class ColumnDiagramView extends View {

    ArrayList<DateWithValue> listOfDates;
    ArrayList<Integer> listOfValuesForAnimation;
    int maximumValue;

    int viewWidth;
    int viewHeight;

    int dateTextColor;
    int columnTextColor;

    //Различные параметры для размещения всякого
    int divideLineY;
    int dateTextLineY;
    int dateTextSize;
    int columnTextSize;
    int maximumHeight;
    int textVerticalPadding;

    public void setData(ArrayList<DateWithValue> listOfDates) {
        this.listOfDates = listOfDates;
        if (listOfDates != null) {
            if (this.listOfDates.size() > 9) {
                Toast.makeText(getContext(), "Too big size, array will be reduced to 9 last elements",
                        Toast.LENGTH_LONG).show();
                while (listOfDates.size()>9){
                    listOfDates.remove(0);
                }
            }

            maximumValue = 0;
            Collections.sort(this.listOfDates);
            for (DateWithValue d : listOfDates) {
                if (d.value > maximumValue) {
                    maximumValue = d.value;
                }
            }

            listOfValuesForAnimation = new ArrayList<>();
            for (int i = 0; i < listOfDates.size(); i++) {
                listOfValuesForAnimation.add(0);
            }
        } else {
            listOfValuesForAnimation = null;
        }
        startMyAnimation();
    }

    public void updateWithRandomValues() {
        Random random = new Random();
        int sizeOfArray = random.nextInt(11);
        ArrayList<DateWithValue> newData;
        if (sizeOfArray != 0) {
            newData = new ArrayList<>(sizeOfArray);
            for (int i = 0; i < sizeOfArray; i++) {
                Calendar date = Calendar.getInstance();
                int pastDays = random.nextInt(100);
                date.add(Calendar.DAY_OF_YEAR, -pastDays);
                int value = random.nextInt(100);
                newData.add(new DateWithValue(value, date));
            }
            setData(newData);
        }
    }

        public void startMyAnimation(){
            final ValueAnimator columnAnimator = ValueAnimator.ofFloat(0, 1);
            columnAnimator.setDuration(300);
            columnAnimator.setInterpolator(new LinearInterpolator());
            columnAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float percentage = (float) columnAnimator.getAnimatedValue();
                    int listSize = (listOfDates != null) ? listOfDates.size() : 0;
                    for (int i = 0; i < listSize; i++) {
                        int currentValue = listOfValuesForAnimation.get(i);
                        int delta = listOfDates.get(i).value - currentValue;
                        int desiredPosition = (int) (delta * percentage);
                        listOfValuesForAnimation.set(i, desiredPosition);
                    }
                    invalidate();
                }
            });
            columnAnimator.start();
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
                init();
                array.recycle();
            }
        }

        public void init () {
            dateTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
            columnTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, getResources().getDisplayMetrics());
        }

        private int measureDimension ( int desiredSize, int measuredSpec){
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
        protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec){
            Log.v("[ColDiagr] onMeasure w", MeasureSpec.toString(widthMeasureSpec));
            Log.v("[ColDiagr] onMeasure h", MeasureSpec.toString(heightMeasureSpec));

            int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
            int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

            viewWidth = measureDimension(desiredWidth, widthMeasureSpec);
            viewHeight = measureDimension(desiredHeight, heightMeasureSpec);

            int restOfHeight = viewHeight / 10;
            maximumHeight = viewHeight * 8 / 10;
            dateTextSize = restOfHeight * 8 / 10;
            textVerticalPadding = restOfHeight / 10;
            divideLineY = viewHeight * 9 / 10;
            dateTextLineY = divideLineY + dateTextSize + textVerticalPadding;

            setMeasuredDimension(viewWidth, viewHeight);
        }

        @Override
        protected void onDraw (Canvas canvas){
            super.onDraw(canvas);
            if ((listOfDates != null) && (listOfDates.size() != 0)) {
                for (int i = 0; i < listOfDates.size(); i++) {
                    drawColumn(i, canvas);
                }
            } else {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                textPaint.setColor(columnTextColor);
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(dateTextSize * 3);
                String message = "Данных нет";
                canvas.drawText(message, viewWidth / 2, viewHeight / 2, textPaint);
            }
        }

        public void drawColumn ( int position, Canvas canvas){
            int x = viewWidth * (position + 1) / (listOfDates.size() + 1);

            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(dateTextColor);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(dateTextSize);
            canvas.drawText(listOfDates.get(position).getFancyDate(), x, dateTextLineY, textPaint);

            Paint columnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            columnPaint.setColor(columnTextColor);
            columnPaint.setStyle(Paint.Style.FILL);
            int columnHeight = (maximumValue > 0) ? maximumHeight * listOfValuesForAnimation.get(position) / maximumValue : 0;
            int columnWidth = viewWidth / (listOfDates.size() + 1) / 15;
            if (columnWidth < 2) {
                columnWidth = 2;
            }
            Log.d("ColDiagr", "Height of " + position + " column:" + columnHeight);
            Log.d("ColDiagr", "Width of " + position + " column:" + columnWidth);
            RectF column = new RectF(x - columnWidth / 2, divideLineY - columnHeight, x + columnWidth / 2, divideLineY);
            canvas.drawRoundRect(column, 10, 10, columnPaint);

            columnPaint.setTextAlign(Paint.Align.CENTER);
            columnPaint.setTextSize(dateTextSize);
            canvas.drawText(listOfDates.get(position).value + "", x, divideLineY - columnHeight - textVerticalPadding, columnPaint);
        }

        @Override
        public boolean onTouchEvent (MotionEvent event){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                clearAnimation();
                updateWithRandomValues();
                return true;
            } else {
                return false;
            }
        }


    }
