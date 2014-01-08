/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author POTCHOLO
 */
public class ClassRecord {
    private int classrecordID;
    private int studentID;
    private int classID;
    private String date;
    private String year;
    private String semester;
    private String score;
    private String perfectScore;
    private String name;
    private boolean empty;
    private boolean dateempty;
    private String totalScore;
    private String totalPerfectScore;

    public String getTotalPerfectScore() {
        return totalPerfectScore;
    }

    public void setTotalPerfectScore(String totalPerfectScore) {
        this.totalPerfectScore = totalPerfectScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public boolean isDateempty() {
        return dateempty;
    }

    public void setDateempty(boolean dateempty) {
        this.dateempty = dateempty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPerfectScore() {
        return perfectScore;
    }

    public void setPerfectScore(String perfectScore) {
        this.perfectScore = perfectScore;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }
    private String columnName;
    private String refTableName;
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public int getClassrecordID() {
        return classrecordID;
    }

    public void setClassrecordID(int classrecordID) {
        this.classrecordID = classrecordID;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getRefTableName() {
        return refTableName;
    }

    public void setRefTableName(String refTableName) {
        this.refTableName = refTableName;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    
    
}
