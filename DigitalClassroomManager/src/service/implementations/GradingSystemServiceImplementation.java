/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.GradingSystemDaoImplementation;
import dataaccesobject.interfaces.GradingSystemDaoInterface;
import java.util.ArrayList;
import model.GradingSystem;
import service.interfaces.GradingSystemServiceInterface;
import tools.ErrorException;

/**
 *
 * @author PAL2X
 */
public class GradingSystemServiceImplementation implements GradingSystemServiceInterface{
   GradingSystemDaoInterface gradingD=new GradingSystemDaoImplementation();
    @Override
    public void addGradingSystem(GradingSystem gs) throws ErrorException {
        gradingD.addGradingSystem(gs);
    }

    @Override
    public GradingSystem getGSfk(String name, int clasID) throws ErrorException {
        return gradingD.getGSfk(name,clasID);
    }

    @Override
    public ArrayList<GradingSystem> getGradingSystem() throws ErrorException {
        return gradingD.getGradingSystem();
    }

    @Override
    public void addGradingSystemEntry(GradingSystem gs) throws ErrorException {
        gradingD.addGradingSystemEntry(gs);
    }

    @Override
    public GradingSystem getGradingSystem(String name) throws ErrorException {
        return gradingD.getGradingSystem(name);
    }

    @Override
    public void updateGradingSystem(GradingSystem gs) throws ErrorException {
        gradingD.updateGradingSystem(gs);
    }

    @Override
    public ArrayList<GradingSystem> getGradingSystem(int classID) throws ErrorException {
        return gradingD.getGradingSystem(classID);
    }

    @Override
    public void deleteEntry(int subjID, String name) throws ErrorException {
        gradingD.deleteEntry(subjID, name);
    }
    
}
