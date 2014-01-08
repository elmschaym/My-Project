/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.interfaces;

import java.util.ArrayList;
import model.Subject;
import tools.ErrorException;


/**
 *
 * @author POTCHOLO
 */
public interface SubjectDaoInterface {
     public void addSubject(Subject subject) throws ErrorException;
     public ArrayList<Subject> getSubjects() throws ErrorException;
     public void deleteSubject(int subjID)throws ErrorException;
     public Subject getSubject(String subject) throws ErrorException;
     public void editSubject(Subject subject) throws ErrorException;
     public ArrayList<Subject> getSubjectSem(String semester) throws ErrorException;
}
