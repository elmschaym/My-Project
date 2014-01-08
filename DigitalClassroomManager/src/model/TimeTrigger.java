/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author sam
 */
public class TimeTrigger {
    
    private String semester;
    private String acadYear;
 
    public String getSemester() {
        
        Date date =new Date();
        int month = date.getMonth()+1;
        if ( month >= 6 && month <= 9 )
            semester = "1st Semester";
        
        else if ( month >= 10 && month <= 12 )
            semester = "2nd Semester";
        else if ( month > 2 && month < 4 )
            semester = "Summer";
        
        return semester;
    }
    
    public String getAcadYear ()
    {
        Date date =new Date();
        int year = (date.getYear()-100)+2000;
        String sem = getSemester();
        
        if ( sem.equals("1st Semester") || sem.equals("2nd Semester"))
        {
            acadYear = year+" - "+(year+1);
        }
        else
            acadYear = (year-1)+" - "+year;
        
        return acadYear;
    }
    
}
