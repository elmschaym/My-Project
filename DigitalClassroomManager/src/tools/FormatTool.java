/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author POTCHOLO
 */
import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Formatter;
public class FormatTool {
        public static final int IMAGE_WIDTH = 105;
    public static final int IMAGE_HEIGHT = 105;
    public static final int THUMBNAIL_WIDTH = 150;
    public static final int THUMBNAIL_HEIGHT = 150;
    public static final int LARGE_IMAGE_WIDTH = 250;
    public static final int LARGE_IMAGE_HEIGHT = 250;
    public static final int large=200;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
        
    public static final String[] numbers = {
        "","One","Two","Three","Four","Five","Six",
        "Seven","Eight","Nine","Ten","Eleven","Twelve",
        "Thirteen","Fourteen","Fifteen","Sixteen","Seventeen",
        "Eighteen"
    };
    public static final String[] tens = {
        "","teen","Twenty","Thirty","Fourty","Fifty",
        "Sixty","Seventy","Eighty","Ninety"
    };
    

    public String date;

    public FormatTool(){
        date = "";
    }
    /**
     * @author Jireh & Mark 
     * @param object
     * @return 
     */
    public Image convertToActualSizeImage(Blob object,int w) {
        if(w==1){
           return convertBlobToImage(object,large,large); 
        }
        else if(w==2){
           return convertBlobToImage(object,100,100);  
        }
        else if(w==3){
            return convertBlobToImage(object,large,230);
        }
        return convertBlobToImage(object,IMAGE_WIDTH,IMAGE_HEIGHT);
    }
    
    /**
     * @author Jireh & Mark 
     * @param object
     * @return 
     */
    private static Image convertBlobToImage(Blob object, int width, int height){
        Image photo = null;
        InputStream input;
        try {
            input = object.getBinaryStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] rb = new byte[1024];
            int ch = 0;
            while ((ch = input.read(rb)) != -1) {
                output.write(rb, 0, ch);
            }
            byte[] b = output.toByteArray();
            input.close();
            output.close();
            photo = Toolkit.getDefaultToolkit().createImage(b).getScaledInstance(width, height, 100);
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, ex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return photo;
    }

    public String currentDay(int day){
        String toDay = null;
        if(day==1)
            toDay = "Monday";
        else if(day==2)
            toDay = "Tuesday";
        else if(day==3)
            toDay = "Wednesday";
        else if(day==4)
            toDay = "Thursday";
        else if(day==5)
            toDay = "Friday";
        else if(day==6)
            toDay = "Saturday";
        else 
            toDay = "Sunday";
        
        return toDay;
    }
    
    
    public String convertDateStringToSQLDate(String stringDate){
        
        String month = "";
        String day = "";
        String year = "";
    
        int numberOfCharactersInDate = 11;
        char[] dateArray = new char[numberOfCharactersInDate];
        
       
        int index = 0;
        char character = '/';
        for(int i = 0; i < stringDate.length(); i++){
            character = stringDate.charAt(i);
            dateArray[index] = character;
            index++;
        }
        
        
        
        int monthIndex = 0;
        //get the month from the string
        while((monthIndex < numberOfCharactersInDate)&&(character != '-')){
            character = dateArray[monthIndex];
            if(character!='-')
            month += character;
            
            monthIndex++;
        }
        
        //get the date from the string
        char dateChar = '/';
        int dateIndex = monthIndex;
        while((dateIndex < numberOfCharactersInDate) && (dateChar != '-')){
            
            dateChar = dateArray[dateIndex];
            if(dateChar!='-')
            day += dateChar;
            
            dateIndex++;
        }
        
        char yearChar = '/';
        int yearIndex = dateIndex;
        while(yearIndex < numberOfCharactersInDate){
            
            yearChar = dateArray[yearIndex];
            if(yearChar!='-')
            year += yearChar;
            yearIndex++;
        }
        
        
        
        
        
        //convert string to digits
        if (month.equals("Jan")) {
            month = "01";
        } else if (month.equals("Feb")) {
            month = "02";
        } else if (month.equals("Mar")) {
            month = "03";
        } else if (month.equals("Apr")) {
            month = "04";
        } else if (month.equals("May")) {
            month = "05";
        } else if (month.equals("Jun")) {
            month = "06";
        } else if (month.equals("Jul")) {
            month = "07";
        } else if (month.equals("Aug")) {
            month = "08";
        } else if (month.equals("Sep")) {
            month = "09";
        } else if (month.equals("Oct")) {
            month = "10";
        } else if (month.equals("Nov")) {
            month = "11";
        } else if (month.equals("Dec")) {
            month = "12";
        }
        
        
        
        date = year + "-" + month + "-" + day;
        return date;
    }
    
    public String convertSQLDateToDateString(String sqlDate){
        
        String month = "";
        String day = "";
        String year = "";
    
        int numberOfCharactersInDate = 11;
        char[] dateArray = new char[numberOfCharactersInDate];
        
       
        int index = 0;
        char character = '/';
        for(int i = 0; i < sqlDate.length(); i++){
            
            character = sqlDate.charAt(i);
            dateArray[index] = character;
            index++;
        }
        
        
        
        int yearIndex = 0;
        //get the year from the string
        while((yearIndex < numberOfCharactersInDate)&&(character != '-')){
            
            character = dateArray[yearIndex];
            if(character!='-')
            year += character;
            
            
            yearIndex++;
        }
        
        //get the month from the string
        char monthChar = '/';
        int monthIndex = yearIndex;
        while((monthIndex < numberOfCharactersInDate) && (monthChar != '-')){
            
            monthChar = dateArray[monthIndex];
            if(monthChar!='-')
            month += monthChar;
            
            monthIndex++;
        }
        
        char dayChar = '/';
        int dayIndex = monthIndex;
        while(dayIndex < numberOfCharactersInDate-1){
           
            dayChar = dateArray[dayIndex];
            if(dayChar!='-')
            day += dayChar;
            dayIndex++;
        }
        
        //convert string to digits
        if (month.equals("01")) {
            month = "Janauary";
        } else if (month.equals("02")) {
            month = "February";
        } else if (month.equals("03")) {
            month = "March";
        } else if (month.equals("04")) {
            month = "Apr";
        } else if (month.equals("05")) {
            month = "May";
        } else if (month.equals("06")) {
            month = "June";
        } else if (month.equals("07")) {
            month = "July";
        } else if (month.equals("08")) {
            month = "August";
        } else if (month.equals("09")) {
            month = "September";
        } else if (month.equals("10")) {
            month = "October";
        } else if (month.equals("11")) {
            month = "November";
        } else if (month.equals("12")) {
            month = "December";
        }
        
        date = month + " " + day + ", " + year;
        return date;
    }
    
//    public void convertAmountInFiguresToWords(Double amountInFigures){
//        
//        double billion = 1000000000;
//        double million = 1000000;
////        double hundredThousand = 100000
//        String amountInWords = "";
//        double quotient = 0;
//        
//        if(amountInFigures > billion){
//            quotient = amountInFigures / billion;
//            amountInFigures = amountInFigures % billion;
//            amountInWords = convertANumber(quotient) + " Billion";
//        }
//        
//        if(amountInFigures > million){
//            quotient = amountInFigures / million;
//            amountInFigures = amountInFigures % million;
//            amountInWords = amountInWords + " " + quotient + " Million";
//        }
//        
////        if(amountInFigures > ){
////           999 999 999 999
////        }
//    }
    
    
    public String makeWord(Double number){
        String s = "";
        System.out.println("under the makeWord" + number);
        if(number<0){
            s="negative ";
            number*=-1;
        }
        
        s+=toWord(number);
        s = s.replace(" zero","");
 
        return s;
    }
    public String toWord(Double number){
        System.out.println("under to word" + number);
                
        if(number>=1000000000){
            return toWord(number/1000000000)+" Billion "+toWord(number%1000000000);
        }
        else if(number>=1000000){
            return toWord(number/1000000)+" Million "+toWord(number%1000000);
        }
        else if(number>=1000){
            return "" +toWord(number/1000)+" Thousand "+toWord(number%1000);
        }
        else if(number>=100){
            
            return numbers[number.intValue()/100] +" Hundred "+toWord(number%100);
        }
        else if(number>=20){
            return tens[number.intValue()/10] + " " + toWord(number%10);
        }
        else if(number>18){
            return numbers[number.intValue()%10]+"teen";
        } 
        else if((number < 1) && (number > 0)){
            String decimalPlaces = number + "";
            String newNumberString = "";
           // char[] decimalCharacters = decimalPlaces.toCharArray();
           // decimalPlaces = "";
            System.out.println("LENGTH : " + decimalPlaces.length());
            for(int i = 1; i < decimalPlaces.length(); i++){
                char character = decimalPlaces.charAt(i);
                if(character != '.' ){
                    newNumberString += character;
                    System.out.println("char :"  + character);
                } else {
                    System.out.println("FOUND THE (" + character + ") !!!");
                }
            }
            number = this.roundUpToTwoDecimals(newNumberString);
            System.err.println("NEW NUMBER : " + number);
            
            return "Peso(s) and "+toWord(number) + "Centavo(s)";
        }        
        else{
            int numInt = number.intValue();
            double numDouble = number - numInt;
           
            if(numInt == 0.0){
                return numbers[number.intValue()];
            } else {
                return numbers[number.intValue()] + " " + toWord(numDouble);
            }
        }
    }
    
    public Double roundUpToTwoDecimals(String numberString){
        System.out.println("INPUT STRING: " + numberString);
        String numString = numberString + "";
        char[] numCharArray = numberString.toCharArray();
        int x = numString.length() - 1;
        for(int i = numCharArray.length - 1; i >= 1 ; i--){
            char numChar = numCharArray[i];
            System.out.println("CHAR in round : " + numChar );
            if(numChar < 5){
                numCharArray[i] = ' ';
                if(i == 1){
                    numCharArray[i] = '0';
                }
            } else {
                numCharArray[i] = ' ';
                char prevChar = numCharArray[i-1];
                double prevInt = Double.parseDouble(prevChar + "");
                double prevInt2 = prevInt++;
                numCharArray[i-1] = (char)prevInt2;
                if(i == 1){
                    numCharArray[i] = '0';
                }
                
            }
        }
        
        String resultString = "";
        
        
        for(int i = 0; i < numCharArray.length; i++){
            resultString += numCharArray[i];
        }
        if(numCharArray.length == 1){
            resultString += "0";
        }
        
        System.out.println("OUTPUT STRING: " + resultString);
        return Double.parseDouble(resultString);
    }
    
    public static Formatter twoDigit(double cost){
        Formatter format = new Formatter();
        format.format("%.2f",cost);
        return format;
    }
//   ORIGINAL toWord 
//   public String toWord(int number){
//        System.out.println("under to word" + number);
//                
//        if(number>=1000000000){
//            return toWord(number/1000000000)+" Billion "+toWord(number%1000000000);
//        }
//        else if(number>=1000000){
//            return toWord(number/1000000)+" Million "+toWord(number%1000000);
//        }
//        else if(number>=1000){
//            return "" +toWord(number/1000)+" Thousand "+toWord(number%1000);
//        }
//        else if(number>=100){
//            
//            return numbers[(int)number/100] +" Hundred "+toWord((int)number%(int)100);
//        }
//        else if(number>=20){
//            return tens[(int)number/10] + " " + toWord(number%10);
//        }
//        else if(number>18){
//            return numbers[(int)number%10]+"teen";
//        }
//        else{
//            return numbers[(int)number];
//        }
//        
//    }
    
    public String conveter(String thedate){
        String date="";
        String month="";
        String year="";
        String time="";
        AboutStrings tool=new AboutStrings();
        month=thedate.substring(0, 3);
        if(tool.isNumber(thedate.charAt(4))&&tool.isNumber(thedate.charAt(5))){
            date=date+thedate.charAt(4)+thedate.charAt(5);
            //System.out.println("Condi1="+date);
            year=year+thedate.substring(8, 12);
          //  System.out.println("Condi1="+year);
            time=time+thedate.substring(13, 18);
           // System.out.println("Condi1="+time);
        }
        else if(tool.isNumber(thedate.charAt(4))&&!tool.isNumber(thedate.charAt(5))){
            date=date+thedate.charAt(4);
         //   System.out.println("Condi2="+date);
            year=year+thedate.substring(7, 11);
        //    System.out.println("Condi2="+year);
            time=time+thedate.substring(12, 17);
        //    System.out.println("Condi2="+time);
        }
        ;
        ;
        
         if (month.equals("Jan")) {
            month = "01";
        } else if (month.equals("Feb")) {
            month = "02";
        } else if (month.equals("Mar")) {
            month = "03";
        } else if (month.equals("Apr")) {
            month = "04";
        } else if (month.equals("May")) {
            month = "05";
        } else if (month.equals("Jun")) {
            month = "06";
        } else if (month.equals("Jul")) {
            month = "07";
        } else if (month.equals("Aug")) {
            month = "08";
        } else if (month.equals("Sep")) {
            month = "09";
        } else if (month.equals("Oct")) {
            month = "10";
        } else if (month.equals("Nov")) {
            month = "11";
        } else if (month.equals("Dec")) {
            month = "12";
        }
        ;
        ;
        if(Integer.parseInt(date)<10){
            date="0"+date;
        }
        return year+"-"+month+"-"+date;
    }
    
}
