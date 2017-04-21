package repository;

import java.sql.*;

public class ScoreRepository {

    private Connection connection;

    private static final String HOST_NAME = "";
    private static final String PORT = "";
    private static final String DB_TABLE_NAME = "";
    private static final String DB_URL = "jdbc:mysql://" + HOST_NAME + ":" + PORT + "/" + DB_TABLE_NAME + "?useSSL=false";
    private static final String DB_USER_NAME = "****";
    private static final String DB_USER_PASSWORD = "****";

    public ScoreRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_USER_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("ドライバを読み込めませんでした " +  e);
        } catch (SQLException e) {
            System.out.println("データベース接続エラー" +  e);
        }
    }

    /**
     * データベースとのコネクションを閉じる
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("データベース接続エラー" +  e);
        }
    }

    /**
     * スコア上位者を取得する
     *
     * @param count 取得件数
     * @return スコアランキング(降順)
     */
    public int[] getRanking(int count) {
        int[] ranking = new int[count];
        String sql = "select * from tetris_score_tbl order by score desc limit ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, count);
            ResultSet rs = pstmt.executeQuery();

            int index = 0;
            while(rs.next()) {
                int score = rs.getInt(2);
                ranking[index++] = score;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("データベース接続エラー" +  e);
        }

        return ranking;
    }

    /**
     * ハイスコアを取得する
     *
     * @return ハイスコア
     */
    public int getHighScore() {
        String sql = "select * from tetris_score_tbl order by score desc limit 1";
        int highScore = 0;

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            highScore = rs.getInt(2);
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("データベース接続エラー" +  e);
        }

        return highScore;
    }

    /**
     * スコアをデータベースに保存する
     *
     * @param score 保存するスコア
     */
    public void save(int score) {
        String sql = "insert into tetris_score_tbl (score) values (?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, score);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("データベース接続エラー" +  e);
        }
    }

}


