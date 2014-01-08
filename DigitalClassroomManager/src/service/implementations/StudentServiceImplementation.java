/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.StudentDaoImplementation;
import dataaccesobject.interfaces.StudentDaoInterface;
import java.util.ArrayList;
import model.Student;
import service.interfaces.StudentServiceInterface;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class StudentServiceImplementation implements StudentServiceInterface {
    StudentDaoInterface studentd= new StudentDaoImplementation();
    
 
    @Override
    public void addStudent(Student student) throws ErrorException {
        studentd.addStudent(student);
    }

    @Override
    public ArrayList<Student> getStudents(int classid) throws ErrorException {
        return studentd.getStudents(classid);
    }

    @Override
    public ArrayList<Student> getStudents(String student) throws ErrorException {
       return studentd.getStudents(student);
    }

    @Override
    public void editStudent(Student student) throws ErrorException {
       studentd.editStudent(student);
    }

    @Override
    public void deleteStudent(int idNumber) throws ErrorException {
        studentd.deleteStudent(idNumber);
    }

    @Override
    public void editPhoto(Student stnt) throws ErrorException {
        studentd.editPhoto(stnt);
    }

    @Override
    public Student getStudent(int stundeID) throws ErrorException {
       return studentd.getStudent(stundeID);
    }
    
}
