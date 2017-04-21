<%@ page import="repository.ScoreRepository" %>
<%--
  Created by IntelliJ IDEA.
  User: thayakawa
  Date: 2017/04/17
  Time: 13:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Tetris</title>
  </head>
  <body>

    <h1>Tetris</h1>
    <applet code="tetris.Tetris" codebase="/Resources" width="368" height="336">
      Applet not found
    </applet>

    <hr>

    <%
      ScoreRepository repository = new ScoreRepository();
      int[] scores = repository.getRanking(10);
      repository.closeConnection();
    %>

    <h1>Help</h1>
    <table border=1>
      <tr> <th>Key</th>   <th>Action</th>               </tr>
      <tr> <td>Up</td>    <td>Turn right</td>           </tr>
      <tr> <td>Left</td>  <td>Move block to left</td>   </tr>
      <tr> <td>Right</td> <td>Move block to right</td>  </tr>
      <tr> <td>Down</td>  <td>Move block to bottom</td> </tr>
      <tr> <td>Space</td> <td>Drop block</td>           </tr>
      <tr> <td>P</td>     <td>Pause</td>                </tr>
    </table>

    <h1>Ranking</h1>
    <table border=1>
      <tr>
        <th>Rank</th><th>Score</th>
      </tr>
      <% for (int i = 0; i < scores.length; i++) { %>
      <tr>
        <td><%= i + 1 %></td><td><%= scores[i] %></td>
      </tr>
      <% } %>
    </table>
  </body>

</html>
