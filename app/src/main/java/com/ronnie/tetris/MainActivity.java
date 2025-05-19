package com.ronnie.tetris;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageView blueView;//成员变量
    ImageView targetView;//成员变量

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);//将view与layout绑定

        //1.使用代码 创建一个控件
        ImageView ivOrange = new ImageView(this);
        ivOrange.setImageResource(R.drawable.orange);//2.配置控件基础属性
        //3/配置控件布局参数  layoutParms-->ViewGroup.layoutParms
        // ConstrainLayout.layoutParms
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.startToStart=ConstraintLayout.LayoutParams.PARENT_ID;//0
        lp.topToTop=ConstraintLayout.LayoutParams.PARENT_ID;//0
        lp.bottomToBottom=ConstraintLayout.LayoutParams.PARENT_ID;//0
        lp.endToEnd=ConstraintLayout.LayoutParams.PARENT_ID;//0
        //4.將控件添加到容器中
        ConstraintLayout container = findViewById(R.id.main);
        container.addView(ivOrange,lp);


        FrameLayout.LayoutParams flp=new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        flp.gravity= Gravity.CENTER;// 设置 FrameLayout 大小
        // 创建 ImageView 作为子控件
        ImageView ivYellow = new ImageView(this);
        ivYellow.setImageResource(R.drawable.yellow);

        TextView textView = new TextView(this);
        textView.setText("text");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(Color.BLACK);


        FrameLayout fm=findViewById(R.id.frameLayout);
        // 添加 ImageView 到 FrameLayout
        fm.addView(ivYellow,flp);
        fm.addView(textView,flp);




        blueView = findViewById(R.id.imageView);
        targetView = findViewById(R.id.targetView);


        //查找并添加触摸事件
        ConstraintLayout layout = findViewById(R.id.purpleContainer);
        layout.setOnTouchListener((v,event )->{

            return true;//false
        });
    }

    //给屏幕添加触摸
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //获取具体的动作
        int action = event.getAction();

        //获取x和y坐标
        float x = event.getX();
        float y = event.getY();//getRowY()是设备的Y坐标

        //设置为中心移动
        blueView.setX(x- blueView.getWidth() / 2f);
        blueView.setY(y- blueView.getHeight()/2f - getStatusBarHeight() );//还应该减去状态栏高度

//        if(isArea(blueView,targetView)){
//            blueView.setImageResource(R.drawable.green);
//        }else {
//            blueView.setImageResource(R.drawable.blue);
//        }

        //滑动过程中触摸点是否在某个控件区域内部
        if ((x >= blueView.getLeft()
                && x <= blueView.getRight())
                && (y >= blueView.getTop()
                && y <= blueView.getBottom())){
            blueView.setImageResource(R.drawable.green);
        }else{
            blueView.setImageResource(R.drawable.blue);
        }

        //判断各种情况  int char
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isArea(View thisView, View targetView) {
        int[] movingLocation = new int[2];
        int[] targetLocation = new int[2];

        thisView.getLocationOnScreen(movingLocation);
        targetView.getLocationOnScreen(targetLocation);

        int movingCenterX = movingLocation[0] + thisView.getWidth() / 2;
        int movingCenterY = movingLocation[1] + thisView.getHeight() / 2;

        int targetLeft = targetLocation[0];
        int targetTop = targetLocation[1];
        int targetRight = targetLeft + targetView.getWidth();
        int targetBottom = targetTop + targetView.getHeight();

        return (movingCenterX >= targetLeft && movingCenterX <= targetRight
                && movingCenterY >= targetTop && movingCenterY <= targetBottom);
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