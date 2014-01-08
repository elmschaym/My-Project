/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.interfaces;

import java.util.ArrayList;
import model.UserType;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface UserTypeService {
    public void addUserType(UserType usertype) throws ErrorException;
    public void editUserType(UserType usertype)throws ErrorException;
    
}
