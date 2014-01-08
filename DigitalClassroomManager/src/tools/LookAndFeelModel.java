/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author POTCHOLO
 */
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LookAndFeelModel {
    
    public static void setLookAndFeelModel()
    {

        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();

        try
        {
            UIManager.setLookAndFeel(lookAndFeel);
        }
        catch (UnsupportedLookAndFeelException e)
        {
            ErrorException exception = new ErrorException(e.getMessage(),"UnsupportedLookAndFeelException");
            exception.printMessage();
        }
        catch (Exception e)
        {
            ErrorException exception = new ErrorException(e.getMessage(),"UnsupportedLookAndFeelException");
            exception.printMessage();
        }

    }
    
}
