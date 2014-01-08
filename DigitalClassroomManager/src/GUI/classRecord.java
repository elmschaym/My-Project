/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import model.ClassRecord;
import model.Classes;
import model.CurrentUser;
import model.GradingSystem;
import service.implementations.ClassRecordServiceImplementation;
import service.implementations.ClassServiceImplementation;
import service.implementations.GradingSystemServiceImplementation;
import service.interfaces.ClassRecordServiceInterface;
import service.interfaces.ClassServiceInterface;
import service.interfaces.GradingSystemServiceInterface;
import tools.ErrorException;
import tools.Transparent;

/**
 *
 * @author sam
 */
public class classRecord extends javax.swing.JDialog {

    private javax.swing.JFrame parentFrame;
    private Classes classesG;
    private ArrayList<ClassRecord> classRecordDate;
    private GradingSystemServiceInterface gradingSystemService;
    private CurrentUser userCurrent;

    /**
     * Creates new form classRecord
     */
    public classRecord(java.awt.Frame parent, boolean modal, Classes classes) {

        super(parent, modal);
        //viewSummaryTable();
        Transparent trans = new Transparent();
        trans.TransparentDForm(this);
        initComponents();
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.parentFrame = (JFrame) parent;
        classesG = classes;
        subjectLabel.setText(classes.getSubjectName());
        instructorLabel.setText(classes.getInstructorName());
        sectionLabel.setText(classes.getSectionID());
        scheduleLabel.setText(classes.getSchedule());
        start();
    }

    public String checkTheSpace(String str) {

        String newString = "";
        String temp;
        StringTokenizer var = new StringTokenizer(str);
        while (var.hasMoreTokens()) {
            temp = var.nextToken();
            if (!temp.equals(" ")) {
                newString += temp;
            }

        }
        return newString;
    }
    JTable myTable = new JTable();
    int myrow;
    int mycol = 4;
    int myGrade[];
    String myinitialCol[] = new String[mycol];
    String myrows[][] = new String[myrow][mycol];
    ClassRecord classRecord;
    ArrayList<ClassRecord> classRecordSearch;
    ClassRecordServiceInterface classrecordService;
    ArrayList<ClassRecord> classGetValue;
    ArrayList<GradingSystem> gradingSearch;

    public void start() {

        classrecordService = new ClassRecordServiceImplementation();
        gradingSystemService = new GradingSystemServiceImplementation();
        classRecord = new ClassRecord();
        classRecordSearch = new ArrayList<>();
        classRecordDate = new ArrayList<>();
        gradingSearch = new ArrayList<>();
        classGetValue = new ArrayList<>();
        classRecord.setClassID(classesG.getClassID());
        try {
            classRecordSearch = classrecordService.getClassRecord(classRecord);
            classRecordDate = classrecordService.getClassDate(classRecord);
        } catch (ErrorException ex) {
            Logger.getLogger(classRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

        int size;

        size = classRecordSearch.size();

        int count = 0;
        int studCount = 0;
        for (ClassRecord temp : classRecordSearch) {
            if (count < size) {
                studCount++;
            }
            count++;
        }

        myrow = studCount + 1;
        myrows = new String[myrow][mycol];
        String defaultRows[][] = new String[myrow][4];
        int i = 1;
        for (ClassRecord temp : classRecordSearch) {
            if (i < myrow) {
                for (int j = 0; j < mycol; j++) {
                    if (j == 0) {
                        myrows[i][j] = "" + temp.getStudentID();
                    } else if (j == 1) {
                        myrows[i][j] = temp.getName();

                    }
                    if (j < mycol - 1) {
                        defaultRows[i][j] = myrows[i][j];
                    }
                }
                i++;
            }
        }
        myinitialCol[2] = "Total Grade";
        myinitialCol[3] = "Final Grade";
        initialTable(jScrollPane1, myTable, myinitialCol, myrows);

        try {
            gradingSearch = gradingSystemService.getGradingSystem(classesG.getClassID());
        } catch (ErrorException ex) {
            Logger.getLogger(classRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

        int entryCount = 0;

        String entryNames[] = new String[gradingSearch.size()];
        String entryName[] = new String[gradingSearch.size()];

        for (GradingSystem temp : gradingSearch) {
            entryNames[entryCount] = checkTheSpace(temp.getEntryName());
            entryName[entryCount] = temp.getEntryName();
            entryCount++;
        }
        for (ClassRecord date : classRecordDate) {
            classRecord.setDate(date.getDate());
            classRecord.setClassID(date.getClassID());
            for (int k = 0; k < entryNames.length; k++) {

                classRecord.setColumnName(entryNames[k]);

                try {
                    classGetValue = classrecordService.getValue(classRecord);
                } catch (ErrorException ex) {
                    Logger.getLogger(classRecord.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //initialize to zero the grades
        myGrade = new int[myrow];
        for (int k = 1; k < myrow; k++) {
            myGrade[k] = 0;
        }

        for (int k = 0; k < entryCount; k++) {

            addColumn(jScrollPane1, myTable, myinitialCol, myrows, myrow, mycol, entryNames[k], entryName[k]);
            addTab(classRecordView, entryNames[k], defaultRows, myrow, entryNames);
        }
        myTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        myTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        myTable.getColumnModel().getColumn(0).setResizable(false);
        myTable.getColumnModel().getColumn(1).setResizable(false);
    }

    public void addTab(JTabbedPane tab, String tabName, String[][] defaultRow, int row, String[] entries) {

        JTable table = new JTable();
        JScrollPane pane = new JScrollPane();
        tab.add(tabName, pane);
        String initial[] = new String[2];
        initialTable(pane, table, initial, defaultRow);


        int dateCount = classRecordDate.size();
        int dateCounter = 0;

        String date[] = new String[dateCount];

        for (ClassRecord getDate : classRecordDate) {
            date[dateCounter] = getDate.getDate();
            dateCounter++;
        }
        int col = 2;

        int count = 0;
        while (count < dateCounter) {

            boolean hasVal = false;
            classGetValue = new ArrayList<>();

            for (ClassRecord getVal : classRecordDate) {
                classRecord.setDate(date[count]);
                classRecord.setColumnName(tabName);
                try {
                    classGetValue = classrecordService.getValue(classRecord);
                } catch (ErrorException ex) {
                    Logger.getLogger(classRecord.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            int resultCounter = 1;
            String perfect = null;
            String resultsVal[] = new String[row];
            for (ClassRecord getResult : classGetValue) {
                if (getResult.getScore() != null) {
                    if (!"0".equals(getResult.getScore())) {
                        hasVal = true;
                    }
                }
                //if(getResult){
                resultsVal[resultCounter] = getResult.getScore();

                perfect = getResult.getPerfectScore();
                resultCounter++;
                // }
            }

            if (hasVal == true) {

                int newcol = col + 1;

                String newCol[] = new String[newcol];
                String newRows[][] = new String[row][newcol];
                newCol[newcol - 1] = date[count];

                for (int i = 0; i < col; i++) {
                    newCol[i] = initial[i];
                }

                for (int i = 0; i < newcol - 1; i++) {
                    newRows[0][i] = defaultRow[0][i];
                }
                for (int i = 1; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        newRows[i][j] = defaultRow[i][j];
                    }
                }

                col = newcol;
                initial = new String[col];
                defaultRow = new String[row][col];

                for (int i = 1; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        defaultRow[i][j] = newRows[i][j];
                    }
                }

                for (int i = 0; i < col; i++) {
                    initial[i] = newCol[i];
                }

                for (int i = 0; i < col; i++) {
                    defaultRow[0][i] = newRows[0][i];
                }

                for (int i = 1; i < row; i++) {
                    defaultRow[i][col - 1] = resultsVal[i];
                }

                defaultRow[0][col - 1] = perfect;
                DefaultTableModel mod = new javax.swing.table.DefaultTableModel(defaultRow, initial) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                table.setModel(mod);
                pane.add(table);
                pane.setViewportView(table);

            }
            count++;
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(75);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(1).setResizable(false);

    }

    public void initialTable(JScrollPane pane, JTable table, String initialCol[], String rows[][]) {
        rows[0][1] = "Perfect Score";
        initialCol[0] = "ID Number";
        initialCol[1] = "Student Name";

        DefaultTableModel mod = new javax.swing.table.DefaultTableModel(rows, initialCol) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(mod);
        pane.add(table);
        pane.setViewportView(table);
    }

    public void addColumn(JScrollPane pane, JTable table, String initialCol[], String rows[][], int row, int col, String name, String name2) {

        int counter = 1;
        String totalScore[] = new String[row];
        Double totalScore2[] = new Double[row];
        String totalPerfectScore = "";
        Double totalPerfectScore2 = 0.0;
        ArrayList<ClassRecord> classGetList = new ArrayList<>();
        ClassRecordServiceInterface classgetTotal = new ClassRecordServiceImplementation();
        ClassServiceInterface classesService = new ClassServiceImplementation();
        Classes classes = new Classes();
        GradingSystemServiceInterface gsService = new GradingSystemServiceImplementation();
        GradingSystem gsPercentage = new GradingSystem();
        int entryPercentage;
        int percentage;

        try {
            classGetList = classgetTotal.getTotals(classesG.getClassID(), name);
            gsPercentage = gsService.getGSfk(name2, classesG.getClassID());
            classes = classesService.getPercentage(classesG.getClassID());
        } catch (ErrorException ex) {
            Logger.getLogger(classRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

        entryPercentage = Integer.parseInt(gsPercentage.getPercentage());
        percentage = classes.getPercentage();

        for (ClassRecord getTotals : classGetList) {
            totalScore[counter] = getTotals.getTotalScore();
            totalScore2[counter] = Double.parseDouble(getTotals.getTotalScore());
            totalPerfectScore = getTotals.getTotalPerfectScore();
            totalPerfectScore2 = Double.parseDouble(getTotals.getTotalPerfectScore());
            System.out.println("dsfsdf "+totalScore2[counter]+"\n\n"+totalPerfectScore2);
            counter++;
        }

        int newcol = col + 1;
        String newCol[] = new String[newcol];
        String newRows[][] = new String[row][newcol];


        for (int i = 0; i < col; i++) {
            newRows[0][i] = myrows[0][i];
        }

        for (int i = 0; i < col; i++) {
            newCol[i] = initialCol[i];
        }
        for (int i = 1; i < row; i++) {
            for (int j = 0; j < col; j++) {
                newRows[i][j] = rows[i][j];
            }
        }

        newCol[newcol - 3] = name + "\n(" + entryPercentage + "%)";
        newCol[newcol - 1] = "Final Grade";
        newCol[newcol - 2] = "Total Grade";


        mycol = newcol;
        myinitialCol = new String[mycol];
        myrows = new String[row][mycol];


        for (int i = 0; i < mycol; i++) {
            myrows[0][i] = newRows[0][i];
        }

        for (int i = 1; i < row; i++) {
            for (int j = 0; j < mycol; j++) {
                myrows[i][j] = newRows[i][j];
            }
        }
        for (int i = 0; i < mycol; i++) {
            myinitialCol[i] = newCol[i];
        }

        myrows[0][mycol - 3] = "" + totalPerfectScore;

        for (int i = 1; i < myrow; i++) {
             String temp;
              System.out.println("dsfsdf "+totalScore2[i]+"\n\n"+totalPerfectScore2);
            DecimalFormat patternFormatter = new DecimalFormat("##0");
            if((totalScore2[i]==null&&totalPerfectScore2==0.0)||(totalScore2[i]!=null&&totalPerfectScore2==0.0)){
                temp=String.valueOf(entryPercentage);
                totalScore[i]="0";
            }else{
                temp = patternFormatter.format((totalScore2[i] / totalPerfectScore2) * entryPercentage);
            }
            myrows[i][mycol - 3] = totalScore[i] + (" (" + temp + "%)");
        }

        for (int i = 1; i < myrow; i++) {
            int tempGrade = myGrade[i];
            if (counter > 1) {
                tempGrade += (totalScore2[i] / totalPerfectScore2) * entryPercentage;

            } else {
                tempGrade += entryPercentage;
            }

            myGrade[i] = tempGrade;
        }

        for (int i = 1; i < myrow; i++) {
            myrows[i][mycol - 1] = "" + finalGrade(myGrade[i], percentage);
        }

        for (int i = 1; i < myrow; i++) {
            myrows[i][mycol - 2] = "" + myGrade[i] + "%";
        }

        DefaultTableModel mod = new javax.swing.table.DefaultTableModel(myrows, myinitialCol) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JPanel panel = new JPanel();
        JScrollPane scrollPane2 = new JScrollPane();

        table.setModel(mod);
        scrollPane2.add(table);
        scrollPane2.setViewportView(table);
        panel.add(scrollPane2);
        pane.add(panel);
        pane.setViewportView(panel);


        int width = 430;
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setMaxWidth(75);
        table.getColumnModel().getColumn(0).setMinWidth(75);
        table.getColumnModel().getColumn(1).setResizable(false);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        table.getColumnModel().getColumn(1).setMinWidth(200);
        for (int i = 2; i < mycol - 2; i++) {
            int size = myinitialCol[i].length() * 8;
            table.getColumnModel().getColumn(i).setResizable(false);
            table.getColumnModel().getColumn(i).setMaxWidth(size);
            table.getColumnModel().getColumn(i).setMinWidth(size);
            width += size;
        }

        table.getColumnModel().getColumn(mycol - 2).setResizable(false);
        table.getColumnModel().getColumn(mycol - 2).setMaxWidth(75);
        table.getColumnModel().getColumn(mycol - 2).setMinWidth(75);
        table.getColumnModel().getColumn(mycol - 1).setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, width, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, (500), Short.MAX_VALUE));

    }

    public double finalGrade(double grade, double passing) {

        double finalG = 0.0;

        double grades[] = new double[10];
        double temp = 1.0;

        grades[9] = 5.0;
        for (int i = 0; i < 9; i++) {
            grades[i] = temp;
            temp += 0.25;
        }

        double rate = (100 - passing) / 9;
        double k[] = new double[9];
        double tempa = passing;
        for (int i = 0; i < 9; i++) {
            k[i] = tempa;
            tempa += rate;
        }


        int b = 8;
        if (grade < passing) {
            finalG = 5.0;
        } else {
            for (int i = 1; i < 10; i++) {
                if (i == 1) {
                    if (grade >= k[i - 1] && grade < k[i]) {
                        finalG = grades[b];
                    }
                } else if (i == 9) {
                    if (grade > k[i - 1] && grade <= 100) {
                        finalG = grades[b];
                    }
                } else {
                    if (grade > k[i - 1] && grade <= k[i]) {
                        finalG = grades[b];
                    }
                }
                b--;
            }
        }
        return finalG;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        classRecordView = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        subjectLabel = new javax.swing.JLabel();
        scheduleLabel = new javax.swing.JLabel();
        instructorLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        sectionLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(48, 130, 193));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - classRcord.jpg"))); // NOI18N

        classRecordView.setBackground(new java.awt.Color(255, 255, 255));
        classRecordView.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        classRecordView.addTab("Summary", jScrollPane1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/printRecord.jpg"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exit.jpg"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DK Crayon Crumble", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Subject      :");

        jLabel2.setFont(new java.awt.Font("DK Crayon Crumble", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Schedule  :");

        jLabel4.setFont(new java.awt.Font("DK Crayon Crumble", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Instructor   :");

        subjectLabel.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        subjectLabel.setForeground(new java.awt.Color(255, 255, 255));
        subjectLabel.setText("Subject");

        scheduleLabel.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        scheduleLabel.setForeground(new java.awt.Color(255, 255, 255));
        scheduleLabel.setText("Time");

        instructorLabel.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        instructorLabel.setForeground(new java.awt.Color(255, 255, 255));
        instructorLabel.setText("Name");

        jLabel5.setFont(new java.awt.Font("DK Crayon Crumble", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Section   :");

        sectionLabel.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        sectionLabel.setForeground(new java.awt.Color(255, 255, 255));
        sectionLabel.setText("section");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(classRecordView, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(53, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(subjectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(instructorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scheduleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 34, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(subjectLabel)
                    .addComponent(jLabel5)
                    .addComponent(sectionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(scheduleLabel))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(instructorLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(classRecordView, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
                
        PrintClassRecord pcr=new PrintClassRecord(parentFrame, true,classesG);
        pcr.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane classRecordView;
    private javax.swing.JLabel instructorLabel;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel scheduleLabel;
    private javax.swing.JLabel sectionLabel;
    private javax.swing.JLabel subjectLabel;
    // End of variables declaration//GEN-END:variables
}
