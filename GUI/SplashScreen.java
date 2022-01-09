package GUI;

import javax.swing.JFrame;

public class SplashScreen extends JFrame{


    SplashScreen(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Course Management System");
        this.setVisible(true);
    }


    public static void main(String[] args) {
        new SplashScreen();
    }
}
