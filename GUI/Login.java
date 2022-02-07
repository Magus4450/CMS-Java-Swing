package GUI;

import Users.Admin;
import Users.Student;
import Users.Teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;


public class Login extends JFrame implements ActionListener {

    private final JLabel message;
    private final JTextField user_text;
    private final JPasswordField password_text;
    private final JButton login;
    private final JButton register;
    private final JRadioButton adminBtn;
    private final JRadioButton studentBtn;
    private final JRadioButton teacherBtn;


    public Login(){

        // Title Label
        JLabel title_label = new JLabel();
        title_label.setText("LOGIN");
        Font titleFont = new Font("Bahnschrift", Font.BOLD, 20);
        title_label.setFont(titleFont);

        // User Label
        JLabel user_label = new JLabel();
        ImageIcon userIcon = new ImageIcon("src/Images/user.png");
        Image userImg = userIcon.getImage();
        Image userNewImg = userImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        user_label.setIcon(new ImageIcon(userNewImg));
        user_text = new JTextField();
        Font normalFont = new Font("Bahnschrift", Font.PLAIN, 13);
        user_text.setFont(normalFont);
        user_text.setText("Username");


        // Password

        JLabel password_label = new JLabel();
        ImageIcon passIcon = new ImageIcon("src/Images/pass.png");
        Image passImg = passIcon.getImage();
        Image passNewImg = passImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        password_label.setIcon(new ImageIcon(passNewImg));
        password_text = new JPasswordField();
        password_text.setFont(new Font("Bahnschrift", Font.BOLD, 13));


        // Radio Buttons

        studentBtn = new JRadioButton("Student");
        studentBtn.setFont(normalFont);
        teacherBtn = new JRadioButton("Teacher");
        teacherBtn.setFont(normalFont);
        adminBtn = new JRadioButton("Admin");
        adminBtn.setFont(normalFont);

//        studentBtn.setBounds();
        studentBtn.addActionListener(this);
        studentBtn.setSelected(true);
        teacherBtn.addActionListener(this);
        adminBtn.addActionListener(this);


        ButtonGroup group = new ButtonGroup();
        group.add(studentBtn);
        group.add(adminBtn);
        group.add(teacherBtn);

        // Message
        message = new JLabel();
        message.setFont(normalFont);


        // Login
        login = new JButton("LOGIN");
        login.setFont(normalFont);
        // Register
        register = new JButton("REGISTER");
        register.setFont(normalFont);


        title_label.setBounds(60, 20, 100, 20);
        user_label.setBounds(60,60,30,30);
        user_text.setBounds(100,60,200,30);
        password_label.setBounds(60, 100, 30, 30);
        password_text.setBounds(100, 100, 200, 30);
        studentBtn.setBounds(60, 130, 80, 30);
        teacherBtn.setBounds(140, 130, 80, 30);
        adminBtn.setBounds(220, 130, 80, 30);
        message.setBounds(60, 160, 200, 30);
        login.setBounds(200, 200, 150 ,30);
        register.setBounds(30, 200, 150, 30);


        JPanel panel = new JPanel(null);
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
        panel.add(login);
        panel.add(register);

        login.addActionListener(this);
        register.addActionListener(this);


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
        if( e.getSource() == login){
            String userName = user_text.getText();
            String password = new String (password_text.getPassword());
            System.out.println(userName);
            System.out.println(password);

            if (studentBtn.isSelected()){
                Student st = new Student(userName, password);
                if (st.login()){
                    this.dispose();
                    try {
                        new StudentPanel(st);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }else {
                    message.setText("Incorrect Credentials");
                }
            } else if(teacherBtn.isSelected()){
                Teacher t = new Teacher(userName, password);
                if(t.login()){
                    this.dispose();
                    try {
                        new TeacherPanel(t);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Teacher logged in!");
                }else{
                    message.setText("Incorrect Credentials");
                }
            } else if(adminBtn.isSelected()){
                Admin a = new Admin(userName, password);
                if(a.login()){
                    try {
                        this.dispose();
                        new AdminPanel(a);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if(a.login()){
                    System.out.println("Admin Logged In!");
                }else {
                    message.setText("Incorrect Credentials");
                }
            }





        } else if(e.getSource() == register){
            this.dispose();
            new Register();
        }

    }
}
