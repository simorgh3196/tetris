package tetris;

/*
 * Created on 2005/08/20
 *
 */

/**
 * @author mori
 *
 */
public class ZShapeBlock extends Block {
    public ZShapeBlock(Board board) {
        super(board);

        // □□□□
        // □□■□
        // □■■□
        // □■□□
        block[1][2] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        block[3][1] = 1;
        
        imageNo = Block.Z_SHAPE;
    }
}
