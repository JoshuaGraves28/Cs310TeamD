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
    JSONObject badgesData = new JSONObject();
    JSONObject punchesData = new JSONObject();
    JSONArray shiftsData = new JSONArray();
        
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
                query = "SELECT * FROM badge";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                /*Execute Selet Query*/
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {
                    if ( hasresults ) {
                        /* Get ResultSet Metadata */
                        resultset = pstSelect.getResultSet();
                        /* Get Data; Print as Table Rows */
                        while(resultset.next()) {
                            Badge currentEmployee;
                            currentEmployee = new Badge(resultset.getString(2), resultset.getString(1));
                            this.badgesData.put(resultset.getString(1), currentEmployee);
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
                /* Prepare Select Punch Query */
                query = "SELECT *, UNIX_TIMESTAMP(originaltimestamp) AS unixtimestamp FROM punch";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                JSONArray rawPunchesData = new JSONArray();
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
                                if (metadata.getColumnLabel(i).equals("unixtimestamp")){
                                    currentJSONObject.put(metadata.getColumnLabel(i), (long)(resultset.getLong(i)*1000));
                                } else if (metadata.getColumnLabel(i).equals("id")) {
                                    currentJSONObject.put(metadata.getColumnLabel(i), (int)(resultset.getInt(i)));
                                } else if (metadata.getColumnLabel(i).equals("terminalid")) {
                                    currentJSONObject.put(metadata.getColumnLabel(i), (int)(resultset.getInt(i)));
                                } else if (metadata.getColumnLabel(i).equals("punchtypeid")) {
                                    currentJSONObject.put(metadata.getColumnLabel(i), (int)(resultset.getInt(i)));
                                }else {
                                    currentJSONObject.put(metadata.getColumnLabel(i), resultset.getString(i));
                                }
                            }
                            rawPunchesData.add(currentJSONObject);
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
                //Code for seting up punch data
                
                for (int i = 0; i < rawPunchesData.size(); i++) {
                    JSONObject currentPunch = (JSONObject)rawPunchesData.get(i);
                    Badge badgeToStore = (Badge)this.badgesData.get((String)currentPunch.get("badgeid"));
                    long originalTimeToStore = (long)currentPunch.get("unixtimestamp");
                    Punch toBeStoredPunch = new Punch(badgeToStore, (int)currentPunch.get("terminalid"), (int)currentPunch.get("punchtypeid"), originalTimeToStore);
                    punchesData.put((int)currentPunch.get("id"), (Punch)toBeStoredPunch);
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
                            for (int i = 1; i <= columnCount; i++){
                                currentJSONObject.put(metadata.getColumnLabel(i), resultset.getString(i));
                            }
                            this.shiftsData.add(currentJSONObject);
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
            //System.out.println(this.shiftsData.get(0));
            Punch testPunch = (Punch)this.punchesData.get(3325);
            System.out.println(testPunch.printOriginalTimestamp());

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
        
       Badge returningBadge = (Badge)this.badgesData.get(badgeNumber);
       return returningBadge;
    }
    
    public Punch getPunch(int punchId){
        Punch returningPunch = (Punch)this.punchesData.get(punchId);
        return returningPunch;
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
