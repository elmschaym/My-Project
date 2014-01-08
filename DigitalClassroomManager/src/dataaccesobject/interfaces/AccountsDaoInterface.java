/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.interfaces;

import java.util.ArrayList;
import model.Accounts;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface AccountsDaoInterface {
    public void addAccounts(Accounts account) throws ErrorException;
    public void editAccounts(Accounts account)throws ErrorException;
    public void deleteAccounts(int idNumber)throws ErrorException;
    public ArrayList<Accounts> getAccounts() throws ErrorException;
    public ArrayList<Accounts> getAccounts(Accounts account)throws ErrorException;
    public Accounts getAccount(String username)throws ErrorException;
    public Accounts getAccount(int idNumber)throws ErrorException;

    
}
