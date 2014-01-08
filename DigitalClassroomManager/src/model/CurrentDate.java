/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Sam
 */
public class CurrentDate {
    Date date = new Date();
    String sem = null;
    public String getSemester(){
        int month = date.getMonth()+1;
        System.out.print("GAGU NA MONTH"+month);
        
        if(month > 6 && month < 11){
            sem = "1st Semester";
        }
        else if (month > 10||month<4){
            sem = "2nd Semester";
        }
        else if (month == 4 && month == 5){
            sem = "Summer";
        }
        System.out.println("paksit "+sem);
        return sem;
    }
    
    public String getAcadYear(){
        int year = date.getYear()+1900;
        
        String acadYear = year+" - "+(year+1);
        
        return acadYear;
    }
    
}
