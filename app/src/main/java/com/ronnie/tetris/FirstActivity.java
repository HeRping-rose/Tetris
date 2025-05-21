package com.ronnie.tetris;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends AppCompatActivity {

    private FrameLayout container;
    private TextView tvTitle;
    //记录9个圆点控件
    private ArrayList<StatefulImageView> dotViews = new ArrayList<>();
    private int dotSize ;
    private int space ;
    private int lastDotTag = 0;
    private int[] linesTagArray={12,23,45,56,78,89,14,25,36,47,58,69,15,26,48,59,24,35,57,68};
    private PrefsUtils prefsUtils=new PrefsUtils();
    private String password;
    private String firstPassword;
    private StringBuilder sb=new StringBuilder();
    private ArrayList<StatefulImageView> selectedViews = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将xml中配置的view 和Activity绑定
        setContentView(R.layout.activity_first);

        //查找容器控件
        container = findViewById(R.id.frameLayout);
        tvTitle = findViewById(R.id.tvTitle);
        //配置sharedPreferrences
        prefsUtils.init(this,"userInfo.db");


        dotSize = dp2px(72);
        space = (dp2px(320) - dotSize*3)/2;

        //添加9个点
        initNineDot();

        //添加线条
        initHorizentalLine();//横线
        initVerticalLine();//竖线
        initSlantLine();//斜线
//        initLeftSlantLine();//斜线
//        initRightSlantLine();//斜线

        //监听容器的触摸事件
        initTouchListener();
        initUI();


    }

    private void initUI() {
        //提取文件中的密码
        password =prefsUtils.getPassword();
        if (password == null) {
            //第一次登录,请设置图案密码
            tvTitle.setText("请设置图案密码");
        } else {
            //登录
            tvTitle.setText("请绘制解锁图案密码");
        }
    }

    //初始化触摸监听事件
    @SuppressLint("ClickableViewAccessibility")
    private void initTouchListener(){
        container.setOnTouchListener((v,event)->{
            if (event.getAction() == MotionEvent.ACTION_UP){
                //获取当前输入的密码
                String currentPwd =sb.toString();
                //抬手
                if (password != null) {
                    if (currentPwd.equals(password)) {
                        //解锁成功
                        tvTitle.setText("登录成功");
                        clearState();

                        Intent intent=new Intent(this, TetrisActivity.class);

                        startActivity(intent);

                    }else {
                        tvTitle.setText("图案密码错误,请重新输入");
                        showError();
                    }
                }else {
                    //设置密码  判断是不是第一次进入程序设置密码
                    if (firstPassword == null) {
                        firstPassword=currentPwd;
                        tvTitle.setText("请确认图案密码");
                        clearState();
                    }else {
                        if (currentPwd.equals(firstPassword)) {
                            tvTitle.setText("密码设置成功");
                            clearState();
                            prefsUtils.savePassword(currentPwd);
                        }else {
                            tvTitle.setText("两次密码不一致,请重新绘制");
                            showError();
                        }
                        firstPassword=null;
                    }
                }
                //清空sb对象的内容,方便下一次输入
                sb.setLength(0);
                lastDotTag=0;
            }else{
                //按下和移动
                //获取触摸点的xy坐标
                float x = event.getX();
                float y = event.getY();

                //获取触摸点所在的控件
                StatefulImageView dotView = findDotView(x,y);
                if (dotView == null) return true;

                //判断是不是第一个点亮的点
                int currentTag = (Integer) dotView.getTag();
                if (lastDotTag == 0){
                    //点亮这个点
                    dotView.changeStateTo(StatefulImageView.State.SELECTED);
                    //记录这个点的tag
                    lastDotTag = currentTag;
                    //记录密码
                    sb.append(currentTag);
                    //保存点亮的控件
                    selectedViews.add(dotView);
                }else{
                    //获取两点连接线的tag值
                    int lineTag = Math.min(lastDotTag,currentTag)*10 + Math.max(lastDotTag,currentTag);
                    //判断路线是否存在
                    if (!lineExists(lineTag))return true;

                    //找到tag对应的控件
                    StatefulImageView lineView = container.findViewWithTag(lineTag);
                    //点亮点和线
                    lineView.changeStateTo(StatefulImageView.State.SELECTED);
                    dotView.changeStateTo(StatefulImageView.State.SELECTED);

                    //记录当前这个点
                    lastDotTag = currentTag;

                    //记录密码
                    sb.append(currentTag);

                    //保存点亮的控件
                    selectedViews.add(dotView);
                    selectedViews.add(lineView);
                }
            }
            return true;
        });
    }

    //显示错误的图案状态
    private void showError() {
        for (StatefulImageView iv : selectedViews) {
            iv.changeStateTo(StatefulImageView.State.ERROR);

        }
        new Handler().postDelayed(() -> clearState(), 1000);
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(()->{
//                    clearState();
//                });
//            }
//        },1000);
    }

    //清空状态
    private void clearState() {
        for (StatefulImageView iv : selectedViews) {
            iv.changeStateTo(StatefulImageView.State.NORMAL);
        }
        selectedViews.clear();
    }

    //查找触摸点是否在某个圆点内部
    private StatefulImageView findDotView(float x, float y){
        //遍历数组中的9个点，一次判断触摸点是否在这个控件的区域内
        for (int i = 0; i < dotViews.size(); i++){
            StatefulImageView dotView = dotViews.get(i);

            if ( (x >= dotView.getLeft() && x <= dotView.getRight()) && (y >= dotView.getTop() && y <= dotView.getBottom())){
                return dotView;
            }
        }

        return null;
    }

    //判断普通数组中是否包含某个元素的方法
    private boolean lineExists(int tag) {
        for (int lineTag : linesTagArray){
            if (lineTag == tag){
                return true;
            }
        }
        return false;
    }

    //添加9个点的方法
    private void initNineDot() {
        int index=1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                StatefulImageView sv = createView(R.drawable.dot_bg,R.drawable.dot_select,R.drawable.dot_error,dotSize,dotSize,j * (dotSize + space),i * (dotSize + space),index);
                //创建控件对象

                //保存这个点
                dotViews.add(sv);

                index++;
            }
        }

    }

    //添加横线的方法
    private void initHorizentalLine(){
        int index=12;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                createView(0,R.drawable.line_horizontal_normal,R.drawable.line_horizontal_error,dotSize+space,dp2px(6),dotSize/2 + col*(dotSize+space),dotSize/2 + row*(dotSize+space) - dp2px(3),index);
                index += 11;
            }
            index+=11;
        }
    }

    //添加竖线的方法
    private void initVerticalLine(){
        int index=14;
        for (int row = 0; row < 2; row++) {  // 每列2条垂直线（从上到下）
            for (int col = 0; col < 3; col++) {
                createView(0,R.drawable.line_vertical_normal,R.drawable.line_vertical_error,dp2px(6),dotSize+space,dotSize/2-dp2px(3)+col*(dotSize+space),dotSize/2 + row*(dotSize + space),index);
                index += 11;
            }
        }
    }

    //添加斜线的方法
    private void initSlantLine(){
        int lindex = 24;
        int rindex = 15;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                //右斜
                createView(0,R.drawable.line_right_slant_normal,R.drawable.line_right_slant_error,space+dotSize,dotSize+space,dotSize/2+j*(dotSize+space),dotSize/2 + i*(dotSize + space),rindex);
                //左斜
                createView(0,R.drawable.line_left_slant_normal,R.drawable.line_left_slant_error,space+dotSize,dotSize+space,dotSize/2+j*(dotSize+space),dotSize/2 + i*(dotSize + space),lindex);

                lindex += 11;
                rindex += 11;
            }

            lindex += 11;
            rindex += 11;
        }
    }
    private void initLeftSlantLine(){
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

    //用于封装 创建控件的方法
    private StatefulImageView createView(int normalRes,int selectedRes,int errorRes,int width,int height,int leftMargin, int topMargin,Integer tag) {
        StatefulImageView view = new StatefulImageView(this, normalRes, selectedRes, errorRes);
        view.changeStateTo(StatefulImageView.State.NORMAL);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setTag(tag);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.leftMargin = leftMargin;
        lp.topMargin = topMargin;
        container.addView(view, lp);

        return view;
    }

    //将dp类型数据转化为px
    private int dp2px(int dp){
        return (int)(getResources().getDisplayMetrics().density * dp);
    }
}