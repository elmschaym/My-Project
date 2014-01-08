/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.AccountsDaoImplementation;
import dataaccesobject.interfaces.AccountsDaoInterface;
import java.util.ArrayList;
import model.Accounts;
import service.interfaces.AccountsServiceInterface;
import tools.ErrorException;



/**
 *
 * @author POTCHOLO
 */
public class AccountsServiceImplementation implements AccountsServiceInterface{
    AccountsDaoInterface accountd= new AccountsDaoImplementation();

    @Override
    public void addAccounts(Accounts account) throws ErrorException {
       accountd.addAccounts(account);
    }

    @Override
    public void editAccounts(Accounts account) throws ErrorException {
        accountd.editAccounts(account);
    }

    @Override
    public void deleteAccounts(int idNumber) throws ErrorException {
        accountd.deleteAccounts(idNumber);
    }

    @Override
    public ArrayList<Accounts> getAccounts() throws ErrorException {
        return accountd.getAccounts();
    }

    @Override
    public ArrayList<Accounts> getAccounts(Accounts account) throws ErrorException {
       return accountd.getAccounts(account);
    }


    @Override
    public Accounts getAccount(String username) throws ErrorException {
        return accountd.getAccount(username);
    }

    @Override
    public Accounts getAccount(int idNumber) throws ErrorException {
        return accountd.getAccount(idNumber);
    }
  
    
}
