/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;


/**
 *
 * @author POTCHOLO
 */
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;


public class DataDispatcher {

    public static void dispatch(ResultSet resultSet, Statement statement, Connection conn) throws ErrorException{

        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException exception) {
                throw new ErrorException(exception.getMessage(),"SQLException");
            }
        }

        if(statement != null){
            try {
                statement.close();
            } catch (SQLException exception) {
                throw new ErrorException(exception.getMessage(),"SQLException");
            }
        }

        if(conn != null){
            try {
                conn.close();
            } catch (SQLException exception) {
                throw new ErrorException(exception.getMessage(),"SQLException");
            }
        }
    }

    public static void dispatch(ResultSet resultSet, PreparedStatement pStatement, Connection conn) throws ErrorException{

        if( resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pStatement != null){
            try {
                pStatement.close();
            } catch (SQLException exception) {
                throw new ErrorException(exception.getMessage(),"SQLException");
            }
        }

        if (conn != null){
            try {
                conn.close();
            } catch (SQLException exception) {
                throw new ErrorException(exception.getMessage(),"SQLException");
            }

        }
    }
    
}
