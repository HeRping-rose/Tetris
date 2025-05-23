package com.ronnie.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//自定义View  需要一个控件?-->控件的布局与显示都要自己绘制-->继承View
public class  BlockView extends View {
    private Map<TetrominoType, Tetromino> tetrominos = new HashMap<>();



    private int BLOCK_SIZE=dp2px(20);
    private final int cols = 10;
    private final int rows = 20;

    private Bitmap[][] arena = new Bitmap[rows][cols];
    private int[][] shape = {
            {1, 1, 1},
            {0, 1, 0}
    };
    private int shapeX = 4;
    private int shapeY = 0;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private Tetromino currentTetromino;


    public BlockView(Context context) {
        super(context,null);
    }

    public BlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        // 👇 加载所有形状和对应图片
        loadTetrominos(context);
        // 默认先用一个形状开始（比如 T 形）
        currentTetromino = tetrominos.get(TetrominoType.T);
        startGameLoop();
        initUI();//初始化UI,UI界面相关都写在里面
//        stopGameLoop();
    }

    private void startGameLoop() {
        handler.removeCallbacks(gameLoopRunnable); // 清除之前的任务
        handler.post(gameLoopRunnable); // 启动新的游戏循环
        startTimer();// 重置开始时间
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isPaused || isGameOver) return;
                updateElapsedTime();
                shapeY++;
                if (checkCollision()) {
                    shapeY--;
                    mergeShape();
                    resetShape();
                }
                postInvalidate(); // 触发 onDraw
            }
        }, 0, 500); // 每 500ms 触发一次
    }
    private void stopGameLoop() {
        handler.removeCallbacks(gameLoopRunnable); // 停止游戏循环
    }
    private Runnable gameLoopRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPaused || isGameOver) return;

            shapeY++; // 方块下落
            if (checkCollision()) {
                shapeY--;
                mergeShape();
                resetShape();
            }

            updateElapsedTime(); // 更新时间
            postInvalidate(); // 刷新界面
            handler.postDelayed(this, 500); // 每500ms更新一次
        }
    };

    private boolean checkGameOver() {
        // 检查新的方块是否在顶部碰到已有的方块
        for (int x = 0; x < currentTetromino.shape[0].length; x++) {
            for (int y = 0; y < currentTetromino.shape.length; y++) {
                // 如果形状的这个位置是 1，并且该位置已经被填充，游戏就结束了
                if (currentTetromino.shape[y][x] != 0) {
                    if (shapeY + y < 0 || arena[shapeY + y][shapeX + x] != null) {
                        return true; // 游戏结束
                    }
                }
            }
        }
        return false; // 游戏没有结束
    }


    //游戏暂停方法
    public void togglePause() {
        isPaused = !isPaused;
        invalidate();
    }
    private void initUI() {

    }
    private void loadTetrominos(Context context) {
        tetrominos.put(TetrominoType.I, new Tetromino(
                new int[][] {
                        {1},
                        {1},
                        {1},
                        {1}
                },
                BitmapFactory.decodeResource(context.getResources(), R.drawable.purple)
        ));

        tetrominos.put(TetrominoType.T, new Tetromino(
                new int[][] {
                        {1, 1, 1},
                        {0, 1, 0}
                },
                BitmapFactory.decodeResource(context.getResources(), R.drawable.orange)
        ));

        tetrominos.put(TetrominoType.Z, new Tetromino(
                new int[][] {
                        {1, 1, 0},
                        {0, 1, 1}
                },
                BitmapFactory.decodeResource(context.getResources(), R.drawable.yellow)
        ));

        tetrominos.put(TetrominoType.S, new Tetromino(
                new int[][] {
                        {0, 1, 1},
                        {1, 1, 0}
                },
                BitmapFactory.decodeResource(context.getResources(), R.drawable.green)
        ));

        tetrominos.put(TetrominoType.J, new Tetromino(
                new int[][] {
                        {1, 0, 0},
                        {1, 1, 1}
                },
                BitmapFactory.decodeResource(context.getResources(), R.drawable.blue)
        ));

        tetrominos.put(TetrominoType.L, new Tetromino(
                new int[][] {
                        {0, 0, 1},
                        {1, 1, 1}
                },
                BitmapFactory.decodeResource(context.getResources(), R.drawable.blue)
        ));

        tetrominos.put(TetrominoType.O, new Tetromino(
                new int[][] {
                        {1, 1},
                        {1, 1}
                },
                BitmapFactory.decodeResource(context.getResources(), R.drawable.yellow)
        ));
    }


    //重写测量方法  父容器测量自身和子控件的尺寸 layout_width layout_height
    //父类测量子类时,传递过来的约束,测量参数
    //widthMeasureSpec 32位 前两位+后30位
    //0000 0000 ... 0001 <30位
    //0100 0000 ... 0001 EXACTLY
    //0100 0000 ... 0010 <30位
    //1000 0000 ... 0000 AT_MOST
    //heightMeasureSpec 32位 前两位+后30位
    //前两位决定父类的测量模式  后30位决定父类测量时给的参考值
    //    200dp   -->EXACTLY+200dp  使用了MeasureSpec类   getSize(),,getMode()

    //MEASURED_STATE_TOO_SMALL  子控件提供的值为精确值



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        //拿到宽度模式
//        int wMode = MeasureSpec.getMode(widthMeasureSpec);
//        //拿到宽度的参考尺寸
//        int wsize = MeasureSpec.getSize(widthMeasureSpec);

        //计算正式高度宽度  精确值EXACTLY 0dp match_parent 200dp  match_Constrain
        //当为wrap_content是AT_MOST
        int desiredWidth = 3*BLOCK_SIZE;
        int desiredHeight= 3*BLOCK_SIZE;
        int w=resolveSizeAndState(desiredWidth, widthMeasureSpec, MEASURED_STATE_TOO_SMALL);
        int h=resolveSizeAndState(desiredHeight, heightMeasureSpec, MEASURED_STATE_TOO_SMALL);

        //配置自己的正式尺寸
        setMeasuredDimension(w,h);

        //拿到高度模式
//        int hMode = MeasureSpec.getMode(heightMeasureSpec);
//        //拿到高度的参考尺寸
//        int hsize = MeasureSpec.getSize(heightMeasureSpec);
    }

    //显示内容 绘制
//    view:画板
//    canvas:画布
//    paint:画笔
//    图形:text circle arc roundRect bezel bitmap...各种图形

//    ANTI_ALIAS_FLAG: 抗锯齿  使其平滑
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float tX=0f;
    private float tY=0f;
    private int score = 0;
    private long startTime;//显示游戏用时
    private long elapsedTime=0;
    private float startX;
    private float startY;
    private Handler handler = new Handler();
    //将res转化成bitmap类型
    private Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);


    private void startTimer() {
        startTime = System.currentTimeMillis();
    }

    private void updateElapsedTime() {

        elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // 秒
    }
    // 不要在onDraw里面创建对象  //绘制区域
    Rect rect = new Rect(0, 0, dp2px(20), dp2px(20));
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawArena(canvas);
        drawShape(canvas);
        //显示“游戏结束”文字
        if (isGameOver) {
            mPaint.setColor(Color.RED);
            mPaint.setTextSize(dp2px(24));
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("游戏结束", getWidth() / 2f, getHeight() / 2f, mPaint);
        }

        //在画面上绘制得分
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(dp2px(16));
        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("得分：" + score, dp2px(4), dp2px(20), mPaint);

        //绘制时间：
        elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(dp2px(14));
        canvas.drawText("时间: " + elapsedTime + "s", getWidth() - dp2px(100), dp2px(18), mPaint);
//        canvas.drawColor(Color.LTGRAY);
        //h绘制矩形
//        mPaint.setColor(Color.GREEN);
//        mPaint.setStrokeWidth(dp2px(4));
//        canvas.drawRect(dp2px(4), dp2px(4), dp2px(32), dp2px(32), mPaint);

        //绘制圆形
//        canvas.drawCircle(getWidth()/2f,getHeight()/2f,getHeight()/2f,mPaint);
        //画线
//        canvas.drawLine(0f,0f,getWidth(),getHeight(),mPaint);

        //终点随触摸点走
//        canvas.drawLine(0f,0f,tX,tY,mPaint);


//        canvas.drawBitmap(bitmap,0f,0f,null);


//        canvas.drawBitmap(bitmap,null,rect,null);


    }
    private void drawArena(Canvas canvas) {
        for (int y = 0; y < arena.length; y++) {
            for (int x = 0; x < arena[y].length; x++) {
                if (arena[y][x] != null) {
                    canvas.drawBitmap(
                            arena[y][x],
                            null,
                            new Rect(
                                    x * BLOCK_SIZE,
                                    y * BLOCK_SIZE,
                                    (x + 1) * BLOCK_SIZE,
                                    (y + 1) * BLOCK_SIZE
                            ),
                            null
                    );
                }
            }
        }
    }

    private void drawShape(Canvas canvas) {
        int[][] shape = currentTetromino.shape;
        Bitmap bmp = currentTetromino.bitmap;

        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    canvas.drawBitmap(
                            bmp,
                            null,
                            new Rect(
                                    (shapeX + x) * BLOCK_SIZE,
                                    (shapeY + y) * BLOCK_SIZE,
                                    (shapeX + x + 1) * BLOCK_SIZE,
                                    (shapeY + y + 1) * BLOCK_SIZE
                            ),
                            null
                    );
                }
            }
        }
    }
    private boolean checkCollision() {
        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    int newY = shapeY + y;
                    int newX = shapeX + x;
                    if (newY >= rows || newX < 0 || newX >= cols || arena[newY][newX] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void mergeShape() {
        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    arena[shapeY + y][shapeX + x] = currentTetromino.bitmap;
                }
            }
        }
        clearFullRows(); // 清除已满的行
    }
    private void clearFullRows() {
        int rowsCleared = 0;

        for (int y = 0; y < arena.length; y++) {
            boolean full = true;
            for (int x = 0; x < arena[y].length; x++) {
                if (arena[y][x] == null) {
                    full = false;
                    break;
                }
            }

            if (full) {
                // 行满了：整行向下移一行
                for (int i = y; i > 0; i--) {
                    System.arraycopy(arena[i - 1], 0, arena[i], 0, arena[i].length);
                }
                // 最顶上那一行清空
                for (int x = 0; x < arena[0].length; x++) {
                    arena[0][x] = null;
                }
                rowsCleared++;
            }
        }

        // 加分：1 行 100 分，2 行 300，3 行 500，4 行 800 分（标准计分）
        switch (rowsCleared) {
            case 1: score += 100; break;
            case 2: score += 300; break;
            case 3: score += 500; break;
            case 4: score += 800; break;
        }
    }

    private void resetShape() {
        TetrominoType[] types = TetrominoType.values();
        int index = (int) (Math.random() * types.length);
        currentTetromino = tetrominos.get(types[index]);
        shape = currentTetromino.shape;
        shapeX = 4;
        shapeY = 0;

        //如果一出现就碰撞，结束游戏！
        if (checkCollision()) {
            isGameOver = true;
            postInvalidate();
        }
    }

    private int dp2px(int dp){
        return (int)(getResources().getDisplayMetrics().density * dp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果游戏结束，点击屏幕重启
                if (isGameOver) {
                    restartGame();
                    return true;
                }
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isGameOver) return true;

                float endX = event.getX();
                float endY = event.getY();
                float deltaX = endX - startX;
                float deltaY = endY - startY;

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (deltaX > 50) {
                        // 向右滑动
                        moveRight();
                    } else if (deltaX < -50) {
                        // 向左滑动
                        moveLeft();
                    }
                } else {
                    if (deltaY < -50) {
                        // 向上滑动 -> 旋转
                        rotate();
                    }else if(deltaY > 50) {
                        // 向下滑动 -> 加速下落
                        dropDownFast();
                    }
                }

                invalidate();
                break;
        }
//        tX=event.getX();
//        tY=event.getY();
//        invalidate();//强制刷新
        return true;
    }
    private void restartGame() {
        // 清空棋盘
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                arena[y][x] = null;
            }
        }

        isGameOver = false;
        score = 0;
        resetShape();
        startGameLoop(); // 启动新的游戏循环

        invalidate(); // 重绘界面
    }
    private void moveLeft() {
        shapeX--;
        if (checkCollision()) {
            shapeX++;
        }
    }

    private void moveRight() {
        shapeX++;
        if (checkCollision()) {
            shapeX--;
        }
    }
    private void rotate() {
        int[][] oldShape = shape;
        shape = rotateMatrix(shape);
        if (checkCollision()) {
            shape = oldShape; // 如果碰撞就取消旋转
        } else {
            currentTetromino.shape = shape;
        }
    }
    //向下加速（下滑或长按）
    private void dropDownFast() {
        while (!checkCollision()) {
            shapeY++;
        }
        shapeY--; // 回退一格
        mergeShape();
        resetShape();
        invalidate();
    }

    // 顺时针旋转二维数组
    private int[][] rotateMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[cols][rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                result[x][rows - 1 - y] = matrix[y][x];
            }
        }
        return result;
    }

}
enum TetrominoType {
    I, O, T, S, Z, J, L
}

class Tetromino {
//    俄罗斯方块
    public int[][] shape;//形状
    public Bitmap bitmap;//位图 视图

    public Tetromino(int[][] shape, Bitmap bitmap) {
        this.shape = shape;
        this.bitmap = bitmap;
    }
}
/**
 * 代码中使用
 * BlockView blockView=new BlockView(this)
 * xml中使用 <BlockView></BlockView> 控件的尺寸最重要  宽度和高度
 *
 *
 * onMeasure:测量控件尺寸
 * 1.父容器指定大小dp和匹配父容器match_parent不需要测量  2.包裹内容和满足自己的比例需要测量
 * onLayout():布局子控件  本身是(ViewGroup容器组件)
 * onDraw():
 */
