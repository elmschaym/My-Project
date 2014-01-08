/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.ClassDaoInterface;
import dataaccesobject.interfaces.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Classes;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class ClassDaoImplementation implements ClassDaoInterface {
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    
    @Override
    public void addClass(Classes classes) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO classes (year,semester,room,schedule,subjectID,section,userID,attempt) "
                + "VALUES(?,?,?,?,?,?,?,?)";
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
            
            pStatement.setString(1, classes.getYear());
            pStatement.setString(2, classes.getSem());
            pStatement.setString(3, classes.getRoom());
            pStatement.setString(4, classes.getSchedule());
            pStatement.setInt(5, classes.getSubjectID());
            pStatement.setString(6, classes.getSectionID());
            pStatement.setInt(7, classes.getUserID());
            pStatement.setBoolean(8, true);
           

            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }

            System.out.println("ADDED. in ClassDAO");
        } catch (SQLException e) {
            System.out.println("Error in addClass"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}        
    

    }
    
    @Override
    public ArrayList<Classes> getClasses() throws ErrorException{
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT s.name, s.description, c.section, c.semester, c.year, c.room, c.schedule, c.classID, c.userID, c.attempt, u.lName, u.fName, u.mName "
                    + "FROM classes c, users u, subject s WHERE s.subjectID = c.subjectID AND u.userID = c.userID";
            ResultSet resultSet = null;
            ArrayList<Classes> classList = new ArrayList<>();
            
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String subjectName = resultSet.getString("name");
                String description = resultSet.getString("description");
                String section = resultSet.getString("section");
                String sem = resultSet.getString("semester");
                String year = resultSet.getString("year");
                String room = resultSet.getString("room");
                String schedule = resultSet.getString("schedule");
                int classID = resultSet.getInt("classID");
                int userID = resultSet.getInt("userID");
                boolean attempt = resultSet.getBoolean("attempt");
                String lName = resultSet.getString("lName");
                String fName = resultSet.getString("fName");
                String mName = resultSet.getString("mName");
                
                Classes classes = new Classes();
                classes.setSubjectName(subjectName);
                classes.setSubjectDescripton(description);
                classes.setSectionID(section);
                classes.setSem(sem);
                classes.setYear(year);
                classes.setRoom(room);
                classes.setSchedule(schedule);
                classes.setClassID(classID);
                classes.setUserID(userID);
                classes.setFirstAttempt(attempt);
                classes.setInstructorName(lName+", "+fName+" "+mName);
                
                classList.add(classes);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getClass"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return classList;
    }
    
    @Override
    public ArrayList<Classes> getClassesUniq() throws ErrorException{
        Connection connection = null;
            Statement statement = null;
            String sql = "SELECT distinct s.name, s.description FROM classes c, subject s WHERE c.subjectID = s.subjectID";
            ResultSet resultSet = null;
            ArrayList<Classes> classList = new ArrayList<>();
            
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String subjectName = resultSet.getString("name");
                String description = resultSet.getString("description");
                
                Classes classes = new Classes();
                classes.setSubjectName(subjectName);
                classes.setSubjectDescripton(description);
                
                classList.add(classes);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getClassUniq"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return classList;
    }
    
    @Override
    public void editClass(Classes classes) throws ErrorException{
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "UPDATE classes SET room=?,schedule=?,section=?,userID=?"
                + " WHERE classID="+classes.getClassID();
        System.out.println(classes.getClassID());
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
            
            pStatement.setString(1, classes.getRoom());
            pStatement.setString(2, classes.getSchedule());
            pStatement.setString(3, classes.getSectionID());
            pStatement.setInt(4, classes.getUserID());
           

            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }

            System.out.println("EDITTED. in ClassDAO");
        } catch (SQLException e) {
            System.out.println("Error in editClass"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}      
    }
    
    @Override
    public void deleteClass(int classID) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "DELETE FROM classes WHERE classID="+classID;
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
            System.out.print("Error in deleteClass"+e.toString());
	}catch(Exception e){
            System.out.print(e);
	} finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}
    }
    
    @Override
    public void changeAttempt(Classes classes) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "UPDATE classes SET attempt = ?"
                + " WHERE classID="+ classes.getClassID();
	ResultSet resultSet = null;
        System.out.println(classes.getClassID()+"changeAttemp "+classes.isFirstAttempt());
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
            
            pStatement.setBoolean(1, classes.isFirstAttempt());
           

            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }

            System.out.println("EDITTED. in ClassDAO");
        } catch (SQLException e) {
            System.out.println("Error in changeAttempt"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}      
    }
    
    @Override
    public Classes getClass(String name, String section, String sem, String year) throws ErrorException{
            Connection connection = null;
            Statement statement = null;
            String sql="SELECT c.year, c.semester, c.classID, s.name, s.description, c.section, c.room, c.schedule, c.attempt FROM classes c, subject s WHERE c.subjectID = s.subjectID AND c.year = '"+year+"' AND c.semester = '"+sem+"' AND c.section ='"+section+"' AND s.name = '"+name+"'";
                    ResultSet resultSet = null;
            Classes classes = new Classes();
            try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
           
            if(resultSet.next()){
                
                String sname=resultSet.getString("name");
                String sect=resultSet.getString("section");
                String desc=resultSet.getString("description");
                String rom=resultSet.getString("room");
                String sched=resultSet.getString("schedule");
                String semester=resultSet.getString("semester");
                String yr=resultSet.getString("year");
                int clasid=resultSet.getInt("classID");
                boolean attem=resultSet.getBoolean("attempt");
                
                classes.setRoom(rom);
                classes.setClassID(clasid);
                classes.setSubjectName(sname);
                classes.setSubjectDescripton(desc);
                classes.setSchedule(sched);
                classes.setSectionID(sect);
                classes.setFirstAttempt(attem);
                classes.setYear(yr);
                classes.setSem(semester);
                classes.setEmpty(false);
            }else{
               classes.setEmpty(true);
            }
            
        } catch (SQLException ex) {
               System.out.println("Error in changeAttempt"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    return classes;
    } 


    @Override
    public Classes getCurrentClass(String subject, String section, String room, String schedule) throws ErrorException{
        Connection connection = null;
            Statement statement = null;
            String sql="SELECT DISTINCT c.classID, c.section, c.schedule, s.name, s.description, u.lName, u.fName, u.mName FROM classes c, subject s, users u WHERE c.subjectID = s.subjectID AND c.userID = u.userID AND s.name = '"+subject+"' AND c.section = '"+section+"' AND c.room = '"+room+"' AND c.schedule = '"+schedule+"'";
                    ResultSet resultSet = null;
            Classes classes = new Classes();
            try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
           
            if(resultSet.next()){
                
                String sname=resultSet.getString("name");
                String sect=resultSet.getString("section");
                String desc=resultSet.getString("description");
                String sched=resultSet.getString("schedule");
                String lName = resultSet.getString("lName");
                String fName = resultSet.getString("fName");
                String mName = resultSet.getString("mName");
                int clasid=resultSet.getInt("classID");
                
                classes.setClassID(clasid);
                classes.setSubjectName(sname);
                classes.setSubjectDescripton(desc);
                classes.setSchedule(sched);
                classes.setSectionID(sect);
                classes.setInstructorName(lName+", "+fName+" "+mName);
            }
            
        } catch (SQLException ex) {
               System.out.println("Error in getCurrentClass"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    return classes;
    }
      @Override
    public void insertPassingRate(int classid,int score) throws ErrorException {
     response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "UPDATE classes SET percentageRate = ?"
                + " WHERE classID="+ classid;
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
            
            pStatement.setInt(1, score);
           

            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }

            System.out.println("EDITTED. in ClassDAO");
        } catch (SQLException e) {
            System.out.println("Error in insertPassingRate"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}      
    }

    @Override
    public Classes getAttempt(int classid) throws ErrorException {
                Connection connection = null;
            Statement statement = null;
            String sql="Select attempt FROM classes WHERE classID="+classid;
                    ResultSet resultSet = null;
            Classes classes = new Classes();
            try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
           
            if(resultSet.next()){
                
                
                boolean attem=resultSet.getBoolean("attempt");
                
                classes.setFirstAttempt(attem);
                classes.setEmpty(false);
            }else{
               classes.setEmpty(true);
            }
            
        } catch (SQLException ex) {
               System.out.println("Error in changeAttempt"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    return classes;
    }

    @Override
    public Classes getPercentage(int classid) throws ErrorException {
                Connection connection = null;
            Statement statement = null;
            String sql="Select percentageRate FROM classes WHERE classID="+classid;
                    ResultSet resultSet = null;
            Classes classes = new Classes();
            try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
           
            if(resultSet.next()){
                
                
                int percentage=resultSet.getInt("percentageRate");
                
                classes.setPercentage(percentage);
                classes.setEmpty(false);
            }else{
               classes.setEmpty(true);
            }
            
        } catch (SQLException ex) {
               System.out.println("Error in changeAttempt"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    return classes;
    }
    
    public Classes getClass(int classid) throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql="SELECT s.name, c.section, s.description, c.room, c.schedule "+
                "FROM subject s, classes c WHERE s.subjectID = c.subjectID AND c.classID = "+classid;
                    ResultSet resultSet = null;
            Classes classes = new Classes();
            try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
           
            if(resultSet.next()){
                
                
                String subjectName = resultSet.getString("name");
                String subjectDescription = resultSet.getString("description");
                String subjectSection = resultSet.getString("section");
                String subjectRoom = resultSet.getString("room");
                String subjectSchedule = resultSet.getString("schedule");
                
                classes.setSubjectName(subjectName);
                classes.setSubjectDescripton(subjectDescription);
                classes.setSectionID(subjectSection);
                classes.setRoom(subjectRoom);
                classes.setSchedule(subjectSchedule);
            }
            
        } catch (SQLException ex) {
               System.out.println("Error in changeAttempt"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    return classes;
    }
}
