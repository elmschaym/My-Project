/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.DBManager;
import dataaccesobject.interfaces.SubjectDaoInterface;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Subject;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class SubjectDaoImplementation implements SubjectDaoInterface{
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;

    @Override
    public void addSubject(Subject subject) throws ErrorException {
                response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO subject (description,units,semester,name) "
                + "VALUES(?,?,?,?)";
	ResultSet resultSet = null;
        System.out.println(subject.getDescription());
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

            int unitNum=subject.getUnits();
           
            pStatement.setString(1,subject.getDescription());
            pStatement.setInt(2,unitNum);
            pStatement.setString(3, subject.getSemester());
            pStatement.setString(4, subject.getSubjectName());
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addStudent"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
    }

    @Override
    public ArrayList<Subject> getSubjects() throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM subject ORDER BY name";
            ResultSet resultSet = null;
            ArrayList<Subject> SubjectList = new ArrayList<Subject> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String subjectName = resultSet.getString("name");
                int unitNum = resultSet.getInt("units");
                String subjDescription=resultSet.getString("description");
                String semester=resultSet.getString("semester");
                Subject subj= new Subject();
                
                subj.setDescription(subjDescription);
                subj.setSemester(semester);
                subj.setSubjectName(subjectName);
                subj.setUnits(unitNum);
                
                
                SubjectList.add(subj);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getSubject"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return SubjectList;
    }

    @Override
    public void deleteSubject(int subjID) throws ErrorException {
                response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "DELETE FROM subject WHERE subjectID="+subjID;
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
            System.out.print("Error in deleteSubject"+e.toString());
	}catch(Exception e){
            System.out.print(e);
	} finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}
    }

    @Override
    public Subject getSubject(String subject) throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM subject WHERE name = '"+subject+"'";
            ResultSet resultSet = null;
             System.out.println(subject);

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();
            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            Subject subj = new Subject();
            if(resultSet.next()){
                String subJ = resultSet.getString("name");
                String sem=resultSet.getString("semester");
                int userid=resultSet.getInt("units");
                String descrption=resultSet.getString("description");
                int subjectID = resultSet.getInt("subjectID");
                
               

                subj.setSubjectName(subJ);
                subj.setSemester(sem);
                subj.setUnits(userid);
                subj.setDescription(descrption);
                subj.setSubjectID(subjectID);
                subj.setEmpty(true);
                
            }
            else{
                subj.setEmpty(false);
            }
            return subj;
        } catch (SQLException ex) {
            throw new ErrorException(ex.getMessage(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    }

    @Override
    public void editSubject(Subject subject) throws ErrorException {
       response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "UPDATE subject SET description=?,units=?,semester=?,name=? "
                + "WHERE subjectID=" + subject.getSubjectID();
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

            int unitNum=subject.getUnits();
           
            pStatement.setString(1,subject.getDescription());
            pStatement.setInt(2,unitNum);
            pStatement.setString(3, subject.getSemester());
            pStatement.setString(4, subject.getSubjectName());
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in editStudent"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
    
    }
    
    public ArrayList<Subject> getSubjectSem(String semester) throws ErrorException {
        Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM subject WHERE semester = '"+semester+"'";
            ResultSet resultSet = null;
            ArrayList<Subject> subjectList = new ArrayList<>();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();
            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
           while(resultSet.next()){
                String name = resultSet.getString("name");
                String descrption=resultSet.getString("description");
                Subject subj = new Subject();
                subj.setSubjectName(name);
                subj.setDescription(descrption);
                
                subjectList.add(subj);
            }
           
        } catch (SQLException ex) {
            throw new ErrorException(ex.getMessage(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return subjectList;
    }
    }
    

