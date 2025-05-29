package com.ronnie.game.model;

public class BlockManager {
    private Block currentBlock;
    private Block nextBlock;
    private int col;
    public static final BlockManager defaultBlockManager=new BlockManager();

    private BlockManager() {
    }

    public void resetColumn(int col) {
        this.col=col;
    }

    public Block getCurrentBlock(){
        if (currentBlock != null) {
            currentBlock=new Block(5,0);
        }
        return currentBlock;
    }
    public Block getNextBlock(){
        if (currentBlock == null) {
            currentBlock = new Block(col / 2 - 1, 0);

        } else {
            currentBlock=nextBlock;
        }
        nextBlock = new Block(col / 2 - 1, 0);
        return nextBlock;
    }
}
