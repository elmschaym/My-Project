/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.DBManager;
import dataaccesobject.interfaces.GradingSystemDaoInterface;
import java.util.ArrayList;
import model.GradingSystem;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.DataDispatcher;
import tools.ErrorException;
/**
 *
 * @author PAL2X
 */
public class GradingSystemDaoImplementation implements GradingSystemDaoInterface{
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    @Override
    public void addGradingSystem(GradingSystem gs) throws ErrorException {
            Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO gradingsystem (classID,percentage,entry) "
                + "VALUES(?,?,?)";
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
           
            pStatement.setInt(1,gs.getClassID());
            pStatement.setString(2,gs.getPercentage());
            pStatement.setString(3, gs.getEntryName());
            
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
    public GradingSystem getGSfk(String name, int clasID) throws ErrorException {
           Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM gradingsystem WHERE classID="
                    + ""+clasID+" AND entry= '"+name+"'";
            ResultSet resultSet = null;
            
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
              GradingSystem gradeS=new GradingSystem(); 
            if(resultSet.next()){
                
                int gsid = resultSet.getInt("gsID");
                int classid = resultSet.getInt("classID");
                String entry=resultSet.getString("entry");
                String percnt=resultSet.getString("percentage");
                
             
                //user.setAccountID(Integer.parseInt(accountID));
                gradeS.setClassID(classid);
                gradeS.setEntryName(entry);
                gradeS.setGsID(gsid);
                gradeS.setPercentage(percnt);
                gradeS.setEmpty(false);
                
                
            }else{
                gradeS.setEmpty(true);
            }
            return gradeS;
        } catch (SQLException ex) {
               throw new ErrorException(ex.getMessage().toString(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    }

    @Override
    public ArrayList<GradingSystem> getGradingSystem() throws ErrorException {
          Connection connection = null;
            Statement statement = null;
            String sql = "SELECT DISTINCT entry FROM gradingsystem ORDER BY entry";
            ResultSet resultSet = null;
            ArrayList<GradingSystem> gradinglist = new ArrayList<> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                String entryname=resultSet.getString("entry");
                GradingSystem gs= new GradingSystem();
                
                gs.setEntryName(entryname);    
                gradinglist.add(gs);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getStudent"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return gradinglist;
    }

    @Override
    public void addGradingSystemEntry(GradingSystem gs) throws ErrorException {
           Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO gradingsystem (classID,entry) "
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
           
            pStatement.setInt(1, gs.getClassID());
            pStatement.setString(2, gs.getEntryName());
            
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }       
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addGSystem"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}             
    }

    @Override
    public GradingSystem getGradingSystem(String name) throws ErrorException {
           Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM gradingsystem WHERE entry= '"+name+"'";
            ResultSet resultSet = null;
            
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
              GradingSystem gradeS=new GradingSystem(); 
            if(resultSet.next()){
                
                int gsid = resultSet.getInt("gsID");
                int classid = resultSet.getInt("classID");
                String entry=resultSet.getString("entry");
                String percnt=resultSet.getString("percentage");
                
             
                //user.setAccountID(Integer.parseInt(accountID));
                gradeS.setClassID(classid);
                gradeS.setEntryName(entry);
                gradeS.setGsID(gsid);
                gradeS.setPercentage(percnt);
                gradeS.setEmpty(false);
                
                
            }else{
                gradeS.setEmpty(true);
            }
            return gradeS;
        } catch (SQLException ex) {
               throw new ErrorException(ex.getMessage().toString(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }
    }

    @Override
    public void updateGradingSystem(GradingSystem gs) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE gradingsystem SET percentage=?, entry=?"
                + " WHERE classID = "+gs.getClassID()+" AND entry = '"+gs.getEntryName()+"'";
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
            
            pStatement.setString(1, gs.getPercentage());
            pStatement.setString(2, gs.getEntryName());
           
     
            if(pStatement.executeUpdate() > 0){
                response = SUCCESS;
            }

        } catch (SQLException ex) {
           System.err.println("Error in updateGrading"+ex.toString());
        } catch (Exception ex) {
             System.err.println("Error updateGrading "+ex.toString());
        } finally{
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        } 
    }

    @Override
    public ArrayList<GradingSystem> getGradingSystem(int classID) throws ErrorException {
           Connection connection = null;
            Statement statement = null;
            String sql = "SELECT  * FROM gradingsystem WHERE classID = "+classID+" ORDER BY entry";
            ResultSet resultSet = null;
            ArrayList<GradingSystem> gradinglist = new ArrayList<> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                int classid = resultSet.getInt("classID");
                int gsid = resultSet.getInt("gsID");
                String percent=resultSet.getString("percentage");
                String entryname=resultSet.getString("entry");
                GradingSystem gs= new GradingSystem();
                gs.setPercentage(percent);
                gs.setClassID(classid);
                gs.setEntryName(entryname);     
                gs.setGsID(gsid);
                gradinglist.add(gs);
                
            }
        } catch (SQLException ex) {
            System.out.println("Error in getGradingSystem"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return gradinglist;
    }
    
    @Override
    public void deleteEntry(int subjID,String name) throws ErrorException {
             response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "DELETE FROM gradingsystem WHERE classID="+subjID+" AND entry= '"+name+"'";
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
            System.out.print("Error in deleteEntry"+e.toString());
	}catch(Exception e){
            System.out.print(e);
	} finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}
    }
    
}
