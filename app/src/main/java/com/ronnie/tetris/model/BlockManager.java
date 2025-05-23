package com.ronnie.tetris.model;

public class BlockManager {
    private Block currentBlock;
    private Block nextBlock;
    public static final BlockManager defaultManager=new BlockManager();//单例设计模式

    //  将当前类的构造方法私有化 外部无法创建这个类的对象
    private BlockManager(){
    }

    //  获取下一个方块
    public Block getNextBlock( int column){
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

    //  获取当前方块
    public Block getCurrentBlock( int column) {
        if (currentBlock == null) {
            //  每个方块都是从顶部中心位置掉落下来,y位0,x为纵向中心
            currentBlock = new Block(column / 2 - 1, 0);
        }

        return currentBlock;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

}
