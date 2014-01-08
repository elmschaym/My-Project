/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

import dataaccesobject.interfaces.DBManager;
import dataaccesobject.interfaces.UserDaoInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import tools.DataDispatcher;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class UserDaoImplementation implements UserDaoInterface {
    private DBManager manager=null;
    private int response;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;

    @Override
    public void addUser(User user) throws ErrorException {
               response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "INSERT INTO users (userID,lName,fName, mName,city,province, "
                + "contactNo, eMail,userPhotos,usertypeID) VALUES(?,?,?,?,?,?,?,?,?,?)";
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
            pStatement.setInt(1,user.getUserID());
            pStatement.setString(2, user.getLastName());
            pStatement.setString(3, user.getFirstName());
            pStatement.setString(4, user.getMiddleName());
            pStatement.setString(5, user.getCity());
            pStatement.setString(6, user.getProvince());
            pStatement.setString(7, user.getContactNum());
            pStatement.setString(8, user.getEmailAdd());
           if(user.getImage()==null){
                pStatement.setBinaryStream(9, null);
            }
            else{                    
                if (user.getImage() instanceof com.mysql.jdbc.Blob) {
                        pStatement.setBlob(9, (Blob) user.getImage());

                    }
                else{

                        try {
                           File file2 = new File (user.getImage().toString());
                
                            FileInputStream fstream = new FileInputStream(file2);
                           pStatement.setBlob(9, fstream,file2.length());
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error in addUser"+ex.toString()) ;  
                        }                           
                }  
            
            }
           pStatement.setInt(10,user.getUserType());
            if(pStatement.executeUpdate()>0){
                response = SUCCESS;
                 System.out.print("sending data");
            }
            System.out.println("ADDED. in DAO");
        } catch (SQLException e) {
        System.out.println("Error in addInstructor"+e.toString()) ;    
        } finally {
            DataDispatcher.dispatch(resultSet, pStatement, connection);
	}        

    }

    @Override
    public void editUser(User user) throws ErrorException {
               response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE users SET lName=?, fName=?,"
                + "mName=?, city=?,province=?, contactNo=?, eMail=?"
                + " WHERE userID=" +user.getUserID();
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
            pStatement.setString(1, user.getLastName());
            pStatement.setString(2, user.getFirstName());
            pStatement.setString(3, user.getMiddleName());
            pStatement.setString(4, user.getCity());
            pStatement.setString(5, user.getProvince());
            pStatement.setString(6, user.getContactNum());
            pStatement.setString(7, user.getEmailAdd());
         /* if(user.getImage()==null){
                pStatement.setBinaryStream(8, null);
                System.out.println("Photos is null");
            }
            else{                    
                if (user.getImage() instanceof com.mysql.jdbc.Blob) {
                        pStatement.setBlob(8, (Blob) user.getImage());

                    }
                else{

                        try {
                           File file2 = (File) (user.getImage());
                
                            FileInputStream fstream = new FileInputStream(file2);
                           pStatement.setBlob(8, fstream,file2.length());
                        } catch (FileNotFoundException ex) {
                            System.out.println("Error in addUser"+ex.toString()) ;  
                        }                           
                }            
            
            }
            */
            if(pStatement.executeUpdate() > 0){
                response = SUCCESS;
            }

        } catch (SQLException ex) {
           System.err.println("Error in editUser"+ex.toString());
        } catch (Exception ex) {
             System.err.println("Error in editUser"+ex.toString());
        } finally{
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        } 
    }

    @Override
    public void deleteUser(int idNumber) throws ErrorException {
        response = FAILED;
        Connection connection = null;
	PreparedStatement pStatement = null;
	String sql = "DELETE FROM users WHERE userID="+idNumber;
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
    public ArrayList<User> getUsers() throws ErrorException {
             Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM users ORDER BY lName";
            ResultSet resultSet = null;
            ArrayList<User> UserList = new ArrayList<User> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String lastName = resultSet.getString("lName");
                String firstName = resultSet.getString("fName");
                String middleName=resultSet.getString("mName");
                String city=resultSet.getString("city");
                String province=resultSet.getString("province");
                String contact=resultSet.getString("contactNO");
                String emailAdd=resultSet.getString("eMail");
                String userTypeId=resultSet.getString("usertypeID");
                String idNum=resultSet.getString("userID");
                Blob pic;
                if (resultSet.getBlob("userPhotos") != null) {
                    pic = (Blob) resultSet.getBlob("userPhotos");
                } else {
                    pic = null;
                }

                User user = new User();
                user.setCity(city);
                user.setProvince(province);
                user.setContactNum(contact);
                user.setEmailAdd(emailAdd);
                user.setFirstName(firstName);
                user.setUserID(Integer.parseInt(idNum));
                user.setLastName(lastName);
                user.setMiddleName(middleName);
                user.setUserType(Integer.parseInt(userTypeId));
                user.setImage(pic);
                
                
                
                UserList.add(user);
            }
        } catch (SQLException ex) {
               System.err.println("Error in getUser"+ex.toString()) ;
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return UserList;
    }

    @Override
    public ArrayList<User> getUsers(User user) throws ErrorException {
            Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM users WHERE fName LIKE '"+user+"%'";
            ResultSet resultSet = null;
            ArrayList<User> UserList = new ArrayList<User> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String lastName = resultSet.getString("lName");
                String firstName = resultSet.getString("fName");
                String middleName=resultSet.getString("mName");
                String address=resultSet.getString("address");
                String contact=resultSet.getString("contactNO");
                String emailAdd=resultSet.getString("eMail");
                String accountID=resultSet.getString("accountID");
                String userTypeId=resultSet.getString("usertypeID");
                String idNum=resultSet.getString("userID");
                
                User usr = new User();
                usr.setAccountID(Integer.parseInt(accountID));
               // usr.setAddress(address);
                usr.setContactNum(contact);
                usr.setEmailAdd(emailAdd);
                usr.setFirstName(firstName);
                usr.setUserID(Integer.parseInt(idNum));
                usr.setLastName(lastName);
                usr.setMiddleName(middleName);
                usr.setUserType(Integer.parseInt(userTypeId));
                
                
                
                UserList.add(usr);
            }
        } catch (SQLException ex) {
               System.err.println("Error in getUser"+ex.toString()) ;
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return UserList;
    }

    @Override
    public ArrayList<User> getUserID(User user) throws ErrorException {
             Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM users WHERE userID="+user.getUserID();
            ResultSet resultSet = null;
            ArrayList<User> UserList = new ArrayList<User> ();

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                
                String lastName = resultSet.getString("lName");
                String firstName = resultSet.getString("fName");
                String middleName=resultSet.getString("mName");
                String city=resultSet.getString("city");
                String province=resultSet.getString("province");
                String contact=resultSet.getString("contactNO");
                String emailAdd=resultSet.getString("eMail");
                String accountID=resultSet.getString("accountID");
                String userTypeId=resultSet.getString("usertypeID");
                String idNum=resultSet.getString("userID");
                Blob pic;
                if (resultSet.getBlob("userPhotos") != null) {
                    pic = (Blob) resultSet.getBlob("userPhotos");
                } else {
                    pic = null;
                }

                
                user.setAccountID(Integer.parseInt(accountID));
                user.setCity(city);
                user.setProvince(province);
                user.setContactNum(contact);
                user.setEmailAdd(emailAdd);
                user.setFirstName(firstName);
                user.setUserID(Integer.parseInt(idNum));
                user.setLastName(lastName);
                user.setMiddleName(middleName);
                user.setUserType(Integer.parseInt(userTypeId));
                user.setImage(pic);
                
                
                
                UserList.add(user);
            }
        } catch (SQLException ex) {
               System.err.println("Error in getUser"+ex.toString()) ;
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

        return UserList;
    }

    @Override
    public User getUser(int userid) throws ErrorException {
             Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM users WHERE userID="+userid+"";
            ResultSet resultSet = null;
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            
            if(resultSet.next()){
                
                String lastName = resultSet.getString("lName");
                String firstName = resultSet.getString("fName");
                String middleName=resultSet.getString("mName");
                String city=resultSet.getString("city");
                String province=resultSet.getString("province");
                String contact=resultSet.getString("contactNO");
                String emailAdd=resultSet.getString("eMail");
                int userTypeId=resultSet.getInt("usertypeID");
                int idNum=resultSet.getInt("userID");
                Blob pic;
                if (resultSet.getBlob("userPhotos") != null) {
                    pic = (Blob) resultSet.getBlob("userPhotos");
                } else {
                    pic = null;
                }

                User user=new User();
                //user.setAccountID(Integer.parseInt(accountID));
                user.setCity(city);
                user.setProvince(province);
                user.setContactNum(contact);
                user.setEmailAdd(emailAdd);
                user.setFirstName(firstName);
                user.setUserID(idNum);
                user.setLastName(lastName);
                user.setMiddleName(middleName);
                user.setUserType(userTypeId);
                user.setImage(pic);
                
                
                
                return user;
            }else{
                return null;
            }
        } catch (SQLException ex) {
               throw new ErrorException(ex.getMessage(), "SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        }

    }

    @Override
    public void editPhoto(User user) throws ErrorException {
              response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE users SET userPhotos=? WHERE userID=" +user.getUserID();
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
            

           if(user.getImage()==null){
                pStatement.setBinaryStream(1, null);
                System.out.println("Photos is null");
            }
            else{                    
                if (user.getImage() instanceof com.mysql.jdbc.Blob) {
                        pStatement.setBlob(1, (Blob) user.getImage());

                    }
                else{

                        try {
                           File file2 = (File) (user.getImage());
                
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
           System.err.println("Error in editUser"+ex.toString());
        } catch (Exception ex) {
             System.err.println("Error in editUser"+ex.toString());
        } finally{
            DataDispatcher.dispatch(resultSet, pStatement, connection);
        } 
    }

    @Override
    public void editUserType(User userID) throws ErrorException {
        response = FAILED;
        Connection connection = null;
        PreparedStatement pStatement = null;
        String sql = "UPDATE users SET usertypeID =? WHERE userID=" +userID.getUserID();
        ResultSet resultSet = null;
        //System.out.println(userID.getUserID());

        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();
            }

            connection = manager.getConnection();
            pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, userID.getUserType());
           
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

    @Override
    public User getUserTypeID(int usertype) throws ErrorException {
                Connection connection = null;
            Statement statement = null;
            String sql = "SELECT * FROM users WHERE usertypeID="+usertype+"";
            ResultSet resultSet = null;
            User user=new User();
        try {

            if (manager == null) {
                manager = DBManagerImplementation.getInstance();

            }

            connection = manager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            
            if(resultSet.next()){
                
                int lastName = resultSet.getInt("usertypeID");
                

                
                //user.setAccountID(Integer.parseInt(accountID));
                user.setUserType(lastName);
                user.setEmpty(false);
                
            }else{
               user.setEmpty(true);
            }
        } catch (SQLException ex) {
               System.out.println(ex.toString()+"SQLException");
        } finally {
            DataDispatcher.dispatch(resultSet, statement, connection);
        } 
        return user;
    }
    
}
