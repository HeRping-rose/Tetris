package com.ronnie.game.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ronnie.game.model.Block;
import com.ronnie.game.utils.Constants;

public class PreViewBlockView extends View {
    private int[][] board;
    private Bitmap bitmap;
    private Rect rect = new Rect();

    public void showBlock(Block block) {
        board=block.getShape();
        // 获取位图  利用位图工厂模式的 解读资源  decode:解读
        bitmap = BitmapFactory.decodeResource(getResources(), block.getBlockRes());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width= Constants.SIZE*3;
        int height= Constants.SIZE*3;
        setMeasuredDimension(width,height);//设置测量尺寸  Dimension:尺寸,范围,规模


    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (board == null) return;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    rect.left=Constants.SIZE*j;
                    rect.top=Constants.SIZE*i;
                    rect.right=Constants.SIZE*(j+1);
                    rect.bottom=Constants.SIZE*(i+1);
                    canvas.drawBitmap(bitmap,null,rect,null);
                }
            }
        }


    }

    public PreViewBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

}
