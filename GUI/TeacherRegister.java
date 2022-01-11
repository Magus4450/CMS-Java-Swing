package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherRegister extends JFrame implements ActionListener {

    JPanel panel;
    JLabel user_label, password_label, message, title_label;
    JTextField user_text;
    JPasswordField password_text;
    JButton register, back;
    JRadioButton adminBtn, studentBtn, teacherBtn;



    TeacherRegister(){

        // Title Label
        title_label = new JLabel();
        title_label.setText("TEACHER REGISTER");
        title_label.setFont(new Font("Comic Sans", Font.BOLD, 20));

        // User Label
        user_label = new JLabel();
        ImageIcon userIcon = new ImageIcon("src/Images/user.png");
        Image userImg = userIcon.getImage();
        Image userNewImg = userImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        user_label.setIcon(new ImageIcon(userNewImg));
        user_text = new JTextField();
        user_text.setText("Username");


        // Password

        password_label = new JLabel();
        ImageIcon passIcon = new ImageIcon("src/Images/pass.png");
        Image passImg = passIcon.getImage();
        Image passNewImg = passImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        password_label.setIcon(new ImageIcon(passNewImg));
        password_text = new JPasswordField();
        password_text.setText("Password");


        // Radio Buttons

        studentBtn = new JRadioButton("Student");
        teacherBtn = new JRadioButton("Teacher");
        adminBtn = new JRadioButton("Admin");

//        studentBtn.setBounds();
        studentBtn.addActionListener(this);
        teacherBtn.addActionListener(this);
        adminBtn.addActionListener(this);


        ButtonGroup group = new ButtonGroup();
        group.add(studentBtn);
        group.add(adminBtn);
        group.add(teacherBtn);

        // Message
        message = new JLabel();


        // Login
        register = new JButton("LOGIN");

        // Register
        back = new JButton("BACK");

        title_label.setBounds(60, 20, 300, 20);
        user_label.setBounds(60,60,30,30);
        user_text.setBounds(100,60,200,30);
        password_label.setBounds(60, 100, 30, 30);
        password_text.setBounds(100, 100, 200, 30);
        studentBtn.setBounds(60, 130, 80, 30);
        teacherBtn.setBounds(140, 130, 80, 30);
        adminBtn.setBounds(220, 130, 80, 30);
        message.setBounds(60, 160, 200, 30);
        register.setBounds(200, 200, 150 ,30);
        back.setBounds(30, 200, 150, 30);






        panel = new JPanel(null);
        panel.setBorder(new EmptyBorder(10,10,10,10));



        panel.add(title_label);
        panel.add(user_label);
        panel.add(user_text);
        panel.add(password_label);
        panel.add(password_text);


        panel.add(message);
        panel.add(studentBtn);
        panel.add(teacherBtn);
        panel.add(adminBtn);
        panel.add(register);
        panel.add(back);

        register.addActionListener(this);
        back.addActionListener(this);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setSize(400, 290);
        // Center the window
        this.setLocationRelativeTo(null);


        this.add(panel, BorderLayout.CENTER);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == back){
            this.dispose();
            new Register();
        } else if(e.getSource() == register){
            System.out.println("Registered");
        }

    }
}
