/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author PAL2X
 */
public class CurrentUser {
        private String currentUser;
        private int userID;
        private int classID;
        private String nameSubject;
        private String sectionSubject;
        private String semesterSubject;
        private String yearSubject;
        private String room;
        private String schedule;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getNameSubject() {
        return nameSubject;
    }

    public void setNameSubject(String nameSubject) {
        this.nameSubject = nameSubject;
    }

    public String getSectionSubject() {
        return sectionSubject;
    }

    public void setSectionSubject(String sectionSubject) {
        this.sectionSubject = sectionSubject;
    }

    public String getSemesterSubject() {
        return semesterSubject;
    }

    public void setSemesterSubject(String semesterSubject) {
        this.semesterSubject = semesterSubject;
    }

    public String getYearSubject() {
        return yearSubject;
    }

    public void setYearSubject(String yearSubject) {
        this.yearSubject = yearSubject;
    }
    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
}
