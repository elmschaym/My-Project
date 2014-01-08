/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementations;

import dataaccesobject.implementations.ClassRecordDaoImplementation;
import dataaccesobject.interfaces.ClassRecordDaoInterface;
import java.util.ArrayList;
import model.ClassRecord;
import model.Classes;
import model.User;
import service.interfaces.ClassRecordServiceInterface;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public class ClassRecordServiceImplementation implements ClassRecordServiceInterface {
    ClassRecordDaoInterface classrD= new ClassRecordDaoImplementation();
    @Override
    public void addColumn(ClassRecord classrecord) throws ErrorException {
        classrD.addColumn(classrecord);
    }

    @Override
    public void createTable(ClassRecord classrecord) throws ErrorException {
        classrD.createTable(classrecord);
    }

    @Override
    public void addStudentClassRecord(ClassRecord classrecord) throws ErrorException {
        classrD.addStudentClassRecord(classrecord);
    }

    @Override
    public ArrayList<ClassRecord> getValue(ClassRecord classrecord) throws ErrorException {
        return classrD.getValue(classrecord);
    }

    @Override
    public void insertScore(ClassRecord classrecord) throws ErrorException {
        classrD.insertScore(classrecord);
    }

    @Override
    public ArrayList<ClassRecord> getClassRecord(ClassRecord classrecord) throws ErrorException {
        return classrD.getClassRecord(classrecord);
         
    }

    @Override
    public ClassRecord getClassRecord(int clasid,String date, String name, int studenid) throws ErrorException {
       return classrD.getClassRecord(clasid,date, name,  studenid);
    }

    @Override
    public void updateAttendance(ClassRecord classrecord) throws ErrorException {
        classrD.updateAttendance(classrecord);
    }

    @Override
    public ClassRecord getClassRecord(int clasid, int studentid) throws ErrorException {
        return classrD.getClassRecord(clasid, studentid);
    }

    @Override
    public ArrayList<ClassRecord> getClassDate(ClassRecord classrecord) throws ErrorException {
        return classrD.getClassDate(classrecord);
    }

    @Override
    public void updateScore(ClassRecord classrecord) throws ErrorException {
       classrD.updateScore(classrecord);
    }

    @Override
    public ClassRecord getClassRecord(int clasid, String date, String name) throws ErrorException {
        return classrD.getClassRecord(clasid, date, name);
    }

    @Override
    public void insertAttendance(ClassRecord classrecord) throws ErrorException {
        classrD.insertAttendance(classrecord);
    }

    @Override
    public void updateAttendanceNoDate(ClassRecord classrecord) throws ErrorException {
        classrD.updateAttendanceNoDate(classrecord);
    }

    @Override
    public ArrayList<ClassRecord> getValue(int classid, int studenid, String name) throws ErrorException {
        return classrD.getValue(classid, studenid, name);
    }

    @Override
    public void updateTotal(ClassRecord classrecord) throws ErrorException {
        classrD.updateTotal(classrecord);
    }
    
    @Override
    public ArrayList<ClassRecord> getTotals(int classid, String columnName) throws ErrorException {
        return classrD.getTotals(classid, columnName);
    }

    @Override
    public ArrayList<ClassRecord> getAttendance(int classid, int StudentID) throws ErrorException {
        return classrD.getAttendance(classid, StudentID);
    }

    @Override
    public void updateAttendancePerfect(ClassRecord classrecord) throws ErrorException {
        classrD.updateAttendancePerfect(classrecord);
    }


    
}
