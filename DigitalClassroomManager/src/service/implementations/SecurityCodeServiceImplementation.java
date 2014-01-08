/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.SecurityCodeDaoImplementation;
import dataaccesobject.interfaces.SecurityCodeDaoInterface;
import java.util.ArrayList;
import model.SecurityCode;
import service.interfaces.SecurityCodeServiceInterface;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public class SecurityCodeServiceImplementation implements SecurityCodeServiceInterface {
    SecurityCodeDaoInterface codeD= new SecurityCodeDaoImplementation();
    @Override
    public void addSecurityCode(SecurityCode code) throws ErrorException {
        codeD.addSecurityCode(code);
    }

    @Override
    public void editSecurityCode(SecurityCode code) throws ErrorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SecurityCode getSecurityCode() throws ErrorException {
       return codeD.getSecurityCode();
    }

    @Override
    public SecurityCode getSecurityCode(String code) throws ErrorException {
        return codeD.getSecurityCode(code);
    }
    
}
