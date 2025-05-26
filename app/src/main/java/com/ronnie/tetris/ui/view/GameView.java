package com.ronnie.tetris.ui.view;

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

import com.ronnie.tetris.controller.GameCenter;
import com.ronnie.tetris.model.Block;
import com.ronnie.tetris.model.BlockManager;
import com.ronnie.tetris.utils.Constants;

public class GameView extends View {

    private int mRow;
    private int mColumn;
    private int mHorizentalSpace;
    private int mVerticalSpace;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int realWidth;
        int realHeight;

        //设置默认高度和宽度区域  宽=列*每格大小
        int defWidth= Constants.COLUMN*Constants.SIZE;
        int defHeight= Constants.ROW*Constants.SIZE;

        //设置宽度
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) {
            realWidth=wSize;
        }else {
            realWidth=defWidth;
        }
        //设置高度
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hMode == MeasureSpec.EXACTLY) {
            realHeight=hSize;
        }else {
            realHeight=defHeight;
        }

        mRow=realHeight/Constants.SIZE;
        mColumn=realWidth/Constants.SIZE;
        mHorizentalSpace=(realWidth-mColumn*Constants.SIZE)/2;
        mVerticalSpace=(realHeight-mRow*Constants.SIZE)/2;

        //配置GameCenter中行和列
        GameCenter.defaultCenter.initRowAndColumn(mRow, mColumn);

        //设计测量的真实值  并给他相应的宽度高度
        setMeasuredDimension(realWidth,realHeight);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawBackGroundGrid(canvas);

        DrawCurrentBlock(canvas);
    }

    //绘制当前面板上的当前图形
    private Rect mRect=new Rect();//绘画的区域
    private void DrawCurrentBlock(Canvas canvas) {
        //获取当前的方块
        Block block= BlockManager.defaultManager.getCurrentBlock();
        //得到他的形状资源是什么形状
        int[][] shapes=block.getShape();
        //创建位图  用位图画上去
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), block.getResourse());
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                //是"1"的才给他绘画上去
                if (shapes[i][j] == 1) {
                    mRect.left=(block.x+j)*Constants.SIZE+mHorizentalSpace;
                    mRect.right=mRect.left+Constants.SIZE;
                    mRect.top=(block.y+i)*Constants.SIZE+mVerticalSpace;
                    mRect.bottom=mRect.top+Constants.SIZE;
                    canvas.drawBitmap(bitmap,null,mRect,null);//bitmap不需要画笔 所以为空
                }

            }
        }

    }

    //绘制背景宫格区域
    private void drawBackGroundGrid(Canvas canvas) {
        for (int i = 0; i <mRow ; i++) {
            for (int j = 0; j < mColumn; j++) {

                if ((i + j) % 2 == 0) {
                    mPaint.setColor(Color.parseColor("#334C64"));
                } else {
                    mPaint.setColor(Color.parseColor("#2A425B"));
                }
                canvas.drawRect(j*Constants.SIZE+mHorizentalSpace,
                        i*Constants.SIZE+mVerticalSpace,
                        (j+1)*Constants.SIZE+mHorizentalSpace,
                        (i+1)*Constants.SIZE+mVerticalSpace,
                        mPaint);
            }
        }
    }

    public void refresh() {
        invalidate();
    }
}
