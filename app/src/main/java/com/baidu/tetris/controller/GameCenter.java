package com.baidu.tetris.controller;

import android.app.Activity;

import com.baidu.tetris.model.Block;
import com.baidu.tetris.model.BlockManager;
import com.baidu.tetris.model.GridModel;
import com.baidu.tetris.ui.view.BlockPreviewView;
import com.baidu.tetris.ui.view.GameView;

import java.util.Timer;
import java.util.TimerTask;

public class GameCenter {
    //提供一个单例对象
    public static final GameCenter defaultCenter = new GameCenter();
    public BlockPreviewView previewView;
    public GameView gameView;
    private int mRow;
    private int mColumn;
    public GridModel[][] boards;
    private Timer mTimer;
    private int mNormalSpeed = 500;
    private int mFastSpeed = 10;
    private boolean isFastMode = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    public OnGameOverListener mGameOverListener;
    public OnScoreChangeListener mScoreChangeListener;
    private int mTotalScore = 0;
    private boolean mGameOver = false;

    //配置游戏面板的行和列数
    public void initRowAndColumn(int row,int column){
        mRow = row;
        mColumn = column;

        //创建默认面板数组
        boards = new GridModel[mRow][mColumn];

        BlockManager.defaultManager.resetColumn(column);
    }

    //开始游戏
    public void start(){
        isStarted = true;

        if (!isPaused) { //开始新的
            //清空内容
            boards = new GridModel[mRow][mColumn];
            //清空分数
            mTotalScore = 0;
            //先展示预览和当前方块
            showNext();
        }else{ //暂停之后开始
            isPaused = false;
        }

        //开启定时器
        startTimer();
    }

    public void pause(){
        isStarted = false;
        isPaused = true;
        stopTimer();
    }

    private void startTimer(){
        mTimer = new Timer();
        int time = mNormalSpeed;
        if (isFastMode){
            time = mFastSpeed;
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
        }
    }



    private GameCenter(){}

    //旋转
    public void rotate() {
        if (!isStarted) return;

        //获取当前操作的block对象
        Block block = BlockManager.defaultManager.getCurrentBlock();
        //对block旋转
        block.rotate();
        //提前判断是否碰壁
        if (isCollision()){
            //还原 上移
            for (int i = 0; i < 3; i++) {
                block.rotate();
            }
        }else {
            //刷新游戏视图
            gameView.refresh();
        }
    }

    public void moveLeft() {
        if (!isStarted) return;

        //获取当前操作的block对象
        Block block = BlockManager.defaultManager.getCurrentBlock();
        block.moveLeft();
        //提前判断是否碰壁
        if (isCollision()){
            //还原 右移
            block.moveRight();
        }else {
            //刷新游戏视图
            gameView.refresh();
        }
    }

    public void moveRight() {
        if (!isStarted) return;

        //获取当前操作的block对象
        Block block = BlockManager.defaultManager.getCurrentBlock();
        block.moveRight();
        //提前判断是否碰壁
        if (isCollision()){
            //还原 左移
            block.moveLeft();
        }else {
            //刷新游戏视图
            gameView.refresh();
        }
    }

    //直接掉落到底部
    public void dropDown(){
        if (!isStarted) return;
        if (isFastMode) return;
        isFastMode = true;
        stopTimer();
        startTimer();
    }

    private void changeToNormalSpeed(){
        isFastMode = false;
        stopTimer();
        startTimer();
    }

    //一次下落一格
    public void moveDown() {
        if (!isStarted) return;

        //获取当前操作的block对象
        Block block = BlockManager.defaultManager.getCurrentBlock();
        block.moveDown();
        //提前判断是否碰壁
        if (isCollision()){
            //还原 上移
            block.moveUp();
            //合并
            merge();
            //清楚方格
            clearLines();
            //判断游戏是否结束
            if (isGameOver()){
                mGameOver = true;

                //暂停定时器
                stopTimer();
                //将游戏结束的事件传递给外部
                if (mGameOverListener != null){
                    //切换到主线程执行任务
                    Activity activity = (Activity)mGameOverListener;
                    activity.runOnUiThread(()->{
                        mGameOverListener.gameOver();
                    });

                }
            }else {
                //显示下一个
                showNext();
                //切换为正常速度
                changeToNormalSpeed();
            }
        }else {
            //刷新游戏视图
            gameView.refresh();
        }
    }

    //判断游戏是否结束
    private boolean isGameOver(){
        for (int column = 0; column < mColumn; column++) {
            GridModel grid = boards[0][column];
            if (grid != null){
                return  true;
            }
        }

        return false;
    }

    //清楚满一行的方格
    private void clearLines(){
        //从boards数组最底部开始判断
        boolean isFull;
        int count = 0;
        for (int i = mRow-1; i >= 0 ; i--) {
            //判断这一行是否已经满了
            isFull = true;
            for (int j = 0; j < mColumn; j++) {
                //获取i-j对应的方格模型对象
                GridModel grid = boards[i][j];
                if (grid == null || !grid.hasBlock()){
                    isFull = false;
                    break;
                }
            }
            if (isFull){
                //需要将i行上面的每一行都向下移动一行
                //用上面一行的内容去覆盖下一面一行的内容
                for (int k = i; k > 0 ; k--) {
                    System.arraycopy(boards[k-1],0,boards[k],0,mColumn);
                }
                //清空第一行的内容
                for (int l = 0; l < mColumn ; l++) {
                    boards[0][l] = null;
                }

                //确保下一次是判断掉落下来的一行的状态
                i++;

                count++;
            }
        }

        //if (count == 0) return;

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

    private boolean isCollision(){
        //获取当前操作的block对象
        Block block = BlockManager.defaultManager.getCurrentBlock();
        //获取二维数据
        int[][] shape = block.getShape();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] == 1){
                    int row = block.y + i;
                    int column = block.x + j;
                    //左边界
                    if (column < 0) return true;
                    //右边界
                    if (column >= mColumn) return true;
                    //下边界
                    if (row >= mRow) return  true;
                    //判断面板中固定的方格是否有了
                    GridModel grid = boards[row][column];
                    if (grid != null && grid.hasBlock()) return true;
                }
            }
        }

        return false;
    }

    //合并当前方格到游戏面板方格中
    private void merge(){
       Block block =  BlockManager.defaultManager.getCurrentBlock();
       int[][] shape = block.getShape();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] == 1){
                    //获取i j对应的方格在面板中的位置
                    int row = block.y + i;
                    int column = block.x + j;
                    boards[row][column] = new GridModel(1,block.getResource());
                }
            }
        }
    }

    //显示下一个
    private void showNext(){
        //生成一个预览方快图形
        Block previewBlock = BlockManager.defaultManager.getNextBlock();
        //将这个方块在预览视图中显示
        previewView.showBlock(previewBlock);
        gameView.refresh();
    }

    //定义接口 协议
    public interface OnGameOverListener{
        void gameOver();
    }
    public interface OnScoreChangeListener{
        void scoreChanged(int score);
    }
}








