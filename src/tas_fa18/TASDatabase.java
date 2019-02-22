/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_fa18;

import java.sql.*;
import org.json.simple.*;

/**
 *
 * @author jdewi
 */
public class TASDatabase {
    public TASDatabase(){
        JSONArray badgesData = new JSONArray();
        JSONArray punchesData = new JSONArray();
        JSONArray shiftsData = new JSONArray();
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
                
                /* Prepare Select Punch Query */
                query = "SELECT * FROM punch";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                /*Execute Selet Query*/
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {
                    if ( hasresults ) {
                        /* Get ResultSet Metadata */
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        /* Get Data; Print as Table Rows */
                        while(resultset.next()) {
                            JSONObject currentJSONObject = new JSONObject();
                            for (int i = 2; i <= columnCount; i++){
                                currentJSONObject.put(metadata.getColumnLabel(i), resultset.getString(i));
                            }
                            punchesData.add(currentJSONObject);
                        }
                    } else {
                        resultCount = pstSelect.getUpdateCount();
                        if ( resultCount == -1 ) {
                            break;
                        }
                    }
                    /* Check for More Data */
                    hasresults = pstSelect.getMoreResults();
                }
                
                /*Prepare Select Shift Query*/
                query = "SELECT * FROM shift";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                /*Execute Selet Query*/
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {
                    if ( hasresults ) {
                        /* Get ResultSet Metadata */
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        /* Get Data; Print as Table Rows */
                        while(resultset.next()) {
                            JSONObject currentJSONObject = new JSONObject();
                            for (int i = 2; i <= columnCount; i++){
                                currentJSONObject.put(metadata.getColumnLabel(i), resultset.getString(i));
                            }
                            shiftsData.add(currentJSONObject);
                        }
                    } else {
                        resultCount = pstSelect.getUpdateCount();
                        if ( resultCount == -1 ) {
                            break;
                        }
                    }
                    /* Check for More Data */
                    hasresults = pstSelect.getMoreResults();
                }
                
                query = "SELECT * FROM badge";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                /*Execute Selet Query*/
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {
                    if ( hasresults ) {
                        /* Get ResultSet Metadata */
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        /* Get Data; Print as Table Rows */
                        while(resultset.next()) {
                            JSONObject currentJSONObject = new JSONObject();
                            for (int i = 1; i <= columnCount; i++){
                                currentJSONObject.put(metadata.getColumnLabel(i), resultset.getString(i));
                            }
                            badgesData.add(currentJSONObject);
                        }
                    } else {
                        resultCount = pstSelect.getUpdateCount();
                        if ( resultCount == -1 ) {
                            break;
                        }
                    }
                    /* Check for More Data */
                    hasresults = pstSelect.getMoreResults();
                }
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
        
       Badge returningNull = new Badge("", "");
       return returningNull;
    }
    
    public Punch getPunch(int punchTime){
        
        Punch returningNull = new Punch("", 0, 0, 0);
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
