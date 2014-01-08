/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.interfaces;

import java.util.ArrayList;
import model.Student;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface StudentServiceInterface {
    public void addStudent(Student student) throws ErrorException;
    public void editStudent(Student student)throws ErrorException;
    public void deleteStudent(int idNumber)throws ErrorException;
    public ArrayList<Student> getStudents( int classid) throws ErrorException;
    public ArrayList<Student> getStudents(String student)throws ErrorException;
    public void editPhoto(Student stnt)throws ErrorException;
    public Student getStudent(int stundeID)throws ErrorException;
}
