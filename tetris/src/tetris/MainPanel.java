package tetris;

import tetris.repository.ScoreRepository;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

/*
 * Created on 2005/08/17
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;

	// パネルサイズ
    private static final int WIDTH = 368;
    private static final int HEIGHT = 336;

    // ボード
    private Board board;

    // ラベル
    private CenterLabel label;

    // 現在のブロック
    private Block block;
    // 次のブロック
    private Block nextBlock;

    // ブロックのイメージ
    private Image blockImage;

    // ゲームループ用スレッド
    private Thread gameLoop;
    
    // データリポジトリ
    private ScoreRepository scoreRepo;

    private Random rand;
    
    // ゲームシーンステータス
    private static final int TITLE = 0;
    private static final int PLAYING = 1;
    private static final int PAUSE = 2;
    private static final int RESULT = 3;
    private int scene = TITLE;

    MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        // ブロックのイメージをロード
        loadImage("res/image/block.gif");

        scoreRepo = new ScoreRepository();
        rand = new Random();

        board = new Board();
        block = createBlock(board);
        nextBlock = createBlock(board);
        board.fixNextBlock(nextBlock.block, nextBlock.imageNo);
        
        board.setHighScore(scoreRepo.getHighScore());

        label = new CenterLabel(WIDTH, HEIGHT);

        addKeyListener(this);

        try {
            // サウンドをロード
            WaveEngine.load("res/se/kachi42.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        try {
            // BGMをロード
            MidiEngine.load("res/bgm/tetrisb.mid");
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }

        // BGMスタート！
        MidiEngine.play(0);

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void run() {
    	while (true) {
    		switch (scene) {
                case TITLE:
                    runTitle();
                    break;
                case PLAYING:
                    runPlaying();
                    break;
                case PAUSE:
                    runPause();
                    break;
                case RESULT:
                    runResult();
                    break;
    		}
    	}
    }

    private void runTitle() {
        repaint();
        WaveEngine.stop(0);
        WaveEngine.render();
    }
    
    private void runPlaying() {
    	// ブロックを移動する
    	// ブロックが固定されたらtrueが返される
    	boolean isFixed = block.move(Block.DOWN);
    	if (isFixed) { // ブロックが固定されたら
    		// かちゃって鳴らす
    		WaveEngine.play(0);

    		// 次のブロックを作成（ランダムに）
    		if (nextBlock != null) {
    			block = nextBlock;
    		} else {
    			block = createBlock(board);
    		}
    		nextBlock = createBlock(board);
    		board.fixNextBlock(nextBlock.block, nextBlock.imageNo);
    	}

    	// ブロックがそろった行を消す
    	int deletedLines = board.deleteLine();
    	
    	// スコアを加算
    	board.addCurrentScore(board.calcScore(deletedLines));

    	// ゲームオーバーか
    	if (board.isStacked()) {
    		scoreRepo.save(board.getCurrentScore());
    		scene = RESULT;
    	}

    	// WAVEファイルのレンダリング
    	WaveEngine.render();

    	// 再描画
    	repaint();

    	// 休止
    	try {
    		Thread.sleep(200);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    }
    
    private void runPause() {
        repaint();
        WaveEngine.stop(0);
        WaveEngine.render();
    }
    
    private void runResult() {
        repaint();
        WaveEngine.stop(0);
        WaveEngine.render();
    }
    
    /**
     * ゲームを初期化してゲームをスタートする
     */
    private void retryPlaying() {
    	board = new Board();
        block = createBlock(board);
        nextBlock = createBlock(board);
        board.fixNextBlock(nextBlock.block, nextBlock.imageNo);
        board.setHighScore(scoreRepo.getHighScore());
    	scene = PLAYING;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ボード（固定ブロックを含む）を描画
        board.draw(g, blockImage);

        // 落ちてくるブロックを描画
        block.draw(g, blockImage);

        switch (scene) {
            case TITLE:
                label.draw(g, "PRESS BUTTON");
                break;
            case PLAYING:
                break;
            case PAUSE:
                label.draw(g, "PAUSE");
                break;
            case RESULT:
                label.draw(g, "GAME OVER");
                break;
        }
    }

    /**
     * ランダムに次のブロックを作成
     * 
     * @param board ボードへの参照
     * @return ブロック
     */
    private Block createBlock(Board board) {
        int blockNo = rand.nextInt(7);
        switch (blockNo) {
            case Block.BAR :
                return new BarBlock(board);
            case Block.Z_SHAPE :
                return new ZShapeBlock(board);
            case Block.SQUARE :
                return new SquareBlock(board);
            case Block.L_SHAPE :
                return new LShapeBlock(board);
            case Block.REVERSE_Z_SHAPE :
                return new ReverseZShapeBlock(board);
            case Block.T_SHAPE :
                return new TShapeBlock(board);
            case Block.REVERSE_L_SHAPE :
                return new ReverseLShapeBlock(board);
        }

        return null;
    }

    /**
     * ブロックのイメージをロード
     * 
     * @param filename 画像名
     */
    private void loadImage(String filename) {
        // ブロックのイメージを読み込む
        // ImageIconを使うとMediaTrackerを使わなくてすむ
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        blockImage = icon.getImage();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (scene) {
            case TITLE:
                scene = PLAYING;
                break;
            case PLAYING:
                if (key == KeyEvent.VK_LEFT) { // ブロックを左へ移動
                    block.move(Block.LEFT);
                } else if (key == KeyEvent.VK_RIGHT) { // ブロックを右へ移動
                    block.move(Block.RIGHT);
                } else if (key == KeyEvent.VK_DOWN) { // ブロックを下へ移動
                    block.move(Block.DOWN);
                    board.addCurrentScore(1);
                } else if (key == KeyEvent.VK_UP) { // ブロックを回転
                    block.turn();
                } else if (key == KeyEvent.VK_SPACE) {
                    int dropped = block.drop();	// ブロックを落とす
                    board.addCurrentScore(dropped * 2);
                } else if (key == KeyEvent.VK_P) {
                    scene = PAUSE;	// ポーズ
                }
                repaint();
                break;
            case PAUSE:
                scene = PLAYING;
                break;
            case RESULT:
                retryPlaying();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}