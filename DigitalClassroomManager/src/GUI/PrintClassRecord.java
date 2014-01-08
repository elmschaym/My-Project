/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PrintClassRecord.java
 *
 * Created on Feb 27, 2013, 2:14:27 PM
 */
package GUI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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
import tools.Printer;

/**
 *
 * @author Acer
 */
public class PrintClassRecord extends javax.swing.JDialog {

    Printer printer;
    private javax.swing.JFrame parentFrame;
    private Classes classesG;
    private ArrayList<ClassRecord> classRecordDate;
    private GradingSystemServiceInterface gradingSystemService;
    private CurrentUser userCurrent;
    DefaultTableModel model;

    /**
     * Creates new form PrintClassRecord
     */
    public PrintClassRecord(java.awt.Frame parent, boolean modal, Classes clas) {
        super(parent, modal);
        initComponents();
        classesG = clas;
        sectionLabel.setText(clas.getSectionID());
        subjectLabel.setText(clas.getSubjectName());
        scheduleLabel.setText(clas.getSchedule());
        prepareName.setText(clas.getInstructorName());
        System.out.println(clas.getInstructorName());
        start();
//        classesG.setClassID(1);
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
        //myinitialCol[2] = "Total Grade";
        myinitialCol[2] = "Final Grade";
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
//            addTab(classRecordView, entryNames[k], defaultRows, myrow, entryNames);
        }
        myTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        myTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        myTable.getColumnModel().getColumn(0).setResizable(false);
        myTable.getColumnModel().getColumn(1).setResizable(false);
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
            System.out.println("dsfsdf " + totalScore2[i] + "\n\n" + totalPerfectScore2);
            DecimalFormat patternFormatter = new DecimalFormat("##0");
            if ((totalScore2[i] == null && totalPerfectScore2 == 0.0) || (totalScore2[i] != null && totalPerfectScore2 == 0.0)) {
                temp = String.valueOf(entryPercentage);
                totalScore[i] = "0";
            } else {
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

        printablePanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        prepareName = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        subjectLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        sectionLabel = new javax.swing.JLabel();
        scheduleLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        printablePanel.setBackground(new java.awt.Color(255, 255, 255));
        printablePanel.setPreferredSize(new java.awt.Dimension(590, 722));
        printablePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel2.setText("College of Information Technology");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jLabel4.setText("Instructor :");

        prepareName.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        prepareName.setText("Sample Name Sample");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jLabel6.setText("Subject     :");

        subjectLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        subjectLabel.setText("Sample Name Sample");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jLabel7.setText("Section      :");

        sectionLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        sectionLabel.setText("Sample ");

        scheduleLabel.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        scheduleLabel.setText("Sample Name Sample");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        jLabel8.setText("Schedule  :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prepareName, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subjectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scheduleLabel))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(62, 62, 62))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(176, 176, 176)
                .addComponent(jLabel2)
                .addContainerGap(201, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(sectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(scheduleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(subjectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(prepareName, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        printablePanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 550, 70));
        printablePanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 1250, 770));

        getContentPane().add(printablePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1360, 910));

        jMenu1.setText("Print");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
// TODO add your handling code here:
    ActionEvent evtNew = null;
    try {
        printer = new Printer(printablePanel);
        printer.setDocumentType("Grade Card");
        printer.actionPerformed(evtNew);
    } catch (Exception e) {
        e.toString();
    } finally {
        this.setVisible(false);
    }
}//GEN-LAST:event_jMenu1MouseClicked
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel prepareName;
    private javax.swing.JPanel printablePanel;
    private javax.swing.JLabel scheduleLabel;
    private javax.swing.JLabel sectionLabel;
    private javax.swing.JLabel subjectLabel;
    // End of variables declaration//GEN-END:variables
}
