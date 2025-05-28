package com.ronnie.tetris.model;

public class BlockShape {
    private static int[][] SHAPE_L={
        {0,1,0},
        {0,1,0},
        {0,1,1}
    };
    private static int[][] SHAPE_T={
            {0,0,0},
            {1,1,1},
            {0,0,0}
    };
    private static int[][] SHAPE_I={
            {0,1,0},
            {0,1,0},
            {0,1,0}
    };
    private static int[][] SHAPE_S={
            {0,1,1},
            {1,1,0},
            {0,0,0}
    };
    private static int[][] SHAPE_Z={
            {1,1,0},
            {0,1,1},
            {0,0,0}
    };
    private static int[][] SHAPE_J={
            {0,1,0},
            {0,1,0},
            {1,1,0}
    };
    private static int[][] SHAPE_O={
            {1,1},
            {1,1}
    };

    public static int[][] getShape(Block.BlockType type) {
        switch (type) {
            case T:return SHAPE_T;
            case I:return SHAPE_I;
            case S:return SHAPE_S;
            case Z:return SHAPE_Z;
            case J:return SHAPE_J;
            case O:return SHAPE_O;
            default:return SHAPE_L;
        }
    }
}
