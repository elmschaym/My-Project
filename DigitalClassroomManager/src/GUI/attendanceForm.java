/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.JFrame;
import tools.Transparent;

import com.mysql.jdbc.Blob;
import dataaccesobject.implementations.StudentDaoImplementation;
import dataaccesobject.interfaces.StudentDaoInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.ClassRecord;
import model.Classes;
import model.Student;
import service.implementations.ClassRecordServiceImplementation;
import service.implementations.StudentServiceImplementation;
import service.interfaces.ClassRecordServiceInterface;
import service.interfaces.StudentServiceInterface;
import tools.ErrorException;
import tools.FormatTool;

/**
 *
 * @author sam
 */
public class attendanceForm extends javax.swing.JDialog {

    File file;
    private javax.swing.JFrame parentFrame;
    private Date dates = new Date();
    /**
     * Creates new form attendanceForm
     */
    String dateNow;
    JLabel[] label;
    JLabel label5;
    JLabel[] label2;
    JLabel[] label3;
    JLabel[] label4;
    String sd;
    JTextField[] nameField;
    Object object;
    String[] list2;
    String[] list;
    JPanel[] panelName;
    String idNumber;
    JLabel background = new JLabel();
    FormatTool formatTool = new FormatTool();
    int MAX = 0;
    boolean chckClick = false;
    private Classes classesFrom;
    ClassRecordServiceInterface classService;
    ClassRecord clasrecord;

    public attendanceForm(java.awt.Frame parent, boolean modal, Classes clas) {
        super(parent, modal);
        Transparent trans = new Transparent();
        trans.TransparentDForm(this);
        initComponents();
        this.setSize(900, 511);
        classesFrom = clas;
        this.setLocationRelativeTo(null);
        jScrollPane1.setHorizontalScrollBar(null);
        sectionLabel.setText(clas.getSectionID());
        subjectLabel.setText(clas.getSubjectName());
        dateNow = formatTool.convertSQLDateToDateString(formatTool.conveter(dates.toLocaleString()));
        dateLabel.setText(dateNow);
        runAttendance();
        this.parentFrame = (JFrame) parent;
    }

    public void brows(JLabel labels) {

        JFileChooser fc = new JFileChooser();
        file = fc.getSelectedFile();
        //  StringTokenizer token = null;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & PNG Images", "jpg", "png");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            file = fc.getSelectedFile();
            // bmp, gif, jpg, png files okay

            Image image = Toolkit.getDefaultToolkit().getImage(file.toString()).getScaledInstance(150, 150, 100);
            object = new ImageIcon(image);
            labels.setIcon((Icon) object);
        } else {
            System.out.println("Cannot open file!");
        }

    }

    public void runAttendance() {
        StudentServiceInterface service = new StudentServiceImplementation();
        Student s = new Student();
        ArrayList<Student> studentSearch = new ArrayList<Student>();
        try {
            studentSearch = service.getStudents(classesFrom.getClassID());
        } catch (ErrorException ex) {
            ex.printStackTrace();
        }
        MAX = studentSearch.size();
        if (studentSearch.isEmpty()) {
            return;
        }
        Student st;

        int count = 0;
        int x = 18;
        int y = 10;
        int i = 0;
        label = new JLabel[MAX];
        label2 = new JLabel[MAX];
        label3 = new JLabel[MAX];
        label4 = new JLabel[MAX];
        list2 = new String[MAX];
        list = new String[MAX];
        panelName = new JPanel[MAX];

        for (Student temp : studentSearch) {
            //st=studentSearch.get(count++);
            Object object;
            object = temp.getImage();
            label[i] = new JLabel();
            label2[i] = new JLabel();
            label3[i] = new JLabel();
            label4[i] = new JLabel();
            panelName[i] = new JPanel();
            panelName[i].setSize(100, 30);
            attendPane.add(panelName[i]);
            attendPane.add(label[i]);
            // attendPane.add(label2[i]);


            label[i].setBounds(x, y, 150, 150);
            label[i].setEnabled(false);
            if (temp.getImage() != null) {
                if (object instanceof com.mysql.jdbc.Blob) {

                    label[i].setIcon(new javax.swing.ImageIcon(formatTool.convertToActualSizeImage((Blob) object, 3)));
                }
            } else {
                label[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/attIcon150.jpg")));
            }

            panelName[i].setBounds(x, y + 155, 150, 30);

            panelName[i].add(label2[i]);
            panelName[i].add(label3[i]);
            panelName[i].add(label4[i]);
            panelName[i].setBackground(new java.awt.Color(6, 49, 237));
            label2[i].setFont(new java.awt.Font("Maiandra GD", Font.BOLD, 13));
            label2[i].setForeground(new java.awt.Color(255, 255, 255));
            label3[i].setForeground(Color.ORANGE);
            label3[i].setBounds(80, 0, 20, 20);
            label3[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/ex.png")));
            label4[i].setForeground(Color.RED);
            label4[i].setBounds(80, 0, 20, 20);
            label4[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/check.png")));
            label4[i].setVisible(false);

            label2[i].setText(String.valueOf(temp.getIdNumber()));
            label[i].setToolTipText(temp.getCompleteName());


            i++;
            x += 165;
            if (x >= 830) {
                y += 189;
                x = 18;
            }


        }

        for (int n = 0; n < i; n++) {
            final int m = n;
            list2[m] = label2[m].getText();
            list[m] = "absent";
            label[n].addMouseListener(new MouseAdapter() {
                String status = "";

                public void mouseClicked(MouseEvent evt) {

                    int Key = evt.getModifiers();



                    if (Key == MouseEvent.BUTTON1_MASK) {
                        idNumber = label2[m].getText();
                        label5 = label[m];
                        if (label[m].isEnabled()) {
                            label[m].setEnabled(false);
                            label2[m].setForeground(Color.WHITE);
                            list2[m] = label2[m].getText();
                            list[m] = "absent";
                            label4[m].setVisible(false);
                            label3[m].setVisible(true);
                        } else {
                            label[m].setEnabled(true);
                            label2[m].setForeground(Color.GREEN);
                            list2[m] = label2[m].getText();
                            label4[m].setVisible(true);
                            label3[m].setVisible(false);
                            list[m] = "present";
                        }
                    }
                    if (Key == MouseEvent.BUTTON3_MASK) {
                    }
                }
            });


        }


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel62 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        attendPane = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        cancel = new javax.swing.JButton();
        ok = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        subjectLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sectionLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel62.setBackground(new java.awt.Color(48, 130, 193));
        jPanel62.setPreferredSize(new java.awt.Dimension(900, 511));
        jPanel62.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Heading - AttendanceForm.jpg"))); // NOI18N
        jPanel62.add(jLabel1);
        jLabel1.setBounds(0, 0, 400, 50);

        attendPane.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout attendPaneLayout = new javax.swing.GroupLayout(attendPane);
        attendPane.setLayout(attendPaneLayout);
        attendPaneLayout.setHorizontalGroup(
            attendPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        attendPaneLayout.setVerticalGroup(
            attendPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1700, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(attendPane);

        jPanel62.add(jScrollPane1);
        jScrollPane1.setBounds(20, 50, 870, 380);

        jLabel32.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("*Images in gray color indicates the student is absent.");
        jPanel62.add(jLabel32);
        jLabel32.setBounds(220, 480, 300, 14);

        cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.jpg"))); // NOI18N
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        jPanel62.add(cancel);
        cancel.setBounds(770, 450, 100, 30);

        ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/submit.jpg"))); // NOI18N
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        jPanel62.add(ok);
        ok.setBounds(640, 450, 123, 30);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/aaaa.jpg"))); // NOI18N
        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel62.add(jButton1);
        jButton1.setBounds(20, 450, 190, 30);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Subject:");
        jPanel62.add(jLabel2);
        jLabel2.setBounds(420, 20, 50, 14);

        subjectLabel.setForeground(new java.awt.Color(255, 255, 255));
        subjectLabel.setText("jLabel3");
        jPanel62.add(subjectLabel);
        subjectLabel.setBounds(470, 20, 110, 14);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Section:");
        jPanel62.add(jLabel3);
        jLabel3.setBounds(600, 20, 50, 14);

        sectionLabel.setForeground(new java.awt.Color(255, 255, 255));
        sectionLabel.setText("jLabel3");
        jPanel62.add(sectionLabel);
        sectionLabel.setBounds(650, 20, 110, 14);

        dateLabel.setForeground(new java.awt.Color(255, 255, 255));
        dateLabel.setText("jLabel4");
        jPanel62.add(dateLabel);
        dateLabel.setBounds(760, 20, 100, 14);

        jLabel34.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("ABSENT.");
        jPanel62.add(jLabel34);
        jLabel34.setBounds(380, 460, 60, 14);

        jLabel35.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Click on again to mark it back as");
        jPanel62.add(jLabel35);
        jLabel35.setBounds(220, 460, 160, 13);

        jLabel36.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Click on each of the pictures to mark that the student is");
        jPanel62.add(jLabel36);
        jLabel36.setBounds(220, 440, 270, 14);

        jLabel37.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("PRESENT.");
        jPanel62.add(jLabel37);
        jLabel37.setBounds(490, 440, 60, 14);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel62, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(this, "Submit Attendance?\nThis will be recorded in your Classrecord!", "Confim", JOptionPane.YES_NO_OPTION);
        if (confirm == 0) {
            dateNow = formatTool.convertSQLDateToDateString(formatTool.conveter(dates.toLocaleString()));
            for (int i = 0; i < MAX; i++) {
                classService = new ClassRecordServiceImplementation();
                clasrecord = new ClassRecord();
                ClassRecordServiceInterface attendService = new ClassRecordServiceImplementation();
                ArrayList<ClassRecord> at = new ArrayList<>();
                try {
                    clasrecord = classService.getClassRecord(classesFrom.getClassID(), dateNow, "Attendance", Integer.parseInt(list2[i]));

                } catch (ErrorException ex) {
                    ex.printStackTrace();
                }

                if (!clasrecord.isEmpty()) {
                    clasrecord.setClassID(clasrecord.getClassID());
                    clasrecord.setStudentID(clasrecord.getStudentID());
                    clasrecord.setDate(clasrecord.getDate());
                    clasrecord.setScore(list[i]);
                    clasrecord.setColumnName("Attendance");
                    classesFrom.setClassID(clasrecord.getClassID());

                    try {
                        classService.updateAttendance(clasrecord);
                    } catch (ErrorException ex) {
                        ex.printStackTrace();
                    }
                } else {

                    classService = new ClassRecordServiceImplementation();
                    clasrecord = new ClassRecord();
                    clasrecord.setClassID(classesFrom.getClassID());
                    clasrecord.setColumnName("Attendance");
                    clasrecord.setDate(dateNow);
                    clasrecord.setScore(list[i]);
                    clasrecord.setStudentID(Integer.parseInt(list2[i]));

                    try {
                        classService.insertAttendance(clasrecord);
                    } catch (ErrorException ex) {
                        Logger.getLogger(attendanceForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    at = attendService.getAttendance(classesFrom.getClassID(), Integer.parseInt(list2[i]));
                } catch (ErrorException ex) {
                    Logger.getLogger(attendanceForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                int count = 0;
                int totalPresent = 0;
                for (ClassRecord temp : at) {

                    if (temp.getScore() != null) {
                        if (!temp.getScore().equals("")) {
                            count++;
                        }
                        if (temp.getScore().equals("present")) {
                            totalPresent++;
                        }
                    }
                }
                clasrecord.setPerfectScore(String.valueOf(count));
                clasrecord.setTotalScore(String.valueOf(totalPresent));
                try {
                    classService.updateAttendancePerfect(clasrecord);
                } catch (ErrorException ex) {
                    Logger.getLogger(attendanceForm.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            this.dispose();
            JOptionPane.showMessageDialog(this, "Attendance has been recorded!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_okActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    brows(label5);
    StudentDaoInterface studentService = new StudentDaoImplementation();
    Student student = new Student();

    object = file;
    student.setIdNumber(Integer.parseInt(idNumber));
    student.setImage(object);
    try {
        studentService.editPhoto(student);
    } catch (ErrorException ex) {
        ex.printStackTrace();
    }

}//GEN-LAST:event_jButton1ActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel attendPane;
    private javax.swing.JButton cancel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton ok;
    private javax.swing.JLabel sectionLabel;
    private javax.swing.JLabel subjectLabel;
    // End of variables declaration//GEN-END:variables
}
