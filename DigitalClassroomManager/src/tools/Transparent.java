/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import com.sun.awt.AWTUtilities;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author sam
 */
public class Transparent {
    
    public void TransparentForm(JFrame form)
    {
        form.setUndecorated(true);
        AWTUtilities.setWindowOpaque(form, false);
    }
    
    public void TransparentDForm(JDialog form)
    {
        form.setUndecorated(true);
        AWTUtilities.setWindowOpaque(form, false);
    }
}
