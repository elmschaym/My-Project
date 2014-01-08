/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.AccountsDaoInterface;
import dataaccesobject.interfaces.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Accounts;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class AccountsDaoImplementation implements AccountsDaoInterface {
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    @Override
    public void addAccounts(Accounts account) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO account (username,password,userID) VALUES(?,?,?)";
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

            //pStatement.setString(1, account.getUserType());
            pStatement.setString(1, account.getUsername());
            pStatement.setString(2, account.getPassword());
            pStatement.setInt(3, account.getUserID());
            
           

            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }

         //newItem.setItemCode(ItemNo);
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
            System.out.println("Error in addAccounts"+e.toString()) ;  
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}        
    

    }

    @Override
    public void editAccounts(Accounts account) throws ErrorException {
           response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE account  SET  password=? "
                + "WHERE userID=" +account.getUserID();
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
            pStatement.setString(1, account.getPassword() );

            if(pStatement.executeUpdate() > 0){
                response = SUCCESS;
            }

        } catch (SQLException ex) {
           System.err.println("Error in editAccounts"+ex.toString());
        } catch (Exception ex) {
             System.err.println("Error in editAccounts"+ex.toString());
        } finally{
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        } 
    }

    @Override
    public void deleteAccounts(int idNumber) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "DELETE FROM account WHERE userID="+idNumber;
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
            System.out.print("Error in deleteuser "+e.toString());
	}catch(Exception e){
            System.out.print(e);
	} finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}
        
        
    }

    @Override
    public ArrayList<Accounts> getAccounts() throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM account ORDER BY accountID";
            ResultSet resultSet = null;
            ArrayList<Accounts> UserList = new ArrayList<Accounts> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String username = resultSet.getString("username");
                //String usertype = resultSet.getString("usertype");
                String password=resultSet.getString("password");
                int userID=resultSet.getInt("userID");
                Accounts acct = new Accounts();
                
                
                acct.setUsername(username);
                acct.setPassword(password);
                acct.setUserID(userID);
               // acct.setUserType(usertype);
               
                UserList.add(acct);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAccounts"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return UserList;
    }

    @Override
    public ArrayList<Accounts> getAccounts(Accounts account) throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM account WHERE username='"+account.getUsername()+"'";
            ResultSet resultSet = null;
            ArrayList<Accounts> UserList = new ArrayList<Accounts>();
            System.out.println(account.getUsername());
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String username = resultSet.getString("username");
                int userID=resultSet.getInt("userID");
                int accountID=resultSet.getInt("accountID");
                String password=resultSet.getString("password");
                Accounts acct = new Accounts();
                
                System.out.println(username);
                acct.setUsername(username);
                acct.setPassword(password);
                acct.setUserID(userID);
                acct.setAccountID(accountID);
               // acct.setUserType(usertype);
               
                UserList.add(acct);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAccounts"+ex.toString());
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return UserList;
    }

  
    @Override
    public Accounts getAccount(String username) throws ErrorException {
           Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM account WHERE username = '"+username+"'";
            ResultSet resultSet = null;

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            Accounts account = new Accounts();
            if(resultSet.next()){
                String usrname = resultSet.getString("username");
                String pass=resultSet.getString("password");
                int userid=resultSet.getInt("userID");
                
               

                account.setUsername(usrname);
                account.setPassword(pass);
                account.setUserID(userid);
                account.setEmpty(false);
            }
            else{
               account.setEmpty(true);
            }
             return account;
        } catch (SQLException ex) {
            throw new ErrorException(ex.getMessage(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

    }
     @Override
    public Accounts getAccount(int idNumber) throws ErrorException {
           Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM account WHERE userID = '"+idNumber+"'";
            ResultSet resultSet = null;
             //System.out.println(username);

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            Accounts account = new Accounts();
            if(resultSet.next()){
                String usrname = resultSet.getString("username");
                String pass=resultSet.getString("password");
                int userid=resultSet.getInt("userID");
                
               

                account.setUsername(usrname);
                account.setPassword(pass);
                account.setUserID(userid);
                account.setEmpty(false);
            }
            else{
               account.setEmpty(true);
            }
             return account;
        } catch (SQLException ex) {
            throw new ErrorException(ex.getMessage(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

    }
       
}
