package GUI;

import DBHelpers.DBCRUD;
import Users.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.TableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class StudentPanel extends JFrame implements ActionListener {

    JPanel topPanel, bottomPanel, menuPanel, modulesInfoPanel, resultInfoPanel, infoPanel;
    JScrollPane modulesPane,resultPane, infoPane;
    JButton logOutBtn, modulesBtn, resultBtn, infoBtn;
    JLabel welcomeLabel;
    String[] modules = {};
    ArrayList<String> studentModules = null;
    ArrayList<String> teacherUsername = new ArrayList<>();
    ArrayList<String> studentMarks = null;
//    Vector columnNames, data;
    final int HEIGHT = 490;
    final int WIDTH = 1200;
    final int ROW_HEIGHT = HEIGHT/7;
    Student st;

    StudentPanel(Student st) throws SQLException {
        this.st = st;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH+10,HEIGHT+35);
        this.setLocationRelativeTo(null);



        topPanel = new JPanel();
        topPanel.setBounds(0,0,WIDTH,ROW_HEIGHT);
        topPanel.setBackground(new Color(204, 204, 204));
        topPanel.setLayout(null);

        welcomeLabel = new JLabel();
        welcomeLabel.setText("Welcome, " + st.getFirstName());
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

        modulesBtn = new JButton("Modules");
        modulesBtn.setSelected(true);


        resultBtn = new JButton("Result");

        infoBtn = new JButton("View Info");


        modulesBtn.addActionListener(this);
        resultBtn.addActionListener(this);
        infoBtn.addActionListener(this);

        menuPanel.add(modulesBtn);
        menuPanel.add(resultBtn);
        menuPanel.add(infoBtn);

        modulesInfoPanel = new JPanel();
        modulesInfoPanel.setBackground(new Color(96, 178, 190));
        modulesInfoPanel.setBorder(new EmptyBorder(0,10,0,10));


        modulesInfoPanel.setLayout(new BoxLayout(modulesInfoPanel, BoxLayout.Y_AXIS));

        showModulePanel();
//        showInstructorsPanel();

        bottomPanel.setLayout(null);
        bottomPanel.add(menuPanel);

        this.setLayout(null);
        this.add(topPanel);
        this.add(bottomPanel);

        this.setResizable(false);
        this.setVisible(true);
    }

    private void showModulePanel() throws SQLException {

        if(resultPane!=null){
            bottomPanel.remove(resultPane);
        }
        if(infoPanel!=null){
            bottomPanel.remove(infoPanel);
        }
        if(modulesPane!=null){
            bottomPanel.remove(modulesPane);
        }

        ResultSet rs = DBCRUD.getCourseData(st.getEnrolledCourse());

        if(rs.next()){
            modules = rs.getString("courseModules").split(" ");
            System.out.println("YES MODULES");

        }
        studentModules = new ArrayList<>(Arrays.asList(modules));
        studentMarks = new ArrayList<>(Arrays.asList(st.getMarks().split(" ")));
        Vector<String> columnNames = new Vector<>();
        columnNames.add("moduleCode");
        columnNames.add("Name");
        columnNames.add("Level");
        columnNames.add("Credits");
        columnNames.add("Teacher");

//        columnNames.addAll();
        Vector<Vector<String>> data = new Vector<>();
        for(int i = 0; i < Math.min(modules.length, 4*(st.getPassedSem()+1)); i++){
            Vector<String> row = new Vector<>(columnNames.size());
            rs = DBCRUD.getModuleData(modules[i]);
            if(rs.next() && rs.getInt("moduleSem") <= st.getPassedSem()+1){
                row.addElement(rs.getString("moduleCode"));
                row.addElement(rs.getString("moduleName"));
                row.addElement(rs.getString("moduleLevel"));
                row.addElement(rs.getString("moduleCredit"));
                teacherUsername.add(rs.getString("moduleTeacher"));
            }
            data.addElement(row);
        }


        rs = DBCRUD.getCourseData(st.getEnrolledCourse());
        String[] modules = {};

        if(rs.next()){
            modules = rs.getString("courseModules").split(" ");
        }

        for(int i = 0; i < Math.min(modules.length, 4*(st.getPassedSem()+1)); i++){

            rs =DBCRUD.getTeacherData(teacherUsername.get(i));
            if(rs.next()){
                Vector<String> v = data.get(i);
                v.addElement(rs.getString("firstName")+ " " +rs.getString("lastName"));
            }

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

        };



        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );


        table.setFont(new Font("Consolas", Font.PLAIN, 15));


        table.setEnabled(false);

        modulesPane = new JScrollPane(table);
        modulesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        modulesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        modulesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);

        bottomPanel.add(modulesPane);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void showResultPane() throws SQLException {

        if(resultPane!=null){
            bottomPanel.remove(resultPane);
        }
        if(infoPanel!=null){
            bottomPanel.remove(infoPanel);
        }
        if(modulesPane!=null){
            bottomPanel.remove(modulesPane);
        }


        Vector<String> columnNames = new Vector<>();
        columnNames.add("Module Code");
        columnNames.add("Module Name");
        columnNames.add("Marks");
        columnNames.add("Remarks");

        JPanel semResultPanel = new JPanel();
        semResultPanel.setLayout(new BoxLayout(semResultPanel, BoxLayout.Y_AXIS));
//        semResultPanel.setSize(new Dimension(400,200));
        for(int i = 0; i < st.getPassedSem()+1; i++){
            JLabel semLabel = new JLabel("Semester " + (i+1));
            semLabel.setFont(new Font("Consolas", Font.BOLD, 20));
            semLabel.setBorder(new EmptyBorder(10,10,10,10));
//            semLabel.add(Box.createRigidArea(new Dimension(10,50)));
            Vector<Vector<String>> data = new Vector<>();
            for(int j = 0; j < 4; j++){
                Vector<String> row = new Vector<>(columnNames.size());
                ResultSet rs = DBCRUD.getModuleData(studentModules.get((i*4)+j));
                if(rs.next()){
                    row.addElement(rs.getString("moduleCode"));
                    row.addElement(rs.getString("moduleName"));
                    row.addElement(studentMarks.get((i*4)+j));
                    row.addElement(st.getRemarks());
                }
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

            };



            table.setRowHeight(30);
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(380);
            table.getColumnModel().getColumn(2).setPreferredWidth(60);
            table.getColumnModel().getColumn(3).setPreferredWidth(60);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setVerticalAlignment( JLabel.CENTER );
            centerRenderer.setHorizontalAlignment( JLabel.CENTER );
            table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
            table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
            table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
            table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );


            table.setFont(new Font("Consolas", Font.PLAIN, 15));


            table.setEnabled(false);
            JScrollPane tablePane = new JScrollPane(table);

            semResultPanel.add(semLabel);
            semResultPanel.add(tablePane);
        }



        modulesPane = new JScrollPane(semResultPanel);
        modulesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        modulesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        modulesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);

        bottomPanel.add(modulesPane);
        SwingUtilities.updateComponentTreeUI(this);
    }




    private void showInfoPane() throws SQLException {
        if (modulesPane != null) {
            bottomPanel.remove(modulesPane);
        }
        if (resultPane != null) {
            bottomPanel.remove(resultPane);
        }
        if(infoPanel!=null){
            bottomPanel.remove(infoPanel);
        }

        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(9, 5));

        ResultSet rs = DBCRUD.getStudentData(st.getUsername());
        if(rs.next()){
            JLabel stdFirstName = new JLabel("First Name");
            JTextField textFirstName = new JTextField(rs.getString("firstName"));

            JLabel stdLastName = new JLabel("Last Name");
            JTextField textLastName = new JTextField(rs.getString("lastName"));

            JLabel stdPassword = new JLabel("Password");
            JTextField textPassword = new JTextField(rs.getString("password"));

            JLabel stdUsername = new JLabel("Username");
            JTextField textUsername = new JTextField(rs.getString("username"));
            textUsername.setEnabled(false);

            JLabel stdAddress = new JLabel("Address");
            JTextField textAddress = new JTextField(rs.getString("address"));

            JLabel stdContact = new JLabel("Contact");
            JTextField textContact = new JTextField(rs.getString("contact"));


            JLabel stdEnrolledCourse = new JLabel("Enrolled Course");
            JTextField textEnrolledCourse = new JTextField(rs.getString("enrolledCourse"));
            textEnrolledCourse.setEnabled(false);

            JLabel stdRemarks = new JLabel("Remarks");
            JTextField textRemarks = new JTextField(rs.getString("studentRemarks"));
            textRemarks.setEnabled(false);

            JLabel stdLevel = new JLabel("Level");
            JTextField textLevel = new JTextField(rs.getString("studentLevel"));
            textLevel.setEnabled(false);

            JButton stdEdit = new JButton("Edit");

            stdEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    st.setFirstName(textFirstName.getText());
                    st.setLastName(textLastName.getText());
                    st.setAddress(textAddress.getText());
                    st.setContact(textContact.getText());
                    st.setPassword(textPassword.getText());

                    if(DBCRUD.updateStudentData(st)){
                        System.out.println("Updated!");
                    }else{
                        System.out.println("Not Updated");
                    }
                }
            });

            infoPanel.add(stdFirstName);
            infoPanel.add(textFirstName);

            infoPanel.add(new JLabel(""));

            infoPanel.add(stdLastName);
            infoPanel.add(textLastName);

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(stdUsername);
            infoPanel.add(textUsername);

            infoPanel.add(new JLabel(""));

            infoPanel.add(stdPassword);
            infoPanel.add(textPassword);

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(stdAddress);
            infoPanel.add(textAddress);

            infoPanel.add(new JLabel(""));

            infoPanel.add(stdContact);
            infoPanel.add(textContact);

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(stdEnrolledCourse);
            infoPanel.add(textEnrolledCourse);

            infoPanel.add(new JLabel(""));

            infoPanel.add(stdLevel);
            infoPanel.add(textLevel);

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(stdRemarks);
            infoPanel.add(textRemarks);

            infoPanel.add(new JLabel(""));

            infoPanel.add(stdEdit);



            infoPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
            infoPanel.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);
            bottomPanel.add(infoPanel);

            // Refresh the JFrame
            SwingUtilities.updateComponentTreeUI(this);
            System.out.println("IN INFO LAST");
        }




    }






    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            if(e.getSource() instanceof JButton) {
                if(e.getSource() == modulesBtn) {
                    showModulePanel();
                }else if(e.getSource() == logOutBtn){
                    this.dispose();
                    new Login();
                }else if(e.getSource() == infoBtn){
                    System.out.println("INFO");
                    showInfoPane();
                }else if (e.getSource() == resultBtn){
                    showResultPane();
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
