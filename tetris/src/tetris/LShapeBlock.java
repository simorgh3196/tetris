package tetris;

/*
 * Created on 2005/08/20
 *
 */

/**
 * @author mori
 *
 */
public class LShapeBlock extends Block {
    public LShapeBlock(Board board) {
        super(board);

        // □□□□
        // □■■□
        // □□■□
        // □□■□
        block[1][1] = 1;
        block[1][2] = 1;
        block[2][2] = 1;
        block[3][2] = 1;
        
        imageNo = Block.L_SHAPE;
    }
}
