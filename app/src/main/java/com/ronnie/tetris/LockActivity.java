package com.ronnie.tetris;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class LockActivity extends AppCompatActivity {
    FrameLayout rootLayout;

    private List<ImageView> lineViews = new ArrayList<>();
    private List<ImageView> dotViews = new ArrayList<>();
    private List<Integer> selectedIndices = new ArrayList<>();
    private List<float[]> selectedPoints = new ArrayList<>();
    private boolean isDrawing = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // 隐藏状态栏（沉浸式全屏）
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT); // 设置状态栏透明


        rootLayout = new FrameLayout(this) {
            @Override
            public boolean performClick() {
                return super.performClick();
            }
        };
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_black));


        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.setPadding(50, 100, 50, 50);
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        TextView title = new TextView(this);
        title.setText("欢迎注册");
        title.setTextSize(26);
        title.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("所有的结果都是从一个决定开始");
        subtitle.setTextSize(14);
        subtitle.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 10, 0, 30);

        TextView hint = new TextView(this);
        hint.setText("请绘制图案密码");
        hint.setTextSize(18);
        hint.setTextColor(Color.parseColor("#447BFE"));
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(0, 10, 0, 30);

        mainLayout.addView(title);
        mainLayout.addView(subtitle);
        mainLayout.addView(hint);

        LinearLayout patternLayout = new LinearLayout(this);
        patternLayout.setOrientation(LinearLayout.VERTICAL);
        patternLayout.setGravity(Gravity.CENTER);

        for (int row = 0; row < 3; row++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER);

            for (int col = 0; col < 3; col++) {
                ImageView dot = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
                params.setMargins(30, 30, 30, 30);
                dot.setLayoutParams(params);
                dot.setImageResource(R.drawable.dot_bg);
                dotViews.add(dot);
                rowLayout.addView(dot);
            }
            patternLayout.addView(rowLayout);
        }

        View topSpacer = new View(this);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        topSpacer.setLayoutParams(spacerParams);
        mainLayout.addView(topSpacer);
        mainLayout.addView(patternLayout);
        View bottomSpacer = new View(this);
        bottomSpacer.setLayoutParams(spacerParams);
        mainLayout.addView(bottomSpacer);

        rootLayout.setOnTouchListener((v, event) -> {
            float touchX = event.getRawX();
            float touchY = event.getRawY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    selectedIndices.clear();
                    selectedPoints.clear();
                    clearLines();
                    isDrawing = true;
                    checkTouch(touchX, touchY);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (isDrawing) {
                        checkTouch(touchX, touchY);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    isDrawing = false;
                    showPattern();
                    return true;
            }
            return false;
        });

        rootLayout.addView(mainLayout);
        setContentView(rootLayout);
    }

    private void checkTouch(float x, float y) {
        for (int i = 0; i < dotViews.size(); i++) {
            ImageView dot = dotViews.get(i);

            int[] loc = new int[2];
            dot.getLocationInWindow(loc); // ✅ 用 getLocationInWindow
            float centerX = loc[0] + dot.getWidth() / 2f;
            float centerY = loc[1] + dot.getHeight() / 2f;
            float radius = dot.getWidth() / 2f;

            if (Math.hypot(centerX - x, centerY - y) < radius) {
                if (!selectedIndices.contains(i)) {
                    selectedIndices.add(i);
                    selectedPoints.add(new float[]{centerX, centerY}); // ✅ 收集中心点
                    dot.setImageResource(R.drawable.dot_select);

                    if (selectedPoints.size() > 1) {
                        float[] prev = selectedPoints.get(selectedPoints.size() - 2);
                        addLineBetweenPoints(prev[0], prev[1], centerX, centerY);
                    }
                }
            }
        }
    }


    private void showPattern() {
        StringBuilder sb = new StringBuilder();
        for (int index : selectedIndices) {
            sb.append(index);
        }
        Toast.makeText(this, "图案为：" + sb.toString(), Toast.LENGTH_SHORT).show();

        for (ImageView dot : dotViews) {
            dot.setImageResource(R.drawable.dot_bg);
        }
    }

    private void clearLines() {
        for (ImageView line : lineViews) {
            rootLayout.removeView(line);
        }
        lineViews.clear();
    }

    private void addLineBetweenPoints(float x1, float y1, float x2, float y2) {
        ImageView line = new ImageView(this);
        line.setImageResource(R.drawable.line_horizontal_normal);

        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        double length = Math.hypot(deltaX, deltaY);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) length, 20);
        params.leftMargin = (int) x1;
        params.topMargin = (int) y1;
        line.setLayoutParams(params);

        line.setPivotX(0);
        line.setPivotY(0);
        line.setRotation((float) angle);

        rootLayout.addView(line);
        lineViews.add(line);
    }
    //获取状态栏高度方法
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}

//package com.ronnie.tetris;
//
//import android.annotation.SuppressLint;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LockActivity extends AppCompatActivity {
//    FrameLayout rootLayout;
//
//    private List<ImageView> lineViews = new ArrayList<>();
//    private List<ImageView> dotViews = new ArrayList<>();
//    private List<Integer> selectedIndices = new ArrayList<>();
//    private List<float[]> selectedPoints = new ArrayList<>();
//    private boolean isDrawing = false;
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //  创建根布局 FrameLayout（用于触摸监听）
//        rootLayout = new FrameLayout(this) {
//            @Override
//            public boolean performClick() {
//                return super.performClick();
//            }
//        };
//        rootLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.bg_black));
//
//        //  创建一个垂直布局，放文本 + 图案
//        LinearLayout mainLayout = new LinearLayout(this);
//        mainLayout.setOrientation(LinearLayout.VERTICAL);
//        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
//        mainLayout.setPadding(50, 100, 50, 50);
//        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT
//        ));
//
//        //  添加文本区域（标题、副标题、提示）
//        TextView title = new TextView(this);
//        title.setText("欢迎注册");
//        title.setTextSize(26);
//        title.setTextColor(ContextCompat.getColor(this, android.R.color.white));
//        title.setGravity(Gravity.CENTER);
//
//        TextView subtitle = new TextView(this);
//        subtitle.setText("所有的结果都是从一个决定开始");
//        subtitle.setTextSize(14);
//        subtitle.setTextColor(ContextCompat.getColor(this,R.color.text_gray));
//        subtitle.setGravity(Gravity.CENTER);
//        subtitle.setPadding(0, 10, 0, 30);
//
//        TextView hint = new TextView(this);
//        hint.setText("请绘制图案密码");
//        hint.setTextSize(18);
////        hint.setTextColor(Color.parseColor("#447BFE"));
//        hint.setTextColor(Color.parseColor("#447BFE"));
//        hint.setGravity(Gravity.CENTER);
//        hint.setPadding(0, 10, 0, 30);
//
//
//
//        //  添加这些文本到 mainLayout
//        mainLayout.addView(title);
//        mainLayout.addView(subtitle);
//        mainLayout.addView(hint);
//
//
//
//        //  创建图案区域（3x3 的点）
//        LinearLayout patternLayout = new LinearLayout(this);
//        patternLayout.setOrientation(LinearLayout.VERTICAL);
//        patternLayout.setGravity(Gravity.CENTER);
//
//        for (int row = 0; row < 3; row++) {
//            LinearLayout rowLayout = new LinearLayout(this);
//            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
//            rowLayout.setGravity(Gravity.CENTER);
//
//            for (int col = 0; col < 3; col++) {
//                ImageView dot = new ImageView(this);
//                dot.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
////                dot.setBackgroundColor(Color.LTGRAY);
//                dot.setImageResource(R.drawable.dot_bg);
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dot.getLayoutParams();
//                params.setMargins(30, 30, 30, 30);
//                dot.setLayoutParams(params);
//                dotViews.add(dot);
//                rowLayout.addView(dot);
//            }
//
//            patternLayout.addView(rowLayout);
//        }
//        //  添加弹性空白区（上）
//        View topSpacer = new View(this);
//        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
//        topSpacer.setLayoutParams(spacerParams);
//        mainLayout.addView(topSpacer);
//
//        //  添加图案区域到 mainLayout
//        mainLayout.addView(patternLayout);
//
//        //  添加弹性空白区（下）
//        View bottomSpacer = new View(this);
//        bottomSpacer.setLayoutParams(spacerParams);
//        mainLayout.addView(bottomSpacer);
//
//
//        //  添加触摸逻辑（在 rootLayout 上）
//        rootLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                float touchX = event.getRawX();
//                float touchY = event.getRawY();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        selectedIndices.clear();
//                        isDrawing = true;
//                        checkTouch(touchX, touchY);
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        if (isDrawing) {
//                            checkTouch(touchX, touchY);
//                        }
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        v.performClick(); //  accessibility 兼容
//                        isDrawing = false;
//                        showPattern();
//                        return true;
//                }
//                return false;
//            }
//        });
//
//        //  把主布局添加到根布局，最后设置为内容视图
//        rootLayout.addView(mainLayout);
//        setContentView(rootLayout);
//    }
//
//    //  检测触摸点是否命中某个圆点
//    private void checkTouch(float x, float y) {
//        for (int i = 0; i < dotViews.size(); i++) {
//            ImageView dot = dotViews.get(i);
//            int[] loc = new int[2];
//            dot.getLocationOnScreen(loc);
//            float centerX = loc[0] + dot.getWidth() / 2f;
//            float centerY = loc[1] + dot.getHeight() / 2f;
//            float radius = dot.getWidth() / 2f;
//
//            if (Math.hypot(centerX - x, centerY - y) < radius) {
//                if (!selectedIndices.contains(i)) {
//                    selectedIndices.add(i);
////                    dot.setBackgroundColor(Color.BLUE);
//                    dot.setImageResource(R.drawable.dot_select);
//                }
//            }
//            if (!selectedIndices.contains(i)) {
//                selectedIndices.add(i);
//                selectedPoints.add(new float[]{centerX, centerY});
//                dot.setBackgroundColor(Color.BLUE);
//
//                // 如果不是第一个点，添加一条线
//                if (selectedPoints.size() > 1) {
//                    float[] prev = selectedPoints.get(selectedPoints.size() - 2);
//                    addLineBetweenPoints(prev[0], prev[1], centerX, centerY);
//                }
//            }
//        }
//    }
//
//    // 显示最终的图案路径
//    private void showPattern() {
//        StringBuilder sb = new StringBuilder();
//        for (int index : selectedIndices) {
//            sb.append(index);
//        }
//
//        Toast.makeText(this, "图案为：" + sb.toString(), Toast.LENGTH_SHORT).show();
//
//        // 重置所有圆点颜色
//        for (ImageView dot : dotViews) {
////            dot.setBackgroundColor(Color.LTGRAY);
//            dot.setImageResource(R.drawable.dot_bg);
//        }
//
//        // 清除所有线条
//        for (ImageView line : lineViews) {
//            rootLayout.removeView(line);
//        }
//        lineViews.clear();
//    }
//
//
//    private void addLineBetweenPoints(float x1, float y1, float x2, float y2) {
//        ImageView line = new ImageView(this);
//        line.setImageResource(R.drawable.line_horizontal_normal); // 你的资源名（换成你自己的）
//
//        // 计算角度
//        double deltaX = x2 - x1;
//        double deltaY = y2 - y1;
//        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
//
//        // 计算两点距离
//        double length = Math.hypot(deltaX, deltaY);
//
//        // 设置布局参数
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) length, 20); // 高度可调整
//        params.leftMargin = (int) Math.min(x1, x2);
//        params.topMargin = (int) Math.min(y1, y2);
//        line.setLayoutParams(params);
//
//        // 设置旋转中心和旋转角度
//        line.setPivotX(0);
//        line.setPivotY(0);
//        line.setRotation((float) angle);
//
//        // 添加到布局
//        rootLayout.addView(line);
//        lineViews.add(line); // 保存下来，方便清除
//    }
//
//}