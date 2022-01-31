package GUI;


import DBHelpers.DBCRUD;
import Users.Student;
import Users.Teacher;
import com.mysql.cj.log.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CourseInfo extends JFrame implements ActionListener {

    JPanel panel, infoPanel;
    JLabel title_label, semester;
    JButton back, enroll;
    Student st;
    int courseId;
    public CourseInfo(Student st, int courseId) throws SQLException {
        this.st = st;
        this.courseId = courseId;

        // Title Label
        ResultSet rs = DBCRUD.getCourseData(courseId);
        if (rs.next()){
            title_label = new JLabel(rs.getString("courseName"));
        }

        title_label.setFont(new Font("Comic Sans", Font.BOLD, 20));


        // Back
        back = new JButton("BACK");


        //Enroll
        enroll = new JButton("ENROLL");

        title_label.setBounds(50, 20, 300, 20);
        back.setBounds(30, 380, 140, 30);
        enroll.setBounds(200, 380, 140 ,30);



        panel = new JPanel(null);
        panel.setBorder(new EmptyBorder(10,10,10,10));

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(Box.createRigidArea(new Dimension(10,10)));

        rs = DBCRUD.getCourseData(courseId);
        String[] modules = {};

        if(rs.next()){
            modules = rs.getString("courseModules").split(" ");
        }
        String module;
        int index = 0;
        for(int sem = 1; sem <= 6; sem++) {
            semester = new JLabel("Semester " + sem);
            semester.setFont(new Font("Consolas", Font.BOLD, 16));

            infoPanel.add(semester);
            for(int i = index; i < index +4 ; i ++){
                module = modules[i];
                System.out.println(module);
                rs  = DBCRUD.getModuleData(module);

                if(rs.next()){
                    infoPanel.add(new JLabel(rs.getString("moduleName")));

                }
            }
            index+=4;

            infoPanel.add(Box.createRigidArea(new Dimension(10,10)));
        }


        JScrollPane pane = new JScrollPane(infoPanel);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        pane.setBorder(new LineBorder(Color.black));
//        pane.setBorder(new EmptyBorder(0, 20, 0, 20));
        pane.setBounds(40,70, 300, 280);


        panel.add(pane);
        panel.add(title_label);
        panel.add(back);
        panel.add(enroll);

        back.addActionListener(this);
        enroll.addActionListener(this);


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
            new CourseRegister(st);
        }else if (e.getSource() == enroll){
            st.setEnrolledCourse(courseId);
            ResultSet rs = DBCRUD.getModules(Integer.toString(st.getEnrolledCourse()), 1);
            ArrayList<String> modules = new ArrayList<>();
            try {

                while(rs.next()){
                    modules.add(rs.getString("moduleCode"));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            st.setEnrolledModules(modules.toString().replace("[", "").replace("]", "").replace(",", ""));
            if(st.register()){
                System.out.println("Student Registered");
                JOptionPane.showMessageDialog(null, "You have been registered successfully.", "Registered", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new Login();

            };
        }


    }
}


