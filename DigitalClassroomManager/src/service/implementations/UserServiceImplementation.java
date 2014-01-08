/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.UserDaoImplementation;
import dataaccesobject.interfaces.UserDaoInterface;
import java.util.ArrayList;
import model.User;
import service.interfaces.UserServiceInterface;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class UserServiceImplementation implements UserServiceInterface {
    UserDaoInterface instructorD= new UserDaoImplementation();
    @Override
    public void addUser(User user) throws ErrorException {
        instructorD.addUser(user);
    }

    @Override
    public void editUser(User user) throws ErrorException {
       instructorD.editUser(user);
    }

    @Override
    public void deleteUser(int idNumber) throws ErrorException {
        instructorD.deleteUser(idNumber);
    }

    @Override
    public ArrayList<User> getUsers() throws ErrorException {
        return instructorD.getUsers();
    }

    @Override
    public ArrayList<User> getUsers(User user) throws ErrorException {
        return instructorD.getUsers(user);
    }

    @Override
    public ArrayList<User> getUserID(User user) throws ErrorException {
        return instructorD.getUsers(user);
    }

    @Override
    public User getUser(int userid) throws ErrorException {
        return instructorD.getUser(userid);
    }

    @Override
    public void editPhoto(User user) throws ErrorException {
        instructorD.editPhoto(user);
    }

    @Override
    public void editUserType(User userID) throws ErrorException {
        instructorD.editUserType(userID);
    }

    @Override
    public User getUserTypeID(int usertype) throws ErrorException {
       return instructorD.getUserTypeID(usertype);
    }
    
}
