package com.ronnie.tetris.model;

public class BlockManager {
    private Block currentBlock;
    private Block nextBlock;
    private int column;

    public static final BlockManager defaultManager=new BlockManager();//单例设计模式

    //  将当前类的构造方法私有化 外部无法创建这个类的对象
    private BlockManager(){
    }

    // 重置列方法
    public void resetColumn(int column) {
        this.column=column;
    }

    //  获取下一个方块
    public Block getNextBlock(){
        //  判断是不是第一次生成方块
        if (currentBlock == null) {
            //  每个方块都是从顶部中心位置掉落下来,y位0,x为纵向中心
            currentBlock = new Block(column / 2 - 1, 0);
        } else {
            //  将下一个作为当前正在操作的方块
            currentBlock=nextBlock;
        }
        nextBlock =new Block(column / 2 - 1, 0);

        return nextBlock;
    }

    // 每次落地后调用

    //  获取当前方块
    public Block getCurrentBlock( int column) {
        if (currentBlock == null) {
            //  每个方块都是从顶部中心位置掉落下来,y位0,x为纵向中心
            currentBlock = new Block(column / 2 - 1, 0);
        }

        return currentBlock;
    }

    public Block getCurrentBlock() {
        // if (currentBlock == null){
        //     //TODO x待定
        //     currentBlock = new Block(column/2-1,0);
        // }
        return currentBlock;
    }


}
