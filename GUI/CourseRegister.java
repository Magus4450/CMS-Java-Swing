package GUI;


import DBHelpers.DBCRUD;
import Users.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseRegister extends JFrame implements ActionListener {

    private final JButton back;
    private final Student st;
    public CourseRegister(Student st){

        Font titleFont = new Font("Bahnschrift", Font.BOLD, 20);
        Font normalFont = new Font("Bahnschrift", Font.PLAIN, 13);

        this.st = st;
        // Title Label
        JLabel title_label = new JLabel();
        title_label.setText("COURSE REGISTER");
        title_label.setFont(titleFont);


        // Back
        back = new JButton("BACK");
        back.setFont(normalFont);

        title_label.setBounds(80, 20, 300, 20);
        back.setBounds(30, 380, 140, 30);


        JPanel panel = new JPanel(null);
        panel.setBorder(new EmptyBorder(10,10,10,10));


        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        coursePanel.setBackground(Color.white);

        ArrayList<JButton> btns = new ArrayList<>();
        ResultSet rs = DBCRUD.getAllActiveCourse();
        try{
            assert rs != null;
            while(rs.next()){
                JButton bj = new JButton(rs.getString("courseName"));
                bj.setFont(normalFont);
                bj.setMaximumSize(new Dimension(300, 50));
                bj.setPreferredSize(new Dimension(300, 50));
                bj.setFocusable(false);
                bj.addActionListener(this);
                btns.add(bj);
            }
            for(JButton b : btns){
                coursePanel.add(b);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }




        JScrollPane pane = new JScrollPane(coursePanel);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setBounds(40,70, 300, 280);


        panel.add(pane);

        panel.add(title_label);
        panel.add(back);

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

        if(e.getSource() instanceof JButton){
            if(!((JButton) e.getSource()).getText().equals("BACK")){
                String courseName = ((JButton) e.getSource()).getText();
                int courseId = DBCRUD.getCourseId(courseName);
                st.setEnrolledCourse(courseId);
                try {
                    this.dispose();
                    new CourseInfo(st, courseId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.out.println(((JButton) e.getSource()).getText());
            }
        }


        if( e.getSource() == back){
            this.dispose();
            new Register();
        }


    }
}


