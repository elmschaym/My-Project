/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.interfaces;

import java.util.ArrayList;
import model.Total;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public interface TotalServiceInterface {
    public void addColumn(Total total) throws ErrorException; 
    public void addStudentTotal(Total total)throws ErrorException;
    public ArrayList<Total> getValue(Total total) throws ErrorException;
    public void addTriggerInsert(Total total)throws ErrorException;    
    public void addTriggerUpdate(Total total)throws ErrorException;
}
