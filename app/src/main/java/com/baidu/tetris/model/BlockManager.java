package com.baidu.tetris.model;

//单例设计模式
public class BlockManager {
    private Block currentBlock;
    private Block nextBlock;
    private int column;
    //提供给外部一个方法 得到这个类的对象
    public static final BlockManager defaultManager = new BlockManager();

    //将当前类的构造方法私有化
    //外部无法直接创建这个类的对象
    private BlockManager(){}

    public void resetColumn(int column){
        this.column = column;
    }

    //获取下一个方块
    public Block getNextBlock(){
        //判断是不是第一次生成方块
        if (currentBlock == null){
            //第一次同时生成两个方块
            //每个方块都是从顶部中心位置掉落下来
            //所以y为0，x位横向中心
            currentBlock = new Block(column/2-1,0);
        }else{
            //将下一个作为当前正在操作的方块
            currentBlock = nextBlock;
        }

        nextBlock = new Block(column/2-1,0);

        return nextBlock;
    }

    //获取正在操作的方块
    public Block getCurrentBlock(){
        return currentBlock;
    }
}
