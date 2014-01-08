/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.interfaces;

import java.sql.Connection;
import tools.ErrorException;

/**
 *
 * @author POTCHOLO
 */
public interface DBManager {
    public Connection getConnection() throws ErrorException;
    public boolean isConnectedToDatabase() throws ErrorException;
    
    
}
