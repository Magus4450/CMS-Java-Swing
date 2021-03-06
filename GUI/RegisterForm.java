package GUI;


import DBHelpers.DBCRUD;
import Users.Admin;
import Users.Student;
import Users.Teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterForm extends JFrame implements ActionListener {

    private final JLabel message;

    private final JTextField user_text;
    private final JTextField first_text;
    private final JTextField last_text;
    private final JTextField address_text;
    private final JTextField contact_text;
    private final JPasswordField password_text;
    private final JPasswordField cPassword_text;
    private final JButton register;
    private final JButton back;
    private final String userType;
    private final Admin a;

    RegisterForm(String userType, Admin a){

        Font titleFont = new Font("Bahnschrift", Font.BOLD, 20);
        Font normalFont = new Font("Bahnschrift", Font.PLAIN, 13);
        Font passwordFont = new Font("Bahnschrift", Font.BOLD, 13);
        this.a = a;
        this.userType = userType;
        // Title Label
        JLabel title_label = new JLabel();
        title_label.setText(userType +" REGISTER");
        title_label.setFont(titleFont);

        // Username Label
        JLabel user_label = new JLabel();
        user_label.setText("Username");
        user_label.setFont(normalFont);
        user_text = new JTextField();
        user_text.setFont(normalFont);

        // First and Last Name
        JLabel first_label = new JLabel();
        first_label.setFont(normalFont);

        first_label.setText("First Name");
        first_text = new JTextField();
        first_text.setFont(normalFont);


        JLabel last_label = new JLabel();
        last_label.setFont(normalFont);

        last_label.setText("Last Name");
        last_text = new JTextField();
        last_text.setFont(normalFont);

        // Address and Contact
        JLabel contact_label = new JLabel();
        contact_label.setFont(normalFont);

        contact_label.setText("Contact");
        contact_text = new JTextField();
        contact_text.setFont(normalFont);

        JLabel address_label = new JLabel();
        address_label.setFont(normalFont);

        address_label.setText("Address");
        address_text = new JTextField();
        address_text.setFont(normalFont);



        // Password
        JLabel password_label = new JLabel();
        password_label.setFont(normalFont);

        password_label.setText("Password");
        password_text = new JPasswordField();
        password_text.setFont(passwordFont);


        // Confirm Password

        JLabel cPassword_label = new JLabel();
        cPassword_label.setFont(normalFont);

        cPassword_label.setText("Confirm");
        cPassword_text = new JPasswordField();
        cPassword_text.setFont(passwordFont);



        // Message
        message = new JLabel();
        message.setFont(normalFont);


        // Register
        register = new JButton("REGISTER");
        register.setFont(normalFont);

        // Back
        back = new JButton("BACK");
        back.setFont(normalFont);

        title_label.setBounds(80, 20, 300, 20);

        user_label.setBounds(50,60,100,30);
        user_text.setBounds(130,60,200,30);

        password_label.setBounds(50, 100, 100, 30);
        password_text.setBounds(130, 100, 200, 30);

        cPassword_label.setBounds(50, 140, 100, 30);
        cPassword_text.setBounds(130, 140, 200, 30);

        first_label.setBounds(50, 180, 100, 30);
        first_text.setBounds(130, 180, 200, 30);

        last_label.setBounds(50, 220, 100, 30);
        last_text.setBounds(130, 220, 200, 30);

        address_label.setBounds(50, 260, 100, 30);
        address_text.setBounds(130, 260, 200, 30);

        contact_label.setBounds(50, 300, 100, 30);
        contact_text.setBounds(130, 300, 200, 30);



        message.setBounds(130, 330, 200, 30);
        message.setText("");
        register.setBounds(200, 380, 140 ,30);
        back.setBounds(30, 380, 140, 30);


        JPanel panel = new JPanel(null);
        panel.setBorder(new EmptyBorder(10,10,10,10));



        panel.add(title_label);
        panel.add(user_label);
        panel.add(user_text);
        panel.add(password_label);
        panel.add(password_text);
        panel.add(cPassword_label);
        panel.add(cPassword_text);
        panel.add(first_label);
        panel.add(first_text);
        panel.add(last_label);
        panel.add(last_text);
        panel.add(contact_label);
        panel.add(contact_text);
        panel.add(address_label);
        panel.add(address_text);


        panel.add(message);
        panel.add(register);
        panel.add(back);

        register.addActionListener(this);
        back.addActionListener(this);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setSize(400, 480);
        // Center the window
        this.setLocationRelativeTo(null);


        this.add(panel, BorderLayout.CENTER);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == back){
            this.dispose();
            new Register();
        } else if(e.getSource() == register){
            String username = user_text.getText();
            ResultSet rs = DBCRUD.getAllStudentData();
            ArrayList<String> allStudents = new ArrayList<>();
            try{
                while(rs.next()){
                    allStudents.add(rs.getString("username"));
                }
                if(allStudents.contains(username)){
                    message.setText("Username already taken");
                    return;
                }
            } catch (SQLException er){
                er.printStackTrace();
            }

            String password = new String (password_text.getPassword());
            String cPassword = new String(cPassword_text.getPassword());

            if (!password.equals(cPassword)){
                message.setText("Password doesnt match");
                return;
            }

            String firstName = first_text.getText();
            String lastName = last_text.getText();
            String address = address_text.getText();
            String contact = contact_text.getText();

            if(username.equals("") || firstName.equals("") || lastName.equals("") || address.equals("") || contact.equals("") || password.equals("")){
                message.setText("Field cannot be empty");
                return;
            }
            if(this.userType.equals("STUDENT")){
                Student st = new Student(username, password, firstName, lastName, address,contact);
                this.dispose();
                new CourseRegister(st);



            }else if(this.userType.equals("TEACHER")){
                Teacher t = new Teacher(username, password, firstName, lastName, address,contact);
                if(t.register()){
                    System.out.println("Teacher Registered");
                    this.dispose();
                    JOptionPane.showMessageDialog(null, "You have been registered successfully.", "Registered", JOptionPane.INFORMATION_MESSAGE);
                    AdminPanel ap;
                    try {
                        ap = new AdminPanel(a);
                        ap.showTeachersPanel();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            }

        }

    }
}


