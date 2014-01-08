/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import javax.swing.JOptionPane;

/**
 *
 * @author POTCHOLO
 */
public class ErrorException extends Exception{

    private String message;
    private String exceptionType;

    public ErrorException(String message,String exceptionType){
       this.message = message;
       this.exceptionType = exceptionType;
    }

    public void printMessage(){
         JOptionPane.showMessageDialog(null, message, exceptionType, JOptionPane.ERROR_MESSAGE);
    }

}

