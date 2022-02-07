package GUI;



import DBHelpers.DBUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DBUserPass extends JFrame {

    public DBUserPass(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Database Config");
        this.setSize(400, 200);
        // Center the window
        this.setLocationRelativeTo(null);

        Font normalFont = new Font("Bahnschrift", Font.PLAIN, 13);

        GridLayout gl = new GridLayout(4,2);
        gl.setVgap(10);
        gl.setHgap(10);
        JPanel panel = new JPanel(gl);
        panel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel info = new JLabel("Enter Database Info");
        info.setFont(normalFont);

        JLabel user = new JLabel("Username");
        user.setFont(normalFont);

        JTextField userText = new JTextField();
        userText.setFont(normalFont);
        userText.setText("root");

        JLabel pass = new JLabel("Password");
        pass.setFont(normalFont);

        JPasswordField passText = new JPasswordField();
        passText.setFont(normalFont);
        passText.setText("");

        JButton submitBtn = new JButton("Submit");
        submitBtn.setFont(normalFont);
        submitBtn.addActionListener(e->{
            String usr = userText.getText();
            String ps = new String(passText.getPassword());
            this.dispose();
            new DBUtils(usr, ps);
            new Login();
        });

        panel.add(info);
        panel.add(new JLabel(""));

        panel.add(user);
        panel.add(userText);

        panel.add(pass);
        panel.add(passText);

        panel.add(new JLabel(""));
        panel.add(submitBtn);







        this.add(panel);
        this.setResizable(false);
        this.setVisible(true);
    }

}
