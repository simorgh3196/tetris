package tetris;

import java.awt.*;

public class CenterLabel {

    int sizeX;
    int sizeY;

    CenterLabel(int x, int y) {
        sizeX = x;
        sizeY = y;
    }

    void draw(Graphics g, String text) {
        // Font設定
        Font font = new Font("Arial", Font.BOLD, 40);
        g.setFont(font);
        g.setColor(Color.ORANGE);

        // サイズ取得
        FontMetrics fm = g.getFontMetrics();
        Rectangle labelRect = fm.getStringBounds(text, g).getBounds();

        // サイズ調整
        int labelPositionX = (sizeX - labelRect.width) / 2;
        int labelPositionY = (sizeY + labelRect.height) / 2;

        // 描画
        g.drawString(text, labelPositionX, labelPositionY);
    }
}
