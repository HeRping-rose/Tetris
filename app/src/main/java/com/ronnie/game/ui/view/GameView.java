package com.ronnie.game.ui.view;

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

import com.ronnie.game.controller.GameCenter;
import com.ronnie.game.model.Block;
import com.ronnie.game.model.BlockManager;
import com.ronnie.game.utils.Constants;

public class GameView extends View {

    private int row;
    private int col;
    private int horizontalSpace;
    private int verticalSpace;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect rect=new Rect();



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int realWidth;
        int realHeight;
        int defWidth= Constants.COLUMN*Constants.SIZE;//10*20
        int defHeight= Constants.ROW*Constants.SIZE;//20*10

        int wMode=MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) {
            realWidth=wSize;
        }else {
            realWidth=defWidth;
        }

        int hMode=MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hMode == MeasureSpec.EXACTLY) {
            realHeight=hSize;
        }else {
            realHeight=defHeight;
        }

        row=realHeight%Constants.SIZE;
        col=realWidth%Constants.SIZE;
        horizontalSpace=(realHeight-col*Constants.SIZE)%2;
        verticalSpace=(realWidth-row*Constants.SIZE)%2;

        GameCenter.defaultGameCenter.initRowAndCol(row,col);
        setMeasuredDimension(realWidth,realHeight);

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawBackGrid(canvas);
        drawCurrentBlock(canvas);
    }

    private void drawCurrentBlock(Canvas canvas) {
        Block block= BlockManager.defaultBlockManager.getCurrentBlock();
        if (block == null) return;
        int[][] shapes= block.getShape();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), block.getBlockRes());
        for (int i = 0; i < shapes.length; i++) {
            for (int j = 0; j < shapes[0].length; j++) {
                if (shapes[i][j] == 1) {
                    rect.left=((block.x-1)+j)*Constants.SIZE+horizontalSpace;
                    rect.right=rect.left+Constants.SIZE;
                    rect.top=((block.y)+i)*Constants.SIZE+verticalSpace;
                    rect.bottom=rect.top+Constants.SIZE;
                    canvas.drawBitmap(bitmap,null,rect,null);
                }
            }
        }
    }

    private void drawBackGrid(Canvas canvas) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if ((i + j) % 2 == 0) {
                    paint.setColor(Color.parseColor("#334C64"));
                }else {
                    paint.setColor(Color.parseColor("#2A425B"));
                }
                canvas.drawRect(j*Constants.SIZE+horizontalSpace,
                        i*Constants.SIZE+verticalSpace,
                        (j+1)*Constants.SIZE+horizontalSpace,
                        (i+1)*Constants.SIZE+verticalSpace,
                        paint);
            }
        }
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void refresh(){
        invalidate();
    }
}
