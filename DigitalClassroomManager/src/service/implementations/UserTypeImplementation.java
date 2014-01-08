/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.UserTypeDaoImplementation;
import dataaccesobject.interfaces.UserTypeDaoInterface;
import model.UserType;
import service.interfaces.UserTypeService;
import tools.ErrorException;



/**
 *
 * @author POTCHOLO
 */
public class UserTypeImplementation implements UserTypeService{
    UserTypeDaoInterface usertyped= new UserTypeDaoImplementation();
    @Override
    public void addUserType(UserType usertype) throws ErrorException {
        usertyped.addUserType(usertype);
    }

    @Override
    public void editUserType(UserType usertype) throws ErrorException {
       usertyped.editUserType(usertype);
    }

}
