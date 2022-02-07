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


public class CourseInfo extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private final JButton back;
    private final JButton enroll;
    private final Student st;
    private final int courseId;
    public CourseInfo(Student st, int courseId) throws SQLException {

        Font titleFont = new Font("Bahnschrift", Font.BOLD, 20);
        Font titleFont2 = new Font("Bahnschrift", Font.BOLD, 15);
        Font normalFont = new Font("Bahnschrift", Font.PLAIN, 13);

        this.st = st;
        this.courseId = courseId;

        // Title Label
        ResultSet rs = DBCRUD.getCourseData(courseId);
        assert rs != null;
        if (rs.next()){
            titleLabel = new JLabel(rs.getString("courseName"));

        }

        titleLabel.setFont(titleFont);


        // Back
        back = new JButton("BACK");
        back.setFont(normalFont);


        //Enroll
        enroll = new JButton("ENROLL");
        enroll.setFont(normalFont);

        titleLabel.setBounds(50, 20, 300, 20);
        back.setBounds(80, 380, 140, 30);
        enroll.setBounds(250, 380, 140 ,30);


        JPanel panel = new JPanel(null);
        panel.setBorder(new EmptyBorder(10,10,10,10));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(Box.createRigidArea(new Dimension(10,10)));

        rs = DBCRUD.getCourseData(courseId);
        String[] modules = {};
        assert rs != null;
        if(rs.next()){
            modules = rs.getString("courseModules").split(" ");
        }
        String module;
        int index = 0;
        for(int sem = 1; sem <= 6; sem++) {
            JLabel semester = new JLabel("Semester " + sem);
            semester.setFont(titleFont2);

            infoPanel.add(semester);
            if(sem <= 4){
                for(int i = index; i < index +4 ; i ++){
                    module = modules[i];
                    System.out.println(module);
                    rs  = DBCRUD.getModuleData(module);
                    assert rs != null;
                    if(rs.next()){
                        JLabel mod = new JLabel(rs.getString("moduleName"));
                        mod.setFont(normalFont);
                        infoPanel.add(mod);

                    }
                }
                index+=4;
            }else{
                for(int i = index; i < index +2 ; i ++){
                    module = modules[i];
                    rs  = DBCRUD.getModuleData(module);
                    assert rs != null;
                    if(rs.next()){
                        JLabel mod = new JLabel(rs.getString("moduleName"));
                        mod.setFont(normalFont);
                        infoPanel.add(mod);

                    }
                }
                JLabel elective1 = new JLabel("(Elective1)");
                JLabel elective2 = new JLabel("(Elective2)");
                elective1.setFont(normalFont);
                elective2.setFont(normalFont);
                infoPanel.add(elective1);
                infoPanel.add(elective2);
                index+=2;
            }


            infoPanel.add(Box.createRigidArea(new Dimension(10,10)));
        }
        JLabel electivesLabel = new JLabel("Electives");
        electivesLabel.setFont(titleFont2);
        infoPanel.add(electivesLabel);
        for(int i = index; i < index+8; i++){
            module = modules[i];
            rs  = DBCRUD.getModuleData(module);
            assert rs != null;
            if(rs.next()){
                JLabel electMod = new JLabel(rs.getString("moduleName").replace("(Elective)",""));
                electMod.setFont(normalFont);
                infoPanel.add(electMod);

            }
        }



        JScrollPane pane = new JScrollPane(infoPanel);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setBounds(40,70, 400, 280);


        panel.add(pane);
        panel.add(titleLabel);
        panel.add(back);
        panel.add(enroll);

        back.addActionListener(this);
        enroll.addActionListener(this);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setSize(500, 480);
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
            System.out.println("ENROLLED->" + st.getEnrolledCourse());
            ArrayList<String> modules = new ArrayList<>();
            try {
                assert rs != null;
                while(rs.next()){
                    modules.add(rs.getString("moduleCode"));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println(modules);
            st.setEnrolledModules(modules.toString().replace("[", "").replace("]", "").replace(",", ""));
            if(st.register()){
                System.out.println("Student Registered");
                JOptionPane.showMessageDialog(null, "You have been registered successfully.", "Registered", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new Login();

            }
        }


    }
}


