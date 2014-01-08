/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;


import dataaccesobject.interfaces.DBManager;
import dataaccesobject.interfaces.StudentDaoInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Student;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public class StudentDaoImplementation implements StudentDaoInterface {
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    
    public StudentDaoImplementation(){
        super();
    }
   
    @Override
    public void addStudent (Student student)throws ErrorException{
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO student VALUES(?,?,?)";
	ResultSet resultSet = null;

        try {

            if(manager==null){

                manager= DBManagerImplementation.getInstance();

            }
            connection = manager.getConnection();
            try {
                pStatement = connection.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(StudentDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }

            int idNumber=student.getIdNumber();
            pStatement.setInt(1,idNumber );
            pStatement.setString(2, student.getCompleteName());
            if(student.getImage()==null){
                pStatement.setBinaryStream(3, null);
            }
            else{                    
                if (student.getImage() instanceof com.mysql.jdbc.Blob) {
                        pStatement.setBlob(3, (Blob) student.getImage());

                    }
                else{

                        try {
                           File file2 = new File (student.getImage().toString());
                
                            FileInputStream fstream = new FileInputStream(file2);
                           pStatement.setBlob(3, fstream,file2.length());
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error in addStudent sa Student Dao "+ex.toString()) ;  
                        }                           
                }            
            
            }
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addStudent sa Student Dao "+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
    }

    @Override
    public ArrayList<Student> getStudents(String student) throws ErrorException {
    
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM student WHERE name LIKE '"+student+"%'";
            ResultSet resultSet = null;
            ArrayList<Student> StudentList = new ArrayList<Student> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String completeName = resultSet.getString("name");
                String idNum = resultSet.getString("studentID");
                Blob pic;
                if (resultSet.getBlob("photos") != null) {
                    pic = (Blob) resultSet.getBlob("photos");
                } else {
                    pic = null;
                }

                Student stdnt = new Student();
                
                stdnt.setImage(pic);
                stdnt.setCompleteName(completeName);
                stdnt.setIdNumber(Integer.parseInt(idNum));
                
                
                StudentList.add(stdnt);
            }
        } catch (SQLException ex) {
            throw new ErrorException(ex.getMessage(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return StudentList;
   
    }

    @Override
    public ArrayList<Student> getStudents(int classid) throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT DISTINCT s.* FROM student s, classes c, classrecord r "
                    + "WHERE s.studentID = r.studentID AND r.classID ="+classid+" ORDER BY s.name";
            ResultSet resultSet = null;
            ArrayList<Student> StudentList = new ArrayList<Student> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String completeName = resultSet.getString("name");
                String idNum = resultSet.getString("studentID");
               Blob pic;
                if (resultSet.getBlob("photos") != null) {
                    pic = (Blob) resultSet.getBlob("photos");
                } else {
                    pic = null;
                }

                Student stdnt = new Student();
                
                stdnt.setImage(pic);
                stdnt.setCompleteName(completeName);
                stdnt.setIdNumber(Integer.parseInt(idNum));
                
                StudentList.add(stdnt);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getStudent"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return StudentList;
    }

    @Override
    public void editStudent(Student student) throws ErrorException {
               response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE student SET name=?, photos=?, "
                + "WHERE studentID=" +student.getIdNumber();
        ResultSet resultSet = null;

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            pStatement = connection.prepareStatement(sql);
            int idNumber=student.getIdNumber();
            
            pStatement.setInt(1, idNumber);
            pStatement.setString(2, student.getCompleteName());
            if(student.getImage()==null){
                pStatement.setBinaryStream(3, null);
            }
            else{                    
                if (student.getImage() instanceof com.mysql.jdbc.Blob) {
                        pStatement.setBlob(3, (Blob) student.getImage());

                    }
                else{

                        try {
                           File file2 = new File (student.getImage().toString());
                
                            FileInputStream fstream = new FileInputStream(file2);
                           pStatement.setBlob(3, fstream,file2.length());
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error in EditStudent"+ex.toString()) ;  
                        }                           
                }            
            
            }

            if(pStatement.executeUpdate() > 0){
                response = SUCCESS;
            }

        } catch (SQLException ex) {
           System.err.println("Error in editStudent"+ex.toString());
        } catch (Exception ex) {
             System.err.println("Error in editStudent"+ex.toString());
        } finally{
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        } 
    }

    @Override
    public void deleteStudent(int idNumber) throws ErrorException {
                response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "DELETE FROM student WHERE studentID="+idNumber;
	ResultSet resultSet = null;

	try {

            if(manager==null){

                manager= DBManagerImplementation.getInstance();

            }
            connection = manager.getConnection();
            pStatement = connection.prepareStatement(sql);
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                System.out.print("Success deleting");
            }
            
        
        } catch (SQLException e) {
            System.out.print("Error in deleteStudent "+e.toString());
	}catch(Exception e){
            System.out.print(e);
	} finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}
    }

    @Override
    public void editPhoto(Student stnt) throws ErrorException {
               response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE student SET photos=? WHERE studentID="+stnt.getIdNumber();
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
            

           if(stnt.getImage()==null){
                pStatement.setBinaryStream(1, null);
                System.out.println("Photos is null");
            }
            else{                    
                if (stnt.getImage() instanceof com.mysql.jdbc.Blob) {
                        pStatement.setBlob(1, (Blob) stnt.getImage());

                    }
                else{

                        try {
                           File file2 = (File) (stnt.getImage());
                
                            FileInputStream fstream = new FileInputStream(file2);
                           pStatement.setBlob(1, fstream,file2.length());
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error in addUser"+ex.toString()) ;  
                        }                           
                }            
            
            }
            if(pStatement.executeUpdate() > 0){
                response = SUCCESS;
            }

        } catch (SQLException ex) {
           System.err.println("Error in editStudentPhotos"+ex.toString());
        } catch (Exception ex) {
             System.err.println("Error in editStudentPhotos"+ex.toString());
        } finally{
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        } 
    }

    @Override
    public Student getStudent(int stundeID) throws ErrorException {
             Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM student WHERE studentID ="+stundeID;
            ResultSet resultSet = null;
            Student stdnt = new Student();
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if(resultSet.next()){
                
                String completeName = resultSet.getString("name");
                String idNum = resultSet.getString("studentID");
                Blob pic;
                if (resultSet.getBlob("photos") != null) {
                    pic = (Blob) resultSet.getBlob("photos");
                } else {
                    pic = null;
                }

                
                
                stdnt.setImage(pic);
                stdnt.setCompleteName(completeName);
                stdnt.setIdNumber(Integer.parseInt(idNum));
                stdnt.setEmpty(false);
            }else{
                stdnt.setEmpty(true);
            }
        } catch (SQLException ex) {
            System.out.println( "Error in getStudent"+ex.getMessage().toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return stdnt;
    }
    
}
