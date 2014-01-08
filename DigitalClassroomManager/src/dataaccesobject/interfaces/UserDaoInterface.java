/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.interfaces;

import java.util.ArrayList;
import model.User;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface UserDaoInterface {
    public void addUser(User user) throws ErrorException;
    public void editUser(User user)throws ErrorException;
    public void editPhoto(User user)throws ErrorException;
    public void deleteUser(int idNumber)throws ErrorException;
    public User getUser(int userid)throws ErrorException;
    public ArrayList<User> getUsers() throws ErrorException;
    public ArrayList<User> getUsers(User user)throws ErrorException;
    public ArrayList<User> getUserID(User user)throws ErrorException;
    public void editUserType(User userID) throws ErrorException;
    public User getUserTypeID(int usertype)throws ErrorException;
  

    
}
