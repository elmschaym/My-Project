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
import javax.swing.SwingUtilities;
public class LookAndFeels {
    private UIManager.LookAndFeelInfo looks[]; // look and feels
    private int lookNum = 1;
    
    //WindowsLookAndFeel is the initial LookAndFeels
    public LookAndFeels( java.awt.Component invoker ) {

        try{
            // get installed look-and-feel information on this platform
            looks = UIManager.getInstalledLookAndFeels();
            //set look and feels
            UIManager.setLookAndFeel( looks[ lookNum ].getClassName() );
             // update Looks and Feels components in this application
            SwingUtilities.updateComponentTreeUI( invoker );
        }// end of try block
        catch( Exception exception ) {
            javax.swing.JOptionPane.showMessageDialog( invoker, exception );
        }//end of catch block
    }//end of method contructor LookAndFeels

    public LookAndFeels( java.awt.Component invoker, int look ) {
        
        try{
            // get installed look-and-feel information on this platform
            looks = UIManager.getInstalledLookAndFeels();
            //set look and feels
            UIManager.setLookAndFeel( looks[ look ].getClassName() );
             // update Looks and Feels components in this application
            SwingUtilities.updateComponentTreeUI( invoker );
            lookNum = look;
        }// end of try block
        catch( Exception exception ) {
            javax.swing.JOptionPane.showMessageDialog( invoker, exception );
        }//end of catch block
    }//end of method contructor LookAndFeels

    //Sets the invokers current look and feel to newLookAndFeel
    public void changeTheLookAndFeels( java.awt.Component invoker, int look ) {
        try{
            // get installed look-and-feel information on this platform
            looks = UIManager.getInstalledLookAndFeels();
            //set look and feels
            UIManager.setLookAndFeel( looks[ look ].getClassName() );
             // update Looks and Feels components in this application
            SwingUtilities.updateComponentTreeUI( invoker );
            lookNum = look;
        }// end of try block
        catch( Exception exception ) {
            javax.swing.JOptionPane.showMessageDialog( invoker, exception );
        }//end of catch block
    }//end of method changeTheLookAndFeels

    public int getLookAndFeel(){
        return this.lookNum;
    }
    
}
