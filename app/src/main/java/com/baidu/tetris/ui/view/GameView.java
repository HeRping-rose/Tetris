package com.baidu.tetris.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.tetris.R;
import com.baidu.tetris.controller.GameCenter;
import com.baidu.tetris.model.Block;
import com.baidu.tetris.model.BlockManager;
import com.baidu.tetris.model.GridModel;
import com.baidu.tetris.utils.Constants;

public class GameView extends View {
    //记录当前游戏面板中的行和列数
    private int mRow ;
    private int mColumn;
    //记录横向和纵向间距
    private int mHorizontalSpace;
    private int mVerticalSpace;
    //背景颜色方格的画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public GameView(Context context) {
        super(context,null);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI() {
    }

    /*
    1. 确定自己的真实尺寸
    2. 确定在这个尺寸下对应row和column  space
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int realWidth;
        int realHeight;

        //默认尺寸
        int defaultWidth = Constants.COLUMN * Constants.SIZE;
        int defaultHeight = Constants.ROW * Constants.SIZE;

        //计算真实的宽度
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY){
            realWidth = wSize;
        }else{
            realWidth = defaultWidth;
        }

        //计算真实的高度
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hMode == MeasureSpec.EXACTLY){
            realHeight = hSize;
        }else{
            realHeight = defaultHeight;
        }

        //计算在当前真实尺寸下对应的行和列数
        mRow = realHeight / Constants.SIZE;
        mColumn = realWidth / Constants.SIZE;
        mHorizontalSpace = (realWidth - mColumn * Constants.SIZE)/2;
        mVerticalSpace = (realHeight - mRow * Constants.SIZE) /2;

        //配置GameCenter中记录的行和列
        GameCenter.defaultCenter.initRowAndColumn(mRow,mColumn);

        //设置自己测量之后的真实尺寸
        setMeasuredDimension(realWidth,realHeight);
    }

    //刷新界面
    public void refresh(){
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        //绘制背景方格
        drawBackgroundGrid(canvas);

        //绘制当前操作的方格
        drawCurrentBlock(canvas);

        //绘制面板固定的方格
        drawFixedBlock(canvas);
    }

    private Rect mRect = new Rect();

    private Bitmap mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
    //绘制固定的方格
    private void drawFixedBlock(Canvas canvas){
        GridModel[][] boards = GameCenter.defaultCenter.boards;
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mColumn; j++) {
                GridModel grid = boards[i][j];
                if (grid != null && grid.hasBlock()){
                    mRect.left = j*Constants.SIZE + mHorizontalSpace;
                    mRect.top = i*Constants.SIZE + mVerticalSpace;
                    mRect.right = mRect.left + Constants.SIZE;
                    mRect.bottom = mRect.top + Constants.SIZE;
                    canvas.drawBitmap(grid.getBitmap(getContext()),null,mRect,null);
                }
            }
        }
    }

    //绘制当前操作的方格
    private void drawCurrentBlock(Canvas canvas) {
        //获取当前的block对象
        Block block = BlockManager.defaultManager.getCurrentBlock();
        if (block == null) return;

        //拿到block对应的二维数组
        int[][] shapes = block.getShape();
        //获取block对应资源的位图对象Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),block.getResource());
        /*
        {
            {0,1,0},
            {0,1,0},
            {1,1,0},
    ````}
         */
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                if (shapes[i][j] == 1){
                    mRect.left = (block.x + j)*Constants.SIZE + mHorizontalSpace;
                    mRect.top = (block.y + i)*Constants.SIZE + mVerticalSpace;
                    mRect.right = mRect.left + Constants.SIZE;
                    mRect.bottom = mRect.top + Constants.SIZE;
                    canvas.drawBitmap(bitmap,null,mRect,null);
                }
            }
        }
    }

    //绘制背景方格
    private void drawBackgroundGrid(Canvas canvas){
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mColumn; j++) {
                if ((i+j)%2 == 0){
                    mPaint.setColor(Color.parseColor("#334C64"));
                }else{
                    mPaint.setColor(Color.parseColor("#2A425B"));
                }
                canvas.drawRect(
                        j*Constants.SIZE + mHorizontalSpace,
                        i*Constants.SIZE + mVerticalSpace,
                        (j+1)*Constants.SIZE + mHorizontalSpace,
                        (i+1)*Constants.SIZE + mVerticalSpace,
                        mPaint
                );
            }
        }
    }
}













