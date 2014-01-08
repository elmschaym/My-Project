/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.interfaces;

import java.util.ArrayList;
import model.GradingSystem;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public interface GradingSystemDaoInterface {
    public void addGradingSystem(GradingSystem gs) throws ErrorException; 
    public void addGradingSystemEntry(GradingSystem gs) throws ErrorException; 
    public GradingSystem getGSfk(String name, int clasID)throws ErrorException;
    public void deleteEntry(int subjID,String name)throws ErrorException;
    public ArrayList<GradingSystem> getGradingSystem() throws ErrorException;
    public ArrayList<GradingSystem> getGradingSystem(int classID) throws ErrorException;
    public GradingSystem getGradingSystem(String name) throws ErrorException;
    public void updateGradingSystem(GradingSystem gs)throws ErrorException;
}
