package tetris;

/*
 * Created on 2005/08/20
 *
 */

/**
 * @author mori
 *
 */
public class ReverseZShapeBlock extends Block {
    public ReverseZShapeBlock(Board board) {
        super(board);

        // □□□□
        // □■□□
        // □■■□
        // □□■□
        block[1][1] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        block[3][2] = 1;
        
        imageNo = Block.REVERSE_Z_SHAPE;
    }
}
