package tetris;

/*
 * Created on 2005/08/17
 *
 */

/**
 * @author mori
 *
 */
public class SquareBlock extends Block {
    public SquareBlock(Board board) {
        super(board);

        // □□□□
        // □■■□
        // □■■□
        // □□□□
        block[1][1] = 1;
        block[1][2] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        
        imageNo = Block.SQUARE;
    }
}
