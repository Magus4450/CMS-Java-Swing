package GUI;

import DBHelpers.DBCRUD;
import Users.Admin;
import Users.Teacher;

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



public class AdminPanel extends JFrame implements ActionListener {

    JPanel topPanel, bottomPanel, menuPanel, modulesInfoPanel, courseEditPanel, teacherEditPanel;
    JScrollPane teachersPane,studentsPane, coursesPane;
    JButton logOutBtn, teachersBtn, studentsBtn, coursesBtn;
    JLabel welcomeLabel;

    AdminPanel ap = this;


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

    private void refresh(){
        if(studentsPane!=null){
            bottomPanel.remove(studentsPane);
        }
        if(coursesPane!=null) {
            bottomPanel.remove(coursesPane);
        }
        if(teachersPane!=null){
            bottomPanel.remove(teachersPane);
        }
        if(courseEditPanel!=null){
            bottomPanel.remove(courseEditPanel);
        }
        if(teacherEditPanel!=null){
            bottomPanel.remove(teacherEditPanel);
        }
    }

    private void showCoursesPanel() throws SQLException {

        refresh();

        ResultSet rs = DBCRUD.getAllCourseData();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Course Id");
        columnNames.add("Course Name");
        columnNames.add("Availability");
        columnNames.add("Students");

        ArrayList<Integer> allCourses = new ArrayList<>();
        Vector<Vector<String>> data = new Vector<>();
        while(rs.next()){
            Vector<String> row = new Vector<>(columnNames.size());
            System.out.println(rs.getString("courseId"));
            row.addElement(rs.getString("courseId"));
            row.add(rs.getString("courseName"));
            int courseIsAvailable = rs.getInt("courseIsAvailable");
            if(courseIsAvailable == 1){
                row.add("Yes");
            }else{
                row.add("No");
            }
            allCourses.add(rs.getInt("courseId"));
            data.addElement(row);
        }
        for(int i = 0 ; i < allCourses.size(); i++){
            Vector<String> v = data.get(i);
            v.addElement(Integer.toString(DBCRUD.getStudentCount(allCourses.get(i))));
        }

        JTable table = new JTable(data, columnNames){

            @Override
            public boolean isCellEditable(int row, int col) {
                System.out.println(row);
                try {
                    editCourses(Integer.parseInt((String)this.getValueAt(row,0)), (String)this.getValueAt(row, 1), false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return  false;
            }



        };

        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int i = 0; i < columnNames.size(); i ++){
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        }


        table.setFont(new Font("Consolas", Font.PLAIN, 15));



        coursesPane = new JScrollPane(table);
        coursesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        coursesPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        coursesPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT-50);


        courseEditPanel = new JPanel(null);
        JButton addCourseBtn = new JButton("Add Course");
        JButton addModuleBtn = new JButton("Add Module");
        addCourseBtn.setBounds(20,10,200,30);
        addModuleBtn.setBounds(250, 10, 200 ,30);
        addCourseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editCourses(0, null, true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        addModuleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addModule();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        courseEditPanel.add(addCourseBtn);
        courseEditPanel.add(addModuleBtn);
        courseEditPanel.setBounds(200, HEIGHT-ROW_HEIGHT-50, WIDTH-200, 50);

        bottomPanel.add(courseEditPanel);
        bottomPanel.add(coursesPane);
        SwingUtilities.updateComponentTreeUI(this);

    }

    private void editCourses(int courseId, String courseName, boolean isForNew) throws SQLException {

        System.out.println(courseName);
        ResultSet rs = null;
        ArrayList<String> courseModules = null;

        rs = DBCRUD.getCourseData(courseId);
        String courseMod = "";
        if (rs.next()){
            courseMod = rs.getString("courseModules");
        }
        courseModules = new ArrayList<>(Arrays.asList(courseMod.split(" ")));


        JFrame editCoursesFrame = new JFrame();

        JPanel editCoursePanel = new JPanel();

        editCoursePanel.setVisible(true);
        GridLayout gl = new GridLayout(30,3);
        gl.setHgap(10);
        gl.setVgap(20);
        editCoursePanel.setLayout(gl);
        editCoursePanel.setBorder(new EmptyBorder(10,30,10,30));


        editCoursesFrame.setVisible(true);
        editCoursesFrame.setResizable(false);
        editCoursesFrame.setSize(1000,600);
        editCoursesFrame.setLocationRelativeTo(null);
        editCoursesFrame.setLayout(null);

        String titleTxt;
        if(isForNew){
            titleTxt = "Add Course";
        }else{
            titleTxt = "Edit Course";
        }
        JLabel editCoursesTitle = new JLabel(titleTxt);
        editCoursesTitle.setFont(new Font("Consolas", Font.BOLD, 20));
        editCoursePanel.add(editCoursesTitle);
        editCoursePanel.add(new JLabel(""));
        editCoursePanel.add(new JLabel(""));

        JLabel editCoursesName = new JLabel("Course Name");
        JTextField editCoursesNameText = new JTextField();
        if(!isForNew){
            editCoursesNameText.setText(courseName);

        }

        ArrayList<JLabel> editNames = new ArrayList<>();
        ArrayList<JTextField> editNamesCode = new ArrayList<>();
        ArrayList<JTextField> editNamesText = new ArrayList<>();
        ArrayList<JComboBox<String>> editComboBox = new ArrayList<>();
        ArrayList<JComboBox<String>> editComboBoxElective = new ArrayList<>();

        editCoursePanel.add(editCoursesName);
        editCoursePanel.add(editCoursesNameText);
        editCoursePanel.add(new JLabel(""));

        JLabel editCourseAvailability = new JLabel("Availability");
        JComboBox<String> editCourseAvailabilityText = new JComboBox<>();
        editCourseAvailabilityText.addItem("Yes");
        editCourseAvailabilityText.addItem("No");
        if(!isForNew){
            if (rs.getInt("courseIsAvailable")==1){
                editCourseAvailabilityText.setSelectedItem("Yes");
            }else{
                editCourseAvailabilityText.setSelectedItem("No");

            }
        }


        editCoursePanel.add(editCourseAvailability);
        editCoursePanel.add(editCourseAvailabilityText);
        editCoursePanel.add(new JLabel(""));

        editCoursePanel.add(new JLabel(""));
        editCoursePanel.add(new JLabel("Module Code"));
        editCoursePanel.add(new JLabel("Module Name"));



        for(int i = 0; i < 24; i++){
            JLabel editCourseM;
            if(i >= 16){
                if(i >= 22){
                    editCourseM = new JLabel("Semester 6 (Elective)");
                }else if (i >= 20) {
                    editCourseM = new JLabel("Semester 5 (Elective)");
                }else if (i >= 18) {
                    editCourseM = new JLabel("Semester 6");
                }else{
                    editCourseM = new JLabel("Semester 5");
                }

            }else{
                editCourseM = new JLabel("Semester " + ((i/4)+1));
            }

            JTextField editCourseMCode = new JTextField();
            JTextField editCourseMText = new JTextField();
            JComboBox<String> moduleCodeBox = null;
            JComboBox<String> electiveModuleCodeBox = null;

            editCourseMText.setPreferredSize(new Dimension(200,30));
            editCourseMText.setMaximumSize(new Dimension(200,30));

            if(!isForNew){
                editCourseMCode.setEnabled(false);
                editCourseMCode.setPreferredSize(new Dimension(200,30));
                editCourseMCode.setMaximumSize(new Dimension(200,30));

            }else{
                editCourseMText.setEnabled(false);
                // For New
                rs = DBCRUD.getAllOptionalModule(0);
                ArrayList<String> allModuleCode = new ArrayList<>();
                ArrayList<String> allModule = new ArrayList<>();
                ArrayList<String> allModuleCodeElective = new ArrayList<>();
                ArrayList<String> allModuleElective = new ArrayList<>();
                while (rs.next()){
                    allModuleCode.add(rs.getString("moduleCode"));
                    allModule.add(rs.getString("moduleName"));

                }
                rs = DBCRUD.getAllOptionalModule(1);
                while (rs.next()){
                    allModuleCodeElective.add(rs.getString("moduleCode"));
                    allModuleElective.add(rs.getString("moduleName"));

                }
                moduleCodeBox = new JComboBox<>();
                electiveModuleCodeBox = new JComboBox<>();
//                JComboBox<String> moduleBox = new JComboBox<>();
                if(i < 8){

                    for(int j = 0 ; j < allModuleCode.size(); j++){

                        if (allModuleCode.get(j).charAt(0)== '4') {
                            moduleCodeBox.addItem(allModuleCode.get(j));
                        }
                    }
                }else if (i < 16){
                    for(int j = 0 ; j < allModuleCode.size(); j++){
                        if (allModuleCode.get(j).charAt(0)== '5') {
                            moduleCodeBox.addItem(allModuleCode.get(j));
                        }
                    }
                }else if(i < 20){
                    for(int j = 0 ; j < allModuleCode.size(); j++){
                        if (allModuleCode.get(j).charAt(0)== '6') {
                            moduleCodeBox.addItem(allModuleCode.get(j));
                        }
                    }
                }else{
                    for(int j = 0 ; j < allModuleCodeElective.size(); j++){
                        if (allModuleCodeElective.get(j).charAt(0)== '6') {
                            electiveModuleCodeBox.addItem(allModuleCodeElective.get(j));
                        }
                    }
                }

                moduleCodeBox.setPreferredSize(new Dimension(200,30));
                moduleCodeBox.setMaximumSize(new Dimension(200,30));
                if(i<20){
                    editComboBox.add(moduleCodeBox);

                }else{
                    editComboBoxElective.add(electiveModuleCodeBox);
                }
                moduleCodeBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JComboBox<String> currentBox = (JComboBox<String>)e.getSource();
                        String currentCode = (String) currentBox.getSelectedItem();
                        int index = editComboBox.indexOf(currentBox);

                        editNamesText.get(index).setText(allModule.get(allModuleCode.indexOf(currentCode)));

                    }
                });
                electiveModuleCodeBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JComboBox<String> currentBox = (JComboBox<String>)e.getSource();
                        String currentCode = (String) currentBox.getSelectedItem();
                        int index = editComboBoxElective.indexOf(currentBox) + 20;

                        editNamesText.get(index).setText(allModuleElective.get(allModuleCodeElective.indexOf(currentCode)));

                    }
                });

            }

            if(!isForNew) {
                rs = DBCRUD.getModuleData(courseModules.get(i));
                if (rs.next()) {
                    editCourseMCode.setText(rs.getString("moduleCode"));
                    editCourseMText.setText(rs.getString("moduleName"));
                }

                editCoursePanel.add(editCourseM);
                editCoursePanel.add(editCourseMCode);
                editCoursePanel.add(editCourseMText);

            } else{

                editCoursePanel.add(editCourseM);
                if(i < 20){
                    editCoursePanel.add(moduleCodeBox);

                }else{
                    editCoursePanel.add(electiveModuleCodeBox);
                }
                editCoursePanel.add(editCourseMText);

            }
            editNames.add(editCourseM);
            editNamesCode.add(editCourseMCode);
            editNamesText.add(editCourseMText);

        }
        String btnText = (isForNew) ? "Add" : "Edit";
        JButton editBtn = new JButton(btnText);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setForeground(new Color(75, 17, 17));

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this course?",
                        "Delete Course",
                        JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    System.out.println("BRUH");
                }else if (result == JOptionPane.NO_OPTION){
                    System.out.println("Nice");;
                }
            }
        });


        JLabel message = new JLabel("");
        editCoursePanel.add(message);
        if(isForNew){
            editCoursePanel.add(new JLabel(""));

        }else{
            editCoursePanel.add(deleteBtn);
        }
        editCoursePanel.add(editBtn);


        editBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String editedCourseName = editCoursesNameText.getText();
                System.out.println("COurseName: " + editedCourseName);
                ArrayList<String> modulesToAdd = new ArrayList<>();
                if(editedCourseName.equals("")){
                    message.setText("Fields cannot be empty");
                    return;
                }
                int isAvailable = (editCourseAvailabilityText.getSelectedItem() == "Yes") ? 1 : 0;
                System.out.println("Isava: " + isAvailable);
                if(!isForNew){
                    DBCRUD.updateCourseData(courseId, editedCourseName, isAvailable);
                }
                for(int i = 0; i < 24; i++) {

                    if(!isForNew){
                        if(editNamesCode.get(i).getText().equals("") || editNamesText.get(i).getText().equals("")){
                            message.setText("Fields cannot be empty");
                            return;
                        }
                        DBCRUD.updateModuleData(editNamesCode.get(i).getText(), editNamesText.get(i).getText());
                    }else{
                        if(editNamesText.get(i).getText().equals("")){
                            System.out.println((i+1)+">"+editNamesText.get(i).getText());
                            message.setText("Fields cannot be empty");
                            return;
                        }
                        if(modulesToAdd.contains((String)editComboBox.get(i).getSelectedItem())){
                            System.out.println(modulesToAdd.toString());
                            System.out.println(editNamesCode.get(i).getText());
                            message.setText("Duplicate Modules");
                            return;
                        }
                        modulesToAdd.add((String)editComboBox.get(i).getSelectedItem());
                    }
                }
                if(isForNew){
                    String finalModules = modulesToAdd.toString().replace("[","").replace("]","").replace(",","");
                    System.out.println(">"+finalModules+"<");

                    String[] insertData = {null, editedCourseName, "3", "6", Integer.toString(isAvailable), finalModules};
                    DBCRUD.insertIntoCourse(insertData);
                }

                editCoursesFrame.dispose();
                ap.dispose();
                try {
                    new AdminPanel(a);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }



            }
        });





//        editCoursePanel.setBounds(0, 0, 780, 600);

        JScrollPane pane = new JScrollPane(editCoursePanel);
        pane.setBounds(0, 0, 980, 600);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editCoursesFrame.add(pane, BorderLayout.CENTER);


    }

    private void addModule() throws SQLException {



        JFrame addModuleFrame = new JFrame();

        JPanel addModulePanel = new JPanel();

        addModulePanel.setVisible(true);
        GridLayout gl = new GridLayout(8,3);
        gl.setHgap(10);
        gl.setVgap(20);
        addModulePanel.setLayout(gl);
        addModulePanel.setBorder(new EmptyBorder(10,30,10,30));


        addModuleFrame.setVisible(true);
        addModuleFrame.setResizable(false);
        addModuleFrame.setSize(1000,500);
        addModuleFrame.setLocationRelativeTo(null);
        addModuleFrame.setLayout(null);


        JLabel editCoursesTitle = new JLabel("Add Module");
        editCoursesTitle.setFont(new Font("Consolas", Font.BOLD, 20));
        addModulePanel.add(editCoursesTitle);
        addModulePanel.add(new JLabel(""));
        addModulePanel.add(new JLabel(""));

        JComboBox<String> levelBox = new JComboBox<>();
        levelBox.addItem("4");
        levelBox.addItem("5");
        levelBox.addItem("6");

        JTextField addModuleCodeText = new JTextField();
        JTextField addModuleNameText = new JTextField();

        levelBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addModuleCodeText.setText((String) levelBox.getSelectedItem());
            }
        });

        addModulePanel.add(new JLabel("Level"));
        addModulePanel.add(levelBox);
        addModulePanel.add(new JLabel(""));

        JComboBox<Integer> sem = new JComboBox<>();
        sem.addItem(1);
        sem.addItem(2);
        sem.setSelectedItem(1);
        addModulePanel.add(new JLabel("Semester"));
        addModulePanel.add(sem);
        addModulePanel.add(new JLabel(""));



        addModulePanel.add(new JLabel("Module Code"));
        addModulePanel.add(addModuleCodeText);
        addModulePanel.add(new JLabel(""));

        addModulePanel.add(new JLabel("Module Name"));
        addModulePanel.add(addModuleNameText);
        addModulePanel.add(new JLabel(""));

        JComboBox<String> isOptional = new JComboBox<>();
        isOptional.addItem("Yes");
        isOptional.addItem("No");
        isOptional.setSelectedItem("No");

        addModulePanel.add(new JLabel("Optional"));
        addModulePanel.add(isOptional);
        addModulePanel.add(new JLabel(""));

        JButton addBtn = new JButton("Add");


        JLabel message = new JLabel("");
        addModulePanel.add(message);
        addModulePanel.add(new JLabel(""));
        addModulePanel.add(addBtn);


        addBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String level = (String)levelBox.getSelectedItem();
                String moduleCode = addModuleCodeText.getText();
                String moduleName = addModuleNameText.getText();
                String optional = (String)isOptional.getSelectedItem();

                int semester = (Integer)sem.getSelectedItem();

                if(level.equals("") || moduleName.equals("") || moduleCode.equals("")){
                    message.setText("Fields cannot be empty");
                    return;
                }
                if(moduleCode.charAt(0) != level.charAt(0)){
                    message.setText("Module Code must start with level");
                    return;
                }
                if(!level.equals("6") && optional.equals("Yes")){
                    message.setText("Optional Module can only be set for level 6.");
                    return;
                }
                if(!moduleName.contains("(Elective)")){
                    moduleName = moduleName + " (Elective)";
                }

                try{
                    ResultSet rs = DBCRUD.getAllModuleData();
                    ArrayList<String> allModules = new ArrayList<>();
                    ArrayList<String> allCodes = new ArrayList<>();
                    while(rs.next()){
                        allModules.add(rs.getString("moduleName"));
                        allCodes.add(rs.getString("moduleCode"));
                    }
                    if(allCodes.contains(moduleCode)){
                        message.setText("Module Code already in use");
                        return;
                    }
                    if(allModules.contains(moduleName)){
                        message.setText("Module Name already in use");
                        return;
                    }
                }catch (SQLException er){
                    er.printStackTrace();
                }
                int finalSem = (level.equals("6")) ? 4 + semester : (level.equals("5") ? 2 + semester : semester);
                String[] moduleData = {moduleCode, moduleName, level, "20", (optional.equals("Yes")) ? "1" : "0", Integer.toString(finalSem), "null", "0"};
                System.out.println(Arrays.toString(moduleData));
                DBCRUD.insertIntoModule(moduleData);

                addModuleFrame.dispose();
                ap.dispose();
                try {
                    new AdminPanel(a);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });




        JScrollPane pane = new JScrollPane(addModulePanel);
        pane.setBounds(0, 0, 980, 500);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addModuleFrame.add(pane, BorderLayout.CENTER);


    }



    public void showTeachersPanel() throws SQLException {

        refresh();


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


            @Override
            public boolean isCellEditable(int row, int col) {

                try {
                    editTeacher((String)this.getValueAt(row,0));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return  false;
            }

        };

        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int i = 0; i < columnNames.size(); i ++){
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        }


        table.setFont(new Font("Consolas", Font.PLAIN, 15));


        teachersPane = new JScrollPane(table);
        teachersPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        teachersPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        teachersPane.setBounds(200, 0, WIDTH-200, HEIGHT-ROW_HEIGHT-50);

        teacherEditPanel = new JPanel(null);
        JButton addTeacherBtn = new JButton("Add Teacher");
        addTeacherBtn.setBounds(20,10,200,30);

        addTeacherBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterForm("TEACHER", a);
                ap.dispose();

            }
        });

        teacherEditPanel.add(addTeacherBtn);
//        teacherEditPanel.setBackground(Color.red);
        teacherEditPanel.setBounds(200, HEIGHT-ROW_HEIGHT-50, WIDTH-200, 50);

        bottomPanel.add(teacherEditPanel);


        bottomPanel.add(teachersPane);
        SwingUtilities.updateComponentTreeUI(this);

    }
    private void editTeacher(String username) throws SQLException {

        ResultSet rs = DBCRUD.getAllModuleData();
        ArrayList<String> allModules = new ArrayList<>();
        while(rs.next()){
            allModules.add(rs.getString("moduleCode"));
        }


        rs = DBCRUD.getTeacherData(username);
        if(rs.next()){

            JFrame editTeacherFrame = new JFrame();

            JPanel editTeacherPanel = new JPanel();

            editTeacherPanel.setVisible(true);
            GridLayout gl = new GridLayout(12,2);
            gl.setHgap(10);
            gl.setVgap(20);
            editTeacherPanel.setLayout(gl);
            editTeacherPanel.setBorder(new EmptyBorder(10,30,10,30));


            editTeacherFrame.setVisible(true);
            editTeacherFrame.setResizable(false);
            editTeacherFrame.setSize(1000,600);
            editTeacherFrame.setLocationRelativeTo(null);
            editTeacherFrame.setLayout(null);


            JLabel editTeacherTitle = new JLabel("Edit Teacher Info");
            editTeacherTitle.setFont(new Font("Consolas", Font.BOLD, 20));
            editTeacherPanel.add(editTeacherTitle);
            editTeacherPanel.add(new JLabel(""));

            JLabel editTeacherUserName = new JLabel("Username");
            JTextField editTeacherUserNameText = new JTextField();
            editTeacherUserNameText.setText(username);
            editTeacherUserNameText.setEnabled(false);

            JLabel editTeacherFirstName = new JLabel("First Name");
            JTextField editTeacherFirstNameText = new JTextField();
            editTeacherFirstNameText.setText(rs.getString("firstName"));

            JLabel editTeacherLastName = new JLabel("Last Name");
            JTextField editTeacherLastNameText = new JTextField();
            editTeacherLastNameText.setText(rs.getString("lastName"));

            JLabel editTeacherAddress = new JLabel("Address");
            JTextField editTeacherAddressText = new JTextField();
            editTeacherAddressText.setText(rs.getString("address"));

            JLabel editTeacherContact = new JLabel("Contact");
            JTextField editTeacherContactText = new JTextField();
            editTeacherContactText.setText(rs.getString("contact"));


            ArrayList<String> teacherModules = new ArrayList<>(Arrays.asList(rs.getString("teacherModules").split(" ")));
            while(teacherModules.size() < 4){
                teacherModules.add("");
            }

            JComboBox<String> mod1 = new JComboBox<>();
            JComboBox<String>mod2 = new JComboBox<>();
            JComboBox<String> mod3 = new JComboBox<>();
            JComboBox<String> mod4 = new JComboBox<>();

            for (String allModule : allModules) {
                mod1.addItem(allModule);
                mod2.addItem(allModule);
                mod3.addItem(allModule);
                mod4.addItem(allModule);
            }

            mod1.addItem("");
            mod2.addItem("");
            mod3.addItem("");
            mod4.addItem("");

            mod1.setEditable(false);
            mod1.setSelectedItem(teacherModules.get(0));

            mod2.setEditable(false);
            mod2.setSelectedItem(teacherModules.get(1));

            mod3.setEditable(false);
            mod3.setSelectedItem(teacherModules.get(2));

            mod4.setEditable(false);
            mod4.setSelectedItem(teacherModules.get(3));


            JLabel editTeacherMod1 = new JLabel("Module 1");

            JLabel editTeacherMod2 = new JLabel("Module 2");

            JLabel editTeacherMod3 = new JLabel("Module 3");

            JLabel editTeacherMod4 = new JLabel("Module 4");



            editTeacherPanel.add(editTeacherUserName);
            editTeacherPanel.add(editTeacherUserNameText);

            editTeacherPanel.add(editTeacherFirstName);
            editTeacherPanel.add(editTeacherFirstNameText);

            editTeacherPanel.add(editTeacherLastName);
            editTeacherPanel.add(editTeacherLastNameText);

            editTeacherPanel.add(editTeacherAddress);
            editTeacherPanel.add(editTeacherAddressText);

            editTeacherPanel.add(editTeacherContact);
            editTeacherPanel.add(editTeacherContactText);

            editTeacherPanel.add(editTeacherMod1);
            editTeacherPanel.add(mod1);

            editTeacherPanel.add(editTeacherMod2);
            editTeacherPanel.add(mod2);

            editTeacherPanel.add(editTeacherMod3);
            editTeacherPanel.add(mod3);

            editTeacherPanel.add(editTeacherMod4);
            editTeacherPanel.add(mod4);


            JButton editBtn = new JButton("Edit");
            editBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String modu1 = (String)mod1.getSelectedItem();
                    String modu2 = (String)mod2.getSelectedItem();
                    String modu3 = (String)mod3.getSelectedItem();
                    String modu4 = (String)mod4.getSelectedItem();
                    String modules = modu1 + " " + modu2 + " " + modu3 + " " + modu4;
                    Teacher t = new Teacher(editTeacherUserNameText.getText());
                    t.setFirstName(editTeacherFirstNameText.getText());
                    t.setLastName(editTeacherLastNameText.getText());
                    t.setAddress(editTeacherAddressText.getText());
                    t.setContact(editTeacherContactText.getText());
                    t.setTeacherModules(modules);
                    DBCRUD.updateTeacherData(t);

                    editTeacherFrame.dispose();
                    ap.dispose();
                    try {
                        AdminPanel adminPanel = new AdminPanel(a);
                        adminPanel.showTeachersPanel();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });


            editTeacherPanel.add(new JLabel(""));
            editTeacherPanel.add(editBtn);



    //        editCoursePanel.setBounds(0, 0, 780, 600);

            JScrollPane pane = new JScrollPane(editTeacherPanel);
            pane.setBounds(0, 0, 980, 600);
            pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            editTeacherFrame.add(pane, BorderLayout.CENTER);

        }
    }

    private void showStudentsPanel() throws SQLException {
        refresh();

        ResultSet rs = DBCRUD.getAllStudentData();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Name");
        columnNames.add("Contact");
        columnNames.add("Level");
        columnNames.add("Semester");
        columnNames.add("Course");

        ArrayList<Integer> studentCourses= new ArrayList<>();
        Vector<Vector<String>> data = new Vector<>();
        while(rs.next()){

            Vector<String> row = new Vector<>(columnNames.size());
            row.addElement(rs.getString("firstName")+ " " + rs.getString("lastName"));
            row.add(rs.getString("contact"));

            studentCourses.add(rs.getInt("enrolledCourse"));
            row.add(rs.getString("studentLevel"));
            row.add(Integer.toString(rs.getInt("passedSem")+1));
            data.addElement(row);
        }

        for(int i = 0; i < studentCourses.size(); i++){

            rs =DBCRUD.getCourseData(studentCourses.get(i));
            if(rs.next()){
                Vector<String> v = data.get(i);
                v.addElement(rs.getString("courseName"));
            }

        }

        JTable table = new JTable(data, columnNames){

            @Override
            public boolean isCellEditable(int row, int col) {
                return  false;
            }

        };

        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(400);
//        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setVerticalAlignment( JLabel.CENTER );
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int i = 0; i < columnNames.size(); i ++){
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        }

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
