/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.interfaces;

import java.util.ArrayList;
import model.SecurityCode;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public interface SecurityCodeDaoInterface {
    public void addSecurityCode(SecurityCode code) throws ErrorException;
    public void editSecurityCode(SecurityCode code)throws ErrorException;  
    public SecurityCode getSecurityCode(String code)throws ErrorException;  
    public SecurityCode getSecurityCode() throws ErrorException;
    
}
