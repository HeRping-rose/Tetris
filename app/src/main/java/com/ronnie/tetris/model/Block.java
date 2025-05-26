package com.ronnie.tetris.model;

import com.ronnie.tetris.R;

import java.util.Random;

public class Block {
    public int x;   //方块偏移量
    public int y;
    private int blockRes=0; //方块显示图片资源

    // 记录自己的shape二维数组
    private int [][] mShape;

    //  记录当前方块类型
    private BlockType type=BlockType.L;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;

        type=getRandomType();
        blockRes=getRandomRes();
        mShape = BlockShape.getShape(type);
    }

    //  获取图形资源
    public int[][] getShape(){
        return mShape;
    }
    public int getResourse(){
        return blockRes;
    }

    //相关操作方法
    public void rotate() {
        if (type == BlockType.O) return;

        int n = mShape.length;

        // 逐层旋转
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - 1 - layer;

            for (int i = first; i < last; i++) {
                int offset = i - first;

                // 保存左上角的值
                int top = mShape[first][i];

                // 左上角 <- 左下角
                mShape[first][i] = mShape[last - offset][first];

                // 左下角 <- 右下角
                mShape[last - offset][first] = mShape[last][last - offset];

                // 右下角 <- 右上角
                mShape[last][last - offset] = mShape[i][last];

                // 右上角 <- 左上角
                mShape[i][last] = top;
            }
        }
    }
    public void moveDown() {
        y++;
    }
    public void moveUp() {
        y--;
    }
    public void moveLeft() {
        x--;
    }
    public void moveRight() {
        x++;
    }


    //  方块的图形类型
    public enum BlockType{
        L,T,I,S,Z,J,O
    }
    private BlockType getRandomType(){
        //  获取枚举中所有的值组成的数组
        BlockType[] types=BlockType.values();

        //  随机生成index
        Random random =new Random();
        int index = random.nextInt(types.length);
        return types[index];
    }
    private int getRandomRes() {
        int[] resourseArray={
                R.drawable.blue,
                R.drawable.green,
                R.drawable.orange,
                R.drawable.yellow,
                R.drawable.purple,
        };

        Random random = new Random();
        int index = random.nextInt(resourseArray.length);

        return resourseArray[index];
    }
}
