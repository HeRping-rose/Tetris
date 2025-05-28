package com.ronnie.tetris;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimplePatternLockView extends GridLayout {

    private static final int COUNT = 3; // 3x3
    private ImageView[][] nodes = new ImageView[COUNT][COUNT];
    private boolean[][] selected = new boolean[COUNT][COUNT];
    private List<Point> pattern = new ArrayList<>();

    public SimplePatternLockView(Context context) {
        super(context);
        init(context);
    }

    public SimplePatternLockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setRowCount(COUNT);
        setColumnCount(COUNT);

        int size = dpToPx(context, 80); // 每个图标大小

        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < COUNT; j++) {
                ImageView iv = new ImageView(context);
                iv.setImageResource(R.drawable.dot_bg);
                LayoutParams params = new LayoutParams();
                params.width = size;
                params.height = size;
                params.rowSpec = spec(i);
                params.columnSpec = spec(j);
                addView(iv, params);
                nodes[i][j] = iv;
                selected[i][j] = false;
            }
        }

        setBackgroundColor(Color.TRANSPARENT);
    }

    private int dpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private Point getTouchedCell(float x, float y) {
        int cellWidth = getWidth() / COUNT;
        int cellHeight = getHeight() / COUNT;
        int col = (int) (x / cellWidth);
        int row = (int) (y / cellHeight);
        if (col >= 0 && col < COUNT && row >= 0 && row < COUNT) {
            return new Point(row, col);
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point point;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                point = getTouchedCell(event.getX(), event.getY());
                if (point != null && !selected[point.x][point.y]) {
                    nodes[point.x][point.y].setImageResource(R.drawable.dot_select);
                    selected[point.x][point.y] = true;
                    pattern.add(point);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pattern.size() < 4) {
                    // 不合格，重置
                    resetPattern();
                } else {
                    // TODO：处理已完成的图案
                }
                break;
        }
        return true;
    }

    private void resetPattern() {
        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < COUNT; j++) {
                selected[i][j] = false;
                nodes[i][j].setImageResource(R.drawable.dot_bg);
            }
        }
        pattern.clear();
    }

    public List<Point> getPattern() {
        return pattern;
    }
}
