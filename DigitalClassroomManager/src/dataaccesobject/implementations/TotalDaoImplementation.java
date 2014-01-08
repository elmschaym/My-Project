/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.TotalDaoInterface;
import java.util.ArrayList;
import model.Total;
import dataaccesobject.interfaces.DBManager;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.DataDispatcher;
import tools.ErrorException;


/**
 *
 * @author PAL2X
 */
public class TotalDaoImplementation implements TotalDaoInterface {
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    @Override
    public void addColumn(Total total) throws ErrorException {
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "ALTER TABLE total ADD "
                + "column "+total.getColumnName()+" DOUBLE DEFAULT 0, ADD "
                + "column Perfect"+total.getColumnName()+" DOUBLE DEFAULT 0 ";
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


            
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addColumn"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
        
    }

    @Override
    public void addStudentTotal(Total total) throws ErrorException {
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO classrecord (classID,studentID) "
                + "VALUES(?,?)";
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

            //int unitNum=subject.getUnits();
           
            pStatement.setInt(2,total.getStudentID());
            pStatement.setInt(1,total.getClassID());
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addStudentTotal"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
    }

    @Override
    public ArrayList<Total> getValue(Total total) throws ErrorException {
             Connection connection = null;
            Statement statement = null;
            String sql = "SELECT  "+total.getColumnName()+" FROM  classrecord "
                    + " WHERE classID = "+total.getClassID();
            ResultSet resultSet = null;
            ArrayList<Total> totalSearch = new ArrayList<>();
            
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            
            while(resultSet.next()){
                
                Double totalResult=resultSet.getDouble(total.getColumnName());
              
                Total totalNew=new Total();
               
               // clasNew.setPerfectScore(perfectscore);
               
                totalNew.setTotal(totalResult);
                
                totalSearch.add(totalNew);
                
            }
        } catch (SQLException ex) {
               System.out.println("Error in getClassRecords"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
        return totalSearch;
    }

    @Override
    public void addTriggerInsert(Total total) throws ErrorException {
       Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "DELIMITER $$ CREATE TRIGGER update_totalInsert_"+total.getColumnName()+" "
                + "AFTER INSERT ON classrecord FOR EACH ROW BEGIN UPDATE total"
                + " SET Total"+total.getColumnName()+" ="
                + " Total"+total.getColumnName()+"+NEW."+total.getColumnName()+" "
                + "WHERE total.classID = NEW.classID AND total.studentID = NEW.studentID; "
                + "END$$; CREATE TRIGGER update_totalUpdate_"+total.getColumnName()+""
                + " AFTER UPDATE ON classrecord FOR EACH ROW BEGIN "
                + "IF NEW."+total.getColumnName()+" <> OLD."+total.getColumnName()+" "
                + "THEN UPDATE total SET Total"+total.getColumnName()+" = "
                + "Total"+total.getColumnName()+"-OLD."+total.getColumnName()+""
                + " WHERE total.classID = NEW.classID AND total.studentID = "
                + "NEW.studentID; UPDATE total SET Total"+total.getColumnName()+""
                + " = Total"+total.getColumnName()+"+NEW."+total.getColumnName()+""
                + " WHERE total.classID = NEW.classID AND total.studentID = "
                + "NEW.studentID; END IF; END$$";
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


            
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addTriggerInsert"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
        
    }

    @Override
    public void addTriggerUpdate(Total total) throws ErrorException {
       Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "CREATE TRIGGER update_totalUpdate_"+total.getColumnName()+""
                + " AFTER UPDATE ON classrecord FOR EACH ROW BEGIN "
                + "IF NEW."+total.getColumnName()+" <> OLD."+total.getColumnName()+" "
                + "THEN UPDATE total SET Total"+total.getColumnName()+" = "
                + "Total"+total.getColumnName()+"-OLD."+total.getColumnName()+""
                + " WHERE total.classID = NEW.classID AND total.studentID = "
                + "NEW.studentID; UPDATE total SET Total"+total.getColumnName()+""
                + " = Total"+total.getColumnName()+"+NEW."+total.getColumnName()+""
                + " WHERE total.classID = NEW.classID AND total.studentID = "
                + "NEW.studentID; END IF; END$$";
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


            
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addTriggerUpdate"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
        
    }
    
}
