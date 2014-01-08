/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.SubjectDaoImplementation;
import dataaccesobject.interfaces.SubjectDaoInterface;
import java.util.ArrayList;
import model.Subject;
import service.interfaces.SubjectServiceInterface;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class SubjectServiceImplementation implements SubjectServiceInterface  {
    SubjectDaoInterface subj= new SubjectDaoImplementation();

    @Override
    public void addSubject(Subject subject) throws ErrorException {
        subj.addSubject(subject);
    }

    @Override
    public ArrayList<Subject> getSubjects() throws ErrorException {
        return subj.getSubjects();
    }

    @Override
    public void deleteSubject(int subjID) throws ErrorException {
        subj.deleteSubject(subjID);
    }

    @Override
    public Subject getSubject(String subject) throws ErrorException {
        return subj.getSubject(subject);
    }

    @Override
    public void editSubject(Subject subject) throws ErrorException {
        subj.editSubject(subject);
    }
    @Override
    public ArrayList<Subject> getSubjectSem(String semester) throws ErrorException {
        return subj.getSubjectSem(semester);
    }
    
}
