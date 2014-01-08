/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;


import dataaccesobject.interfaces.DBManager;
import dataaccesobject.interfaces.SecurityCodeDaoInterface;
import dataaccesobject.interfaces.UserTypeDaoInterface;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.SecurityCode;
import model.UserType;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public class SecurityCodeDaoImplementation implements SecurityCodeDaoInterface {
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    @Override
    public void addSecurityCode(SecurityCode code) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "UPDATE code SET securitycode=? WHERE codeID=1";
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

            
           
            pStatement.setString(1, code.getSecurityCode());
            
           

            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }

         //newItem.setItemCode(ItemNo);
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.err.println("Error in securityCode"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}        
    

    }

    @Override
    public void editSecurityCode(SecurityCode code) throws ErrorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SecurityCode getSecurityCode() throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM code";
            ResultSet resultSet = null;

                SecurityCode security = new SecurityCode();
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String securityCode = resultSet.getString("securitycode");
               
              
                security.setSecurityCode(securityCode);
                
                
            }
        } catch (SQLException ex) {
               System.err.println("Error in getCode"+ex.toString()) ;
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return security;
    }

    @Override
    public SecurityCode getSecurityCode(String code) throws ErrorException {
         Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM code WHERE securitycode = '"+code+"'";
            ResultSet resultSet = null;
           // boolean check=true;
             

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            SecurityCode security = new SecurityCode();
            if(resultSet.next()){
                String codeSecurity = resultSet.getString("securitycode");
                int idCode=resultSet.getInt("codeID");
                
                
               

                security.setSecurityCode(codeSecurity);
                security.setCodeID(idCode);

                security.setEmpty(false);
            }
            else{
                //System.out.println("null");
                
                security.setEmpty(true);
                
            }
            return security;
        } catch (SQLException ex) {
            throw new ErrorException(ex.getMessage(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

    }
    
}
