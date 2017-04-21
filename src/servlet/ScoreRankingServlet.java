package servlet;

import repository.ScoreRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;

@WebServlet(name = "ScoreRankingServlet")
public class ScoreRankingServlet extends HttpServlet {

    private static int GET_RANKING_COUNT = 10;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ScoreRepository repository = new ScoreRepository();

        // DBから値を取得する
        int[] ranking = repository.getRanking(GET_RANKING_COUNT);
        repository.closeConnection();

        // 値が取得できたか確認
        if (ranking.length == 0) {
            System.out.println("Get ranking failure.");
            return;
        }

        // ランキングの値を、レスポンス用に成型する
        StringBuilder builder = new StringBuilder(String.valueOf(ranking[0]));
        for (int i = 1; i < ranking.length; i++) {
            builder.append(",");
            builder.append(String.valueOf(ranking[i]));
        }
        String resString = builder.toString();
        System.out.println(resString);

        // レスポンスを返す
        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        out.writeUTF(resString);
        out.close();
    }
}
