package tetris;

import java.awt.*;

/*
 * Created on 2005/08/17
 *
 */

/**
 * @author mori
 *
 */
public class Board {
    // フィールドのサイズ
    static final int MAX_X = 12;
    private static final int MAX_Y = 21;

    // マスのサイズ
    static final int TILE_SIZE = 16;

    // ボード
    private int[][] board;
    // ボードのイメージ
    private int[][] boardImage;

    // サイドボード
    private SideBoard sideBoard;

    private int score = 0;

    public Board() {
        board = new int[MAX_Y][MAX_X];
        boardImage = new int[MAX_Y][MAX_X];
        sideBoard = new SideBoard();
        init();
    }

    /**
     * フィールドを初期化する
     */
    private void init() {
        for (int y = 0; y < MAX_Y; y++) {
            for (int x = 0; x < MAX_X; x++) {
                // 壁をつくる
                if (x == 0 || x == MAX_X - 1) {
                    board[y][x] = Block.TYPE_BLOCK;
                    boardImage[y][x] = Block.WALL;
                } else if (y == MAX_Y - 1) {
                    board[y][x] = Block.TYPE_BLOCK;
                    boardImage[y][x] = Block.WALL;
                } else {
                    board[y][x] = Block.TYPE_NONE;
                }
            }
        }
    }
    
    /**
     * ボード（固定ブロックを含む）の描画
     * @param g 描画オブジェクト
     */
    void draw(Graphics g, Image blockImage) {
        for (int y = 0; y < MAX_Y; y++) {
            for (int x = 0; x < MAX_X; x++) {
                if (board[y][x] == Block.TYPE_BLOCK || board[y][x] == Block.TYPE_GHOST_BLOCK) {
                    g.drawImage(blockImage, x * TILE_SIZE, y * TILE_SIZE, x * TILE_SIZE + TILE_SIZE, y * TILE_SIZE + TILE_SIZE,
                            boardImage[y][x] * TILE_SIZE, 0, boardImage[y][x] * TILE_SIZE + TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
        
        // サイドボードを描画
        sideBoard.draw(g, blockImage);
    }

    /**
     * ブロックを移動できるか調べる
     * @param newPos ブロックの移動先座標
     * @param block ブロック
     * @return 移動できたらtrue
     */
    boolean isMovable(Point newPos, int[][] block) {
        for (int y = 0; y < Block.MAX_Y; y++) {
            for (int x = 0; x < Block.MAX_X; x++) {
                if (block[y][x] == 1) {  // 4×4内でブロックのあるマスのみ調べる
                    if (newPos.y + y < 0) {  // そのマスが画面の上端外のとき
                        // ブロックのあるマスが壁のある0列目以下または
                        // MAX_X-1列目以上に移動しようとしてる場合は移動できない
                        if (newPos.x + x <= 0 || newPos.x + x >= MAX_X - 1) {
                            return false;
                        }
                    } else if (board[newPos.y + y][newPos.x + x] == 1) {  // そのマスが画面内のとき
                        // 移動先にすでにブロックがある場合は移動できない
                        return false;
                    }
                }
            }
        }

        return true;
    }

    void paintGhostBlock(Point pos, int[][] block, int imageNo) {
    	Point newPos = getFallingPosition(pos, block);
    	removeGhostBlocks();
    	fixGhostBlock(newPos, block, imageNo);
    }

    /**
     * ゴーストのブロックを出す位置を返す
     * 
     * @param pos 現在のブロックの位置
     * @param block ブロックの形
     * @return ゴーストブロックの位置
     */
    Point getFallingPosition(Point pos, int[][] block) {
     	Point newPos = new Point(pos);
     	while (true) {
     		newPos.y += 1;
     		if (!isMovable(newPos, block)) {
     			newPos.y -= 1;
     			return newPos;
     		}
     	}
    }

    /**
     * ブロックのゴーストを置く
     * 
     * @param pos ブロックの位置
     * @param block ブロック
     * @param imageNo ブロックの色
     */
    private void fixGhostBlock(Point pos, int[][] block, int imageNo) {
        for (int y = 0; y < Block.MAX_Y; y++) {
            for (int x = 0; x < Block.MAX_X; x++) {
                if (block[y][x] != 1 || pos.y + y < 0) { continue; }
                board[pos.y + y][pos.x + x] = 2;
                boardImage[pos.y + y][pos.x + x] = imageNo + 8;
            }
        }
    }
    
    /**
     * ゴーストのブロックを取り除く
     */
    void removeGhostBlocks() {
        for (int y = 0; y < MAX_Y; y++) {
            for (int x = 0; x < MAX_X; x++) {
                if (board[y][x] != 2) { continue; }
                board[y][x] = 0;
                boardImage[y][x] = 0;
            }
        }
    }
    
    /**
     * 次に落ちてくるブロックを表示
     * 
     * @param block ブロック
     * @param imageNo ブロックの色
     */
    void fixNextBlock(int[][] block, int imageNo) {
    	sideBoard.fixNextBlock(block, imageNo);
    }

    /**
     * 落ちきったブロックをボードに固定する
     * 
     * @param pos ブロックの位置
     * @param block ブロック
     * @param imageNo ブロックの色
     */
    void fixBlock(Point pos, int[][] block, int imageNo) {
        for (int y = 0; y < Block.MAX_Y; y++) {
            for (int x = 0; x < Block.MAX_X; x++) {
                if (block[y][x] != 1 || pos.y + y < 0) { continue; }
                board[pos.y + y][pos.x + x] = 1;
                boardImage[pos.y + y][pos.x + x] = imageNo;
            }
        }
    }
    
    /**
     * そろった行を削除
     */
    int deleteLine() {
    	int deleteCount = 0;
        for (int y = 0; y < MAX_Y - 1; y++) {
            int count = 0;
            for (int x = 1; x < MAX_X - 1; x++) {
                // ブロックがある列を数える
                if (board[y][x] == 1) count++;
            }
            // そろった行が見つかった
            if (count == Board.MAX_X - 2) {
                // その行を消去
            	deleteCount++;
                for (int x = 1; x < MAX_X - 1; x++) {
                    board[y][x] = 0;
                }
                // それより上の行を落とす
                for (int ty = y; ty > 0; ty--) {
                    for (int tx = 1; tx < MAX_X-1; tx++) {
                        board[ty][tx] = board[ty - 1][tx];
                        boardImage[ty][tx] = boardImage[ty - 1][tx];
                    }
                }
            }
        }
        
        return deleteCount;
    }
    
    /**
     * ブロックが積み上がってるか
     * @return 最上行まで積み上がってたらtrue
     */
    boolean isStacked() {
        for (int x = 1; x < MAX_X - 1; x++) {
            if (board[0][x] == 1) {
                return true;
            }
        }
        
        return false;
    }
    
    void addCurrentScore(int score) {
    	sideBoard.addCurrentScore(score);
    }
    
    int getCurrentScore() {
    	return sideBoard.getCurrentScore();
    }
    
    void setHighScore(int score) {
    	sideBoard.setHighScore(score);
    }
    
    int calcScore(int deleteLine) {
    	if (deleteLine == 1) {
    		return 100;
    	} else if (deleteLine == 2) {
    		return 250;
    	} else if (deleteLine == 3) {
    		return 450;
    	} else if (deleteLine == 4) {
    		return 800;
    	} else {
    		return 0;
    	}
    }
}
