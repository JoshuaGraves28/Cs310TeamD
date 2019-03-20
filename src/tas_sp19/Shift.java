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
    
    public Shift(){
        
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
        return this.possibleDailySchedule.get(this.dailyScheduleId).getInterval();
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
    
    public void determineIfScheduleShouldChange(long timestamp) {
        GregorianCalendar thisCalendar = new GregorianCalendar();
        thisCalendar.setTimeInMillis(timestamp);
        
        for (int i = 0; i < scheduledOverrides.size(); i++){
            JSONObject scheduleToCheck = scheduledOverrides.get(i)
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
