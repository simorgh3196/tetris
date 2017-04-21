package tetris;

/*
<applet code="applet3.class" width="368" height="336"> 
</applet>
*/

import javax.swing.*;
import java.awt.*;

/*
 * Created on 2005/08/17
 *
 */

/**
 * @author mori
 *
 */
public class Tetris extends JApplet {
    public Tetris() {
        // �^�C�g����ݒ�
//        setTitle("�e�g���X");
        // �T�C�Y�ύX�s��
//        setResizable(false);

        // ���C���p�l�����쐬���ăt���[���ɒǉ�
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // �p�l���T�C�Y�ɍ��킹�ăt���[���T�C�Y�������ݒ�
//        pack();
    }

//    public static void main(String[] args) {
//        Tetris frame = new Tetris();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//    }
}
