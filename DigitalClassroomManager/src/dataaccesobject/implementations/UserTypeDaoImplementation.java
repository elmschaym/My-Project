/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.DBManager;
import dataaccesobject.interfaces.UserTypeDaoInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.UserType;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class UserTypeDaoImplementation implements UserTypeDaoInterface {
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    @Override
    public void addUserType(UserType usertype) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO usertype(usertype) VALUES(?)";
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

            
           
            pStatement.setString(1, usertype.getUserType());
            
           

            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }

         //newItem.setItemCode(ItemNo);
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.err.println("Error in addUserTypec"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}        
    

        
    }

    @Override
    public void editUserType(UserType usertype) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE usertype SET usertype_id=?, usertype=? "
                + "WHERE stud_id=" +usertype.getUserTypeID();
        ResultSet resultSet = null;
        System.out.println("Pumasok "+usertype.getUserTypeID());

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            pStatement = connection.prepareStatement(sql);
            
            
            pStatement.setInt(1, usertype.getUserTypeID());
            pStatement.setString(2, usertype.getUserType());

            if(pStatement.executeUpdate() > 0){
                response = SUCCESS;
            }

        } catch (SQLException ex) {
           System.err.println("Error in editUserType"+ex.toString());
        } catch (Exception ex) {
             System.err.println("Error in editUserType"+ex.toString());
        } finally{
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        } 
    }

    
}
