/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ClassRecord;
import model.Classes;
import model.CurrentUser;
import model.GradingSystem;
import model.Student;
import service.implementations.ClassRecordServiceImplementation;
import service.implementations.ClassServiceImplementation;
import service.implementations.GradingSystemServiceImplementation;
import service.implementations.StudentServiceImplementation;
import service.interfaces.ClassRecordServiceInterface;
import service.interfaces.ClassServiceInterface;
import service.interfaces.GradingSystemServiceInterface;
import service.interfaces.StudentServiceInterface;
import tools.ErrorException;
import tools.FormatTool;
import tools.Transparent;

/**
 *
 * @author sam
 */
public class updateRecord extends javax.swing.JDialog {

    private GradingSystemServiceInterface gradingService;
    private GradingSystem gradingSys;
    private javax.swing.JFrame parentFrame;
    private CurrentUser userCurrent;
    private StudentServiceInterface service;
    private Classes getClass;
    private Date dates = new Date();
    private FormatTool formatTool = new FormatTool();
    private ClassRecordServiceInterface classrecordSrvice;
    private ClassRecord classrecord;

    public updateRecord(java.awt.Frame parent, boolean modal, CurrentUser user, Classes clas) {
        super(parent, modal);
        Transparent trans = new Transparent();
        trans.TransparentDForm(this);
        initComponents();
        userCurrent = user;
        getClass = clas;
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.parentFrame = (JFrame) parent;
        updateTable();
        updateEntryCombo();
        perfectScore.requestFocus();
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

    public void updateEntryCombo() {
        gradingService = new GradingSystemServiceImplementation();
        ArrayList<GradingSystem> gradingSearch = new ArrayList<>();
        try {
            gradingSearch = gradingService.getGradingSystem(userCurrent.getClassID());
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }
        for (GradingSystem gsD : gradingSearch) {
            if (!gsD.getEntryName().equalsIgnoreCase("Attendance")) {
                entryBox.addItem(gsD.getEntryName());
            }
        }
    }

    public void updateTable() {

        StudentServiceInterface service = new StudentServiceImplementation();
        Student s = new Student();
        ArrayList<Student> studentSearch = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) this.studentTable.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        try {
            studentSearch = service.getStudents(userCurrent.getClassID());
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }
        int MAX = studentSearch.size();
        if (studentSearch.isEmpty()) {
            System.out.print("walang laman");
            return;
        }

        for (Student temp : studentSearch) {
            model.addRow(new Object[]{temp.getIdNumber(), temp.getCompleteName()});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        studentTable = new javax.swing.JTable();
        perfectScore = new javax.swing.JTextField();
        entryBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(48, 130, 193));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/updateRec.jpg"))); // NOI18N

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/submit.jpg"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.jpg"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        studentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Integer(1), null, null},
                { new Integer(2), null, null},
                { new Integer(3), null, null},
                { new Integer(4), null, null},
                { new Integer(5), null, null},
                { new Integer(6), null, null},
                { new Integer(7), null, null},
                { new Integer(8), null, null},
                { new Integer(9), null, null},
                { new Integer(10), null, null},
                { new Integer(11), null, null},
                { new Integer(12), null, null},
                { new Integer(13), null, null},
                { new Integer(14), null, null},
                { new Integer(15), null, null},
                { new Integer(16), null, null},
                { new Integer(17), null, null},
                { new Integer(18), null, null},
                { new Integer(19), null, null},
                { new Integer(20), null, null},
                { new Integer(21), null, null},
                { new Integer(22), null, null},
                { new Integer(23), null, null},
                { new Integer(24), null, null},
                { new Integer(25), null, null},
                { new Integer(26), null, null},
                { new Integer(27), null, null},
                { new Integer(28), null, null},
                { new Integer(29), null, null},
                {null, null, null}
            },
            new String [] {
                "No.", "Student's Name", "Points"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(studentTable);
        studentTable.getColumnModel().getColumn(0).setMinWidth(80);
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(0).setMaxWidth(80);
        studentTable.getColumnModel().getColumn(1).setMinWidth(240);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(240);
        studentTable.getColumnModel().getColumn(1).setMaxWidth(240);
        studentTable.getColumnModel().getColumn(2).setMinWidth(65);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(65);
        studentTable.getColumnModel().getColumn(2).setMaxWidth(65);

        perfectScore.setFont(new java.awt.Font("Maiandra GD", 0, 15)); // NOI18N
        perfectScore.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        perfectScore.setText("0");
        perfectScore.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                perfectScoreFocusGained(evt);
            }
        });

        entryBox.setFont(new java.awt.Font("DK Crayon Crumble", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("DK Crayon Crumble", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Perfect Score");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(perfectScore, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(entryBox, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(entryBox, 0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(perfectScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 420, 580);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        DefaultTableModel model = (DefaultTableModel) this.studentTable.getModel();
        String dateNow = formatTool.convertSQLDateToDateString(formatTool.conveter(dates.toLocaleString()));
        int size = model.getRowCount();
        int pScore = Integer.parseInt(perfectScore.getText());
        boolean checkPercntage = false;

        for (int i = 0; i < size; i++) {
            if (pScore < Integer.parseInt(model.getValueAt(i, 2).toString()) || Integer.parseInt(model.getValueAt(i, 2).toString()) < 0) {
                JOptionPane.showMessageDialog(null, Integer.parseInt(model.getValueAt(i, 0).toString()) + "Please type in valid scores!", "Error", JOptionPane.ERROR_MESSAGE);
                checkPercntage = true;
                break;
            }

        }
        if (!checkPercntage) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit this result?", "Confim", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                for (int i = 0; i < size; i++) {
                    Double totals = 0.0;
                    Double totalsPerfect = 0.0;
                    classrecordSrvice = new ClassRecordServiceImplementation();
                    classrecord = new ClassRecord();
                    ClassRecord c = new ClassRecord();
                    ClassRecordServiceInterface clasDSrvice = new ClassRecordServiceImplementation();
                    ArrayList<ClassRecord> clasSearch = new ArrayList<>();
                    classrecord.setClassID(userCurrent.getClassID());
                    classrecord.setColumnName(checkTheSpace(entryBox.getSelectedItem().toString()));
                    classrecord.setPerfectScore(perfectScore.getText());
                    classrecord.setStudentID(Integer.parseInt(model.getValueAt(i, 0).toString()));
                    classrecord.setDate(dateNow);
                    try {
                        c = classrecordSrvice.getClassRecord(userCurrent.getClassID(), dateNow, checkTheSpace(entryBox.getSelectedItem().toString()), Integer.parseInt(model.getValueAt(i, 0).toString()));
                    } catch (ErrorException ex) {
                        ex.printStackTrace();
                    }

                    classrecord.setTotalScore(String.valueOf(totals));
                    if (model.getValueAt(i, 2).toString().equals(null)) {
                        classrecord.setScore("0");
                    } else {
                        classrecord.setScore(model.getValueAt(i, 2).toString());
                    }
                    
                    if (!c.isEmpty()) {

                        try {
                            classrecordSrvice.updateScore(classrecord);
                        } catch (ErrorException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        try {
                            System.out.println("Empty lng buddy");
                            classrecordSrvice.insertScore(classrecord);
                        } catch (ErrorException ex) {
                            ex.printStackTrace();
                        }
                    }
                    try {
                        clasSearch = clasDSrvice.getValue(userCurrent.getClassID(), Integer.parseInt(model.getValueAt(i, 0).toString()), checkTheSpace(entryBox.getSelectedItem().toString()));
                    } catch (ErrorException ex) {
                        ex.printStackTrace();
                    }
                    for (ClassRecord cl : clasSearch) {
                        totals += Double.parseDouble(cl.getScore());
                        totalsPerfect += Double.parseDouble(cl.getPerfectScore());
                    }
                    classrecord.setTotalScore(String.valueOf(totals));
                    classrecord.setTotalPerfectScore(String.valueOf(totalsPerfect));
                    try {
                        classrecordSrvice.updateTotal(classrecord);
                    } catch (ErrorException ex) {
                        ex.printStackTrace();
                    }

                }
                this.dispose();
                JOptionPane.showMessageDialog(this, "Records successfully added to your classrecord!", "Successful", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void perfectScoreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_perfectScoreFocusGained
        // TODO add your handling code here:
        perfectScore.setText(null);
    }//GEN-LAST:event_perfectScoreFocusGained
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox entryBox;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField perfectScore;
    private javax.swing.JTable studentTable;
    // End of variables declaration//GEN-END:variables
}
