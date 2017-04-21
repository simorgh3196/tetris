package tetris;

import java.awt.*;

public class SideBoard {
    // サイドフィールドのサイズ
    public static final int MAX_X = 11;
    public static final int MAX_Y = 21;

    // マスのサイズ
    public static final int TILE_SIZE = 16;

    // ボード
    private int[][] board;
    // ボードのイメージ
    private int[][] boardImage;
    
    private int INSET_X = Board.MAX_X * Board.TILE_SIZE;
    
    private int currentScore;
    private int highScore;
    
    
    /*
     * ■■■■■■■■■■■
     * □□□□□□□□□□■
     * □                □■
     * □                □■
     * □   NEXT BLOCK   □■
     * □                □■
     * □                □■
     * □□□□□□□□□□■
     * ■■■■■■■■■■■
     * □□□□□□□□□□■
     * □                □■
     * □ CURRENT  SCORE □■
     * □                □■
     * □□□□□□□□□□■
     * ■■■■■■■■■■■
     * □□□□□□□□□□■
     * □                □■
     * □   HIGH  SCORE  □■
     * □                □■
     * □□□□□□□□□□■
     * ■■■■■■■■■■■
     */

    public SideBoard() {
        board = new int[MAX_Y][MAX_X];
        boardImage = new int[MAX_Y][MAX_X];
        init();
    }

    /**
     * フィールドを初期化する
     */
    public void init() {
        for (int y = 0; y < MAX_Y; y++) {
            for (int x = 0; x < MAX_X; x++) {
                // 壁をつくる
                if (y == 0) {	// トップ
                    board[y][x] = 1;
                    boardImage[y][x] = Block.WALL;
                } else if (y == 8) {	// ブロックの下
                    board[y][x] = 1;
                    boardImage[y][x] = Block.WALL;
                } else if (y == 14) {	// 現在スコアの下
                    board[y][x] = 1;
                    boardImage[y][x] = Block.WALL;
                } else if (y == MAX_Y - 1) {	// ボトム
                    board[y][x] = 1;
                    boardImage[y][x] = Block.WALL;
                } else if (x == MAX_X - 1) {
                    board[y][x] = 1;
                    boardImage[y][x] = Block.WALL;
                } else {
                    board[y][x] = 0;
                }
            }
        }
    }
    
    /**
     * サイドボードの描画
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g, Image blockImage) {
    	// 次のブロックを表示
        for (int y = 0; y < MAX_Y; y++) {
            for (int x = 0; x < MAX_X; x++) {
                if (board[y][x] == Block.TYPE_BLOCK) {
                    g.drawImage(blockImage,
                    		INSET_X + x * TILE_SIZE, y * TILE_SIZE,
                    		INSET_X + x * TILE_SIZE + TILE_SIZE, y * TILE_SIZE + TILE_SIZE,
                            boardImage[y][x] * TILE_SIZE, 0, boardImage[y][x] * TILE_SIZE + TILE_SIZE, TILE_SIZE, null);
                }
            }
        }

        Font scoreLabelFont = new Font("", Font.BOLD, 26);
        g.setFont(scoreLabelFont);
        g.setColor(Color.red);

        g.drawString("Score", INSET_X + 5, 10 * TILE_SIZE + 10);
        g.drawString("HighScore", INSET_X + 5, 16 * TILE_SIZE + 10);

        Font font = new Font("Arial", Font.BOLD, 26);
        g.setFont(font);
        g.setColor(Color.black);
        FontMetrics fm = g.getFontMetrics();

        // 現在スコアの表示
        String currentScoreText = String.valueOf(currentScore);
        int currentScoreX = INSET_X + 9 * TILE_SIZE;
        int currentScoreY = 13 * TILE_SIZE;
		Rectangle currentScoreRect = fm.getStringBounds(currentScoreText, g).getBounds();
		currentScoreX -= currentScoreRect.width;
        g.drawString(currentScoreText, currentScoreX, currentScoreY);

        // ハイスコアの表示
        String highScoreText = String.valueOf(highScore);
        int highScoreX = INSET_X + 9 * TILE_SIZE;
        int highScoreY = 19 * TILE_SIZE;
		Rectangle highScoreRect = fm.getStringBounds(highScoreText, g).getBounds();
		highScoreX -= highScoreRect.width;
        g.drawString(highScoreText, highScoreX, highScoreY);
    }
    
    /**
     * 次に落ちるブロックを設定する
     * 
     * @param block
     * @param imageNo
     */
    public void fixNextBlock(int[][] block, int imageNo) {
        for (int y = 0; y < Block.MAX_Y; y++) {
            for (int x = 0; x < Block.MAX_X; x++) {
                if (block[y][x] == 1) {
                	board[2 + y][3 + x] = Block.TYPE_BLOCK;
                	boardImage[2 + y][3 + x] = imageNo;
                } else {
                	board[2 + y][3 + x] = Block.TYPE_NONE;
                	boardImage[2 + y][3 + x] = 0;
                }
            }
        }
    }
    
    /**
     * 現在スコアにスコアを追加する
     * 
     * @param score 追加するスコア
     */
    public void addCurrentScore(int score) {
    	currentScore += score;
    }
    
    /**
     * 現在スコアを取得する
     * 
     * @return 現在スコア
     */
    public int getCurrentScore() {
    	return currentScore;
    }
    
    /**
     * 最高スコアを設定する
     * 
     * @param score 設定するスコア
     */
    public void setHighScore(int score) {
    	highScore = score;
    }
}
