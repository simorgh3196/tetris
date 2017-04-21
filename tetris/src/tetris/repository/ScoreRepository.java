package tetris.repository;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ScoreRepository {

    private static URL scoreURL;
    private static URL rankingURL;

    public ScoreRepository() {
        try {
            scoreURL = new URL("http://133.68.13.152:8080/api/score");
            rankingURL = new URL("http://133.68.13.152:8080/api/ranking");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ハイスコアを取得する
     *
     * @return ハイスコア
     */
    public int getHighScore() {
        try {
            // コネクションを繋ぐ
            HttpURLConnection conn = (HttpURLConnection) scoreURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();

            // レスポンスを受ける
            DataInputStream in = new DataInputStream(conn.getInputStream());
            int highScore = in.readInt();
            in.close();

            // コネクションを閉じる
            conn.disconnect();

            return highScore;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * スコアをデータベースに保存する
     *
     * @param score 保存するスコア
     */
    public void save(int score) {
        System.out.println("send save");
        try {
            // コネクションを繋ぐ
            HttpURLConnection conn = (HttpURLConnection) scoreURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // データを付与する
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeInt(score);
            out.flush();
            out.close();

            // 送信
            conn.connect();

            // レスポンスを受ける
            DataInputStream in = new DataInputStream(conn.getInputStream());
            String result = in.readUTF();
            System.out.println(result);
            in.close();

            // コネクションを閉じる
            conn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}