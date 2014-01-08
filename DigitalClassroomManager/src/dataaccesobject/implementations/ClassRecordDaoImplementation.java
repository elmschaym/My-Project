/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.DBManager;
import dataaccesobject.interfaces.ClassRecordDaoInterface;
import java.util.ArrayList;
import model.Classes;
import model.GradingSystem;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClassRecord;
import model.Student;
import model.User;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class ClassRecordDaoImplementation implements ClassRecordDaoInterface {

    private DBManager manager = null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;

    @Override
    public void addColumn(ClassRecord classrecord) throws ErrorException {
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "ALTER TABLE classrecord ADD "
                + "column " + classrecord.getColumnName() + " DOUBLE DEFAULT 0, ADD "
                + "column Perfect" + classrecord.getColumnName() + " DOUBLE DEFAULT 0 , "
                + "ADD column Total" + classrecord.getColumnName() + " DOUBLE DEFAULT 0, "
                + "ADD column TotalPerfect" + classrecord.getColumnName() + " DOUBLE DEFAULT 0 ";
        ResultSet resultSet = null;
        try {

            if (manager == null) {

                manager = DBManagerImplementation.getInstance();

            }
            connection = manager.getConnection();
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }



            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
                System.out.print("sending data");
            }
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addColumn" + e.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }

    }

    @Override
    public void createTable(ClassRecord classrecord) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "CREATE TABLE " + classrecord.getTableName() + ""
                + "(classID INTEGER, studentID INTEGER,"
                + "CONSTRAINT fk_classID FOREIGN KEY (classID) REFERENCES "
                + "classes(classID), CONSTRAINT fk_student FOREIGN KEY "
                + "(studentID) REFERENCES student(studentID) ";
        ResultSet resultSet = null;

        try {

            if (manager == null) {

                manager = DBManagerImplementation.getInstance();

            }
            connection = manager.getConnection();
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }


            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
                System.out.print("sending data");
            }
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in createTable" + e.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public void addStudentClassRecord(ClassRecord classrecord) throws ErrorException {
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "INSERT INTO classrecord (classID,studentID,date) "
                + "VALUES(?,?,?)";
        ResultSet resultSet = null;
        try {

            if (manager == null) {

                manager = DBManagerImplementation.getInstance();

            }
            connection = manager.getConnection();
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }

            //int unitNum=subject.getUnits();

            pStatement.setInt(2, classrecord.getStudentID());
            pStatement.setInt(1, classrecord.getClassID());
            pStatement.setString(3, classrecord.getDate());
            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
                System.out.print("sending data");
            }
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addStudentCLassrecord" + e.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public ArrayList<ClassRecord> getValue(ClassRecord classrecord) throws ErrorException {
        Connection connection = null;
        Statement statement = null;

        String sql = "SELECT  studentID, " + classrecord.getColumnName() + ", Perfect"+ classrecord.getColumnName() +"  FROM  classrecord "
                + " WHERE classID = " + classrecord.getClassID() + " AND date = '" + classrecord.getDate() + "' ORDER BY studentID";

        ResultSet resultSet = null;
        ArrayList<ClassRecord> classSearch = new ArrayList<>();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
               // if (!resultSet.getString("Perfect" + classrecord.getColumnName()).equals("PerfectAttendance")) {
                    String score = resultSet.getString(classrecord.getColumnName());
                    String perfectScore = resultSet.getString("Perfect" + classrecord.getColumnName());

                    ClassRecord clasNew = new ClassRecord();

                    clasNew.setScore(score);
                    clasNew.setPerfectScore(perfectScore);

                    classSearch.add(clasNew);
                }
          //  }
        } catch (SQLException ex) {
            System.out.println("Error in getClassRecords" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return classSearch;

    }

    @Override
    public void insertScore(ClassRecord classrecord) throws ErrorException {
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "INSERT INTO classrecord (classID, studentID, date," + classrecord.getColumnName() + ""
                + ",Perfect" + classrecord.getColumnName() + ", Total" + classrecord.getColumnName() + ") "
                + "VALUES(?,?,?,?,?,?)";
        ResultSet resultSet = null;

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();
            }
            connection = manager.getConnection();
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }

            //int unitNum=subject.getUnits();

            pStatement.setInt(1, classrecord.getClassID());
            pStatement.setInt(2, classrecord.getStudentID());
            pStatement.setString(3, classrecord.getDate());
            pStatement.setString(4, classrecord.getScore());
            pStatement.setString(5, classrecord.getPerfectScore());
            pStatement.setString(6, classrecord.getTotalScore());
            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
                System.out.print("sending data");
            }
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in insertScore" + e.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public ArrayList<ClassRecord> getClassRecord(ClassRecord classrecord) throws ErrorException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT DISTINCT r.classID, r.studentID, s.name FROM student s,"
                + " classes c, classrecord r WHERE  s.studentID = r.studentID"
                + " AND c.classID = r.classID AND r.classID=" + classrecord.getClassID()+" ORDER BY r.studentID";
        ResultSet resultSet = null;
        ArrayList<ClassRecord> classRecord = new ArrayList<ClassRecord>();
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                int classid = resultSet.getInt("classID");
                int studentid = resultSet.getInt("studentID");
                String studentName = resultSet.getString("name");

                ClassRecord clasNew = new ClassRecord();

                clasNew.setClassID(classid);
                clasNew.setName(studentName);
                clasNew.setStudentID(studentid);

                classRecord.add(clasNew);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getClassRecords" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return classRecord;
    }

    @Override
    public ClassRecord getClassRecord(int clasid, String date, String name, int studenid) throws ErrorException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT r.date, r.classID, r.studentID,"
                + " r." + name + " FROM student s,"
                + " classes c, classrecord r WHERE r.studentID = " + studenid + " "
                + " AND r.classID = " + clasid + "  AND r.date = '" + date + "' ORDER BY r.studentID";
        ResultSet resultSet = null;
        ClassRecord clasNew = new ClassRecord();
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {

                int classid = resultSet.getInt("classID");
                int studentid = resultSet.getInt("studentID");
                String names = resultSet.getString(name);
                String dates = resultSet.getString("date");
                //  String perfectscore=resultSet.getString("Perfect"+classrecord.getColumnName());
                if (date.isEmpty()) {
                    clasNew.setDateempty(true);
                } else {
                    clasNew.setDateempty(false);
                }


                clasNew.setStudentID(studentid);
                clasNew.setClassID(classid);
                clasNew.setStudentID(studentid);
                clasNew.setColumnName(names);
                clasNew.setDate(dates);
                clasNew.setEmpty(false);

            } else {
                clasNew.setEmpty(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage().toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return clasNew;
    }

    @Override
    public void updateAttendance(ClassRecord classrecord) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE classrecord SET Attendance=?"
                + " WHERE classID = " + classrecord.getClassID() + " AND studentID = " + classrecord.getStudentID() + " AND date = '" + classrecord.getDate() + "'";
        ResultSet resultSet = null;

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            //pStatement = connection.prepareStatement(sql);
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }

            //pStatement.setInt(1, user.getUserID());
            pStatement.setString(1, classrecord.getScore());

            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
            }

        } catch (SQLException ex) {
            System.err.println("Error in updateGrading" + ex.toString());
        } catch (Exception ex) {
            System.err.println("Error updateGrading " + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public void updateAttendanceNoDate(ClassRecord classrecord) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE classrecord SET  date=?"
                + " WHERE classID = " + classrecord.getClassID() + " AND studentID = " + classrecord.getStudentID();
        ResultSet resultSet = null;
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            //pStatement = connection.prepareStatement(sql);
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }

            //pStatement.setInt(1, user.getUserID());
            //pStatement.setString(1, classrecord.getScore());
            pStatement.setString(1, classrecord.getDate());



            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
            }

        } catch (SQLException ex) {
            System.err.println("Error in updateNoDate" + ex.toString());
        } catch (Exception ex) {
            System.err.println("Error updateNoDate " + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public ClassRecord getClassRecord(int clasid, int studentid) throws ErrorException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT  classID, studentID FROM classrecord WHERE studentID= " + studentid + " AND classID =" + clasid+" ORDER BY studentID";
        ResultSet resultSet = null;
        ClassRecord clasNew = new ClassRecord();
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {

                int classid = resultSet.getInt("classID");
                int studID = resultSet.getInt("studentID");
                //  String perfectscore=resultSet.getString("Perfect"+classrecord.getColumnName());



                clasNew.setStudentID(studID);
                clasNew.setClassID(classid);
                clasNew.setEmpty(false);

            } else {
                clasNew.setEmpty(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage().toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return clasNew;
    }

    @Override
    public ArrayList<ClassRecord> getClassDate(ClassRecord classrecord) throws ErrorException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT DISTINCT date, classID"
                + " FROM classrecord  WHERE classID =" + classrecord.getClassID();
        ResultSet resultSet = null;
        ArrayList<ClassRecord> classSearch = new ArrayList<>();
        try {
            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                String date = resultSet.getString("date");
                int classID = resultSet.getInt("classID");
                ClassRecord clasNew = new ClassRecord();
                clasNew.setDate(date);
                clasNew.setClassID(classID);

                classSearch.add(clasNew);

            }
        } catch (SQLException ex) {
            System.out.println("Error in getClassDate" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return classSearch;
    }

    @Override
    public void updateScore(ClassRecord classrecord) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE classrecord SET " + classrecord.getColumnName() + "=?, Perfect" + classrecord.getColumnName() + "=?,"
                + " Total" + classrecord.getColumnName() + "=? WHERE date= '" + classrecord.getDate() + "' AND "
                + "classID=" + classrecord.getClassID() + " AND studentID=" + classrecord.getStudentID();
        ResultSet resultSet = null;
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            pStatement = connection.prepareStatement(sql);


            pStatement.setString(1, classrecord.getScore());
            pStatement.setString(2, classrecord.getPerfectScore());
            pStatement.setString(3, classrecord.getTotalScore());



            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
            }

        } catch (SQLException ex) {
            System.err.println("Error in updateScore" + ex.toString());
        } catch (Exception ex) {
            System.err.println("Error in updateScore" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public ClassRecord getClassRecord(int clasid, String date, String name) throws ErrorException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT DISTINCT r.date, r.classID, r.studentID,"
                + " r." + name + " FROM student s,"
                + " classes c, classrecord r WHERE r.classID = " + clasid + "  AND r.date = '" + date + "' ORDER BY r.studentID";
        ResultSet resultSet = null;
        ClassRecord clasNew = new ClassRecord();
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {

                int classid = resultSet.getInt("classID");
                int studentid = resultSet.getInt("studentID");
                String names = resultSet.getString(name);
                String dates = resultSet.getString("date");
                //  String perfectscore=resultSet.getString("Perfect"+classrecord.getColumnName());



                clasNew.setClassID(classid);
                clasNew.setStudentID(studentid);
                clasNew.setColumnName(names);
                clasNew.setDate(dates);
                clasNew.setEmpty(false);

            } else {
                clasNew.setEmpty(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage().toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return clasNew;
    }

    @Override
    public void insertAttendance(ClassRecord classrecord) throws ErrorException {
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "INSERT INTO classrecord (classID, studentID, date," + classrecord.getColumnName() + ") "
                + "VALUES(?,?,?,?)";
        ResultSet resultSet = null;

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();
            }
            connection = manager.getConnection();
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }

            //int unitNum=subject.getUnits();

            pStatement.setInt(1, classrecord.getClassID());
            pStatement.setInt(2, classrecord.getStudentID());
            pStatement.setString(3, classrecord.getDate());
            pStatement.setString(4, classrecord.getScore());
            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
                System.out.print("sending data");
            }
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in insertScore" + e.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public ArrayList<ClassRecord> getValue(int classid, int studenid, String name) throws ErrorException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT  studentID, " + name + ", Perfect"+ name + " FROM  classrecord "
                + " WHERE classID = " + classid + " AND studentID = " + studenid +" ORDER BY studentID";
        ResultSet resultSet = null;
        ArrayList<ClassRecord> classSearch = new ArrayList<>();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                String score = resultSet.getString(name);
                String perfectScore = resultSet.getString("Perfect" + name);

                ClassRecord clasNew = new ClassRecord();

                // clasNew.setPerfectScore(perfectscore);

                clasNew.setScore(score);
                clasNew.setPerfectScore(perfectScore);

                classSearch.add(clasNew);

            }
        } catch (SQLException ex) {
            System.out.println("Error in getClassValue" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return classSearch;
    }

    @Override
    public void updateTotal(ClassRecord classrecord) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE classrecord SET Total" + classrecord.getColumnName() + "=?, TotalPerfect" + classrecord.getColumnName() + "=?"
                + " WHERE classID=" + classrecord.getClassID() + " AND "
                + "studentID=" + classrecord.getStudentID();
        ResultSet resultSet = null;
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            pStatement = connection.prepareStatement(sql);


            pStatement.setString(1, classrecord.getTotalScore());
            pStatement.setString(2, classrecord.getTotalPerfectScore());



            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
            }

        } catch (SQLException ex) {
            System.err.println("Error in updateTotal" + ex.toString());
        } catch (Exception ex) {
            System.err.println("Error in updateTotal" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }

    @Override
    public ArrayList<ClassRecord> getTotals(int classid, String columnName) throws ErrorException {
        Connection connection = null;
        Statement statement = null;
        String sql = "SELECT DISTINCT studentID, Total" + columnName + ", TotalPerfect" + columnName + " FROM  classrecord "
                + " WHERE classID = " + classid +" ORDER BY studentID";
        ResultSet resultSet = null;
        ArrayList<ClassRecord> classSearch = new ArrayList<>();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                if (resultSet.getDouble("Total" + columnName) != 0 || resultSet.getDouble("TotalPerfect" + columnName) != 0) {
                    String tscore = resultSet.getString("Total" + columnName);
                    String tperfectScore = resultSet.getString("TotalPerfect" + columnName);

                    ClassRecord clasNew = new ClassRecord();

                    // clasNew.setPerfectScore(perfectscore);

                    clasNew.setTotalScore(tscore);
                    clasNew.setTotalPerfectScore(tperfectScore);

                    classSearch.add(clasNew);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getClassTotals" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return classSearch;
    }

    @Override
    public ArrayList<ClassRecord> getAttendance(int classid, int StudentID) throws ErrorException {
       Connection connection = null;
        Statement statement = null;
        String sql = "SELECT Attendance, studentID FROM classrecord WHERE classID="+classid+" AND studentID="+StudentID+" ORDER BY studentID";
        ResultSet resultSet = null;
        ArrayList<ClassRecord> classSearch = new ArrayList<>();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                
                String score = resultSet.getString("Attendance");
//                if(!score.equals("")){

                ClassRecord clasNew = new ClassRecord();

                // clasNew.setPerfectScore(perfectscore);

                clasNew.setScore(score);

                classSearch.add(clasNew);
            //    }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAttendNCE" + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return classSearch;
    }

    @Override
    public void updateAttendancePerfect(ClassRecord classrecord) throws ErrorException {
         response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE classrecord SET  TotalPerfectAttendance=?, TotalAttendance=?"
                + " WHERE classID = " + classrecord.getClassID() + " AND studentID = " + classrecord.getStudentID();
        ResultSet resultSet = null;
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            //pStatement = connection.prepareStatement(sql);
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }

            //pStatement.setInt(1, user.getUserID());
            //pStatement.setString(1, classrecord.getScore());
            pStatement.setString(1, classrecord.getPerfectScore());
            pStatement.setString(2, classrecord.getTotalScore());



            if (pStatement.executeUpdate() > 0) {
                response = SUCCESS;
            }

        } catch (SQLException ex) {
            System.err.println("Error in updateNoDate" + ex.toString());
        } catch (Exception ex) {
            System.err.println("Error updateNoDate " + ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        }
    }
}
