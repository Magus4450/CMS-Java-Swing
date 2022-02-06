package GUI;

import DBHelpers.DBCRUD;
import Users.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class StudentPanel extends JFrame implements ActionListener {

    private final JPanel bottomPanel;
    private JPanel infoPanel;
    private JPanel chooseElectivePanel;
    private JScrollPane modulesPane;
    private final JButton logOutBtn;
    private final JButton modulesBtn;
    private final JButton resultBtn;
    private final JButton infoBtn;
    private String[] modules = {};
    private ArrayList<String> studentModules = null;
    private final ArrayList<String> teacherUsername = new ArrayList<>();
    private ArrayList<String> studentMarks = null;
    private final int HEIGHT = 490;
    private final int WIDTH = 1200;
    private final int ROW_HEIGHT = HEIGHT/7;
    private final Student st;
    private final Font titleFont2 = new Font("Bahnschrift", Font.BOLD, 14);
    private final Font normalFont = new Font("Bahnschrift", Font.PLAIN, 13);

    private final StudentPanel s;


    public StudentPanel(Student st) throws SQLException {
        this.st = st;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH+10,HEIGHT+35);
        this.setLocationRelativeTo(null);


        JPanel topPanel = new JPanel();
        topPanel.setBounds(0,0,WIDTH,ROW_HEIGHT);
        topPanel.setBackground(new Color(204, 204, 204));
        topPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setText("Welcome, " + st.getFirstName());
        Font titleFont = new Font("Bahnschrift", Font.PLAIN, 20);
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

        s = this;
    }

    private void refresh(){

        if(infoPanel!=null){
            bottomPanel.remove(infoPanel);
        }
        if(modulesPane!=null){
            bottomPanel.remove(modulesPane);
        }
        if(chooseElectivePanel!=null){
            bottomPanel.remove(chooseElectivePanel);

        }
    }
    private void showModulePanel() throws SQLException {

        refresh();

        ResultSet rs = DBCRUD.getStudentData(st.getUsername());

        if(rs.next()){
            modules = rs.getString("enrolledModules").split(" ");
        }
//        rs = DBCRUD.getCourseData(st.getEnrolledCourse());
//        if(rs.next()){
//            studentModules = new ArrayList<>(Arrays.asList(rs.getString("courseModules").split(" ")));
//        }
        studentModules = new ArrayList<>(Arrays.asList(modules));
        studentMarks = new ArrayList<>(Arrays.asList(st.getMarks().split(" ")));

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Module Code");
        columnNames.add("Enrolled Modules");
        columnNames.add("Level");
        columnNames.add("Credits");
        columnNames.add("Teacher");


        Vector<Vector<String>> data = new Vector<>();
//        for(int i = 4*(st.getPassedSem()); i < Math.min(modules.length, 4*(st.getPassedSem()+1)); i++){
        int start = 0;
        int end = modules.length;
        if(st.getPassedSem() < 5){
            start = 4*(st.getPassedSem());
        }else if(st.getPassedSem() == 5){
            start = 4*(st.getPassedSem()) - 1 ;
        }
        for(int i = start; i < end; i++){

            Vector<String> row = new Vector<>(columnNames.size());
            if(modules[i].equals("(Elective)")){
                row.addElement("Elective");
                row.addElement("Elective");
                row.addElement("Elective");
                row.addElement("Elective");
                teacherUsername.add("Elective");
            }else{
                rs = DBCRUD.getModuleData(modules[i]);
                if(rs.next() && rs.getInt("moduleSem") <= st.getPassedSem()+1){
                    row.addElement(rs.getString("moduleCode"));
                    row.addElement(rs.getString("moduleName"));
                    row.addElement(rs.getString("moduleLevel"));
                    row.addElement(rs.getString("moduleCredit"));
                    teacherUsername.add(rs.getString("moduleTeacher"));
                }
            }

            data.addElement(row);
        }



        rs = DBCRUD.getCourseData(st.getEnrolledCourse());
//        String[] modules = {};
//
//        if(rs.next()){
//            modules = rs.getString("courseModules").split(" ");
//        }

        for(int i = 0; i < end-start; i++){
            Vector<String> v = data.get(i);
            if(teacherUsername.get(i).equals("Elective")){
                v.addElement("Elective");
            }
            rs =DBCRUD.getTeacherData(teacherUsername.get(i));
            if(rs.next()){
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

        table.getTableHeader().setFont(titleFont2);
        table.setFont(normalFont);

        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int i = 0; i < columnNames.size(); i++){
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        }






        table.setEnabled(false);

        modulesPane = new JScrollPane(table);
        modulesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        modulesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        modulesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT-50);


        bottomPanel.add(modulesPane);

        chooseElectivePanel = new JPanel(null);
        JButton chooseElectiveBtn = new JButton("Choose Elective");
        chooseElectiveBtn.setFont(normalFont);
        chooseElectiveBtn.setBounds(20,10,200,30);
//        if(st.getPassedSem()<4){
//            chooseElectiveBtn.setEnabled(false);
//        }else
        if(!st.getEnrolledModules().contains("(Elective)")){
            chooseElectiveBtn.setEnabled(false);
        }
        chooseElectivePanel.add(chooseElectiveBtn);

        chooseElectiveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> courseModules = null;
                ArrayList<String> electiveMods = new ArrayList<>();

                JFrame electiveFrame = new JFrame();
                electiveFrame.setVisible(true);
                electiveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                electiveFrame.setSize(600,200);
                electiveFrame.setLocationRelativeTo(null);
                electiveFrame.setTitle("Choose Elective Module");


                GridLayout gl = new GridLayout(2,2);
                gl.setVgap(40);
                JPanel electivePanel = new JPanel(gl);
                electivePanel.setVisible(true);
                electivePanel.setBorder(new EmptyBorder(20,20,20,20));

                JLabel electiveLabel = new JLabel("Level " + (st.getPassedSem() + 1) + " Elective:");
                electiveLabel.setFont(normalFont);

                JComboBox<String> electiveBox = new JComboBox<>();


                ResultSet rs = DBCRUD.getCourseData(st.getEnrolledCourse());
                try {
                    if (rs.next()) {
                        courseModules = new ArrayList<>(Arrays.asList(rs.getString("courseModules").split(" ")));
                    }


                    for (String courseMod : courseModules) {
                        rs = DBCRUD.getModuleData(courseMod);
                        if (rs.next()) {
                            if (rs.getInt("moduleSem") == st.getPassedSem() + 1 && rs.getInt("isOptional") == 1) {
                                electiveMods.add(courseMod);
                                electiveBox.addItem(rs.getString("moduleName").replace("(Elective)",""));
                            }
                        }
                    }


                }catch (SQLException er){
                    er.printStackTrace();
                }




                JButton addElectiveBtn = new JButton("Confirm");
                addElectiveBtn.setFont(normalFont);

                addElectiveBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String chosenMod = electiveMods.get(electiveBox.getSelectedIndex());
                        st.setEnrolledModules(studentModules.toString().replace("(Elective)", chosenMod).replace("[","").replace(",","").replace("]",""));
                        DBCRUD.updateStudentData(st);
                        s.dispose();
                        try {
                            new StudentPanel(st);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

                electivePanel.add(electiveLabel);
                electivePanel.add(electiveBox);
                electivePanel.add(new JLabel(""));
                electivePanel.add(addElectiveBtn);

                electiveFrame.add(electivePanel);
            }
        });

//        chooseElectivePanel.add(addTeacherBtn);
//        teacherEditPanel.setBackground(Color.red);
        chooseElectivePanel.setBounds(200, HEIGHT-ROW_HEIGHT-50, WIDTH-200, 50);

        bottomPanel.add(chooseElectivePanel);


        SwingUtilities.updateComponentTreeUI(this);
    }

    private void showResultPane() throws SQLException {

        refresh();

        ArrayList<String> studentRemarks = new ArrayList<>(Arrays.asList(st.getRemarks().split(" ")));
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Module Code");
        columnNames.add("Module Name");
        columnNames.add("Marks");




        JPanel semResultPanel = new JPanel();
        semResultPanel.setLayout(new BoxLayout(semResultPanel, BoxLayout.Y_AXIS));
//        semResultPanel.setSize(new Dimension(400,200));
        for(int i = 0; i < st.getPassedSem()+1; i++){
            ArrayList<String> currentMarks = new ArrayList<>();
            JLabel semLabel = new JLabel("Semester " + (i+1));
            semLabel.setFont(new Font("Consolas", Font.BOLD, 20));
            semLabel.setBorder(new EmptyBorder(10,10,10,10));
//            semLabel.add(Box.createRigidArea(new Dimension(10,50)));
            Vector<Vector<String>> data = new Vector<>();
            if(i < 4){
                for(int j = 0; j < 4; j++){
                    Vector<String> row = new Vector<>(columnNames.size());
                    ResultSet rs = DBCRUD.getModuleData(studentModules.get((i*4)+j));
                    if(rs.next()){
                        row.addElement(rs.getString("moduleCode"));
                        row.addElement(rs.getString("moduleName"));
                        row.addElement(studentMarks.get((i*4)+j));
                        currentMarks.add(studentMarks.get((i*4)+j));
                    }
                    data.addElement(row);
                }
            }else if (i==4){
                for(int j = 0; j < 3; j++){
                    Vector<String> row = new Vector<>(columnNames.size());
                    ResultSet rs = DBCRUD.getModuleData(studentModules.get(16+j));
                    if(rs.next()){
                        row.addElement(rs.getString("moduleCode"));
                        row.addElement(rs.getString("moduleName"));
                        row.addElement(studentMarks.get((i*4)+j));
                        currentMarks.add(studentMarks.get((i*4)+j));
                    }
                    data.addElement(row);
                }
            }else{
                for(int j = 0; j < 3; j++){
                    Vector<String> row = new Vector<>(columnNames.size());
                    ResultSet rs = DBCRUD.getModuleData(studentModules.get(19+j));
                    if(rs.next()){
                        row.addElement(rs.getString("moduleCode"));
                        row.addElement(rs.getString("moduleName"));
                        row.addElement(studentMarks.get((i*4)+j));
                        currentMarks.add(studentMarks.get((i*4)+j));
                    }
                    data.addElement(row);
                }
            }



            JTable table = new JTable(data, columnNames);



            table.setRowHeight(30);
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(380);
            table.getColumnModel().getColumn(2).setPreferredWidth(60);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setVerticalAlignment( JLabel.CENTER );
            centerRenderer.setHorizontalAlignment( JLabel.CENTER );
            for(int j = 0; j < columnNames.size(); j++){
                table.getColumnModel().getColumn(j).setCellRenderer( centerRenderer );
            }

            table.getTableHeader().setFont(titleFont2);
            table.setFont(normalFont);


            table.setEnabled(false);
            JScrollPane tablePane = new JScrollPane(table);
            tablePane.setPreferredSize(new Dimension(WIDTH-200, 200));

            GridLayout gh = new GridLayout(1,5);
            gh.setHgap(10);

            JPanel summaryPanel = new JPanel(gh);
            summaryPanel.setBorder(new EmptyBorder(30,30,120,30));


            int avg = 0;
            for(String marks: currentMarks){
                if(!marks.equals("TBD")){
                    avg+=Integer.parseInt(marks);
                }
            }
            if(i<4){
                avg = avg/4;

            }else{
               avg = avg/3;
            }
            JLabel percentageLabel = new JLabel("Percentage");
            percentageLabel.setFont(titleFont2);

            JLabel percentageText = new JLabel(Integer.toString(avg));
            percentageText.setFont(titleFont2);

            JLabel remarksLabel = new JLabel("Remarks");
            remarksLabel.setFont(titleFont2);
            JLabel remarksText = (studentRemarks.get(i).equals("null"))? new JLabel("TBD"): new JLabel(studentRemarks.get(i));
            remarksText.setFont(titleFont2);


            summaryPanel.add(percentageLabel);
            summaryPanel.add(percentageText);
            summaryPanel.add(new JLabel(""));
            summaryPanel.add(remarksLabel);
            summaryPanel.add(remarksText);


            semResultPanel.add(semLabel);
            semResultPanel.add(tablePane);
            semResultPanel.add(summaryPanel);




        }



        modulesPane = new JScrollPane(semResultPanel);
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

        ResultSet rs = DBCRUD.getStudentData(st.getUsername());
        if(rs.next()){
            JLabel stdFirstName = new JLabel("First Name");
            stdFirstName.setFont(normalFont);
            JTextField textFirstName = new JTextField(rs.getString("firstName"));
            textFirstName.setFont(normalFont);

            JLabel stdLastName = new JLabel("Last Name");
            stdLastName.setFont(normalFont);
            JTextField textLastName = new JTextField(rs.getString("lastName"));
            textLastName.setFont(normalFont);

            JLabel stdPassword = new JLabel("Password");
            stdPassword.setFont(normalFont);

            JTextField textPassword = new JTextField(rs.getString("password"));
            textPassword.setFont(normalFont);

            JLabel stdUsername = new JLabel("Username");
            stdUsername.setFont(normalFont);
            JTextField textUsername = new JTextField(rs.getString("username"));
            textUsername.setFont(normalFont);
            textUsername.setEnabled(false);

            JLabel stdAddress = new JLabel("Address");
            stdAddress.setFont(normalFont);
            JTextField textAddress = new JTextField(rs.getString("address"));
            textAddress.setFont(normalFont);

            JLabel stdContact = new JLabel("Contact");
            stdContact.setFont(normalFont);
            JTextField textContact = new JTextField(rs.getString("contact"));
            textContact.setFont(normalFont);




            JLabel stdLevel = new JLabel("Level");
            stdLevel.setFont(normalFont);
            JTextField textLevel = new JTextField(rs.getString("studentLevel"));
            textLevel.setFont(normalFont);
            textLevel.setEnabled(false);

            JLabel stdEnrolledCourse = new JLabel("Enrolled Course");
            stdEnrolledCourse.setFont(normalFont);
            int courseId = rs.getInt("enrolledCourse");
            ResultSet rs2 = DBCRUD.getCourseData(courseId);
            JTextField textEnrolledCourse = null;
            if(rs2.next()){
                textEnrolledCourse = new JTextField(rs2.getString("courseName"));
            }

            textEnrolledCourse.setEnabled(false);

            JButton stdEdit = new JButton("Edit");
            stdEdit.setFont(normalFont);

            stdEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    st.setFirstName(textFirstName.getText());
                    st.setLastName(textLastName.getText());
                    st.setAddress(textAddress.getText());
                    st.setContact(textContact.getText());
                    st.setPassword(textPassword.getText());

                    if(DBCRUD.updateStudentData(st)){
                        JOptionPane.showMessageDialog(null, "Information Updated Successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "Information could not be updated!", "Error", JOptionPane.ERROR_MESSAGE);
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

            infoPanel.add(new JLabel(""));
            infoPanel.add(new JLabel(""));

            infoPanel.add(new JLabel(""));

            infoPanel.add(stdEdit);



            infoPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
            infoPanel.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT);
            bottomPanel.add(infoPanel);

            // Refresh the JFrame
            SwingUtilities.updateComponentTreeUI(this);
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
                    showInfoPane();
                }else if (e.getSource() == resultBtn){
                    showResultPane();
                }
            }
        }catch (SQLException er){
            er.printStackTrace();
        }

    }


}
