/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.ClassDaoImplementation;
import dataaccesobject.interfaces.ClassDaoInterface;
import java.util.ArrayList;
import model.Classes;
import service.interfaces.ClassServiceInterface;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class ClassServiceImplementation implements ClassServiceInterface {
    ClassDaoInterface classd = new ClassDaoImplementation();
    
    @Override
    public void addClass(Classes classes) throws ErrorException {
        classd.addClass(classes);
    }
    @Override
    public ArrayList<Classes> getClasses() throws ErrorException{
        return classd.getClasses();
    }
    @Override
    public ArrayList<Classes> getClassesUniq() throws ErrorException{
        return classd.getClassesUniq();
    }
    @Override
    public void editClass(Classes classes) throws ErrorException{
        classd.editClass(classes);
    }
    @Override
    public void deleteClass(int classID) throws ErrorException {
        classd.deleteClass(classID);
    }
    @Override
    public void changeAttempt(Classes classes) throws ErrorException {
        classd.changeAttempt(classes);
    }
    
    @Override
    public Classes getClass(String name, String section, String sem, String year) throws ErrorException{
        return classd.getClass(name, section, sem, year);
    }
    
    
    @Override
    public Classes getCurrentClass(String subject, String section, String room, String schedule) throws ErrorException{
        return classd.getCurrentClass(subject, section, room, schedule);
    }

    @Override
    public void insertPassingRate(int classid, int score) throws ErrorException {
        classd.insertPassingRate(classid, score);
    }

    @Override
    public Classes getAttempt(int classid) throws ErrorException {
        return classd.getAttempt(classid);
    }

    @Override
    public Classes getPercentage(int classid) throws ErrorException {
        return classd.getPercentage(classid);
    }
    
    public Classes getClass(int classid) throws ErrorException {
        return classd.getClass(classid);
    }
}
