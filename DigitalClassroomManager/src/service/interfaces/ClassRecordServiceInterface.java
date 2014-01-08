/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.interfaces;

import java.util.ArrayList;
import model.ClassRecord;
import model.Classes;
import model.User;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface ClassRecordServiceInterface {
   public void createTable(ClassRecord classrecord) throws ErrorException;
   public void addStudentClassRecord (ClassRecord classrecord)throws ErrorException;
   public void addColumn(ClassRecord classrecord) throws ErrorException; 
   public ArrayList<ClassRecord> getValue(ClassRecord classrecord) throws ErrorException;
   public ArrayList<ClassRecord> getValue(int classid, int studenid, String name) throws ErrorException;
   public void insertScore(ClassRecord classrecord)throws ErrorException;
   public ArrayList<ClassRecord> getClassRecord(ClassRecord classrecord) throws ErrorException;
   public ClassRecord getClassRecord(int clasid,String date, String name, int studenid) throws ErrorException ;
   public ClassRecord getClassRecord(int clasid,String date, String name) throws ErrorException ;
   public void updateAttendance(ClassRecord classrecord)throws ErrorException;
   public void updateScore(ClassRecord classrecord)throws ErrorException;
   public ClassRecord getClassRecord(int clasid, int studentid) throws ErrorException ;
   public ArrayList<ClassRecord> getClassDate(ClassRecord classrecord) throws ErrorException;
   public void insertAttendance(ClassRecord classrecord) throws ErrorException;
   public void updateAttendanceNoDate(ClassRecord classrecord) throws ErrorException ;
   public void updateTotal(ClassRecord classrecord)throws ErrorException ;
   public ArrayList<ClassRecord> getTotals(int classid, String columnName) throws ErrorException;
   public ArrayList<ClassRecord> getAttendance(int classid, int StudentID) throws ErrorException;
   public void updateAttendancePerfect(ClassRecord classrecord) throws ErrorException ;
}
