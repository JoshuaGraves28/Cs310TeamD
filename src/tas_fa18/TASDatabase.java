/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_fa18;

import java.sql.*;

/**
 *
 * @author jdewi
 */
public class TASDatabase {
    public TASDatabase(){
       Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String query;
        
        boolean hasresults;
        int resultCount, columnCount;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/tas");
            String username = "tasuser";
            String password = "CSTEAMD";
            System.out.println("Connecting to " + server + "...");
            
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (conn.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                /* Prepare Select Query */
            }
            
            /* Close Database Connection */
            
            conn.close();
            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        } 
    }
    
    public Badge getBadge(String badgeNumber) {
        
       Badge returningNull = new Badge();
       return returningNull;
    }
    
    public Punch getPunch(int punchTime){
        
        Punch returningNull = new Punch(0);
        return returningNull;
    }
    
    public Shift getShift(int badgenumber) {
        
        Shift returningNull = new Shift();
        return returningNull;
    }
    
    public Shift getShift(Badge testBadge) {
        
        Shift returningNull = new Shift();
        return returningNull;
    }
}
