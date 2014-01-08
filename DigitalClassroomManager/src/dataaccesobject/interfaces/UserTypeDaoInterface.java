/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.interfaces;

import model.UserType;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface UserTypeDaoInterface {
    public void addUserType(UserType usertype) throws ErrorException;
    public void editUserType(UserType usertype)throws ErrorException;    
}
