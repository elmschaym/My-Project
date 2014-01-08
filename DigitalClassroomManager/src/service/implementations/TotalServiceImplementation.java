/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.TotalDaoImplementation;
import dataaccesobject.interfaces.TotalDaoInterface;
import java.util.ArrayList;
import model.Total;
import service.interfaces.TotalServiceInterface;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public class TotalServiceImplementation implements TotalServiceInterface{
    TotalDaoInterface totalD=new TotalDaoImplementation();
    @Override
    public void addColumn(Total total) throws ErrorException {
        totalD.addColumn(total);
    }

    @Override
    public void addStudentTotal(Total total) throws ErrorException {
        totalD.addStudentTotal(total);
    }

    @Override
    public ArrayList<Total> getValue(Total total) throws ErrorException {
        return totalD.getValue(total);
    }

    @Override
    public void addTriggerInsert(Total total) throws ErrorException {
        totalD.addTriggerInsert(total);
    }

    @Override
    public void addTriggerUpdate(Total total) throws ErrorException {
        totalD.addTriggerUpdate(total);
    }
    
}
