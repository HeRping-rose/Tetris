package com.baidu.tetris.model;

import com.baidu.tetris.R;

import java.util.Random;

public class Block {
    //方块在面板中的偏移量
    public int x;
    public int y;
    //方块显示的图片资源
    private int blockRes = 0;
    //记录当前方块的类型
    private BlockType type = BlockType.L;
    //记录自己的shape二维数组
    private int[][] mShape;

    public Block(int x,int y) {
        this.y = y;
        this.x = x;
        //随机产生这个方块的类型
        type = getRandomType();
        //随机产生一个图片资源
        blockRes = getRandomRes();
        //获取当前这个type对应的shape
        mShape = BlockShape.getShape(type);
    }

    //返回图片资源id
    public int getResource(){
        return blockRes;
    }

    //获取图形对应的二维数组
    public int[][] getShape(){
        return mShape;
    }

    //左移
    public void moveLeft(){
        x--;
    }
    //右移
    public void moveRight(){
        x++;
    }
    //下移
    public void moveDown(){
        y++;
    }
    //上移
    public void moveUp(){
        y--;
    }

    //旋转
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

    //方块的图形类型
    public enum BlockType{
        L,T,I,S,Z,J,O
    }

    //随机生成一个类型
    private BlockType getRandomType(){
        //获取枚举中所有的值组成的数组
        BlockType[] types = BlockType.values();

        //随机生成一个索引值
        Random random = new Random();
        int index = random.nextInt(types.length);
        return types[index];
    }

    //随机生成一个资源
    private int getRandomRes(){
        int[] resourceArray = {
                R.drawable.blue,
                R.drawable.green,
                R.drawable.orange,
                R.drawable.yellow,
                R.drawable.purple,
        };
        //随机生成一个索引值
        Random random = new Random();
        int index = random.nextInt(resourceArray.length);

        return resourceArray[index];
    }
}
