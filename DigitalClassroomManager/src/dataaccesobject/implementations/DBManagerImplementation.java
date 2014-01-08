/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesobject.implementations;

/**
 *
 * @author POTCHOLO
 */
import dataaccesobject.interfaces.DBManager;
import tools.ErrorException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
public class DBManagerImplementation implements DBManager{
    
   private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String DATABASE_URL = "jdbc:mysql://localhost:3306/classmanagement";
    private String username = "root";
    private String password = "root";
    private boolean connectedToDatabase = false;
    private volatile static DBManager INSTANCE = null;

    public DBManagerImplementation() throws ErrorException{
        try {
            Class.forName(JDBC_DRIVER);
	} catch (ClassNotFoundException Exception) {
            throw new ErrorException(Exception.getMessage(),"ClassNotFoundException");
        }
    }
    public Connection getConnection() throws ErrorException{
        Connection connection=null;
        try{
            connection= DriverManager.getConnection( DATABASE_URL, username, password );
            connectedToDatabase = true;
        }
        catch(SQLException e){
            throw new ErrorException("Error creating a connection" +
            "url:"+DATABASE_URL+
            "driverName:"+JDBC_DRIVER+
            "userName: "+username, "SQLException");
        }
        return connection;
    }

    public boolean isConnectedToDatabase(){
        return connectedToDatabase;
    }
    
    public static DBManager getInstance() throws ErrorException{
        if(INSTANCE==null){
            synchronized(DBManager.class){
		if(INSTANCE==null){
                    INSTANCE= new DBManagerImplementation();
		}
            }
        }
        return INSTANCE;
    }


    
}
