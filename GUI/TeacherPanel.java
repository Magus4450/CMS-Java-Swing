package GUI;

import DBHelpers.DBCRUD;
import Users.Student;
import Users.Teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class TeacherPanel extends JFrame implements ActionListener {

    private final JPanel bottomPanel;
    private JPanel infoPanel;
    private JScrollPane modulesPane,resultPane;
    private final JButton logOutBtn;
    private final JButton modulesBtn;
    private final JButton resultBtn;
    private final JButton infoBtn;
    private final ArrayList<Integer> teacherModulesSem = new ArrayList<>();
    private ArrayList<String> teacherModules = new ArrayList<>();



    private final int HEIGHT = 490;
    private final int WIDTH = 1200;
    private final int ROW_HEIGHT = HEIGHT/7;
    private final Teacher t;

    private final Font titleFont = new Font("Bahnschrift", Font.PLAIN, 20);
    private final Font titleFont2 = new Font("Bahnschrift", Font.BOLD, 14);
    private final Font normalFont = new Font("Bahnschrift", Font.PLAIN, 13);

    TeacherPanel(Teacher t) throws SQLException {
        this.t = t;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH+10,HEIGHT+35);
        this.setLocationRelativeTo(null);


        JPanel topPanel = new JPanel();
        topPanel.setBounds(0,0,WIDTH,ROW_HEIGHT);
        topPanel.setBackground(new Color(204, 204, 204));
        topPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setText("Welcome, " + t.getFirstName());
        welcomeLabel.setFont(titleFont);
        welcomeLabel.setBounds(25,15,300,45);

//

        logOutBtn = new JButton("Logout");
        logOutBtn.setFont(normalFont);
        logOutBtn.setFocusable(false);
        logOutBtn.setBounds(1070,17,100,40);
        logOutBtn.addActionListener(this);


        topPanel.add(welcomeLabel);
        topPanel.add(logOutBtn);


        bottomPanel = new JPanel();
        bottomPanel.setBounds(0, ROW_HEIGHT, WIDTH,HEIGHT-ROW_HEIGHT );
        bottomPanel.setBackground(new Color(241, 241, 241));


        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(0, 0, 200,HEIGHT-ROW_HEIGHT);
        menuPanel.setBackground(new Color(109, 176, 109));
        menuPanel.setLayout(new GridLayout(6,1));

        modulesBtn = new JButton("Modules");
        modulesBtn.setFont(normalFont);
        modulesBtn.setSelected(true);


        resultBtn = new JButton("Result");
        resultBtn.setFont(normalFont);

        infoBtn = new JButton("View Info");
        infoBtn.setFont(normalFont);


        modulesBtn.addActionListener(this);
        resultBtn.addActionListener(this);
        infoBtn.addActionListener(this);

        menuPanel.add(modulesBtn);
        menuPanel.add(resultBtn);
        menuPanel.add(infoBtn);

        JPanel modulesInfoPanel = new JPanel();
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

    private void refresh(){
        if(resultPane!=null){
            bottomPanel.remove(resultPane);
        }
        if(infoPanel!=null) {
            bottomPanel.remove(infoPanel);
        }
        if(modulesPane!=null){
            bottomPanel.remove(modulesPane);
        }
    }


    private void showModulePanel() throws SQLException {

        refresh();



        ResultSet rs = DBCRUD.getTeacherData(t.getUsername());
//        String[] teacherModules = {};
        if(rs.next()){
            teacherModules = new ArrayList<>(Arrays.asList(rs.getString("teacherModules").split(" ")));
        }

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Module Code");
        columnNames.add("Module Name");
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

        table.getTableHeader().setFont(titleFont2);
        table.setFont(normalFont);





        modulesPane = new JScrollPane(table);
        modulesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        modulesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        modulesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);

        bottomPanel.add(modulesPane);
        SwingUtilities.updateComponentTreeUI(this);

    }



    private void showResultPanel(String moduleCode) throws SQLException {

        refresh();
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
                Object data = model.getValueAt(row, column);
                System.out.println(data);
                if(column == 5 && !data.equals("TBD")){
                    try{
                        int dataInt = Integer.parseInt((String) data);
                        if(dataInt < 0 || dataInt > 100){
                            JOptionPane.showMessageDialog(null, "Marks should be in range 0-100", "Incorrect Assignment", JOptionPane.ERROR_MESSAGE);
                            model.setValueAt("TBD", row, column);
                            return;
                        }
                        String student = (String)model.getValueAt(row,0);
                        ResultSet rs = DBCRUD.getStudentData(student);
                        Student st;

                        if (rs.next()){
                            st = new Student(rs.getString("username"), rs.getString("password"));
                            st.setMarksModule(moduleCode, dataInt);
                            DBCRUD.updateStudentData(st);
                        }

                    }catch (NumberFormatException er) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid integer!", "Incorrect Assignment", JOptionPane.ERROR_MESSAGE);
                        model.setValueAt("TBD", row, column);

                    }catch (ClassCastException er){
                        return;

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


        table.getTableHeader().setFont(titleFont2);
        table.setFont(normalFont);

        modulesPane = new JScrollPane(table);
        modulesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        modulesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        modulesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);


        bottomPanel.add(modulesPane);
        SwingUtilities.updateComponentTreeUI(this);

    }



    private void showInfoPane() throws SQLException {
        refresh();

        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(9, 5));

        ResultSet rs = DBCRUD.getTeacherData(t.getUsername());
        if (rs.next()) {
            JLabel tFirstName = new JLabel("First Name");
            tFirstName.setFont(normalFont);
            JTextField textFirstName = new JTextField(rs.getString("firstName"));
            textFirstName.setFont(normalFont);

            JLabel tLastName = new JLabel("Last Name");
            tLastName.setFont(normalFont);
            JTextField textLastName = new JTextField(rs.getString("lastName"));
            textLastName.setFont(normalFont);

            JLabel tPassword = new JLabel("Password");
            tPassword.setFont(normalFont);
            JTextField textPassword = new JTextField(rs.getString("password"));
            textPassword.setFont(normalFont);

            JLabel tUsername = new JLabel("Username");
            tUsername.setFont(normalFont);
            JTextField textUsername = new JTextField(rs.getString("username"));
            textUsername.setFont(normalFont);
            textUsername.setEnabled(false);

            JLabel tAddress = new JLabel("Address");
            tAddress.setFont(normalFont);
            JTextField textAddress = new JTextField(rs.getString("address"));
            textAddress.setFont(normalFont);

            JLabel tContact = new JLabel("Contact");
            tContact.setFont(normalFont);
            JTextField textContact = new JTextField(rs.getString("contact"));
            textContact.setFont(normalFont);


            JButton tEdit = new JButton("Edit");
            tEdit.setFont(normalFont);

            tEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    t.setFirstName(textFirstName.getText());
                    t.setLastName(textLastName.getText());
                    t.setAddress(textAddress.getText());
                    t.setContact(textContact.getText());
                    t.setPassword(textPassword.getText());

                    if(DBCRUD.updateTeacherData(t)){
                        JOptionPane.showMessageDialog(null, "Information Updated Successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "Information could not be updated!", "Error", JOptionPane.ERROR_MESSAGE);
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

}
