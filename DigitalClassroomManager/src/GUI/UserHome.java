/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.mysql.jdbc.Blob;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.Accounts;
import model.ClassRecord;
import model.CurrentUser;
import model.OldUser;
import model.Subject;
import model.User;
import model.Classes;
import model.CurrentDate;
import model.GradingSystem;
import model.Student;
import service.implementations.AccountsServiceImplementation;
import service.implementations.ClassRecordServiceImplementation;
import service.implementations.SubjectServiceImplementation;
import service.implementations.ClassServiceImplementation;
import service.implementations.GradingSystemServiceImplementation;
import service.implementations.StudentServiceImplementation;
import service.implementations.UserServiceImplementation;
import service.interfaces.AccountsServiceInterface;
import service.interfaces.ClassRecordServiceInterface;
import service.interfaces.ClassServiceInterface;
import service.interfaces.GradingSystemServiceInterface;
import service.interfaces.StudentServiceInterface;
import service.interfaces.SubjectServiceInterface;
import service.interfaces.UserServiceInterface;
import tools.ErrorException;
import tools.ExtensionFileFilter;
import tools.FormatTool;
import tools.Transparent;

/**
 *
 * @author sam
 */
public class UserHome extends javax.swing.JFrame {

    private File file;
    private Object object;
    private FormatTool formatTool = new FormatTool();
    private UserServiceInterface userService;
    private AccountsServiceInterface accountService;
    private User user;
    private Accounts acnt;
    private StudentServiceInterface studentService;
    private Student student;
    private ArrayList<Subject> subjectSearch;
    private ArrayList<User> userSearch;
    private javax.swing.border.EtchedBorder boarder;
    private SubjectServiceInterface subjectService;
    private Subject subject;
    private ClassServiceInterface classService;
    private Classes classes;
    private ArrayList<Classes> classSearch;
    private int subjectID;
    private boolean bPic = false;
    private boolean createDone = false;
    private String tusername;
    private String tlname;
    private String tfname;
    private String tmname;
    private String tcity;
    private String tprovince;
    private String tcontacNo;
    private String temail;
    private String tfacultyID;
    private CurrentUser userCurrent;
    private UserHome parentFrame;
    private profileFrame profileF;
    private int classID;
    private DefaultComboBoxModel comboBox;
    private int overallOld;
    private JFileChooser fc = new JFileChooser();
    private CurrentDate cDate;
    private AddEntry addM;
    private GradingSystem gradesTo;
    private GradingSystem fromGradeDialog = new GradingSystem();
    private int classIDNumberSelected;
    private Classes classesTo = new Classes();
    private boolean uploaded = false;
    private int pPercentage = 100;
    private String nameSubject;
    private String yearSubject;
    private String sectionSubject;
    private String semesterSubject;
    private int userType;
    private Date dates = new Date();
    private FileFilter filter1 = new ExtensionFileFilter("Text", new String("TXT"));
    String classSubject1;
    String classYear1;
    String classSection1;
    String classSemester1;
    String classDescription1;
    String classRoom1;
    String classSchedule1;

    /**
     * Creates new form UserHome
     */
    public UserHome(CurrentUser currentUser, GradingSystem gradeFromDialog) {
        this.mainFieldG = new JTextField[20];
        Transparent trans = new Transparent();
        trans.TransparentForm(this);
        initComponents();
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        accountFrame.setBounds(-10, -28, 520, 529);
        homeFrame.setBounds(-10, -25, 508, 529);
        addClass.setBounds(-10, -25, 508, 529);
        editClass.setBounds(-10, -25, 508, 529);
        setUpClass.setBounds(-10, -25, 508, 529);
        manageClass.setBounds(-10, -25, 508, 529);
        gradingSystem.setBounds(-10, -25, 508, 529);
        instructorFrame.setBounds(-10, -25, 508, 529);
        subjectsFrame.setBounds(-10, -25, 508, 529);
        addSubFrame.setBounds(-10, -25, 508, 529);
        editSubFrame.setBounds(-10, -25, 508, 529);
        viewClassFrame.setBounds(-10, -25, 508, 529);
        userCurrent = currentUser;

        fromGradeDialog = gradeFromDialog;
        //setGradingSystemView();
        homeViewClass();
        setCurrentUser();
        setProfile();
        close();
        homeFrame.setVisible(true);
        browse.setVisible(false);
        save.setVisible(false);
        cancel.setVisible(false);
        cancelAccount.setVisible(false);
        //setGradingSystemView3();

        jScrollPane1.setAutoscrolls(true);
        jPanel5.setAutoscrolls(true);
        passingRate.requestFocus();
    }

    public void openPDF() {
        try {
            System.out.println("Opening PDF");
            File file = new File("User'sManual.pdf");
            Desktop.getDesktop().open(file);
            String absolutePDFpath = file.getAbsolutePath().replace("" + (char) 92, "" + (char) 92 + (char) 9);
            System.out.println("Path = " + absolutePDFpath);
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + absolutePDFpath);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot locate the User Manual file!"
                    + " document\nFoutcode: 0xFF05");
            Logger.getLogger(PrintJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disableAttendance(int classid) {
        GradingSystemServiceInterface gsService = new GradingSystemServiceImplementation();
        GradingSystem gs = new GradingSystem();
        try {
            gs = gsService.getGSfk("Attendance", classid);
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }
        if (gs.isEmpty()) {
            checkAttendance.setEnabled(false);
        } else {
            checkAttendance.setEnabled(true);
        }

    }

    public void setGradingSystemView() {
        // jLabel57.setText(String.valueOf(overall));
        int totalPercentage = 0;
        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        GradingSystemServiceInterface gradingService = new GradingSystemServiceImplementation();
        ArrayList<GradingSystem> gradingSearch = new ArrayList<>();
        try {
            gradingSearch = gradingService.getGradingSystem(fromGradeDialog.getClassID());
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }
        classIDNumberSelected = fromGradeDialog.getClassID();
        for (GradingSystem temp : gradingSearch) {
            model.addRow(new Object[]{temp.getEntryName(), temp.getPercentage()});
            pPercentage = pPercentage - Integer.parseInt(temp.getPercentage());

        }

        jLabel57.setText(String.valueOf(pPercentage));
        close();
        gradingSystem.setVisible(true);
    }

    public void setGradingSystemView3() {
        // jLabel57.setText(String.valueOf(overall));
        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        GradingSystemServiceInterface gradingService = new GradingSystemServiceImplementation();
        ArrayList<GradingSystem> gradingSearch = new ArrayList<>();
        try {
            gradingSearch = gradingService.getGradingSystem(classIDNumberSelected);
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }
        // classIDNumberSelected=fromGradeDialog.getClassID();
        for (GradingSystem temp : gradingSearch) {
            model.addRow(new Object[]{temp.getEntryName(), temp.getPercentage()});
            pPercentage = pPercentage - Integer.parseInt(temp.getPercentage());
        }

        jLabel57.setText(String.valueOf(pPercentage));
        close();
        gradingSystem.setVisible(true);
    }

    public void setGradingSystemView2() {

        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        GradingSystemServiceInterface gradingService = new GradingSystemServiceImplementation();
        //GradingSystem gSystem=new GradingSystem();
        ArrayList<GradingSystem> gradingSearch = new ArrayList<>();
        try {
            gradingSearch = gradingService.getGradingSystem(fromGradeDialog.getClassID());
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }
        classIDNumberSelected = fromGradeDialog.getClassID();
        for (GradingSystem temp : gradingSearch) {
            model.addRow(new Object[]{temp.getEntryName(), temp.getPercentage()});

            overallOld += Integer.parseInt(temp.getPercentage());

        }
        jLabel57.setText(String.valueOf(100 - overallOld));
        close();
        gradingSystem.setVisible(true);
    }

    public void updateTable() {
        subjectService = new SubjectServiceImplementation();
        subjectSearch = new ArrayList<Subject>();
        DefaultTableModel model = (DefaultTableModel) this.subjList.getModel();

        subjectSearch.clear();
        try {
            subjectSearch = subjectService.getSubjects();
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }

        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (Subject su : subjectSearch) {
            model.addRow(new Object[]{su.getSubjectName(), su.getDescription(), su.getUnits(), su.getSemester()});

        }
    }

    public void setCurrentUser() {
        userService = new UserServiceImplementation();
        accountService = new AccountsServiceImplementation();
        user = new User();
        acnt = new Accounts();
        try {
            user = userService.getUser(userCurrent.getUserID());
            acnt = accountService.getAccount(userCurrent.getCurrentUser());
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }

        tusername = acnt.getUsername();
        tlname = user.getLastName();
        tfname = user.getFirstName();
        tmname = user.getMiddleName();
        tcity = user.getCity();
        tprovince = user.getProvince();
        tcontacNo = user.getContactNum();
        temail = user.getEmailAdd();
        tfacultyID = String.valueOf(acnt.getUserID());
        if (user.getImage() != null) {
            object = user.getImage();
        }
        userType = user.getUserType();

        if (userType == 2) {
            viewClasses.setVisible(false);
            viewInstructors.setVisible(false);
            viewSubject.setVisible(false);
            setCode.setVisible(false);
            home.setVisible(false);
        }
    }

    public void setProfile() {
        fullName.setText(tlname + ", " + tfname + " " + tmname);
        contact.setText(tcontacNo);
        address.setText(tcity + ", " + tprovince);
        email.setText(temail);
        if (object instanceof com.mysql.jdbc.Blob) {

            proPic.setIcon(new javax.swing.ImageIcon(formatTool.convertToActualSizeImage((Blob) object, 2)));

        }

    }

    public void edit(boolean a) {
        browse.setVisible(a);
        save.setVisible(a);
        cancel.setVisible(a);
        elastname.setEditable(a);
        efirstname.setEditable(a);
        emiddlename.setEditable(a);
        ecity.setEditable(a);
        eprovince.setEditable(a);
        econtactNo.setEditable(a);
        eemailadd.setEditable(a);
        if (userType == 2) {
            cancelAccount.setVisible(a);
        }

    }

    public void close() {
        accountFrame.dispose();
        homeFrame.dispose();
        addClass.dispose();
        editClass.dispose();
        setUpClass.dispose();
        manageClass.dispose();
        gradingSystem.dispose();
        instructorFrame.dispose();
        subjectsFrame.dispose();
        addSubFrame.dispose();
        editSubFrame.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        layeredPanel = new javax.swing.JPanel();
        layerPane = new javax.swing.JLayeredPane();
        homeFrame = new javax.swing.JInternalFrame();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        homeTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        manage = new javax.swing.JButton();
        yearCB = new javax.swing.JComboBox();
        semesterCB = new javax.swing.JComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel60 = new javax.swing.JLabel();
        homeSemester = new javax.swing.JLabel();
        accountFrame = new javax.swing.JInternalFrame();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        efacultyID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        imagePanel = new javax.swing.JPanel();
        proPic1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        browse = new javax.swing.JButton();
        eusername = new javax.swing.JTextField();
        elastname = new javax.swing.JTextField();
        efirstname = new javax.swing.JTextField();
        emiddlename = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ecity = new javax.swing.JTextField();
        eprovince = new javax.swing.JTextField();
        econtactNo = new javax.swing.JTextField();
        eemailadd = new javax.swing.JTextField();
        editAcc = new javax.swing.JButton();
        editpass = new javax.swing.JButton();
        back = new javax.swing.JButton();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        cancelAccount = new javax.swing.JButton();
        viewClassFrame = new javax.swing.JInternalFrame();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        addSection = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        marker = new javax.swing.JComboBox();
        bySemester = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        classList = new javax.swing.JTable();
        subjects = new javax.swing.JScrollPane();
        subjectList = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        offeredSubjectList = new javax.swing.JTable();
        jLabel49 = new javax.swing.JLabel();
        offerSubject = new javax.swing.JButton();
        delSubjectOffered = new javax.swing.JButton();
        classListSem = new javax.swing.JComboBox();
        classListYear = new javax.swing.JComboBox();
        jLabel61 = new javax.swing.JLabel();
        addClass = new javax.swing.JInternalFrame();
        jPanel9 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        classSection = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        classRoom = new javax.swing.JTextField();
        classSchedule = new javax.swing.JTextField();
        addClassButton = new javax.swing.JButton();
        cancelClassButton = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel47 = new javax.swing.JLabel();
        classInstructorID = new javax.swing.JComboBox();
        instructorName = new javax.swing.JLabel();
        subjectCode = new javax.swing.JLabel();
        subjectDescription = new javax.swing.JLabel();
        acadYearLabel = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        semesterLabel = new javax.swing.JLabel();
        editClass = new javax.swing.JInternalFrame();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        saveEditClass = new javax.swing.JButton();
        cancelEditClass = new javax.swing.JButton();
        editRoom = new javax.swing.JTextField();
        editSchedule = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        editSection = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        instructorID = new javax.swing.JComboBox();
        jLabel48 = new javax.swing.JLabel();
        subjectLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        instructorLabel = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        acadYearLabel2 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        semesterLabel2 = new javax.swing.JLabel();
        setUpClass = new javax.swing.JInternalFrame();
        jPanel10 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        continuebutton = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        scSection = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        scSchedule = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        scDescription = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        scRoom = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        scSubject = new javax.swing.JTextField();
        jSeparator15 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        manageClass = new javax.swing.JInternalFrame();
        jPanel11 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        mcSubject = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        mcSection = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        mcDescription = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        mcRoom = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        mcSchedule = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        checkAttendance = new javax.swing.JButton();
        updateClassrecord = new javax.swing.JButton();
        viewClassrecord = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        instructorFrame = new javax.swing.JInternalFrame();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        insList = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        viewPro = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        editSubFrame = new javax.swing.JInternalFrame();
        jPanel20 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        saveSubject = new javax.swing.JButton();
        cancel5 = new javax.swing.JButton();
        eSemester = new javax.swing.JComboBox();
        eSubjectUnits = new javax.swing.JSpinner();
        eSubject = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        eSubjectDescription = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        subjectsFrame = new javax.swing.JInternalFrame();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        subjList = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        addSub = new javax.swing.JButton();
        editSub = new javax.swing.JButton();
        deleteSub = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        addSubFrame = new javax.swing.JInternalFrame();
        jPanel14 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        aSubjectDescription = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        add3 = new javax.swing.JButton();
        cancel3 = new javax.swing.JButton();
        aSubjectCode = new javax.swing.JTextField();
        aSubjectUnits = new javax.swing.JSpinner();
        aSemester = new javax.swing.JComboBox();
        jSeparator13 = new javax.swing.JSeparator();
        gradingSystem = new javax.swing.JInternalFrame();
        jPanel12 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        outputPanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel50 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        passingRate = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        proPic = new javax.swing.JLabel();
        fullName = new javax.swing.JLabel();
        contact = new javax.swing.JLabel();
        address = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        account = new javax.swing.JButton();
        logout = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        viewClasses = new javax.swing.JButton();
        viewInstructors = new javax.swing.JButton();
        viewSubject = new javax.swing.JButton();
        setCode = new javax.swing.JButton();
        home = new javax.swing.JButton();
        off = new javax.swing.JButton();
        help = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        background.setBackground(new java.awt.Color(6, 49, 237));
        background.setOpaque(false);
        background.setLayout(null);

        layeredPanel.setBackground(new java.awt.Color(255, 255, 255));
        layeredPanel.setOpaque(false);

        homeFrame.setVisible(true);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        homeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Csc181N", "Software Engineering", "2012-2013", "BbWw", "CAF-C", "TF 10:00am-1:00pm"},
                {"", null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Subject", "Description", "Year", "Section", "Room", "Schedule"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        homeTable.setName("My Classes"); // NOI18N
        homeTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(homeTable);
        homeTable.getColumnModel().getColumn(0).setMinWidth(60);
        homeTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        homeTable.getColumnModel().getColumn(0).setMaxWidth(60);
        homeTable.getColumnModel().getColumn(1).setMinWidth(150);
        homeTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        homeTable.getColumnModel().getColumn(1).setMaxWidth(150);
        homeTable.getColumnModel().getColumn(2).setMinWidth(60);
        homeTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        homeTable.getColumnModel().getColumn(2).setMaxWidth(60);
        homeTable.getColumnModel().getColumn(3).setMinWidth(60);
        homeTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        homeTable.getColumnModel().getColumn(3).setMaxWidth(60);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - Home.jpg"))); // NOI18N

        manage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/manage.jpg"))); // NOI18N
        manage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageActionPerformed(evt);
            }
        });

        yearCB.setBackground(new java.awt.Color(6, 49, 237));
        yearCB.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        yearCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "2014 - 2015", "2013 - 2014", "2012 - 2013" }));
        yearCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearCBActionPerformed(evt);
            }
        });

        semesterCB.setBackground(new java.awt.Color(6, 49, 237));
        semesterCB.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        semesterCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "1st Semester", "2nd Semester", "Summer" }));
        semesterCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                semesterCBActionPerformed(evt);
            }
        });

        jLabel60.setText("Semester :");

        homeSemester.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        homeSemester.setText("    ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(manage, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(yearCB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(semesterCB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(homeSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGap(28, 28, 28))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(semesterCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(homeSemester))
                .addGap(9, 9, 9)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout homeFrameLayout = new javax.swing.GroupLayout(homeFrame.getContentPane());
        homeFrame.getContentPane().setLayout(homeFrameLayout);
        homeFrameLayout.setHorizontalGroup(
            homeFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        homeFrameLayout.setVerticalGroup(
            homeFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        homeFrame.setBounds(0, 0, 460, 460);
        layerPane.add(homeFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        accountFrame.setVisible(true);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setOpaque(false);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(6, 49, 237));
        jLabel2.setText("Faculty ID No.");

        efacultyID.setEditable(false);
        efacultyID.setFont(new java.awt.Font("Maiandra GD", 1, 18)); // NOI18N
        efacultyID.setText("1053019");
        efacultyID.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        efacultyID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efacultyIDActionPerformed(evt);
            }
        });

        imagePanel.setBackground(new java.awt.Color(6, 47, 237));
        imagePanel.setMaximumSize(new java.awt.Dimension(100, 100));
        imagePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        proPic1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/proIcon.jpg"))); // NOI18N
        proPic1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(proPic1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(proPic1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
        );

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Heading - Account.jpg"))); // NOI18N

        browse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/browse.jpg"))); // NOI18N
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        eusername.setEditable(false);
        eusername.setFont(new java.awt.Font("Maiandra GD", 1, 18)); // NOI18N
        eusername.setText("samrahbpetiilan");
        eusername.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        eusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eusernameActionPerformed(evt);
            }
        });
        eusername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eusernameFocusGained(evt);
            }
        });

        elastname.setEditable(false);
        elastname.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        elastname.setText("Petiilan");
        elastname.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        elastname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elastnameActionPerformed(evt);
            }
        });
        elastname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                elastnameFocusGained(evt);
            }
        });

        efirstname.setEditable(false);
        efirstname.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        efirstname.setText("Samra");
        efirstname.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        efirstname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efirstnameActionPerformed(evt);
            }
        });
        efirstname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                efirstnameFocusGained(evt);
            }
        });

        emiddlename.setEditable(false);
        emiddlename.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        emiddlename.setText("Hadji Basher");
        emiddlename.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        emiddlename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emiddlenameActionPerformed(evt);
            }
        });
        emiddlename.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emiddlenameFocusGained(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(6, 49, 237));
        jLabel5.setText("Username");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(6, 49, 237));
        jLabel6.setText("Last Name");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(6, 49, 237));
        jLabel7.setText("First Name");

        jLabel11.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(6, 49, 237));
        jLabel11.setText("Middle Name");

        jLabel12.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(6, 49, 237));
        jLabel12.setText("City");

        jLabel13.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(6, 49, 237));
        jLabel13.setText("Province");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(6, 49, 237));
        jLabel14.setText("Contact No.");

        jLabel15.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(6, 49, 237));
        jLabel15.setText("Email Address");

        ecity.setEditable(false);
        ecity.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        ecity.setText("Cagayan de Oro");
        ecity.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ecity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ecityActionPerformed(evt);
            }
        });
        ecity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ecityFocusGained(evt);
            }
        });

        eprovince.setEditable(false);
        eprovince.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        eprovince.setText("Misamis Oriental");
        eprovince.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        eprovince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eprovinceActionPerformed(evt);
            }
        });
        eprovince.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eprovinceFocusGained(evt);
            }
        });

        econtactNo.setEditable(false);
        econtactNo.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        econtactNo.setText("09093196983");
        econtactNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        econtactNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                econtactNoActionPerformed(evt);
            }
        });
        econtactNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                econtactNoFocusGained(evt);
            }
        });
        econtactNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                econtactNoKeyTyped(evt);
            }
        });

        eemailadd.setEditable(false);
        eemailadd.setFont(new java.awt.Font("Maiandra GD", 0, 18)); // NOI18N
        eemailadd.setText("samrahbpetiilan@gmail.com");
        eemailadd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        eemailadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eemailaddActionPerformed(evt);
            }
        });
        eemailadd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eemailaddFocusGained(evt);
            }
        });

        editAcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/bigEditAcc.jpg"))); // NOI18N
        editAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editAccActionPerformed(evt);
            }
        });

        editpass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/bigEditPass.jpg"))); // NOI18N
        editpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editpassActionPerformed(evt);
            }
        });

        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/bigBack.jpg"))); // NOI18N
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/bigSave.jpg"))); // NOI18N
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        save.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                saveFocusGained(evt);
            }
        });

        cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/bigCancel.jpg"))); // NOI18N
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        cancel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cancelFocusGained(evt);
            }
        });

        cancelAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancelAccount.jpg"))); // NOI18N
        cancelAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelAccountActionPerformed(evt);
            }
        });
        cancelAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cancelAccountFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel15))
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(editAcc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(browse, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                    .addComponent(editpass, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(eusername)
                                .addComponent(elastname)
                                .addComponent(efirstname)
                                .addComponent(emiddlename)
                                .addComponent(ecity)
                                .addComponent(eprovince, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                .addComponent(econtactNo)
                                .addComponent(efacultyID))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eemailadd, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cancelAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editpass, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(browse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(efacultyID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(elastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(efirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emiddlename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(ecity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eprovince, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(econtactNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(eemailadd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(cancelAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jScrollPane1.setViewportView(jPanel5);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout accountFrameLayout = new javax.swing.GroupLayout(accountFrame.getContentPane());
        accountFrame.getContentPane().setLayout(accountFrameLayout);
        accountFrameLayout.setHorizontalGroup(
            accountFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        accountFrameLayout.setVerticalGroup(
            accountFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        accountFrame.setBounds(0, 0, 460, 460);
        layerPane.add(accountFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        viewClassFrame.setVisible(true);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - viewClasses.jpg"))); // NOI18N

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        addSection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/addsec.jpg"))); // NOI18N
        addSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSectionActionPerformed(evt);
            }
        });

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/edit.jpg"))); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/delete.jpg"))); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        marker.setBackground(new java.awt.Color(6, 49, 237));
        marker.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        marker.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mark All", "Unmark All" }));
        marker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markerActionPerformed(evt);
            }
        });

        bySemester.setBackground(new java.awt.Color(6, 49, 237));
        bySemester.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        bySemester.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "1st Semester", "2nd Semester", "Summer" }));
        bySemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bySemesterActionPerformed(evt);
            }
        });

        classList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Csc181N", "Software Engineering", "BbWw", null},
                {"", null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Subject", "Description", "Section", "Instructor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(classList);
        classList.getColumnModel().getColumn(1).setPreferredWidth(175);
        classList.getColumnModel().getColumn(2).setPreferredWidth(50);
        classList.getColumnModel().getColumn(3).setPreferredWidth(150);

        subjectList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "", "Subject", "Decription"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subjectList.getTableHeader().setReorderingAllowed(false);
        subjects.setViewportView(subjectList);
        subjectList.getColumnModel().getColumn(0).setResizable(false);
        subjectList.getColumnModel().getColumn(0).setPreferredWidth(5);

        jLabel16.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(48, 130, 193));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/subjects.jpg"))); // NOI18N

        jLabel17.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(48, 130, 193));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/offered.jpg"))); // NOI18N

        offeredSubjectList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject", "Decription"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(offeredSubjectList);

        jLabel49.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(48, 130, 193));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/classList.jpg"))); // NOI18N

        offerSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/unoffer.jpg"))); // NOI18N
        offerSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offerSubjectActionPerformed(evt);
            }
        });

        delSubjectOffered.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/unoffer2.jpg"))); // NOI18N
        delSubjectOffered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delSubjectOfferedActionPerformed(evt);
            }
        });

        classListSem.setBackground(new java.awt.Color(6, 49, 237));
        classListSem.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        classListSem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "1st Semester", "2nd Semester", "Summer" }));
        classListSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classListSemActionPerformed(evt);
            }
        });

        classListYear.setBackground(new java.awt.Color(6, 49, 237));
        classListYear.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        classListYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "2014-2015", "2013-2014", "2012-2013" }));
        classListYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classListYearActionPerformed(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("DK Crayon Crumble", 0, 15)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(48, 130, 193));
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("VIEW CLASSES");
        jLabel61.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel61MouseClicked(evt);
            }
        });
        jLabel61.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel61FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jLabel61FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(subjects, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 114, Short.MAX_VALUE)
                                .addComponent(offerSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(delSubjectOffered, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(addSection, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 257, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(marker, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bySemester, 0, 1, Short.MAX_VALUE))
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(classListYear, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(classListSem, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 197, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel61, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(marker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bySemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(subjects, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delSubjectOffered, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addSection, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(offerSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classListYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classListSem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jScrollPane6.setViewportView(jPanel2);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 454, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator14, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)))
                .addGap(28, 28, 28))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout viewClassFrameLayout = new javax.swing.GroupLayout(viewClassFrame.getContentPane());
        viewClassFrame.getContentPane().setLayout(viewClassFrameLayout);
        viewClassFrameLayout.setHorizontalGroup(
            viewClassFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        viewClassFrameLayout.setVerticalGroup(
            viewClassFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewClassFrameLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        viewClassFrame.setBounds(0, 0, 460, 460);
        layerPane.add(viewClassFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        addClass.setVisible(true);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - AddClass.jpg"))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel19.setText("Subject");

        jLabel20.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel20.setText("Section");

        classSection.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        classSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classSectionActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel21.setText("Room");

        jLabel22.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel22.setText("Schedule");

        classRoom.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        classRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classRoomActionPerformed(evt);
            }
        });

        classSchedule.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        classSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classScheduleActionPerformed(evt);
            }
        });

        addClassButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.jpg"))); // NOI18N
        addClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassButtonActionPerformed(evt);
            }
        });

        cancelClassButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/cancel.jpg"))); // NOI18N
        cancelClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelClassButtonActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel41.setText("AcadYear :");

        jLabel47.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel47.setText("Instructor");

        classInstructorID.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        classInstructorID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { }));
        classInstructorID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classInstructorIDActionPerformed(evt);
            }
        });

        instructorName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        instructorName.setText(" ");

        subjectCode.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        subjectCode.setText("Subject Code");

        subjectDescription.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        subjectDescription.setText("Subject Descripton");

        acadYearLabel.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        acadYearLabel.setText("                     ");

        jLabel58.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel58.setText("Semester :");

        semesterLabel.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        semesterLabel.setText("         ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(semesterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel47)
                            .addComponent(jLabel19)
                            .addComponent(jLabel41))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(classInstructorID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(classSchedule, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                    .addComponent(instructorName, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(acadYearLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cancelClassButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addClassButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(classRoom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                        .addComponent(classSection, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                        .addComponent(subjectCode, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(subjectDescription, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(acadYearLabel)
                    .addComponent(jLabel58)
                    .addComponent(semesterLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectCode)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectDescription)
                .addGap(13, 13, 13)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classInstructorID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(instructorName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addClassLayout = new javax.swing.GroupLayout(addClass.getContentPane());
        addClass.getContentPane().setLayout(addClassLayout);
        addClassLayout.setHorizontalGroup(
            addClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        addClassLayout.setVerticalGroup(
            addClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        addClass.setBounds(0, 0, 500, 500);
        layerPane.add(addClass, javax.swing.JLayeredPane.DEFAULT_LAYER);

        editClass.setVisible(true);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - editClass.jpg"))); // NOI18N

        saveEditClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/save.jpg"))); // NOI18N
        saveEditClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveEditClassActionPerformed(evt);
            }
        });

        cancelEditClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.jpg"))); // NOI18N
        cancelEditClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelEditClassActionPerformed(evt);
            }
        });

        editRoom.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        editRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRoomActionPerformed(evt);
            }
        });

        editSchedule.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        editSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editScheduleActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel43.setText("Schedule");

        jLabel44.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel44.setText("Room");

        editSection.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        editSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSectionActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel45.setText("Section");

        jLabel51.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel51.setText("Subject");

        instructorID.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        instructorID.setModel(new javax.swing.DefaultComboBoxModel(new String[] {  }));
        instructorID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instructorIDActionPerformed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel48.setText("Instructor");

        subjectLabel.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        subjectLabel.setText("Subject");

        descriptionLabel.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        descriptionLabel.setText("Description");

        instructorLabel.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        instructorLabel.setText(" ");

        jLabel42.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel42.setText("AcadYear :");

        acadYearLabel2.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        acadYearLabel2.setText("                     ");

        jLabel59.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel59.setText("Semester :");

        semesterLabel2.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        semesterLabel2.setText("         ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addGap(41, 41, 41)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(editSection, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(subjectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(78, 78, 78))
                                    .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel44)
                                        .addComponent(jLabel43)
                                        .addComponent(jLabel48))
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                            .addGap(29, 29, 29)
                                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(instructorID, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(editSchedule, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                                        .addComponent(saveEditClass, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(cancelEditClass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(instructorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(editRoom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addGap(21, 21, 21)
                                .addComponent(acadYearLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(semesterLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(acadYearLabel2)
                    .addComponent(semesterLabel2)
                    .addComponent(jLabel59))
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(subjectLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionLabel)
                .addGap(13, 13, 13)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(editSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(instructorID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instructorLabel)
                .addGap(18, 18, 18)
                .addComponent(saveEditClass, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelEditClass, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout editClassLayout = new javax.swing.GroupLayout(editClass.getContentPane());
        editClass.getContentPane().setLayout(editClassLayout);
        editClassLayout.setHorizontalGroup(
            editClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        editClassLayout.setVerticalGroup(
            editClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        editClass.setBounds(0, 0, 500, 500);
        layerPane.add(editClass, javax.swing.JLayeredPane.DEFAULT_LAYER);

        setUpClass.setVisible(true);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - SeUpCLass.jpg"))); // NOI18N

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/uploadMasterList.jpg"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/createGraSys.jpg"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        continuebutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/continue.jpg"))); // NOI18N
        continuebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continuebuttonActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.jpg"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        scSection.setEditable(false);
        scSection.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        scSection.setBorder(null);
        scSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scSectionActionPerformed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(48, 130, 193));
        jLabel62.setText("Section");

        scSchedule.setEditable(false);
        scSchedule.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        scSchedule.setBorder(null);
        scSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scScheduleActionPerformed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(48, 130, 193));
        jLabel63.setText("Description");

        jLabel64.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(48, 130, 193));
        jLabel64.setText("Room");

        scDescription.setEditable(false);
        scDescription.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        scDescription.setBorder(null);
        scDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scDescriptionActionPerformed(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(48, 130, 193));
        jLabel65.setText("Schedule");

        scRoom.setEditable(false);
        scRoom.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        scRoom.setBorder(null);
        scRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scRoomActionPerformed(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(48, 130, 193));
        jLabel66.setText("Subject");

        scSubject.setEditable(false);
        scSubject.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        scSubject.setBorder(null);
        scSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scSubjectActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel26.setText(" ");

        jLabel24.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel24.setText("Masterlist has been successfully uploaded.");

        jLabel25.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel25.setText("Grading System has been saved to database.");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel63)
                            .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel64, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel65, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                            .addComponent(scSchedule, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                            .addComponent(scSubject, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                            .addComponent(scSection, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                            .addComponent(scDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 28, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator15, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26))
                            .addComponent(jLabel25))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(304, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(continuebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(scSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(scSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65))
                .addGap(18, 18, 18)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26))
                .addGap(3, 3, 3)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(continuebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout setUpClassLayout = new javax.swing.GroupLayout(setUpClass.getContentPane());
        setUpClass.getContentPane().setLayout(setUpClassLayout);
        setUpClassLayout.setHorizontalGroup(
            setUpClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        setUpClassLayout.setVerticalGroup(
            setUpClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setUpClass.setBounds(0, 0, 500, 500);
        layerPane.add(setUpClass, javax.swing.JLayeredPane.DEFAULT_LAYER);

        manageClass.setVisible(true);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - Classroom.jpg"))); // NOI18N

        jLabel28.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(48, 130, 193));
        jLabel28.setText("Subject");

        mcSubject.setEditable(false);
        mcSubject.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mcSubject.setText("CSc181N");
        mcSubject.setBorder(null);
        mcSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcSubjectActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(48, 130, 193));
        jLabel29.setText("Section");

        mcSection.setEditable(false);
        mcSection.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mcSection.setText("VvWw");
        mcSection.setBorder(null);
        mcSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcSectionActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(48, 130, 193));
        jLabel30.setText("Description");

        mcDescription.setEditable(false);
        mcDescription.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mcDescription.setText("Software Engineering");
        mcDescription.setBorder(null);
        mcDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcDescriptionActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(48, 130, 193));
        jLabel31.setText("Room");

        mcRoom.setEditable(false);
        mcRoom.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mcRoom.setText("CAF-C/CSD102");
        mcRoom.setBorder(null);
        mcRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcRoomActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(48, 130, 193));
        jLabel32.setText("Schedule");

        mcSchedule.setEditable(false);
        mcSchedule.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        mcSchedule.setText("TF10:00-11:30TF11:30-01:00");
        mcSchedule.setBorder(null);
        mcSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcScheduleActionPerformed(evt);
            }
        });

        checkAttendance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/checkAtt.jpg"))); // NOI18N
        checkAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAttendanceActionPerformed(evt);
            }
        });

        updateClassrecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/upDateRecoed.jpg"))); // NOI18N
        updateClassrecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateClassrecordActionPerformed(evt);
            }
        });

        viewClassrecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewClassrecord.jpg"))); // NOI18N
        viewClassrecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewClassrecordActionPerformed(evt);
            }
        });

        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/homesmall.jpg"))); // NOI18N
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewGrading.jpg"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator10, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mcRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                            .addComponent(mcSchedule, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                            .addComponent(mcSubject, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                            .addComponent(mcSection, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                            .addComponent(mcDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))))
                .addGap(43, 43, 43))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 28, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateClassrecord, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(viewClassrecord, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(mcSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(mcSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mcDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mcRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mcSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(checkAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(viewClassrecord, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(updateClassrecord, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout manageClassLayout = new javax.swing.GroupLayout(manageClass.getContentPane());
        manageClass.getContentPane().setLayout(manageClassLayout);
        manageClassLayout.setHorizontalGroup(
            manageClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        manageClassLayout.setVerticalGroup(
            manageClassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        manageClass.setBounds(0, 0, 500, 500);
        layerPane.add(manageClass, javax.swing.JLayeredPane.DEFAULT_LAYER);

        instructorFrame.setVisible(true);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        insList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", ""},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Full Name", "Phone Number", "Staff ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        insList.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(insList);
        insList.getColumnModel().getColumn(1).setPreferredWidth(20);
        insList.getColumnModel().getColumn(2).setPreferredWidth(5);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - viewInstructors.jpg"))); // NOI18N

        viewPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewProfile.jpg"))); // NOI18N
        viewPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewProActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator4)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(302, 302, 302)
                        .addComponent(viewPro, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(viewPro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout instructorFrameLayout = new javax.swing.GroupLayout(instructorFrame.getContentPane());
        instructorFrame.getContentPane().setLayout(instructorFrameLayout);
        instructorFrameLayout.setHorizontalGroup(
            instructorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        instructorFrameLayout.setVerticalGroup(
            instructorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        instructorFrame.setBounds(0, 0, 500, 500);
        layerPane.add(instructorFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        editSubFrame.setVisible(true);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - editSubject.jpg"))); // NOI18N

        saveSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/save.jpg"))); // NOI18N
        saveSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSubjectActionPerformed(evt);
            }
        });

        cancel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.jpg"))); // NOI18N
        cancel5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel5ActionPerformed(evt);
            }
        });

        eSemester.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        eSemester.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1st Semester", "2nd Semester", "Summer" }));
        eSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eSemesterActionPerformed(evt);
            }
        });

        eSubjectUnits.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        eSubjectUnits.setModel(new javax.swing.SpinnerNumberModel(3, 0, 6, 1));

        eSubject.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        eSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eSubjectActionPerformed(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel52.setText("Subject Code");

        eSubjectDescription.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        eSubjectDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eSubjectDescriptionActionPerformed(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel53.setText("Description");

        jLabel54.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel54.setText("Semester");

        jLabel55.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel55.setText("Units");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel52)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout.createSequentialGroup()
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel54, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(14, 14, 14)))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(eSubject)
                            .addComponent(eSubjectUnits)
                            .addComponent(eSubjectDescription)
                            .addComponent(eSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(39, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(329, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saveSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eSubjectDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eSubjectUnits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addGap(34, 34, 34)
                .addComponent(saveSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(358, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout editSubFrameLayout = new javax.swing.GroupLayout(editSubFrame.getContentPane());
        editSubFrame.getContentPane().setLayout(editSubFrameLayout);
        editSubFrameLayout.setHorizontalGroup(
            editSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        editSubFrameLayout.setVerticalGroup(
            editSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        editSubFrame.setBounds(0, 0, 24, 26);
        layerPane.add(editSubFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        subjectsFrame.setVisible(true);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        subjList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Subject Name", "Description", "Units", "Semester"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subjList.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(subjList);
        subjList.getColumnModel().getColumn(0).setPreferredWidth(20);
        subjList.getColumnModel().getColumn(2).setPreferredWidth(3);
        subjList.getColumnModel().getColumn(3).setPreferredWidth(25);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
        );

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - viewSubjects.jpg"))); // NOI18N

        addSub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.jpg"))); // NOI18N
        addSub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSubActionPerformed(evt);
            }
        });

        editSub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.jpg"))); // NOI18N
        editSub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSubActionPerformed(evt);
            }
        });

        deleteSub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.jpg"))); // NOI18N
        deleteSub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSubActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator5)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addComponent(addSub, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editSub, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteSub, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteSub, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(editSub, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addSub, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout subjectsFrameLayout = new javax.swing.GroupLayout(subjectsFrame.getContentPane());
        subjectsFrame.getContentPane().setLayout(subjectsFrameLayout);
        subjectsFrameLayout.setHorizontalGroup(
            subjectsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        subjectsFrameLayout.setVerticalGroup(
            subjectsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        subjectsFrame.setBounds(0, 0, 500, 500);
        layerPane.add(subjectsFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        addSubFrame.setVisible(true);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - AddSubject.jpg"))); // NOI18N

        jLabel37.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel37.setText("Subject Code");

        jLabel38.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel38.setText("Description");

        aSubjectDescription.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        aSubjectDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aSubjectDescriptionActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel39.setText("Units");

        jLabel40.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel40.setText("Semester");

        add3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.jpg"))); // NOI18N
        add3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add3ActionPerformed(evt);
            }
        });

        cancel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.jpg"))); // NOI18N
        cancel3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel3ActionPerformed(evt);
            }
        });

        aSubjectCode.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        aSubjectCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aSubjectCodeActionPerformed(evt);
            }
        });

        aSubjectUnits.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        aSubjectUnits.setModel(new javax.swing.SpinnerNumberModel(3, 0, 6, 1));

        aSemester.setFont(new java.awt.Font("Maiandra GD", 0, 20)); // NOI18N
        aSemester.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1st Semester", "2nd Semester", "Summer" }));
        aSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aSemesterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 29, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cancel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel37)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(14, 14, 14)))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(aSubjectCode)
                                    .addComponent(aSubjectUnits)
                                    .addComponent(aSubjectDescription)
                                    .addComponent(aSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(add3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aSubjectCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aSubjectDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aSubjectUnits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addGap(34, 34, 34)
                .addComponent(add3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addSubFrameLayout = new javax.swing.GroupLayout(addSubFrame.getContentPane());
        addSubFrame.getContentPane().setLayout(addSubFrameLayout);
        addSubFrameLayout.setHorizontalGroup(
            addSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        addSubFrameLayout.setVerticalGroup(
            addSubFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        addSubFrame.setBounds(0, 0, 500, 500);
        layerPane.add(addSubFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        gradingSystem.setVisible(true);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - GradingSystem.jpg"))); // NOI18N

        outputPanel.setBackground(new java.awt.Color(255, 255, 255));
        outputPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Entry Name", "Percentage"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(1).setMinWidth(75);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(75);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(75);

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        );

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/addEntry.jpg"))); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.jpg"))); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/back.jpg"))); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/continue.jpg"))); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("DK Crayon Crumble", 0, 15)); // NOI18N
        jLabel50.setText("Overall Percentage");

        jLabel56.setFont(new java.awt.Font("DK Crayon Crumble", 0, 15)); // NOI18N
        jLabel56.setText("Passing Rate");

        passingRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passingRateActionPerformed(evt);
            }
        });
        passingRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passingRateKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passingRateKeyTyped(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setText("100");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(passingRate, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel12Layout.createSequentialGroup()
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel56)
                                        .addComponent(jLabel50))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel57))))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(jLabel57))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel56)
                            .addComponent(passingRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout gradingSystemLayout = new javax.swing.GroupLayout(gradingSystem.getContentPane());
        gradingSystem.getContentPane().setLayout(gradingSystemLayout);
        gradingSystemLayout.setHorizontalGroup(
            gradingSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        gradingSystemLayout.setVerticalGroup(
            gradingSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gradingSystem.setBounds(0, 0, 500, 500);
        layerPane.add(gradingSystem, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPanelLayout = new javax.swing.GroupLayout(layeredPanel);
        layeredPanel.setLayout(layeredPanelLayout);
        layeredPanelLayout.setHorizontalGroup(
            layeredPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layeredPanelLayout.createSequentialGroup()
                .addComponent(layerPane, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );
        layeredPanelLayout.setVerticalGroup(
            layeredPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layeredPanelLayout.createSequentialGroup()
                .addComponent(layerPane, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        background.add(layeredPanel);
        layeredPanel.setBounds(310, 30, 490, 460);

        jPanel1.setBackground(new java.awt.Color(6, 49, 237));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        proPic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/proIcon.jpg"))); // NOI18N
        proPic.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(proPic, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(proPic, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(30, 40, 100, 100);

        fullName.setFont(new java.awt.Font("Maiandra GD", 0, 14)); // NOI18N
        fullName.setText("Petiilan, Samra Hadji Basher");
        jPanel1.add(fullName);
        fullName.setBounds(30, 150, 250, 18);

        contact.setFont(new java.awt.Font("Maiandra GD", 0, 14)); // NOI18N
        contact.setText("09093196983");
        jPanel1.add(contact);
        contact.setBounds(30, 170, 250, 20);

        address.setFont(new java.awt.Font("Maiandra GD", 0, 14)); // NOI18N
        address.setText("Cagayan de Oro City, Misamis Oriental");
        jPanel1.add(address);
        address.setBounds(30, 190, 250, 20);

        email.setFont(new java.awt.Font("Maiandra GD", 0, 14)); // NOI18N
        email.setText("samrahbpetiilan@gmail.com");
        jPanel1.add(email);
        email.setBounds(30, 210, 250, 20);

        account.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/accountSetting.jpg"))); // NOI18N
        account.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountActionPerformed(evt);
            }
        });
        jPanel1.add(account);
        account.setBounds(130, 110, 150, 30);

        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/logout.jpg"))); // NOI18N
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        jPanel1.add(logout);
        logout.setBounds(130, 80, 150, 30);
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(30, 240, 250, 10);

        viewClasses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewClasses.jpg"))); // NOI18N
        viewClasses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewClassesActionPerformed(evt);
            }
        });
        jPanel1.add(viewClasses);
        viewClasses.setBounds(30, 290, 250, 29);

        viewInstructors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewInstructors.jpg"))); // NOI18N
        viewInstructors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewInstructorsActionPerformed(evt);
            }
        });
        jPanel1.add(viewInstructors);
        viewInstructors.setBounds(30, 330, 250, 29);

        viewSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewSubjects.jpg"))); // NOI18N
        viewSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSubjectActionPerformed(evt);
            }
        });
        jPanel1.add(viewSubject);
        viewSubject.setBounds(30, 370, 250, 29);

        setCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/setRegCOde.jpg"))); // NOI18N
        setCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCodeActionPerformed(evt);
            }
        });
        jPanel1.add(setCode);
        setCode.setBounds(30, 410, 250, 29);

        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/home.jpg"))); // NOI18N
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });
        jPanel1.add(home);
        home.setBounds(30, 250, 250, 30);

        off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/exit2.jpg"))); // NOI18N
        off.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offActionPerformed(evt);
            }
        });
        jPanel1.add(off);
        off.setBounds(30, 450, 250, 30);

        help.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/help.jpg"))); // NOI18N
        help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        jPanel1.add(help);
        help.setBounds(140, 50, 22, 23);

        background.add(jPanel1);
        jPanel1.setBounds(0, 0, 300, 500);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Home.png"))); // NOI18N
        background.add(jLabel9);
        jLabel9.setBounds(0, 0, 800, 500);

        getContentPane().add(background);
        background.setBounds(0, 0, 810, 510);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        // TODO add your handling code here:
        setHomeIcons();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?\nAll unfinished processes will be lost!", "Confim", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            this.dispose();
            Home home = new Home();
            home.setVisible(true);
        }
    }//GEN-LAST:event_logoutActionPerformed
    public void updateComboBox(JComboBox c) {
        comboBox = new DefaultComboBoxModel();
        c.setModel(comboBox);
        userService = new UserServiceImplementation();
        userSearch = new ArrayList<>();
        try {
            userSearch = userService.getUsers();
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (User usr : userSearch) {
            comboBox.addElement(usr.getUserID());
        }
    }
    private void accountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountActionPerformed
        // TODO add your handling code here:
        setHomeIcons();
        efacultyID.setText(tfacultyID);
        eusername.setText(tusername);
        elastname.setText(tlname);
        efirstname.setText(tfname);
        ecity.setText(tcity);
        emiddlename.setText(tmname);
        eprovince.setText(tprovince);
        econtactNo.setText(tcontacNo);
        eemailadd.setText(temail);
        if (object instanceof com.mysql.jdbc.Blob) {

            proPic1.setIcon(new javax.swing.ImageIcon(formatTool.convertToActualSizeImage((Blob) object, 2)));

        }
        close();
        accountFrame.setVisible(true);
        close();
        accountFrame.setVisible(true);
    }//GEN-LAST:event_accountActionPerformed

    public void setTemp() {
        tusername = eusername.getText();
        tlname = elastname.getText();
        tfname = efirstname.getText();
        tmname = emiddlename.getText();
        tcity = ecity.getText();
        tprovince = eprovince.getText();
        tcontacNo = econtactNo.getText();
        temail = eemailadd.getText();
    }

    public void setHomeIcons() {
        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/home.jpg")));
        viewClasses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewClasses.jpg")));
        viewInstructors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewInstructors.jpg")));
        viewSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewSubjects.jpg")));
        setCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/setRegCOde.jpg")));
        off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/exit2.jpg")));
    }

    public void viewClassOffered() {
        close();
        DefaultTableModel model1 = (DefaultTableModel) this.subjectList.getModel();
        DefaultTableModel model2 = (DefaultTableModel) this.offeredSubjectList.getModel();
        int sel = subjectList.getSelectedRow();
        int tableIndex = 0;
        int row1 = model1.getRowCount();
        int row2 = model2.getRowCount();
        boolean sameadd = false;

        while (row1 > 0) {
            if (model1.getValueAt(tableIndex, 0).equals(true)) {
                index = 0;
                while (index < row2) {
                    if (model1.getValueAt(tableIndex, 1).toString().equalsIgnoreCase(model2.getValueAt(index, 0).toString())) {
                        JOptionPane.showMessageDialog(this, model1.getValueAt(tableIndex, 1).toString() + " is already offered.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        sameadd = true;
                    }
                    index++;
                }
                if (!sameadd) {
                    model2.addRow(new Object[]{model1.getValueAt(tableIndex, 1), model1.getValueAt(tableIndex, 2)});
                }
            }
            sameadd = false;
            row1--;
            tableIndex++;

        }
        if (sel >= 0 && model1.getValueAt(sel, 0).equals(false)) {
            index = 0;
            while (index < row2) {
                if (model1.getValueAt(sel, 1).toString().equalsIgnoreCase(model2.getValueAt(index, 0).toString())) {
                    JOptionPane.showMessageDialog(this, model1.getValueAt(sel, 1).toString() + " is already offered.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    sameadd = true;
                }
                index++;
            }
            if (!sameadd) {
                model2.addRow(new Object[]{model1.getValueAt(sel, 1), model1.getValueAt(sel, 2)});
            }
        }
    }

    public void homeViewClass() {
        DefaultTableModel model = (DefaultTableModel) this.homeTable.getModel();
        classService = new ClassServiceImplementation();
        classSearch = new ArrayList<>();
        userService = new UserServiceImplementation();
        String year = yearCB.getSelectedItem().toString();
        String sem = semesterCB.getSelectedItem().toString();

        try {
            classSearch = classService.getClasses();
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (Classes temp : classSearch) {
            homeSemester.setText(temp.getSem().toUpperCase());
            if (temp.getUserID() == userCurrent.getUserID()) {
                if (yearCB.getSelectedItem().toString().equalsIgnoreCase("All")) {
                    if (semesterCB.getSelectedItem().toString().equalsIgnoreCase("All")) {
                        model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getYear(), temp.getSectionID(), temp.getRoom(), temp.getSchedule()});
                    } else if (temp.getSem().toString().equalsIgnoreCase(sem)) {
                        model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getYear(), temp.getSectionID(), temp.getRoom(), temp.getSchedule()});
                    }
                } else {
                    if (temp.getYear().toString().equalsIgnoreCase(year)) {
                        if (semesterCB.getSelectedItem().toString().equalsIgnoreCase("All")) {
                            model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getYear(), temp.getSectionID(), temp.getRoom(), temp.getSchedule()});
                        } else if (temp.getSem().toString().equalsIgnoreCase(sem)) {
                            model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getYear(), temp.getSectionID(), temp.getRoom(), temp.getSchedule()});
                        }
                    }
                }
            }
        }
    }

    public void viewClassList() {
        close();
        DefaultTableModel model = (DefaultTableModel) this.classList.getModel();
        classService = new ClassServiceImplementation();
        classSearch = new ArrayList<>();
        String year = classListYear.getSelectedItem().toString();
        String sem = classListSem.getSelectedItem().toString();

        try {
            classSearch = classService.getClasses();
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (Classes temp : classSearch) {
            if (classListYear.getSelectedItem().toString().equalsIgnoreCase("All")) {
                if (classListSem.getSelectedItem().toString().equalsIgnoreCase("All")) {
                    model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getSectionID(), temp.getInstructorName()});
                } else if (temp.getSem().toString().equalsIgnoreCase(sem)) {
                    model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getSectionID(), temp.getInstructorName()});
                }
            } else {
                if (temp.getYear().toString().equalsIgnoreCase(year)) {
                    if (classListSem.getSelectedItem().toString().equalsIgnoreCase("All")) {
                        model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getSectionID(), temp.getInstructorName()});
                    } else if (temp.getSem().toString().equalsIgnoreCase(sem)) {
                        model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton(), temp.getSectionID(), temp.getInstructorName()});
                    }
                }
            }
        }
    }

    public void viewClass() {
        close();

        DefaultTableModel model = (DefaultTableModel) this.subjectList.getModel();
        subjectService = new SubjectServiceImplementation();
        subjectSearch = new ArrayList<>();

        if (bySemester.getSelectedItem().toString().equals("All")) {
            try {
                subjectSearch = subjectService.getSubjects();
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String sem = bySemester.getSelectedItem().toString();
            try {
                subjectSearch = subjectService.getSubjectSem(sem);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        close();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (Subject temp : subjectSearch) {
            model.addRow(new Object[]{false, temp.getSubjectName(), temp.getDescription()});
        }
    }

    private void viewClassesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewClassesActionPerformed
        setHomeIcons();
        viewClasses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewClassesB.jpg")));
        DefaultTableModel model = (DefaultTableModel) this.offeredSubjectList.getModel();
        classService = new ClassServiceImplementation();
        classSearch = new ArrayList<>();
        try {
            classSearch = classService.getClassesUniq();
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }

        for (Classes temp : classSearch) {

            model.addRow(new Object[]{temp.getSubjectName(), temp.getSubjectDescripton()});
        }
        viewClass();
        viewClassList();
        viewClassFrame.setVisible(true);
    }//GEN-LAST:event_viewClassesActionPerformed

    private void saveEditClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveEditClassActionPerformed

        if (editSection.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please provide a section for this class!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            close();
            userService = new UserServiceImplementation();
            user = new User();
            classService = new ClassServiceImplementation();
            classes = new Classes();
            classSearch = new ArrayList<>();

            int id = Integer.parseInt(instructorID.getSelectedItem().toString());

            try {
                user = userService.getUser(id);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            close();

            String instructor = user.getLastName() + ", " + user.getFirstName() + " " + user.getMiddleName();
            int sel = classList.getSelectedRow();
            String section = editSection.getText();
            String room = editRoom.getText();
            String schedule = editSchedule.getText();
            classes.setRoom(room);
            classes.setSchedule(schedule);
            classes.setSectionID(section);
            classes.setUserID(user.getUserID());
            classes.setClassID(classID);

            try {
                classService.editClass(classes);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            classList.setValueAt(section, sel, 2);
            classList.setValueAt(instructor, sel, 3);
            viewClassList();
            viewClassFrame.setVisible(true);
        }
    }//GEN-LAST:event_saveEditClassActionPerformed

    private void cancelEditClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelEditClassActionPerformed
        // TODO add your handling code here:
        close();
        viewClassFrame.setVisible(true);
    }//GEN-LAST:event_cancelEditClassActionPerformed

    private void classSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classSectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_classSectionActionPerformed

    private void classRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_classRoomActionPerformed

    private void classScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classScheduleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_classScheduleActionPerformed

    private void addClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassButtonActionPerformed
        // TODO add your handling code here:
        if (classSection.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please provide a section for this class!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            close();
            cDate = new CurrentDate();
            DefaultTableModel model = (DefaultTableModel) this.classList.getModel();
            subjectService = new SubjectServiceImplementation();
            subject = new Subject();
            userService = new UserServiceImplementation();
            user = new User();
            classService = new ClassServiceImplementation();
            classes = new Classes();
            String subjct = subjectCode.getText();
            String section = classSection.getText();
            String academicYear = cDate.getAcadYear().toString();
            String sem = cDate.getSemester().toString();
            String room = classRoom.getText();
            String schedule = classSchedule.getText();
            int id = Integer.parseInt(classInstructorID.getSelectedItem().toString());
            try {
                subject = subjectService.getSubject(subjct);
                user = userService.getUser(id);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }

            classes.setYear(academicYear);
            classes.setSem(sem);
            classes.setRoom(room);
            classes.setSchedule(schedule);
            classes.setSubjectID(subject.getSubjectID());
            classes.setSectionID(section);
            classes.setUserID(user.getUserID());

            try {
                classService.addClass(classes);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            String instructor = user.getLastName() + ", " + user.getFirstName() + " " + user.getMiddleName();


            model.addRow(new Object[]{subjct, subject.getDescription(), section, instructor});
            viewClassFrame.setVisible(true);

            jLabel61.requestFocus();
        }

    }//GEN-LAST:event_addClassButtonActionPerformed

    private void cancelClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelClassButtonActionPerformed
        // TODO add your handling code here:
        close();
        viewClassFrame.setVisible(true);
    }//GEN-LAST:event_cancelClassButtonActionPerformed

    private void setCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCodeActionPerformed
        // TODO add your handling code here:
        setHomeIcons();
        setCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/setCodeB.jpg")));
        setRegCode code = new setRegCode(parentFrame, true, userCurrent);
        code.setVisible(true);
    }//GEN-LAST:event_setCodeActionPerformed

    private void manageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageActionPerformed
        int sel = homeTable.getSelectedRow();
        classService = new ClassServiceImplementation();
        classes = new Classes();

        if (sel >= 0) {
            String classSubject = homeTable.getValueAt(sel, 0).toString();
            String classYear = homeTable.getValueAt(sel, 2).toString();
            String section = homeTable.getValueAt(sel, 3).toString();
            String classSemester = homeSemester.getText().toString();
            String classDescription = homeTable.getValueAt(sel, 1).toString();
            String room = homeTable.getValueAt(sel, 4).toString();
            String schedule = homeTable.getValueAt(sel, 5).toString();

            try {
                classes = classService.getClass(classSubject, section, classSemester, classYear);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            scSubject.setText(classSubject);
            scSection.setText(section);
            scDescription.setText(classDescription);
            scRoom.setText(room);
            scSchedule.setText(schedule);
            mcSubject.setText(classSubject);
            mcSection.setText(section);
            mcDescription.setText(classDescription);
            mcRoom.setText(room);
            mcSchedule.setText(schedule);
            if (!classes.isEmpty()) {
                yearSubject = classes.getYear();
                sectionSubject = classes.getSectionID();
                semesterSubject = classes.getSem();
                nameSubject = classes.getSubjectName();
                if (classes.isFirstAttempt()) {
                    jLabel24.setVisible(false);
                    jLabel25.setVisible(false);
                    setUpClass.setVisible(true);
                } else {
                    manageClass.setVisible(true);
                }
                classesTo.setSem(classes.getSem());
                classesTo.setYear(classes.getYear());
                classIDNumberSelected = classes.getClassID();

                disableAttendance(classIDNumberSelected);
            }
        }
    }//GEN-LAST:event_manageActionPerformed

    private void continuebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continuebuttonActionPerformed
       
         DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        pPercentage=100;
        passingRate.setText(null);
        jLabel57.setText("100");
       uploaded=false;
        off.setEnabled(true);
        logout.setEnabled(true);
        jButton8.setEnabled(true);
        disableAttendance(classIDNumberSelected);
        if (createDone) {
            classService = new ClassServiceImplementation();
            classes = new Classes();
            classSearch = new ArrayList<>();
            jLabel24.setVisible(false);
            jLabel25.setVisible(false);
            jButton2.setEnabled(true);
            jButton3.setEnabled(true);

            try {
                classes = classService.getClass(nameSubject, sectionSubject, semesterSubject, yearSubject);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            // int nClassID=0;
            if (!classes.isEmpty()) {

                classIDNumberSelected = classes.getClassID();

            }
            classes = new Classes();
            classService = new ClassServiceImplementation();
            classes.setClassID(classIDNumberSelected);
            classes.setFirstAttempt(false);
            try {
                classService.changeAttempt(classes);
                classes = classService.getClass(classIDNumberSelected);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            mcSubject.setText(classes.getSubjectName());
            mcSection.setText(classes.getSectionID());
            mcDescription.setText(classes.getSubjectDescripton());
            mcRoom.setText(classes.getRoom());
            mcSchedule.setText(classes.getSchedule());

            manageClass.setVisible(true);
            
            System.out.println("continute  "+classIDNumberSelected);
            uploaded = false;
        } else {
            JOptionPane.showMessageDialog(this, "Grading System has not yet been created!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_continuebuttonActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        close();
        homeFrame.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void mcSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mcSubjectActionPerformed

    private void mcSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcSectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mcSectionActionPerformed

    private void mcDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mcDescriptionActionPerformed

    private void mcRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mcRoomActionPerformed

    private void mcScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcScheduleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mcScheduleActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        close();
        homeFrame.setVisible(true);
    }//GEN-LAST:event_exitActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        System.out.println("CRATE RADIN SYSTEM  "+classIDNumberSelected);
        if (uploaded) {
            close();
            setGradingSystemView3();
            gradingSystem.setVisible(true);
          //  createDone=true;
        } else {
            JOptionPane.showMessageDialog(this, "Class masterlist has not yet been uploaded!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        fc.setFileFilter(filter1);
        ClassRecordServiceInterface classrecService;
        ClassRecord clasRec;
        studentService = new StudentServiceImplementation();
        student = new Student();
        file = fc.getSelectedFile();
        StringTokenizer token = null;
        Scanner inFile = null;
        String skip[] = new String[100];
        String studName[] = new String[100];
        String studId[] = new String[100];

        int returnVal = fc.showOpenDialog(this);

        int lineCount = 0;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                inFile = new Scanner(new File(fc.getSelectedFile().toString()));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (inFile.hasNextLine()) {
                String temp = inFile.nextLine();
                token = new StringTokenizer(temp);

                if (++lineCount > 6) {
                    if (!inFile.hasNextLine()) {
                        break;
                    }
                    skip[index] = token.nextToken();
                    studId[index] = token.nextToken();
                    studName[index] = token.nextToken();
                    while (token.hasMoreTokens()) {
                        studName[index] += (" " + token.nextToken());
                    }
                    index++;
                }
            }
            String dateNow = formatTool.convertSQLDateToDateString(formatTool.conveter(dates.toLocaleString()));
            for (int i = 0; i < index; i++) {
                clasRec = new ClassRecord();
                classrecService = new ClassRecordServiceImplementation();
                studentService = new StudentServiceImplementation();
                student = new Student();
                try {
                    student = studentService.getStudent(Integer.parseInt(studId[i]));
                    clasRec = classrecService.getClassRecord(classIDNumberSelected, Integer.parseInt(studId[i]));
                } catch (ErrorException ex) {
                    ex.printStackTrace();
                }
                if (student.isEmpty()) {
                    student.setIdNumber(Integer.parseInt(studId[i]));
                    student.setCompleteName(studName[i]);
                    try {
                        studentService.addStudent(student);
                    } catch (ErrorException ex) {
                        Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (clasRec.isEmpty()) {
                    clasRec.setClassID(classIDNumberSelected);
                    clasRec.setStudentID(Integer.parseInt(studId[i]));
                    clasRec.setDate(dateNow);
                    classIDNumberSelected = clasRec.getClassID();
                    try {
                        classrecService.addStudentClassRecord(clasRec);
                    } catch (ErrorException ex) {
                        ex.printStackTrace();
                    }
                }

            }

            uploaded = true;
            jLabel24.setVisible(true);
            jButton2.setEnabled(false);
            jButton8.setEnabled(false);
        } else {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void viewInstructorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewInstructorsActionPerformed

        setHomeIcons();
        viewInstructors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewInstructorsB.jpg")));
        close();
        DefaultTableModel model = (DefaultTableModel) this.insList.getModel();
        userService = new UserServiceImplementation();
        userSearch = new ArrayList<>();
        try {
            userSearch = userService.getUsers();
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();

        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (User temp : userSearch) {
            if (temp.getUserType() == 2) {
                model.addRow(new Object[]{temp.getLastName() + ", " + temp.getFirstName() + " " + temp.getMiddleName(), temp.getContactNum(), temp.getUserID()});
            }
        }
        instructorFrame.setVisible(true);
    }//GEN-LAST:event_viewInstructorsActionPerformed

    private void viewProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewProActionPerformed
        int sel = insList.getSelectedRow();

        if (sel >= 0) {
            userService = new UserServiceImplementation();
            user = new User();
            OldUser userOld = new OldUser();
            DefaultTableModel model = (DefaultTableModel) this.insList.getModel();
            String id = model.getValueAt(sel, 2).toString();
            int data = Integer.parseInt(id);
            try {
                user = userService.getUser(data);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (user.getUserID() == data) {
                user.setLastName(user.getLastName());
                user.setFirstName(user.getFirstName());
                user.setMiddleName(user.getMiddleName());
                user.setCity(user.getCity());
                user.setProvince(user.getProvince());
                user.setContactNum(user.getContactNum());
                user.setEmailAdd(user.getEmailAdd());
                user.setImage(user.getImage());

            }
            userOld.setIdNumber(userCurrent.getUserID());
            profileF = new profileFrame(parentFrame, true, user, userOld, this);
            profileF.setVisible(true);
    }//GEN-LAST:event_viewProActionPerformed
    }
    private void addSubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSubActionPerformed
        // TODO add your handling code here:
        close();
        addSubFrame.setVisible(true);
    }//GEN-LAST:event_addSubActionPerformed

    public void disableBack() {
        jButton15.setEnabled(false);
    }
    private void editSubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSubActionPerformed

        if (subjList.getSelectedRowCount() > 0) {
            close();
            editSubFrame.setVisible(true);
            DefaultTableModel model = (DefaultTableModel) this.subjList.getModel();
            subjectService = new SubjectServiceImplementation();
            subject = new Subject();
            int sel = this.subjList.getSelectedRow();
            String name = model.getValueAt(sel, 0).toString();
            try {
                subject = subjectService.getSubject(name);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (subject.isEmpty()) {
                eSubject.setText(subject.getSubjectName());
                eSubjectDescription.setText(subject.getDescription());
                eSubjectUnits.setValue(subject.getUnits());
                eSemester.setSelectedItem(subject.getSemester());
            }
        }
    }//GEN-LAST:event_editSubActionPerformed

    private void deleteSubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSubActionPerformed
        if (subjList.getSelectedRowCount() > 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this subject?", "Confim", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                DefaultTableModel model = (DefaultTableModel) this.subjList.getModel();
                subjectService = new SubjectServiceImplementation();
                subject = new Subject();

                int sel = this.subjList.getSelectedRow();
                String name = model.getValueAt(sel, 0).toString();

                try {
                    subject = subjectService.getSubject(name);
                } catch (ErrorException ex) {
                    Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (subject.isEmpty()) {

                    subjectID = subject.getSubjectID();
                    try {
                        subjectService.deleteSubject(subjectID);
                    } catch (ErrorException ex) {
                        Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                updateTable();
                subjectsFrame.setVisible(true);
            }
        }
    }//GEN-LAST:event_deleteSubActionPerformed

    private void viewSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSubjectActionPerformed
        // TODO add your handling code here:
        setHomeIcons();
        viewSubject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/viewSubjectsB.jpg")));
        close();
        subjectsFrame.setVisible(true);
        updateTable();
    }//GEN-LAST:event_viewSubjectActionPerformed

    private void aSubjectDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aSubjectDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_aSubjectDescriptionActionPerformed

    private void add3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add3ActionPerformed
        if (aSubjectCode.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Provide a subject name first!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            close();
            subjectService = new SubjectServiceImplementation();
            subject = new Subject();
            int sel = this.subjList.getSelectedRow();
            String name = aSubjectCode.getText();
            try {
                subject = subjectService.getSubject(name);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!subject.isEmpty()) {
                subject.setSubjectName(aSubjectCode.getText().toUpperCase());
                subject.setDescription(aSubjectDescription.getText().toUpperCase());
                subject.setUnits(Integer.parseInt(aSubjectUnits.getValue().toString()));
                subject.setSemester(aSemester.getSelectedItem().toString());
                try {
                    subjectService.addSubject(subject);
                } catch (ErrorException ex) {
                    Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            updateTable();
            subjectsFrame.setVisible(true);
            aSubjectCode.setText(null);
            aSubjectDescription.setText(null);
            aSubjectUnits.setValue(0);
        }
    }//GEN-LAST:event_add3ActionPerformed

    private void cancel3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel3ActionPerformed
        // TODO add your handling code here:
        close();
        subjectsFrame.setVisible(true);
    }//GEN-LAST:event_cancel3ActionPerformed

    private void aSubjectCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aSubjectCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_aSubjectCodeActionPerformed

    private void saveSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSubjectActionPerformed
        if (eSubject.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Provide a subject name first!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            close();
            DefaultTableModel model = (DefaultTableModel) this.subjList.getModel();
            subjectService = new SubjectServiceImplementation();
            subject = new Subject();

            int sel = this.subjList.getSelectedRow();
            String name = model.getValueAt(sel, 0).toString();
            try {
                subject = subjectService.getSubject(name);
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (subject.isEmpty()) {
                subject.setSubjectName(eSubject.getText().toUpperCase());
                subject.setDescription(eSubjectDescription.getText().toUpperCase());
                subject.setUnits(Integer.parseInt(eSubjectUnits.getValue().toString()));
                subject.setSemester(eSemester.getSelectedItem().toString());
                subject.getSubjectID();
                try {
                    subjectService.editSubject(subject);
                } catch (ErrorException ex) {
                    Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            updateTable();

            subjectsFrame.setVisible(true);
        }
    }//GEN-LAST:event_saveSubjectActionPerformed

    private void cancel5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel5ActionPerformed
        // TODO add your handling code here:
        close();
        subjectsFrame.setVisible(true);
    }//GEN-LAST:event_cancel5ActionPerformed

    private void checkAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAttendanceActionPerformed

        classesTo.setClassID(classIDNumberSelected);
        attendanceForm sheet = new attendanceForm(parentFrame, true, classesTo);
        sheet.setVisible(true);

    }//GEN-LAST:event_checkAttendanceActionPerformed

    private void viewClassrecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewClassrecordActionPerformed
        // TODO add your handling code here:
        System.out.println("vieClass button " + classIDNumberSelected);
        classService = new ClassServiceImplementation();
        classes = new Classes();
        Classes clas = new Classes();
        String subjectName = mcSubject.getText().toString();
        String subjectSection = mcSection.getText().toString();
        String subjectRoom = mcRoom.getText().toString();
        String subjectSchedule = mcSchedule.getText().toString();
        try {
            clas = classService.getCurrentClass(subjectName, subjectSection, subjectRoom, subjectSchedule);
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        classes.setClassID(classIDNumberSelected);
        classes.setRoom(clas.getRoom());
        classes.setInstructorName(clas.getInstructorName());
        classes.setSchedule(clas.getSchedule());
        classes.setSubjectName(clas.getSubjectName());
        classes.setSectionID(clas.getSectionID());
        classRecord class1 = new classRecord(parentFrame, true, classes);
        class1.setVisible(true);



    }//GEN-LAST:event_viewClassrecordActionPerformed

    private void updateClassrecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateClassrecordActionPerformed
        // TODO add your handling code here:
        userCurrent.setClassID(classIDNumberSelected);
        updateRecord record = new updateRecord(parentFrame, true, userCurrent, classesTo);
        record.setVisible(true);
    }//GEN-LAST:event_updateClassrecordActionPerformed

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        // TODO add your handling code here:
        setHomeIcons();
        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/homeB.jpg")));
        close();
        homeViewClass();
        homeFrame.setVisible(true);
    }//GEN-LAST:event_homeActionPerformed

    private void editRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editRoomActionPerformed

    private void editScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editScheduleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editScheduleActionPerformed

    private void editSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editSectionActionPerformed

    private void aSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aSemesterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_aSemesterActionPerformed

    private void eSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eSemesterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eSemesterActionPerformed

    private void eSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eSubjectActionPerformed

    private void eSubjectDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eSubjectDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eSubjectDescriptionActionPerformed

    private void offActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offActionPerformed
        // TODO add your handling code here:
        setHomeIcons();
        off.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/exitB.jpg")));
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the applicaton?\nAll unfinished processes will be lost!", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            this.dispose();
        }
    }//GEN-LAST:event_offActionPerformed

    private void helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpActionPerformed
        openPDF();
        setHomeIcons();
    }//GEN-LAST:event_helpActionPerformed

    private void cancelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cancelFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(cancel.getBounds());
    }//GEN-LAST:event_cancelFocusGained

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        // TODO add your handling code here:
        JScrollBar verticalScrollBar = jScrollPane1.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
        edit(false);
        back.setEnabled(true);
        editAcc.setEnabled(true);
        editpass.setEnabled(true);
        eusername.setText(tusername);
        elastname.setText(tlname);
        efirstname.setText(tfname);
        emiddlename.setText(tmname);
        ecity.setText(tcity);
        eprovince.setText(tprovince);
        econtactNo.setText(tcontacNo);
        eemailadd.setText(temail);
    }//GEN-LAST:event_cancelActionPerformed

    private void saveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_saveFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(save.getBounds());
    }//GEN-LAST:event_saveFocusGained

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed

        if (efirstname.getText().equals("") || emiddlename.getText().equals("") || elastname.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Empty Fields!", "Error", JOptionPane.ERROR_MESSAGE);
            efirstname.requestFocus();
        } else {
            JScrollBar verticalScrollBar = jScrollPane1.getVerticalScrollBar();;
            verticalScrollBar.setValue(verticalScrollBar.getMinimum());
            edit(false);
            back.setEnabled(true);
            editAcc.setEnabled(true);
            editpass.setEnabled(true);
            String name = elastname.getText() + ", " + efirstname.getText() + " " + emiddlename.getText();
            String addr = ecity.getText() + ", " + eprovince.getText();
            fullName.setText(name);
            contact.setText(econtactNo.getText());
            address.setText(addr);
            email.setText(eemailadd.getText());

            userService = new UserServiceImplementation();
            user = new User();
            String firstName = efirstname.getText();
            String lastName = elastname.getText();
            String mName = emiddlename.getText();
            String cNumber = econtactNo.getText();
            String eemail = eemailadd.getText();
            String city = ecity.getText();
            String province = eprovince.getText();
            int idNo = Integer.parseInt(efacultyID.getText());
            user.setFirstName(firstName);
            user.setCity(city);
            user.setProvince(province);
            user.setLastName(lastName);
            user.setMiddleName(mName);
            user.setContactNum(cNumber);
            user.setEmailAdd(eemail);
            user.setUserID(idNo);

            if (object != null && file != null) {
                //   proPic.setIcon((Icon)object);
                object = file;
                user.setImage(object);
                try {
                    userService.editPhoto(user);
                } catch (ErrorException ex) {
                    ex.printStackTrace();
                }
            }

            UserServiceInterface usrService = new UserServiceImplementation();
            User usr = new User();
            try {
                userService.editUser(user);
                usr = usrService.getUser(idNo);
            } catch (ErrorException ex) {
                ex.printStackTrace();
            }

            Object objct = new Object();
            if (usr.getImage() != null) {
                objct = usr.getImage();
                if (objct instanceof com.mysql.jdbc.Blob) {
                    this.proPic.setIcon(new javax.swing.ImageIcon(formatTool.convertToActualSizeImage((Blob) objct, 2)));
                }
            }
            JOptionPane.showMessageDialog(this, "You have successfully updated your account!", "Succesful", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_saveActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        // TODO add your handling code here:
        JScrollBar verticalScrollBar = jScrollPane1.getVerticalScrollBar();;
        close();
        homeFrame.setVisible(true);
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
    }//GEN-LAST:event_backActionPerformed

    private void editpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editpassActionPerformed
        // TODO add your handling code here:
        editPassword frame = new editPassword(parentFrame, true, userCurrent);
        frame.setVisible(true);
    }//GEN-LAST:event_editpassActionPerformed

    private void editAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editAccActionPerformed
        // TODO add your handling code here:
        JScrollBar verticalScrollBar = jScrollPane1.getVerticalScrollBar();;
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
        edit(true);
        back.setEnabled(false);
        editAcc.setEnabled(false);
        editpass.setEnabled(false);
        elastname.requestFocus();

        setTemp();
    }//GEN-LAST:event_editAccActionPerformed

    private void eemailaddFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eemailaddFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(eemailadd.getBounds());
    }//GEN-LAST:event_eemailaddFocusGained

    private void eemailaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eemailaddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eemailaddActionPerformed

    private void econtactNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_econtactNoFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(econtactNo.getBounds());
    }//GEN-LAST:event_econtactNoFocusGained

    private void econtactNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_econtactNoActionPerformed
        // TODO add your handling code here:

        int i = econtactNo.getX();
        JScrollBar verticalScrollBar = jScrollPane1.getVerticalScrollBar();;
        verticalScrollBar.setValue(i);
    }//GEN-LAST:event_econtactNoActionPerformed

    private void eprovinceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eprovinceFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(eprovince.getBounds());
    }//GEN-LAST:event_eprovinceFocusGained

    private void eprovinceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eprovinceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eprovinceActionPerformed

    private void ecityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ecityFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(ecity.getBounds());
    }//GEN-LAST:event_ecityFocusGained

    private void ecityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ecityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ecityActionPerformed

    private void emiddlenameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emiddlenameFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(emiddlename.getBounds());
    }//GEN-LAST:event_emiddlenameFocusGained

    private void emiddlenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emiddlenameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emiddlenameActionPerformed

    private void efirstnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efirstnameFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(efirstname.getBounds());
    }//GEN-LAST:event_efirstnameFocusGained

    private void efirstnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efirstnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efirstnameActionPerformed

    private void elastnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_elastnameFocusGained
        // TODO add your handling code here:
        jPanel5.scrollRectToVisible(elastname.getBounds());
    }//GEN-LAST:event_elastnameFocusGained

    private void elastnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elastnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elastnameActionPerformed

    private void eusernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eusernameFocusGained
        // TODO add your handling code here:
        eusername.setAutoscrolls(true);
        eusername.scrollRectToVisible(eusername.getBounds());
    }//GEN-LAST:event_eusernameFocusGained

    private void eusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eusernameActionPerformed

    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
        // TODO add your handling code here:
        file = fc.getSelectedFile();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & PNG Images", "jpg", "png");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            file = fc.getSelectedFile();
            // bmp, gif, jpg, png files okay

            Image image = Toolkit.getDefaultToolkit().getImage(file.toString()).getScaledInstance(formatTool.IMAGE_WIDTH, formatTool.IMAGE_HEIGHT, 100);
            object = new ImageIcon(image);
            this.proPic1.setIcon((Icon) object);
            bPic = true;
        } else {
            System.out.println("Cannot open file!");
            bPic = false;
        }
    }//GEN-LAST:event_browseActionPerformed

    private void efacultyIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efacultyIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efacultyIDActionPerformed
    public JTextField[] mainFieldG = new JTextField[20];
    public JTextField[] mainRateFieldG = new JTextField[20];
    public JTextField[] subFieldG = new JTextField[20];
    public JTextField[] subRateFieldG = new JTextField[20];
    int index = 0;
    int subIndex = 5;
    int xAxis = 5;
    int yAxis = 5;
    boolean status = false;
    JLabel[] labelMain = new JLabel[10];
    JLabel[] labelSub = new JLabel[10];

    private void addSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSectionActionPerformed

        close();
        cDate = new CurrentDate();
        updateComboBox(classInstructorID);
        userService = new UserServiceImplementation();
        userSearch = new ArrayList<>();
        int sel = offeredSubjectList.getSelectedRow();
        if (sel >= 0) {
            String name = offeredSubjectList.getValueAt(sel, 0).toString();
            String description = offeredSubjectList.getValueAt(sel, 1).toString();
            acadYearLabel.setText(cDate.getAcadYear().toString());
            semesterLabel.setText(cDate.getSemester().toString());
            subjectCode.setText(name);
            subjectDescription.setText(description);
            classSection.setText(null);
            classRoom.setText(null);
            classSchedule.setText(null);
            classInstructorID.setSelectedItem(userCurrent.getUserID());
            addClass.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select subject first to PROCEED.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addSectionActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed

        classService = new ClassServiceImplementation();
        classSearch = new ArrayList<>();
        updateComboBox(instructorID);
        cDate = new CurrentDate();
        close();
        int sel = classList.getSelectedRow();
        if (sel >= 0) {
            String subjectName = classList.getValueAt(sel, 0).toString();
            String description = classList.getValueAt(sel, 1).toString();
            String subjectSection = classList.getValueAt(sel, 2).toString();
            try {
                classSearch = classService.getClasses();
            } catch (ErrorException ex) {
                Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Classes temp : classSearch) {
                if (temp.getSubjectName().equalsIgnoreCase(subjectName)
                        && temp.getSectionID().equalsIgnoreCase(subjectSection)) {
                    acadYearLabel2.setText(temp.getYear().toString());
                    semesterLabel2.setText(temp.getSem().toString());
                    subjectLabel.setText(subjectName);
                    descriptionLabel.setText(description);
                    editSection.setText(subjectSection);
                    editRoom.setText(temp.getRoom().toString());
                    editSchedule.setText(temp.getSchedule().toString());
                    classID = temp.getClassID();
                    instructorID.setSelectedItem(temp.getUserID());
                    instructorLabel.setText(temp.getInstructorName().toString());
                }
            }

            editClass.setVisible(true);
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        classService = new ClassServiceImplementation();
        classSearch = new ArrayList<>();
        classes = new Classes();
        int sel = classList.getSelectedRow();
        if (sel >= 0) {

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this subject?\n", "Confim", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                String subjectName = classList.getValueAt(sel, 0).toString();
                String subjectSection = classList.getValueAt(sel, 2).toString();
                try {
                    classSearch = classService.getClasses();
                } catch (ErrorException ex) {
                    Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (Classes temp : classSearch) {
                    if (temp.getSubjectName().equalsIgnoreCase(subjectName) && temp.getSectionID().equalsIgnoreCase(subjectSection)) {
                        int id = temp.getClassID();
                        try {
                            classService.deleteClass(id);
                        } catch (ErrorException ex) {
                            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                viewClassList();
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void cancelAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAccountActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel your account?\nYou won't be able to retrieve this account anymore!", "Confim", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            confirm frame = new confirm(parentFrame, true, userCurrent, this);
            frame.setVisible(true);
        }
    }//GEN-LAST:event_cancelAccountActionPerformed

    private void cancelAccountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cancelAccountFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelAccountFocusGained

    private void classInstructorIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classInstructorIDActionPerformed
        int id = Integer.parseInt(classInstructorID.getSelectedItem().toString());
        userService = new UserServiceImplementation();
        user = new User();
        try {
            user = userService.getUser(id);
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        String instructor = user.getLastName() + ", " + user.getFirstName() + " " + user.getMiddleName();
        instructorName.setText(instructor);
    }//GEN-LAST:event_classInstructorIDActionPerformed

    private void instructorIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instructorIDActionPerformed
        int id = Integer.parseInt(instructorID.getSelectedItem().toString());
        userService = new UserServiceImplementation();
        user = new User();
        try {
            user = userService.getUser(id);
        } catch (ErrorException ex) {
            Logger.getLogger(UserHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        String instructor = user.getLastName() + ", " + user.getFirstName() + " " + user.getMiddleName();
        instructorLabel.setText(instructor);
    }//GEN-LAST:event_instructorIDActionPerformed

    private void offerSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offerSubjectActionPerformed
        viewClassOffered();
    }//GEN-LAST:event_offerSubjectActionPerformed

    private void bySemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bySemesterActionPerformed
        viewClass();
    }//GEN-LAST:event_bySemesterActionPerformed

    private void markerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markerActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.subjectList.getModel();
        index = 0;
        int tableRow = model.getRowCount();
        if (marker.getSelectedItem().toString().equals("Mark All")) {
            while (tableRow-- > 0) {
                model.setValueAt(true, index++, 0);
            }
        } else {
            while (tableRow-- > 0) {
                model.setValueAt(false, index++, 0);
            }
        }
    }//GEN-LAST:event_markerActionPerformed

    private void delSubjectOfferedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delSubjectOfferedActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.offeredSubjectList.getModel();
        model.removeRow(offeredSubjectList.getSelectedRow());
    }//GEN-LAST:event_delSubjectOfferedActionPerformed

    private void classListSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classListSemActionPerformed
        viewClassList();
    }//GEN-LAST:event_classListSemActionPerformed

    private void classListYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classListYearActionPerformed
        viewClassList();
    }//GEN-LAST:event_classListYearActionPerformed

private void yearCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearCBActionPerformed
    homeViewClass();
}//GEN-LAST:event_yearCBActionPerformed

private void semesterCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_semesterCBActionPerformed
    homeViewClass();
}//GEN-LAST:event_semesterCBActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        userCurrent.setNameSubject(nameSubject);
        userCurrent.setSectionSubject(sectionSubject);
        userCurrent.setSemesterSubject(semesterSubject);
        userCurrent.setYearSubject(yearSubject);
        if (pPercentage > 0 && pPercentage <= 100) {
            gradesTo = new GradingSystem();
            gradesTo.setClassID(classIDNumberSelected);
            gradesTo.setCheckPercentage(pPercentage);

            addM = new AddEntry(parentFrame, true, gradesTo, userCurrent, this);
            addM.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Oopss Only 100 percent ", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        close();
        setUpClass.setVisible(true);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        off.setEnabled(false);
        logout.setEnabled(false);
        classService = new ClassServiceImplementation();
        classes = new Classes();

        nameSubject = userCurrent.getNameSubject();
        sectionSubject = userCurrent.getSectionSubject();
        semesterSubject = userCurrent.getSemesterSubject();

        yearSubject = userCurrent.getYearSubject();
        ClassServiceInterface chserv = new ClassServiceImplementation();
        Classes ch = new Classes();

        String passR = passingRate.getText();
        if (Integer.parseInt(jLabel57.getText()) <= 0) {
            if (passR.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill the passing rate field first!", "Error", JOptionPane.ERROR_MESSAGE);
                passingRate.requestFocus();
            } else {
                classService = new ClassServiceImplementation();
                classes = new Classes();
                try {
                    classService.insertPassingRate(classIDNumberSelected, Integer.parseInt(passR));
                    classes = classService.getClass(classIDNumberSelected);
                } catch (ErrorException ex) {
                    ex.printStackTrace();
                }
                createDone = true;
                close();
                scSubject.setText(classes.getSubjectName());
                System.out.println("Subject: " + classes.getSubjectName());
                scSection.setText(classes.getSectionID());
                scDescription.setText(classes.getSubjectDescripton());
                scRoom.setText(classes.getRoom());
                scSchedule.setText(classes.getSchedule());
                jLabel25.setVisible(true);
                jButton3.setEnabled(false);
                jButton2.setEnabled(false);
                jButton8.setEnabled(false);
                setUpClass.setVisible(true);

            }
        } else {
            JOptionPane.showMessageDialog(null, "Percentage total must reach 100%!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void econtactNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_econtactNoKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();

        if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_econtactNoKeyTyped
    Color fontC;
    private void jLabel61FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel61FocusGained
        // TODO add your handling code here:
        fontC = jLabel61.getForeground();
        jLabel61.setForeground(Color.red);
    }//GEN-LAST:event_jLabel61FocusGained

    private void jLabel61FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel61FocusLost
        // TODO add your handling code here:
        jLabel61.setForeground(fontC);
    }//GEN-LAST:event_jLabel61FocusLost

    private void jLabel61MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel61MouseClicked
        // TODO add your handling code here:
        JScrollBar verticalScrollBar = jScrollPane6.getVerticalScrollBar();;
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }//GEN-LAST:event_jLabel61MouseClicked

private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
    if (jTable1.getSelectedRowCount() > 0) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entry?\n", "Confim", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
            GradingSystemServiceInterface gradeService = new GradingSystemServiceImplementation();



            int sel = this.jTable1.getSelectedRow();
            String name = model.getValueAt(sel, 0).toString();
            int prct = Integer.parseInt(model.getValueAt(sel, 1).toString());
            pPercentage = pPercentage + prct;
            int gradeid = classIDNumberSelected;
            try {
                gradeService.deleteEntry(classIDNumberSelected, name);
            } catch (ErrorException ex) {
                ex.printStackTrace();
            }
            
            classIDNumberSelected = gradeid;
            setGradingSystemView2();
            gradingSystem.setVisible(true);
            JOptionPane.showMessageDialog(this, "Grade Entry has been deleted from the system!", "Succesful", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}//GEN-LAST:event_jButton14ActionPerformed

private void passingRateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passingRateKeyTyped
    char c = evt.getKeyChar();

    if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE)
            || (c == KeyEvent.VK_DELETE))) {
        getToolkit().beep();
        evt.consume();
    }
}//GEN-LAST:event_passingRateKeyTyped

private void passingRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passingRateActionPerformed
    jButton16.doClick();
}//GEN-LAST:event_passingRateActionPerformed

private void passingRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passingRateKeyPressed
    char c = evt.getKeyChar();

    if (c == KeyEvent.VK_ENTER) {
        jButton16.doClick();
    }
}//GEN-LAST:event_passingRateKeyPressed

    private void scSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scSectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scSectionActionPerformed

    private void scScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scScheduleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scScheduleActionPerformed

    private void scDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scDescriptionActionPerformed

    private void scRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scRoomActionPerformed

    private void scSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scSubjectActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    userCurrent.setClassID(classIDNumberSelected);
    editGradingSystem egs = new editGradingSystem(parentFrame, true, userCurrent);
    egs.setVisible(true);
}//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox aSemester;
    private javax.swing.JTextField aSubjectCode;
    private javax.swing.JTextField aSubjectDescription;
    private javax.swing.JSpinner aSubjectUnits;
    private javax.swing.JLabel acadYearLabel;
    private javax.swing.JLabel acadYearLabel2;
    private javax.swing.JButton account;
    private javax.swing.JInternalFrame accountFrame;
    private javax.swing.JButton add3;
    private javax.swing.JInternalFrame addClass;
    private javax.swing.JButton addClassButton;
    private javax.swing.JButton addSection;
    private javax.swing.JButton addSub;
    private javax.swing.JInternalFrame addSubFrame;
    private javax.swing.JLabel address;
    private javax.swing.JButton back;
    private javax.swing.JPanel background;
    private javax.swing.JButton browse;
    private javax.swing.JComboBox bySemester;
    private javax.swing.JButton cancel;
    private javax.swing.JButton cancel3;
    private javax.swing.JButton cancel5;
    private javax.swing.JButton cancelAccount;
    private javax.swing.JButton cancelClassButton;
    private javax.swing.JButton cancelEditClass;
    private javax.swing.JButton checkAttendance;
    private javax.swing.JComboBox classInstructorID;
    private javax.swing.JTable classList;
    private javax.swing.JComboBox classListSem;
    private javax.swing.JComboBox classListYear;
    private javax.swing.JTextField classRoom;
    private javax.swing.JTextField classSchedule;
    private javax.swing.JTextField classSection;
    private javax.swing.JLabel contact;
    private javax.swing.JButton continuebutton;
    private javax.swing.JButton delSubjectOffered;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteSub;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JComboBox eSemester;
    private javax.swing.JTextField eSubject;
    private javax.swing.JTextField eSubjectDescription;
    private javax.swing.JSpinner eSubjectUnits;
    private javax.swing.JTextField ecity;
    private javax.swing.JTextField econtactNo;
    private javax.swing.JButton editAcc;
    private javax.swing.JButton editButton;
    private javax.swing.JInternalFrame editClass;
    private javax.swing.JTextField editRoom;
    private javax.swing.JTextField editSchedule;
    private javax.swing.JTextField editSection;
    private javax.swing.JButton editSub;
    private javax.swing.JInternalFrame editSubFrame;
    private javax.swing.JButton editpass;
    private javax.swing.JTextField eemailadd;
    private javax.swing.JTextField efacultyID;
    private javax.swing.JTextField efirstname;
    private javax.swing.JTextField elastname;
    private javax.swing.JLabel email;
    private javax.swing.JTextField emiddlename;
    private javax.swing.JTextField eprovince;
    private javax.swing.JTextField eusername;
    private javax.swing.JButton exit;
    private javax.swing.JLabel fullName;
    private javax.swing.JInternalFrame gradingSystem;
    private javax.swing.JButton help;
    private javax.swing.JButton home;
    private javax.swing.JInternalFrame homeFrame;
    private javax.swing.JLabel homeSemester;
    private javax.swing.JTable homeTable;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JTable insList;
    private javax.swing.JInternalFrame instructorFrame;
    private javax.swing.JComboBox instructorID;
    private javax.swing.JLabel instructorLabel;
    private javax.swing.JLabel instructorName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable1;
    private javax.swing.JLayeredPane layerPane;
    private javax.swing.JPanel layeredPanel;
    private javax.swing.JButton logout;
    private javax.swing.JButton manage;
    private javax.swing.JInternalFrame manageClass;
    private javax.swing.JComboBox marker;
    private javax.swing.JTextField mcDescription;
    private javax.swing.JTextField mcRoom;
    private javax.swing.JTextField mcSchedule;
    private javax.swing.JTextField mcSection;
    private javax.swing.JTextField mcSubject;
    private javax.swing.JButton off;
    private javax.swing.JButton offerSubject;
    private javax.swing.JTable offeredSubjectList;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextField passingRate;
    private javax.swing.JLabel proPic;
    private javax.swing.JLabel proPic1;
    private javax.swing.JButton save;
    private javax.swing.JButton saveEditClass;
    private javax.swing.JButton saveSubject;
    private javax.swing.JTextField scDescription;
    private javax.swing.JTextField scRoom;
    private javax.swing.JTextField scSchedule;
    private javax.swing.JTextField scSection;
    private javax.swing.JTextField scSubject;
    private javax.swing.JComboBox semesterCB;
    private javax.swing.JLabel semesterLabel;
    private javax.swing.JLabel semesterLabel2;
    private javax.swing.JButton setCode;
    private javax.swing.JInternalFrame setUpClass;
    private javax.swing.JTable subjList;
    private javax.swing.JLabel subjectCode;
    private javax.swing.JLabel subjectDescription;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JTable subjectList;
    private javax.swing.JScrollPane subjects;
    private javax.swing.JInternalFrame subjectsFrame;
    private javax.swing.JButton updateClassrecord;
    private javax.swing.JInternalFrame viewClassFrame;
    private javax.swing.JButton viewClasses;
    private javax.swing.JButton viewClassrecord;
    private javax.swing.JButton viewInstructors;
    private javax.swing.JButton viewPro;
    private javax.swing.JButton viewSubject;
    private javax.swing.JComboBox yearCB;
    // End of variables declaration//GEN-END:variables
}
