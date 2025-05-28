package com.baidu.tetris.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.tetris.model.Block;
import com.baidu.tetris.utils.Constants;

public class BlockPreviewView extends View {
    private int[][] board;
    private Bitmap mBitmap;

    public BlockPreviewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    //显示预览图
    public void showBlock(Block block){
        board = block.getShape();
        //将block对应的图片资源转化为位图bitmap类型
        mBitmap = BitmapFactory.decodeResource(getResources(),block.getResource());

        //TODO：修改内容如果需要让控件及时显示，必须调用刷新的方法
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算出自己需要的尺寸
        int mWidth = Constants.SIZE * 3;
        int mHeight = Constants.SIZE * 3;

        setMeasuredDimension(mWidth,mHeight);
    }

    private Rect mRect = new Rect();
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (board == null) return;

        //遍历二维数组
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1){
                    mRect.left = j * Constants.SIZE;
                    mRect.top = i * Constants.SIZE;
                    mRect.right = mRect.left + Constants.SIZE;
                    mRect.bottom = mRect.top + Constants.SIZE;
                    canvas.drawBitmap(mBitmap,null,mRect,null);
                }
            }
        }

    }
}

















