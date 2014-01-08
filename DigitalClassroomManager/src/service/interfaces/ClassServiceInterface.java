/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.interfaces;

import java.util.ArrayList;
import model.Classes;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface ClassServiceInterface {
    public void addClass(Classes classes) throws ErrorException;
    public ArrayList<Classes> getClasses() throws ErrorException;
    public ArrayList<Classes> getClassesUniq() throws ErrorException;
    public void editClass(Classes classes) throws ErrorException;
    public void deleteClass(int classID) throws ErrorException;
    public void changeAttempt(Classes classes) throws ErrorException;
    public Classes getAttempt(int classid) throws ErrorException;
    public void insertPassingRate(int classid,int score) throws ErrorException;
    public Classes getClass(String name, String section, String sem, String year) throws ErrorException;
    public Classes getCurrentClass(String subject, String section, String room, String schedule) throws ErrorException;
    public Classes getPercentage(int classid)throws ErrorException;
    public Classes getClass(int classid) throws ErrorException;
}
