package tas_sp19;

import java.time.*;
import java.util.*;
import org.json.simple.*;
/*
 * @author jdewi
 */
public class Shift {
    
    private int shiftId;
    private int dailyScheduleId;
    private final int DAYS_IN_A_WEEK = 7;
    private String shiftType;
    private HashMap<Integer, DailySchedule> possibleDailySchedule;
    private HashMap<Integer, JSONObject> scheduledOverrides;
    
    public Shift(int shiftId, String shiftType, int dailyScheduleId, HashMap<Integer, DailySchedule> possibleDailySchedule, HashMap<Integer, JSONObject> scheduleOverrideData){
        this.shiftId = shiftId;
        this.shiftType = shiftType;
        this.dailyScheduleId = dailyScheduleId;
        this.possibleDailySchedule = possibleDailySchedule;
        this.scheduledOverrides = scheduleOverrideData;
    }
    
    public void setDailyScheduleId(int newDailyScheduleId) {
        this.dailyScheduleId = newDailyScheduleId;
    }
    
    public LocalTime getStart(){
        return this.possibleDailySchedule.get(this.dailyScheduleId).getStart();
    }
    
    public LocalTime getStartDay(int day){
        return this.possibleDailySchedule.get(this.dailyScheduleId).getStart();
    }
    
    public LocalTime getStop() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getStop();
    }
    
    public LocalTime getStopDay(int day) {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getStop();
    }
     
    public LocalTime getLunchStart() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getLunchStart();
    }
    
    public LocalTime getLunchStartDay(int day) {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getLunchStart();
    }
    
    public LocalTime getLunchStop() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getLunchStop();
    }
    
    public LocalTime getLunchStopDay(int day) {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getLunchStop();
    }
    
    public int getInterval() {
        DailySchedule test = possibleDailySchedule.get(this.dailyScheduleId);
        
        return test.getInterval();
    }
    
    public int getGracePeriod() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getGracePeriod();
    }
    
    public int getDock() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getDock();
    }
    
    public int getLunchDeduct() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getLunchDeduct();
    }
    
    public void determineIfScheduleShouldChange(long timestamp, String badgeid) {
        GregorianCalendar thisCalendar = new GregorianCalendar();
        thisCalendar.setTimeInMillis(timestamp);
        
        //empty Calendar to check with
        GregorianCalendar defaultCalendar = new GregorianCalendar();
        defaultCalendar.setTimeInMillis(0);
        
        for (int i = 0; i < scheduledOverrides.size(); i++){
            
            JSONObject scheduleToCheck = (JSONObject) scheduledOverrides.get(i);
            
            String overrideBadgeId = (String)scheduleToCheck.get("badgeid");
            if (overrideBadgeId == null){
                
                
                if ((long)scheduleToCheck.get("enddate") != 0) {
                    
                    GregorianCalendar startCheck = new GregorianCalendar();
                    GregorianCalendar stopCheck = new GregorianCalendar();
                    
                    System.out.println(scheduleToCheck.get("startdate"));
                    System.out.println(scheduleToCheck.get("enddate"));
                    
                    startCheck.setTimeInMillis((long) scheduleToCheck.get("startdate"));
                    stopCheck.setTimeInMillis((long) scheduleToCheck.get("enddate"));
                    
                    if (thisCalendar.after(startCheck) && thisCalendar.before(stopCheck)){
                        this.dailyScheduleId = (int) scheduleToCheck.get("dailyscheduleid");
                    }
                } else {
                    //Making sure it falls on the same day as the override
                    /*
                    GregorianCalendar startCheck = new GregorianCalendar();
                    startCheck.setTimeInMillis((Long) scheduleToCheck.get("startdate"));
                    if (thisCalendar.after(startCheck) && thisCalendar.get)){
                        this.dailyScheduleId = (int) scheduleToCheck.get("dailyscheduleid");
                    }
                    */
                }
            } else {
                
            System.out.println("test2");
                if (badgeid.equals(overrideBadgeId)) {
                    if ((long)scheduleToCheck.get("enddate") != 0) {
                        GregorianCalendar startCheck = new GregorianCalendar();
                        GregorianCalendar stopCheck = new GregorianCalendar();

                        startCheck.setTimeInMillis((Long) scheduleToCheck.get("startdate"));
                        stopCheck.setTimeInMillis((Long) scheduleToCheck.get("enddate"));

                        if (thisCalendar.after(startCheck) && thisCalendar.before(stopCheck)){
                            this.dailyScheduleId = (int) scheduleToCheck.get("dailyscheduleid");
                        }
                    } else {
                        //Making sure it falls on the same day as the override
                        /*
                        GregorianCalendar startCheck = new GregorianCalendar();
                        startCheck.setTimeInMillis((Long) scheduleToCheck.get("startdate"));
                        if (thisCalendar.after(startCheck) && thisCalendar.get)){
                            this.dailyScheduleId = (int) scheduleToCheck.get("dailyscheduleid");
                        }
                        */
                    }
                }
            }
        }
    }
    
    @Override 
    public String toString(){
        String returningString;
        String shiftName = this.shiftType;
        String startStopTime = getStart().toString() + " - " + getStop().toString() + " (" + Duration.between(getStart(), getStop()).toMinutes() + " minutes)";
        String lunchStartStopTime = getLunchStart().toString() + " - " + getLunchStop().toString() + " (" + Duration.between(getLunchStart(), getLunchStop()).toMinutes()+ " minutes)";
        returningString = shiftName + ": " + startStopTime + "; Lunch: " + lunchStartStopTime;
        return returningString;
    }
}
