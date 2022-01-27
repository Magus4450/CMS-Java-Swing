package GUI;

import DBHelpers.DBCRUD;
import DBHelpers.DBUtils;
import Users.Admin;
import Users.Student;
import Users.Teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.TableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class AdminPanel extends JFrame implements ActionListener {

    JPanel topPanel, bottomPanel, menuPanel, modulesInfoPanel, resultInfoPanel, coursesPanel;
    JScrollPane teachersPane,studentsPane, coursesPane;
    JButton logOutBtn, teachersBtn, studentsBtn, coursesBtn;
    JLabel welcomeLabel;
    String[] modules = {};
    ArrayList<Integer> teacherModulesSem = new ArrayList<>();
    ArrayList<String> teacherModules = new ArrayList<>();


    //    Vector columnNames, data;
    final int HEIGHT = 490;
    final int WIDTH = 1200;
    final int ROW_HEIGHT = HEIGHT/7;
    Admin a;

    AdminPanel(Admin a) throws SQLException {
        this.a = a;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH+10,HEIGHT+35);
        this.setLocationRelativeTo(null);



        topPanel = new JPanel();
        topPanel.setBounds(0,0,WIDTH,ROW_HEIGHT);
        topPanel.setBackground(new Color(204, 204, 204));
        topPanel.setLayout(null);

        welcomeLabel = new JLabel();
        welcomeLabel.setText("Welcome, " + a.getUsername());
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        welcomeLabel.setBounds(25,15,300,45);

//

        logOutBtn = new JButton("Logout");
        logOutBtn.setFocusable(false);
        logOutBtn.setBounds(1070,17,100,40);
        logOutBtn.addActionListener(this);


        topPanel.add(welcomeLabel);
        topPanel.add(logOutBtn);


        bottomPanel = new JPanel();
        bottomPanel.setBounds(0, ROW_HEIGHT, WIDTH,HEIGHT-ROW_HEIGHT );
        bottomPanel.setBackground(new Color(241, 241, 241));



        menuPanel = new JPanel();
        menuPanel.setBounds(0, 0, 200,HEIGHT-ROW_HEIGHT);
        menuPanel.setBackground(new Color(109, 176, 109));
        menuPanel.setLayout(new GridLayout(6,1));

        teachersBtn = new JButton("Teachers");
        teachersBtn.setSelected(true);


        studentsBtn = new JButton("Students");

        coursesBtn = new JButton("Courses");

        coursesBtn.addActionListener(this);
        teachersBtn.addActionListener(this);
        studentsBtn.addActionListener(this);


        menuPanel.add(coursesBtn);
        menuPanel.add(teachersBtn);
        menuPanel.add(studentsBtn);

        modulesInfoPanel = new JPanel();
        modulesInfoPanel.setBackground(new Color(96, 178, 190));
        modulesInfoPanel.setBorder(new EmptyBorder(0,10,0,10));


        modulesInfoPanel.setLayout(new BoxLayout(modulesInfoPanel, BoxLayout.Y_AXIS));

        showCoursesPanel();


        bottomPanel.setLayout(null);
        bottomPanel.add(menuPanel);

        this.setLayout(null);
        this.add(topPanel);
        this.add(bottomPanel);

        this.setResizable(false);
        this.setVisible(true);
    }

    private void showCoursesPanel() throws SQLException {

        if(studentsPane!=null){
            bottomPanel.remove(studentsPane);
        }
        if(coursesPanel!=null) {
            bottomPanel.remove(coursesPanel);
        }
        if(teachersPane!=null){
            bottomPanel.remove(teachersPane);
        }

        ResultSet rs = DBCRUD.getAllCourseData();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Course Id");
        columnNames.add("Course Name");
        columnNames.add("Students");

        ArrayList<String> allCourses = new ArrayList<>();
        Vector<Vector<String>> data = new Vector<>();
        while(rs.next()){
            Vector<String> row = new Vector<>(columnNames.size());
            System.out.println(rs.getString("courseId"));
            row.addElement(rs.getString("courseId"));
            row.add(rs.getString("courseName"));
            allCourses.add(rs.getString("courseName"));
            data.addElement(row);
        }
        for(int i = 0 ; i < allCourses.size(); i++){
            Vector<String> v = data.get(i);
            v.addElement(Integer.toString(DBCRUD.getStudentCount(allCourses.get(i))));
        }

        JTable table = new JTable(data, columnNames){
//            public Class getColumnClass(int column){
//                for(int row =0; row<getRowCount(); row++){
//                    Object o = getValueAt(row, column);
//                    if(o!= null){
//                        return o.getClass();
//                    }
//                }
//                return Object.class;
//            }

            @Override
            public boolean isCellEditable(int row, int col) {

                return  false;
            }



        };

        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
//        table.getColumnModel().getColumn(3).setPreferredWidth(100);
//        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
//        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
//        table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );

        table.setFont(new Font("Consolas", Font.PLAIN, 15));



        coursesPane = new JScrollPane(table);
        coursesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        coursesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        coursesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);

        bottomPanel.add(coursesPane);
        SwingUtilities.updateComponentTreeUI(this);

    }



    private void showTeachersPanel() throws SQLException {

        if(studentsPane!=null){
            bottomPanel.remove(studentsPane);
        }
        if(coursesPane!=null) {
            bottomPanel.remove(coursesPane);
        }
        if(teachersPane!=null){
            bottomPanel.remove(teachersPane);
        }


        ResultSet rs = DBCRUD.getAllTeacherData();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Username");
        columnNames.add("Name");
        columnNames.add("Contact");
        columnNames.add("Modules");


        Vector<Vector<String>> data = new Vector<>();
        while(rs.next()){
            Vector<String> row = new Vector<>(columnNames.size());

            row.add(rs.getString("username"));
            row.addElement(rs.getString("firstName")+ " " + rs.getString("lastName"));
            row.add(rs.getString("contact"));
            row.add(rs.getString("teacherModules"));
            data.addElement(row);
        }

        JTable table = new JTable(data, columnNames){
            public Class getColumnClass(int column){
                for(int row =0; row<getRowCount(); row++){
                    Object o = getValueAt(row, column);
                    if(o!= null){
                        return o.getClass();
                    }
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return  false;
            }

        };

        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);
//        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
//        table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );

        table.setFont(new Font("Consolas", Font.PLAIN, 15));


        teachersPane = new JScrollPane(table);
        teachersPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        teachersPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        teachersPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);


        bottomPanel.add(teachersPane);
        SwingUtilities.updateComponentTreeUI(this);

    }

    private void showStudentsPanel() throws SQLException {
        if (teachersPane != null) {
            bottomPanel.remove(teachersPane);
        }
        if (studentsPane != null) {
            bottomPanel.remove(studentsPane);
        }
        if (coursesPane != null) {
            bottomPanel.remove(coursesPane);
        }

        ResultSet rs = DBCRUD.getAllStudentData();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Name");
        columnNames.add("Contact");
        columnNames.add("Course");
        columnNames.add("Level");
        columnNames.add("Semester");


        Vector<Vector<String>> data = new Vector<>();
        while(rs.next()){
            Vector<String> row = new Vector<>(columnNames.size());
            row.addElement(rs.getString("firstName")+ " " + rs.getString("lastName"));
            row.add(rs.getString("contact"));

            row.add(rs.getString("enrolledCourse"));
            row.add(rs.getString("studentLevel"));
            row.add(Integer.toString(rs.getInt("passedSem")+1));
            data.addElement(row);
        }

        JTable table = new JTable(data, columnNames){
            public Class getColumnClass(int column){
                for(int row =0; row<getRowCount(); row++){
                    Object o = getValueAt(row, column);
                    if(o!= null){
                        return o.getClass();
                    }
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return  false;
            }

        };

        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
//        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
//        table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );

        table.setFont(new Font("Consolas", Font.PLAIN, 15));

        studentsPane = new JScrollPane(table);
        studentsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        studentsPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        studentsPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);


        bottomPanel.add(studentsPane);
        SwingUtilities.updateComponentTreeUI(this);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            if(e.getSource() instanceof JButton) {
                if(e.getSource() == teachersBtn) {
                    System.out.println("TEACHERS");
                    showTeachersPanel();
                }else if(e.getSource() == logOutBtn){
                    this.dispose();
                    new Login();
                }else if(e.getSource() == coursesBtn){
                    System.out.println("INFO");
                    showCoursesPanel();
                }else if(e.getSource() == studentsBtn){
                    System.out.println("Students");
                    showStudentsPanel();
                }
            }
        }catch (SQLException er){
            er.printStackTrace();
        }

    }

//    public static void main(String[] args) {
//        new StudentPanel("Magus");
//    }
}
