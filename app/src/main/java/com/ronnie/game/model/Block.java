package com.ronnie.game.model;

import com.ronnie.game.R;

import java.util.Random;

public class Block {
    //     设置方块偏移量
    public int x;
    public int y;
    private int blockRes;
    private int[][] shape;
    private BlockType blockType;


    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        blockType = getRandomType();
        blockRes = getRandomBlockRes();
        shape = BlockShape.getShape(blockType);//获取形状

    }

    public int getBlockRes() {
        return blockRes;
    }

    public int[][] getShape() {
        return shape;
    }

    private BlockType getRandomType() {
        BlockType[] types = BlockType.values();
        Random random = new Random();
        int index = random.nextInt(types.length);
        return types[index];
    }


    private int getRandomBlockRes() {
        int[] resArray = {
                R.drawable.blue,
                R.drawable.yellow,
                R.drawable.orange,
                R.drawable.green,
                R.drawable.purple,
        };
        Random random = new Random();
        int index = random.nextInt(resArray.length);
        return resArray[index];
    }


    public enum BlockType {
        L, T, I, S, O, J, Z
    }
}
