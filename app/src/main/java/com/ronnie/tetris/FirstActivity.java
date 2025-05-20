package com.ronnie.tetris;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {

    private FrameLayout container;
    private StatefulImageView LineView;
    //记录9个圆点控件
    private ArrayList<ImageView> dotViews = new ArrayList<>();
    private int dotSize ;
    private int space ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将xml中配置的view 和Activity绑定
        setContentView(R.layout.activity_first);

        //查找容器控件
        container = findViewById(R.id.frameLayout);

        dotSize = dp2px(72);
        space = (dp2px(320) - dotSize*3)/2;

        //添加9个点
        initNineDot();

        //添加线条
        initHorizentalLine();
        initVerticalLine();
        initLeftSlantLine();
        initRightSlantLine();

        //监听容器的触摸事件
        initTouchListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initTouchListener(){
        container.setOnTouchListener((v,event)->{
            if (event.getAction() == MotionEvent.ACTION_UP){
                //抬手
            }else{
                //按下和移动
                //获取触摸点的xy坐标
                float x = event.getX();
                float y = event.getY();

                //遍历数组中的9个点，一次判断触摸点是否在这个控件的区域内
                for (int i = 0; i < dotViews.size(); i++){
                    ImageView dotView = dotViews.get(i);

                    if ( (x >= dotView.getLeft() && x <= dotView.getRight()) && (y >= dotView.getTop() && y <= dotView.getBottom())){
                        //触摸点在这个控件内部 点亮
                        dotView.setImageResource(R.drawable.dot_select);
                    }
                }
            }
            return true;
        });
    }

    private void initNineDot() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //创建控件对象
                StatefulImageView iv = new StatefulImageView(this,R.drawable.dot_bg,R.drawable.dot_select,R.drawable.dot_error);
                //设置控件的属性
                iv.changeStateTo(StatefulImageView.State.NORMAL);
                //配置布局参数
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dotSize,dotSize);
                lp.leftMargin = j * (dotSize + space);
                lp.topMargin = i * (dotSize + space);
                //添加到容器中
                container.addView(iv,lp);

                //保存这个点
                dotViews.add(iv);
            }
        }

    }
    private void initHorizentalLine(){
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                StatefulImageView lineView = new StatefulImageView(this, 0, R.drawable.line_horizontal_normal, R.drawable.line_horizontal_error);
                lineView.changeStateTo(StatefulImageView.State.SELECTED);
                lineView.setScaleType(ImageView.ScaleType.FIT_XY);

                // 计算 leftMargin 和 topMargin
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dotSize + space, dp2px(10));
                lp.leftMargin = col * (dotSize + space) + dotSize-dotSize/2; // 起点是下一个点的左边缘
                lp.topMargin = row * (dotSize + space) + (dotSize - dp2px(10)) / 2;

                container.addView(lineView, lp);
            }
        }
//        StatefulImageView lineView = new StatefulImageView(this, 0, R.drawable.line_horizontal_normal, R.drawable.line_horizontal_error);
//
//        lineView.changeStateTo(StatefulImageView.State.SELECTED);
//        lineView.setScaleType(ImageView.ScaleType.FIT_XY);
//        //配置布局参数
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dotSize+space,dp2px(10));
//
//        lp.leftMargin = (dotSize)/2;
//
//        lp.topMargin = dotSize/2-dp2px(5);
//
//        //添加到容器中
//        container.addView(lineView,lp);
    }
    private void initVerticalLine(){
        for (int row = 0; row < 2; row++) {  // 每列2条垂直线（从上到下）
            for (int col = 0; col < 3; col++) {
                StatefulImageView lineView = new StatefulImageView(this, 0, R.drawable.line_vertical_normal, R.drawable.line_vertical_error);
                lineView.changeStateTo(StatefulImageView.State.SELECTED);
                lineView.setScaleType(ImageView.ScaleType.FIT_XY);

                // 配置布局参数
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dp2px(10), dotSize + space); // 宽为线条宽度，高为线条长度

                // leftMargin：每列点的中心位置减去半宽
                lp.leftMargin = col * (dotSize + space) + (dotSize - dp2px(10)) / 2;

                // topMargin：行号 ×（点 + 间距）+ 点高（从下面那个点起）
                lp.topMargin = row * (dotSize + space) + dotSize-dotSize/2;

                container.addView(lineView, lp);
            }
        }
    }
    private void initLeftSlantLine(){

//        StatefulImageView lineView = new StatefulImageView(this, 0, R.drawable.line_right_slant_normal, R.drawable.line_right_slant_error);
//        lineView.changeStateTo(StatefulImageView.State.SELECTED);
//        lineView.setScaleType(ImageView.ScaleType.FIT_XY);
//        // 配置布局参数
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dotSize+space, dotSize + space); // 宽为线条宽度，高为线条长度
//// leftMargin：每列点的中心位置减去半宽
//        lp.leftMargin = dotSize-dotSize/ 2;
//
//        // topMargin：行号 ×（点 + 间距）+ 点高（从下面那个点起）
//        lp.topMargin =  dotSize-dotSize/2;
//
//        container.addView(lineView, lp);


//        // 斜线图片宽高
        int lineWidth = (int) Math.sqrt((dotSize + space) * (dotSize + space) * 2); // 斜边长度（点距是斜边）
        int lineHeight = dp2px(10); // 线的粗细

        // 需要的四条斜线
        int[][] positions = {
                {0, 0}, // 0→4
                {0, 1}, // 1→5
                {1, 0}, // 3→7
                {1, 1}  // 4→8
        };

        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];

            StatefulImageView lineView = new StatefulImageView(this, 0, R.drawable.line_right_slant_normal, R.drawable.line_right_slant_error);
            lineView.changeStateTo(StatefulImageView.State.SELECTED);
            lineView.setScaleType(ImageView.ScaleType.FIT_XY);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dotSize+space, dotSize+space);

            // 计算左上角坐标（起始点）
            int left = col * (dotSize + space) + (dotSize - lineHeight) / 2;
            int top = row * (dotSize + space) + (dotSize - lineHeight) / 2;

            lp.leftMargin = left;
            lp.topMargin = top;

            container.addView(lineView, lp);
        }
    }
    private void initRightSlantLine(){
        // 斜线图片宽高
        int lineWidth = (int) Math.sqrt((dotSize + space) * (dotSize + space) * 2); // 斜边长度
        int lineHeight = dp2px(10); // 线条粗细

        // 需要的四条 ↙ 斜线（row, col）分别为起点坐标
        int[][] positions = {
                {0, 1}, // 1 → 3
                {0, 2}, // 2 → 4
                {1, 1}, // 4 → 6
                {1, 2}  // 5 → 7
        };

        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];

            StatefulImageView lineView = new StatefulImageView(this, 0, R.drawable.line_left_slant_normal, R.drawable.line_left_slant_error);
            lineView.changeStateTo(StatefulImageView.State.SELECTED);
            lineView.setScaleType(ImageView.ScaleType.FIT_XY);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dotSize+space, dotSize+space);

            // 计算左上角坐标（起点为右上的点）
            int left = (col-1) * (dotSize + space) + (dotSize - lineHeight) / 2;
            int top = (row) * (dotSize + space) + (dotSize - lineHeight) / 2;

            lp.leftMargin = left;
            lp.topMargin = top;


            container.addView(lineView, lp);
        }
    }

    //将dp类型数据转化为px
    private int dp2px(int dp){
        return (int)(getResources().getDisplayMetrics().density * dp);
    }



}