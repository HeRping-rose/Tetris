package com.ronnie.tetris.model;

import com.ronnie.tetris.R;

import java.util.Random;

public class Block {
    public int x;   //方块偏移量
    public int y;
    private int blockRes=0; //方块显示图片资源

    //  记录当前方块类型
    private BlockType type=BlockType.L;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;

        type=getRandomType();
        blockRes=getRandomRes();
    }

    //  获取图形资源
    public int[][] getShape(){
        return BlockShape.getShape(type);

    }
    public int getResourse(){
        return blockRes;
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
