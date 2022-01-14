package GUI;

import javax.swing.*;
import java.awt.*;

public class StudentPanel extends JFrame {

    JPanel topPanel, bottomPanel, menuPanel, infoPanel;
    final int HEIGHT = 490;
    final int WIDTH = 800;
    final int ROW_HEIGHT = HEIGHT/7;

    StudentPanel(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH+10,HEIGHT+35);



        topPanel = new JPanel();
        topPanel.setBounds(0,0,WIDTH,ROW_HEIGHT);
        topPanel.setBackground(new Color(204, 204, 204));

        bottomPanel = new JPanel();
        bottomPanel.setBounds(0, ROW_HEIGHT, WIDTH,HEIGHT-ROW_HEIGHT );
        bottomPanel.setBackground(new Color(241, 241, 241));



        menuPanel = new JPanel();
        menuPanel.setBounds(0, 0, 200,HEIGHT-ROW_HEIGHT);
        menuPanel.setBackground(new Color(109, 176, 109));

        infoPanel = new JPanel();
//        infoPanel.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);
        infoPanel.setBackground(new Color(96, 178, 190));


        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));


        for(int i = 0; i < 10 ;i++){
            JButton bj = new JButton("APPLE");
            bj.setMaximumSize(new Dimension(WIDTH-200, ROW_HEIGHT));
            bj.setPreferredSize(new Dimension(WIDTH-200, ROW_HEIGHT));
            infoPanel.add(bj);
        }
//        infoPanel.add(new JButton("1"));
//        infoPanel.add(new JButton("2"));
//        infoPanel.add(new JButton("3"));
//        infoPanel.add(new JButton("4"));
//        infoPanel.add(new JButton("5"));
//        infoPanel.add(new JButton("6"));
//        infoPanel.add(new JButton("7"));
//        infoPanel.add(new JButton("8"));

        JScrollPane pane = new JScrollPane(infoPanel);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);


        bottomPanel.setLayout(null);
        bottomPanel.add(menuPanel);
        bottomPanel.add(pane);

        this.setLayout(null);
        this.add(topPanel);
        this.add(bottomPanel);

        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new StudentPanel();
    }
}
