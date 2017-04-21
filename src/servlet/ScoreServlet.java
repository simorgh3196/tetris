package servlet;

import repository.ScoreRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@WebServlet(name = "ScoreServlet")
public class ScoreServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Get save request");
        String resString;
        try {
            // パラメータを受け取る
            DataInputStream in = new DataInputStream(request.getInputStream());
            int score = in.readInt();

            // DBに保存する
            ScoreRepository repository = new ScoreRepository();
            repository.save(score);
            repository.closeConnection();

            // ログ
            System.out.println("Saved score " + score);

            resString = "Save success";
        } catch (NumberFormatException e) {
            System.out.println("Invalid parameter.");
            resString = "Save failure";
        }

        // レスポンスを返す
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        out.writeUTF(resString);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DBから値を取得する
        ScoreRepository repository = new ScoreRepository();
        int highScore = repository.getHighScore();
        repository.closeConnection();

        // レスポンスを返す
        DataOutputStream out = new DataOutputStream(response.getOutputStream());
        out.writeInt(highScore);
        out.close();
    }
}
