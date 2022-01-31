package GUI;

import DBHelpers.DBCRUD;
import DBHelpers.DBUtils;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class TeacherPanel extends JFrame implements ActionListener {

    JPanel topPanel, bottomPanel, menuPanel, modulesInfoPanel, resultInfoPanel, infoPanel;
    JScrollPane modulesPane,resultPane, infoPane;
    JButton logOutBtn, modulesBtn, resultBtn, infoBtn;
    JLabel welcomeLabel;
    String[] modules = {};
    ArrayList<Integer> teacherModulesSem = new ArrayList<>();
    ArrayList<String> teacherModules = new ArrayList<>();


    //    Vector columnNames, data;
    final int HEIGHT = 490;
    final int WIDTH = 1200;
    final int ROW_HEIGHT = HEIGHT/7;
    Teacher t;

    TeacherPanel(Teacher t) throws SQLException {
        this.t = t;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH+10,HEIGHT+35);
        this.setLocationRelativeTo(null);



        topPanel = new JPanel();
        topPanel.setBounds(0,0,WIDTH,ROW_HEIGHT);
        topPanel.setBackground(new Color(204, 204, 204));
        topPanel.setLayout(null);

        welcomeLabel = new JLabel();
        welcomeLabel.setText("Welcome, " + t.getFirstName());
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
        if(infoPanel!=null) {
            bottomPanel.remove(infoPanel);
        }
        if(modulesPane!=null){
            bottomPanel.remove(modulesPane);
        }

        // Module Code -- Module Name -- Module Level -- Number of Students



        ResultSet rs = DBCRUD.getTeacherData(t.getUsername());
//        String[] teacherModules = {};
        if(rs.next()){
            teacherModules = new ArrayList<>(Arrays.asList(rs.getString("teacherModules").split(" ")));
        }

        Vector<String> columnNames = new Vector<>();
        columnNames.add("moduleCode");
        columnNames.add("moduleName");
        columnNames.add("Level");
        columnNames.add("Semester");
        columnNames.add("Students");

        Vector<Vector<String>> data = new Vector<>();
        if(!t.getTeacherModules().equals("null")){

            for(int i = 0; i < teacherModules.size(); i++){

                Vector<String> row = new Vector<>(columnNames.size());
                System.out.println(teacherModules.get(i));
                rs = DBCRUD.getModuleData(teacherModules.get(i));
                if(rs.next()){
                    row.addElement(rs.getString("moduleCode"));
                    row.addElement(rs.getString("moduleName"));
                    row.addElement(rs.getString("moduleLevel"));
                    row.addElement(rs.getString("moduleSem"));
                    teacherModulesSem.add(rs.getInt("moduleSem"));
//                row.addElement(Integer.toString(DBCRUD.getStudentCount(rs.getString("moduleCode"))));
                }
                data.addElement(row);
            }

            for(int i = 0; i < teacherModules.size(); i++){
                Vector<String> v = data.get(i);
                v.addElement(Integer.toString(DBCRUD.getStudentCount(teacherModules.get(i), teacherModulesSem.get(i))));


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

            @Override
            public boolean isCellEditable(int row, int col) {
                try {
                    showResultPanel(teacherModules.get(row));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return  false;
            }

        };


        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
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
//        table.setEnabled(false);




        modulesPane = new JScrollPane(table);
        modulesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        modulesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        modulesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);

        bottomPanel.add(modulesPane);
        SwingUtilities.updateComponentTreeUI(this);

    }



    private void showResultPanel(String moduleCode) throws SQLException {

        if(resultPane!=null){
            bottomPanel.remove(resultPane);
        }
        if(infoPanel!=null) {
            bottomPanel.remove(infoPanel);
        }
        if(modulesPane!=null){
            bottomPanel.remove(modulesPane);
        }
        if(t.getTeacherModules()==null){
            return;
        }


        JComboBox jComboBox = new JComboBox(teacherModules.toArray());




        Vector<String> columnNames = new Vector<>();
        columnNames.add("Username");
        columnNames.add("Student");
        columnNames.add("Module Code");
        columnNames.add("Level");
        columnNames.add("Semester");
        columnNames.add("Marks");


        Vector<Vector<String>> data = new Vector<>();
//        String currentModule = teacherModules[0];


        ResultSet rs = DBCRUD.getStudentDataFromModule(moduleCode);
        while(rs.next()){
            assert teacherModules != null;
            if(rs.getInt("passedSem")+1 != teacherModulesSem.get(teacherModules.indexOf(moduleCode))){
                continue;
            }
            Vector<String> row = new Vector<>(columnNames.size());
            ArrayList<String> studentModules;
            ArrayList<String> studentMarks;
            row.addElement(rs.getString("username"));
            row.addElement(rs.getString("firstName")+ " " +rs.getString("lastName"));
            row.addElement(moduleCode);
            row.addElement(rs.getString("studentLevel"));
            row.addElement(Integer.toString(rs.getInt("passedSem")+1));
            studentModules = new ArrayList<>(Arrays.asList(rs.getString("enrolledModules").split(" ")));
            studentMarks = new ArrayList<>(Arrays.asList(rs.getString("marks").split(" ")));

            row.addElement(studentMarks.get(studentModules.indexOf(moduleCode)));
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
                int moduleSem = Integer.parseInt((String)this.getValueAt(row,3));
                System.out.println(row +" " + col);
                return col == 5;

            }


        };

        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel)e.getSource();
                String columnName = model.getColumnName(column);
                Object data = model.getValueAt(row, column);
                System.out.println(data);
                if(column == 5){
                    try{
                        int dataInt = Integer.parseInt((String) data);
                        String student = (String)model.getValueAt(row,0);
                        ResultSet rs = DBCRUD.getStudentData(student);
                        Student st;

                        if (rs.next()){
                            st = new Student(rs.getString("username"), rs.getString("password"));
                            st.setMarksModule(moduleCode, dataInt);
                            System.out.println(st.getMarks());
                            DBCRUD.updateStudentData(st);
                        }

                    }catch (ClassCastException | NumberFormatException er){
                        JOptionPane.showMessageDialog(null, "Please enter a valid integer!", "Admin Register", JOptionPane.ERROR_MESSAGE);
                        model.setValueAt(0, row, column);


                    }catch(SQLException er){
                        er.printStackTrace();
                    }
                }

            }
        });


        table.setRowHeight(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int i = 0; i < columnNames.size(); i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );

        }


        table.setFont(new Font("Consolas", Font.PLAIN, 15));
//        table.setEnabled(false);

        modulesPane = new JScrollPane(table);
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
        if (infoPanel != null) {
            bottomPanel.remove(infoPanel);
        }

        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(9, 5));

        ResultSet rs = DBCRUD.getTeacherData(t.getUsername());
        if (rs.next()) {
            JLabel tFirstName = new JLabel("First Name");
            JTextField textFirstName = new JTextField(rs.getString("firstName"));

            JLabel tLastName = new JLabel("Last Name");
            JTextField textLastName = new JTextField(rs.getString("lastName"));

            JLabel tPassword = new JLabel("Password");
            JTextField textPassword = new JTextField(rs.getString("password"));

            JLabel tUsername = new JLabel("Username");
            JTextField textUsername = new JTextField(rs.getString("username"));
            textUsername.setEnabled(false);

            JLabel tAddress = new JLabel("Address");
            JTextField textAddress = new JTextField(rs.getString("address"));

            JLabel tContact = new JLabel("Contact");
            JTextField textContact = new JTextField(rs.getString("contact"));


            JButton tEdit = new JButton("Edit");

            tEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    t.setFirstName(textFirstName.getText());
                    t.setLastName(textLastName.getText());
                    t.setAddress(textAddress.getText());
                    t.setContact(textContact.getText());
                    t.setPassword(textPassword.getText());

                    if (DBCRUD.updateTeacherData(t)) {
                        System.out.println("Updated!");
                    } else {
                        System.out.println("Not Updated");
                    }
                }
            });

            infoPanel.add(tFirstName);
            infoPanel.add(textFirstName);

            infoPanel.add(new JLabel(""));

            infoPanel.add(tLastName);
            infoPanel.add(textLastName);

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(tUsername);
            infoPanel.add(textUsername);

            infoPanel.add(new JLabel(""));

            infoPanel.add(tPassword);
            infoPanel.add(textPassword);

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(tAddress);
            infoPanel.add(textAddress);

            infoPanel.add(new JLabel(""));

            infoPanel.add(tContact);
            infoPanel.add(textContact);

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(new JLabel(""));

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(new JLabel(""));

            infoPanel.add(tEdit);


            infoPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
            infoPanel.setBounds(200, 0, WIDTH - 200, HEIGHT - ROW_HEIGHT);
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
                }else if(e.getSource() == resultBtn){
                    showResultPanel(teacherModules.get(0));
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
