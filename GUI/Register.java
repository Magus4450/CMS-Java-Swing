package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame implements ActionListener {

    JPanel panel;
    JLabel user_label, password_label, message, title_label;
    JTextField user_text;
    JPasswordField password_text;
    JButton goNext, back;
    JRadioButton adminBtn, studentBtn, teacherBtn;
    ButtonGroup group;


    Register(){

        // Title Label
        title_label = new JLabel();
        title_label.setText("REGISTER");
        title_label.setFont(new Font("Comic Sans", Font.BOLD, 20));



        // Radio Buttons

        studentBtn = new JRadioButton("Student");
        teacherBtn = new JRadioButton("Teacher");
        adminBtn = new JRadioButton("Admin");

//        studentBtn.setBounds();
        studentBtn.addActionListener(this);
        studentBtn.setSelected(true);
        teacherBtn.addActionListener(this);
        adminBtn.addActionListener(this);


        group = new ButtonGroup();
        group.add(studentBtn);
        group.add(adminBtn);
        group.add(teacherBtn);



        // Register
        goNext = new JButton("NEXT");

        // Back
        back = new JButton("BACK");

        title_label.setBounds(60, 20, 200, 20);
        studentBtn.setBounds(60, 70, 80, 30);
        teacherBtn.setBounds(140, 70, 80, 30);
        adminBtn.setBounds(220, 70, 80, 30);
        goNext.setBounds(200, 130, 150, 30);
        back.setBounds(30, 130, 150, 30);






        panel = new JPanel(null);
        panel.setBorder(new EmptyBorder(10,10,10,10));



        panel.add(title_label);


        panel.add(studentBtn);
        panel.add(teacherBtn);
        panel.add(adminBtn);
        panel.add(goNext);
        panel.add(back);


        goNext.addActionListener(this);
        back.addActionListener(this);



        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setSize(400, 240);
        // Center the window
        this.setLocationRelativeTo(null);


        this.add(panel, BorderLayout.CENTER);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Register();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == goNext){
            if (studentBtn.isSelected()){
                System.out.println("Student");
                this.dispose();
                new RegisterForm("STUDENT");
            } else if (teacherBtn.isSelected()){
                System.out.println("Teacher");
                this.dispose();
                new RegisterForm("TEACHER");
            }else if (adminBtn.isSelected()){
                JOptionPane.showMessageDialog(null, "Please contact administration to register as an admin.", "Admin Register", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Admin");
            }

        } else if (e.getSource() == back){
            this.dispose();
            new Login();
        }

    }
}
