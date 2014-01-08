/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author POTCHOLO
 */
public class GradingSystem {
   
    private String entryName;
    private String percentage;
    private boolean empty;
    private boolean done;
  private int checkPercentage;
 

   
    public int getCheckPercentage() {
        return checkPercentage;
    }

    public void setCheckPercentage(int checkPercentage) {
        this.checkPercentage = checkPercentage;
    }
    public boolean isEmpty() {
        return empty;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
    private int gsID;
    private int classID;

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        
        this.classID = classID;
    }

    public int getGsID() {
        return gsID;
    }

    public void setGsID(int gsID) {
        this.gsID = gsID;
    }
  
    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
    
}
