package tas_sp19;

import java.sql.*;
import java.text.*;
import org.json.simple.*;
import java.time.*;
import java.util.*;
/*
 * @author jdewi
 */
public class TASDatabase {
    JSONObject absenteeismData = new JSONObject();
    JSONObject badgesData = new JSONObject();
    JSONObject punchesData = new JSONObject();
    JSONObject punchTypeData = new JSONObject();
    JSONObject shiftsData = new JSONObject();
    JSONObject shiftsBadgeData = new JSONObject();
    HashMap<Integer, JSONObject> scheduleOverrideData = new HashMap();
    HashMap<Integer, DailySchedule> dailyScheduleData = new HashMap();
    private int DAYS_IN_A_WEEK = 7;
    int lowestPunchId = 0;
    int greatestPunchId = 0;
    
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
                JSONObject maxIdPull = (JSONObject)rawPunchesData.get(0);
                this.lowestPunchId = (int)maxIdPull.get("id");
                this.greatestPunchId = this.lowestPunchId;
                for (int i = 0; i < rawPunchesData.size(); i++) {
                    JSONObject currentPunch = (JSONObject)rawPunchesData.get(i);
                    Badge badgeToStore = (Badge)this.badgesData.get((String)currentPunch.get("badgeid"));
                    long originalTimeToStore = (long)currentPunch.get("unixtimestamp");
                    Punch toBeStoredPunch = new Punch(badgeToStore, (int)currentPunch.get("id"), (int)currentPunch.get("terminalid"), (int)currentPunch.get("punchtypeid"), originalTimeToStore);
                    if ((int)currentPunch.get("id") > this.greatestPunchId) {
                        this.greatestPunchId = (int)currentPunch.get("id");
                    }
                    punchesData.put((int)currentPunch.get("id"), (Punch)toBeStoredPunch);
                }
                
                JSONArray rawEmployeeData = new JSONArray();
                query = "Select badgeid, shiftid FROM employee";
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
                                if (metadata.getColumnLabel(i).equals("badgeid")){
                                    currentJSONObject.put(metadata.getColumnLabel(i), resultset.getString(i));
                                } else {
                                    currentJSONObject.put(metadata.getColumnLabel(i), resultset.getInt(i));
                                }
                            }
                            rawEmployeeData.add(currentJSONObject);
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
                for (int i = 0; i < rawEmployeeData.size(); i++) {
                    JSONObject currentEmployee = (JSONObject)rawEmployeeData.get(i);
                    this.shiftsBadgeData.put((String)currentEmployee.get("badgeid"), (int)currentEmployee.get("shiftid"));
                }

                /*Prepare Select Shift Query*/
                JSONArray rawDailyScheduleData = new JSONArray();
                query = "SELECT Hour(start) as starthour,Minute(start) as startminute, Hour(stop) as stophour, Minute(stop) as stopminute, Hour(lunchstart) as lunchstarthour, Minute(lunchstart) as lunchstartminute, Hour(lunchstop) as lunchstophour, Minute(lunchstop) as lunchstopminute, id, `interval`, graceperiod, dock, lunchdeduct FROM dailyschedule";
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
                                /*
                                if ((metadata.getColumnLabel(i).equals("id")) || (metadata.getColumnLabel(i).equals("interval")) || (metadata.getColumnLabel(i).equals("graceperiod")) || (metadata.getColumnLabel(i).equals("dock")) || (metadata.getColumnLabel(i).equals("lunchDeduct"))) {
                                    currentJSONObject.put(metadata.getColumnLabel(i), resultset.getInt(i));
                                } else {
                                    currentJSONObject.put(metadata.getColumnLabel(i), resultset.getLong(i));
                                }
                                */
                                currentJSONObject.put(metadata.getColumnLabel(i), resultset.getInt(i));
                            }
                            rawDailyScheduleData.add(currentJSONObject);
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
                for (int i = 0; i < rawDailyScheduleData.size(); i++){
                    JSONObject currentDailySchedule = (JSONObject)rawDailyScheduleData.get(i);
                    LocalTime start = LocalTime.of((int)currentDailySchedule.get("starthour"), (int)currentDailySchedule.get("startminute"));
                    LocalTime stop = LocalTime.of((int)currentDailySchedule.get("stophour"), (int)currentDailySchedule.get("stopminute"));
                    LocalTime lunchStart = LocalTime.of((int)currentDailySchedule.get("lunchstarthour"), (int)currentDailySchedule.get("lunchstartminute"));
                    LocalTime lunchStop = LocalTime.of((int)currentDailySchedule.get("lunchstophour"), (int)currentDailySchedule.get("lunchstopminute"));
                    DailySchedule returningDailySchedule = new DailySchedule((int)currentDailySchedule.get("id"), start, stop, lunchStart, lunchStop, (int)currentDailySchedule.get("interval"), (int)currentDailySchedule.get("graceperiod"), (int)currentDailySchedule.get("dock"), (int)currentDailySchedule.get("lunchdeduct"));
                    dailyScheduleData.put((int)currentDailySchedule.get("id"), (DailySchedule)returningDailySchedule);
                }
                
                query = "SELECT id, badgeid, day, dailyscheduleid, UNIX_TIMESTAMP(start) as startdate, UNIX_TIMESTAMP(end) as enddate from scheduleoverride";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                JSONArray rawOverrideData = new JSONArray();
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
                                if ((metadata.getColumnLabel(i).equals("startdate")) || (metadata.getColumnLabel(i).equals("enddate"))){
                                    currentJSONObject.put(metadata.getColumnLabel(i), (long)(resultset.getLong(i)));
                                } else if (metadata.getColumnLabel(i).equals("badgeid")){
                                    currentJSONObject.put(metadata.getColumnLabel(i), (String)(resultset.getString(i)));
                                    //System.out.print    
                                } else  {
                                    currentJSONObject.put(metadata.getColumnLabel(i), (int)(resultset.getInt(i)));
                                }
                            }
                            rawOverrideData.add(currentJSONObject);
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
                int id = 0;
                for (int i = 0; i < rawOverrideData.size(); i++) {
                    JSONObject currentOverride = (JSONObject)rawOverrideData.get(i);
                    this.scheduleOverrideData.put(id, (JSONObject) currentOverride);
                    id++;
                }
                
                query = "SELECT * from shift";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                JSONArray rawShiftData = new JSONArray();
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
                                if (metadata.getColumnLabel(i).equals("description")){
                                    currentJSONObject.put(metadata.getColumnLabel(i), (String)(resultset.getString(i)));
                                } else {
                                    currentJSONObject.put(metadata.getColumnLabel(i), (int)(resultset.getInt(i)));
                                }
                            }
                            rawShiftData.add(currentJSONObject);
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
                
                for (int i = 0; i < rawShiftData.size(); i++) {
                    JSONObject currentShift = (JSONObject)rawShiftData.get(i);
                    Shift returningShift = new Shift((int)currentShift.get("id"), (String)currentShift.get("description"), (int)currentShift.get("dailyscheduleid"), (HashMap<Integer, DailySchedule>) this.dailyScheduleData, (HashMap<Integer, JSONObject>) this.scheduleOverrideData);
                    this.shiftsData.put((int)currentShift.get("id"), (Shift) returningShift);
                }
                
                query = "SELECT * from absenteeism";
                pstSelect = conn.prepareStatement(query);
                hasresults = pstSelect.execute();
                JSONArray rawAbsenteeismData = new JSONArray();
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
                                if (metadata.getColumnLabel(i).equals("badgeid")){
                                    currentJSONObject.put(metadata.getColumnLabel(i), (String)(resultset.getString(i)));
                                } else if (metadata.getColumnLabel(i).equals("payperiod")) {
                                    currentJSONObject.put(metadata.getColumnLabel(i), (long)(resultset.getLong(i)));
                                } else if (metadata.getColumnLabel(i).equals("percentage")) {
                                    currentJSONObject.put(metadata.getColumnLabel(i), (int)(resultset.getDouble(i)));
                                }
                            }
                            rawAbsenteeismData.add(currentJSONObject);
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
                
                for (int i = 0; i < rawAbsenteeismData.size(); i++) {
                    JSONObject currentAbsenteeism = (JSONObject)rawAbsenteeismData.get(i);
                    
                    Absenteeism madeFromDatabase = new Absenteeism((String)currentAbsenteeism.get("badgeid"), (long)currentAbsenteeism.get("payperiod"), (double)currentAbsenteeism.get("percentage"));
                    
                    this.absenteeismData.put((String)currentAbsenteeism.get("badgeid"), (Absenteeism) madeFromDatabase);
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
        
       Badge returningBadge = (Badge)this.badgesData.get(badgeNumber);
       return returningBadge;
    }
    
    public Punch getPunch(int punchId){
        Punch returningPunch = (Punch)this.punchesData.get(punchId);
        return returningPunch;
    }
    
    public Shift getShift(int shiftId) {
        
        Shift returning = (Shift)this.shiftsData.get(shiftId);
        return returning;
    }
    
    public Shift getShift(Badge testBadge) {
        
        int shiftId = (int)this.shiftsBadgeData.get((String)testBadge.getId());
        Shift returningShift = (Shift)this.shiftsData.get(shiftId);
        return returningShift;
    }
    
    public Shift getShift(Badge badge, long timestamp) {
     
        Shift returningShift = getShift(badge);
        returningShift.determineIfScheduleShouldChange(timestamp, badge.getId());
        return returningShift;
    }
    
    public int insertPunch(Punch punchToBeInserted) {
        this.greatestPunchId++;
        punchToBeInserted.addPunchId(this.greatestPunchId);
        this.punchesData.put(this.greatestPunchId, punchToBeInserted);
        return this.greatestPunchId;
    }
    
    public JSONObject getScheduleOverride() {
        return (JSONObject)scheduleOverrideData;
    }
    
    public ArrayList getDailyPunchList(Badge b, long ts) {
        ArrayList<Punch> returningPunchList = new ArrayList();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        GregorianCalendar calendarToCheckWith = new GregorianCalendar();
        calendarToCheckWith.setTimeInMillis(ts);
        java.util.Date dateToCheckWith = calendarToCheckWith.getTime();
        
        for (int i = 0; i < this.punchesData.size(); i++){
            if (this.punchesData.containsKey(this.lowestPunchId + i)){
                Punch currentPunch = (Punch)this.punchesData.get(this.lowestPunchId + i);
                String currentPunchBadgeId = (String)currentPunch.getBadgeid();
                if (currentPunchBadgeId.equals((String)b.getId())){
                    GregorianCalendar calendarOfCurrentPunch = new GregorianCalendar();
                    calendarOfCurrentPunch.setTimeInMillis(currentPunch.getOriginaltimestamp());
                    java.util.Date currentPunchDate = calendarOfCurrentPunch.getTime();
                    if (fmt.format(dateToCheckWith).equals(fmt.format(currentPunchDate))){
                        returningPunchList.add(currentPunch);
                    }
                }
            }
        }
        
        return returningPunchList;
    }
    
    public ArrayList getPayPeriodPunchList(Badge badge, long timestamp) {
        ArrayList<Punch> finalPunchList = new ArrayList();
        ArrayList<ArrayList<Punch>> returnedDays = new ArrayList();
        GregorianCalendar calendarToCheckWith = new GregorianCalendar();
        calendarToCheckWith.setTimeInMillis(timestamp);
        int daysToIncreaseCalendar;
        
        int dayOfWeek = calendarToCheckWith.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != DAYS_IN_A_WEEK){
            daysToIncreaseCalendar = DAYS_IN_A_WEEK - dayOfWeek;
        } else {
            daysToIncreaseCalendar = 0;
        }
        
        calendarToCheckWith.add(Calendar.DAY_OF_MONTH, daysToIncreaseCalendar);
        
        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            ArrayList<Punch> returnedDay = getDailyPunchList(badge, calendarToCheckWith.getTimeInMillis());
            returnedDays.add(returnedDay);
            
            calendarToCheckWith.add(Calendar.DAY_OF_MONTH, -1);
        }

        for (ArrayList<Punch> pList : returnedDays) {
            for (Punch p : pList){
                finalPunchList.add(p);
            }
        }

        return finalPunchList;
    }
    
    public Absenteeism getAbsenteeism(String badgeId, long originalTimestamp) {
        Absenteeism foundAbsenteeism;
        
        if (absenteeismData.containsKey(badgeId)) {
            foundAbsenteeism = (Absenteeism)absenteeismData.get(badgeId);
        } else {
            ArrayList<Punch> payPeriodOffTimeStamp = getPayPeriodPunchList(getBadge(badgeId), originalTimestamp);
            double thisAbsenteeism = TASLogic.calculateAbsenteeism(payPeriodOffTimeStamp, getShift(getBadge(badgeId)));
            foundAbsenteeism = new Absenteeism(badgeId, originalTimestamp, thisAbsenteeism);
            insertAbsenteeism(foundAbsenteeism);
        }
        
        return foundAbsenteeism;
    }
    
    public void insertAbsenteeism(Absenteeism absenteeismToBeInserted) {
        if (!absenteeismData.containsKey(absenteeismToBeInserted.getBadgeId())) {
            absenteeismData.put(absenteeismToBeInserted.getBadgeId(), absenteeismToBeInserted);
        } else {
            Absenteeism absenteeismToBeChanged = (Absenteeism)absenteeismData.get(absenteeismToBeInserted.getBadgeId());
            absenteeismData.remove(absenteeismToBeInserted.getBadgeId());
            
            absenteeismToBeChanged.setAbsenteeismPercentage(absenteeismToBeInserted.getAbsenteeismPercentage());
            absenteeismData.put(absenteeismToBeChanged.getBadgeId(), absenteeismToBeChanged);
        }
    }
}
