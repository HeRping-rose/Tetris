package com.baidu.tetris.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.baidu.tetris.R;

public class CircleView extends View {
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    Bitmap bitmap;

    public CircleView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        BitmapFactory.Options options =  new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bg_image,options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = getWidth();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg_image,options);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int count = canvas.saveLayer(new RectF(0,0,getWidth(),getHeight()),mPaint);
        canvas.drawOval(0,0,getWidth(),getHeight(),mPaint);
        mPaint.setXfermode(xfermode); //设置混合模式
        canvas.drawBitmap(bitmap,0,0,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(count);
    }
}
