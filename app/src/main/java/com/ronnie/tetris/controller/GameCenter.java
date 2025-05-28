package com.ronnie.tetris.controller;

import android.app.Activity;

import com.ronnie.tetris.model.Block;
import com.ronnie.tetris.model.BlockManager;
import com.ronnie.tetris.model.GridModel;
import com.ronnie.tetris.ui.view.BlockPreviewView;
import com.ronnie.tetris.ui.view.GameView;

import java.util.Timer;
import java.util.TimerTask;

public class GameCenter {
    public static final GameCenter defaultCenter=new GameCenter();  //提供单例对象

    public BlockPreviewView previewView;
    public GameView gameView;
    private int mRow;//行
    private int mColumn;//列
    //

    public GridModel[][] boards;
    private Timer mTimer; // 创建定时器

    private int normalSpeed=500;
    private int fastSpeed=25;//快速下降的时间ms

    private boolean isFastMode=false;

    private boolean isStarted=false;
    private boolean isPaused=false;
    public OnGameOverListener mGameOverListener;
    public OnScoreChangeListener mScoreChangeListener;
    private int mTotalScore = 0;


    //开始游戏
    public void start(){
        isStarted=true;
        //  先生成预览方块
        //  TODO:给一个临时的数字,BlockView绘制的时候需要回调这个值过来
        // Block previewBlock = BlockManager.defaultManager.getNextBlock();
        //
        // //  将这个方块在预览视图中显示

        // previewView.showBlock(previewBlock);
        if (!isPaused) {//开始新的
            // 清空内容
            boards = new GridModel[mRow][mColumn];

            // 清空分数
            mTotalScore=0;
            showNext();//展示预览和当前方块
        }else {
            isPaused=false;
        }

        startTimer();//开启定时器

    }
    // 开始计时器
    private void startTimer(){
        mTimer=new Timer();
        int time=normalSpeed;
        if (isFastMode) {
            time=fastSpeed;
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                moveDown();
            }
        },0,time);
    }
    private void stopTimer(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    public void pause(){
        isStarted=false;
        isPaused=true;
        stopTimer();
    }

    //  初始化行和列数  提供一个方法  因为是单例设计模式   或者说使用数据回调
    public void initRowAndColumn(int row, int column){
        mRow=row;
        mColumn=column;
        //创建默认面板数组
        boards = new GridModel[mRow][mColumn];
        BlockManager.defaultManager.resetColumn(column);
    }
    // public void initMap(int rows, int cols) {
    //     map = new int[rows][cols];
    // }
    // public int[][] getMap() {
    //     return map;
    // }

    private GameCenter(){}

    // 旋转
    public void rotate() {
        if(!isStarted) return;

        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.rotate();
        // 判断是否碰撞
        if (isCollision()) {
            for (int i = 0; i < 3; i++) {
                block.rotate();
            }
        } else {
            gameView.refresh();
        }

    }

    // 长按快速下滑
    public void dropDown() {
        // if(!isStarted) return;
        if (!isStarted) {
            return;
        }
        if(isFastMode) return;
        isFastMode=true;
        stopTimer();//结束之前的定时器
        startTimer();//开启新的定时器
    }

    // 下移
    public void moveDown() {
        if(!isStarted) return;

        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.moveDown();
        if (isCollision()) {
            // 还原上移
            block.moveUp();

            merge();//合并
            checkEliminateRows();//清除满行
            // 判断游戏是否结束
            if (isGameOver()) {
                stopTimer();//暂停计时器
            //     将游戏结束时间传递给外部
                if (mGameOverListener != null) {
                //     切换到主线程执行任务
                    Activity activity=(Activity) mGameOverListener;
                    activity.runOnUiThread(()->{
                        mGameOverListener.gameover();
                    });
                }
            }else {
                showNext();//显示下一个
                //切换为正常速度
                changeToNormalSpeed();
            }
        } else {
            gameView.refresh();
        }
        // if(block.y+ block.x>=mRow){
        //     mergeShape();
        // }
    }

    //
    // 游戏结束检测  若方块刚合并后顶部已被占满，应终止游戏。
    private boolean isGameOver() {
        for (int j = 0; j < mColumn; j++) {

            if (boards[0][j] != null && boards[0][j].hasBlock()) {
                return true;
            }
        }
        return false;

    }


    private void changeToNormalSpeed() {
        isFastMode=false;
        stopTimer();
        startTimer();
    }

    // 左移
    public void moveLeft() {
        if(!isStarted) return;

        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.moveLeft();

        if (isCollision()) {
            // 还原右移
            block.moveRight();
        } else {
            gameView.refresh();
        }
    }

    // 右移
    public void moveRight() {
        if(!isStarted) return;

        Block block=BlockManager.defaultManager.getCurrentBlock();
        block.moveRight();

        if (isCollision()) {
            // 还原左移
            block.moveLeft();
        } else {
            gameView.refresh();
        }
    }

    // 判断碰撞条件
    private boolean isCollision() {

        // 获取当前操作的block对象
        Block block=BlockManager.defaultManager.getCurrentBlock();
        int[][] shape = block.getShape();//获取二维数组
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] == 1) {
                    int row=block.y+i;
                    int col =block.x+j;

                    if (col<0) return true;//左边界
                    if (col>=mColumn) return true;// 右边界
                    if (row>=mRow) return true;// 下边界

                    //判断面板中固定的方格是否有了
                    GridModel grid = boards[row][col];
                    if(grid!=null&&grid.hasBlock()) return true;
                }
            }
        }
        return false;
    }

    //合并方块
    private void merge() {
        Block block = BlockManager.defaultManager.getCurrentBlock();
        int[][] shape = block.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int row = block.y + i;//第几行
                    int col = block.x + j;//第几列

                    boards[row][col] = new GridModel(1, block.getResourse());
                }
            }
        }
    }

    //排除满行
    private void checkEliminateRows() {

        int count = 0; //记录消除的行数

        for (int i = mRow - 1; i >= 0; i--) {
            boolean fullRow = true;//满行的标记
            for (int j = 0; j < mColumn; j++) {
                if (boards[i][j] == null || !boards[i][j].hasBlock()) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                removeRow(i);
                i++; // 当前行重新检查（因为上面的行下来了）
                count++;
            }
        }
        //计算当前分数
        mTotalScore  += count * 100;

        //将分数回调给外部
        if (mScoreChangeListener != null){
            //切换到主线程执行任务
            Activity activity = (Activity)mScoreChangeListener;
            activity.runOnUiThread(()->{
                mScoreChangeListener.scoreChanged(mTotalScore);
            });
        }

    }

    //移除行操作
    private void removeRow(int row) {
        // 用上面一行去覆盖下面一行
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < mColumn; j++) {
                boards[i][j] = boards[i - 1][j];
            }
        }
        // 最上面一行清空
        for (int j = 0; j < mColumn; j++) {
            boards[0][j] = null;
        }
    }

    // 展示下一个方块
    private void showNext(){
        //生成一个预览方块图形
        Block previewBlock = BlockManager.defaultManager.getNextBlock();
        //  将这个方块在预览视图中显示
        previewView.showBlock(previewBlock);
        //刷新
        gameView.refresh();
    }

    public interface OnGameOverListener{
        void gameover();
    }
    public interface OnScoreChangeListener{
        void scoreChanged(int score);
    }

}
