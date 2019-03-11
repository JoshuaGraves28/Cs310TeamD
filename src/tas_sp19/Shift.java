package tas_sp19;

import java.time.*;
import java.util.*;
/*
 * @author jdewi
 */
public class Shift {
    
    private int shiftId;
    private int dailyScheduleId;
    private String shiftType;
    private HashMap<Integer, DailySchedule> possibleDailySchedule;
    
    public Shift(int shiftId, String shiftType, int dailyScheduleId, HashMap<Integer, DailySchedule> possibleDailySchedule){
        this.shiftId = shiftId;
        this.shiftType = shiftType;
        this.dailyScheduleId = dailyScheduleId;
        this.possibleDailySchedule = possibleDailySchedule;
    }
    
    public Shift() {
        
    }
    
    public LocalTime getStart(){
        return this.possibleDailySchedule.get(this.dailyScheduleId).getStart();
    }
    
    public LocalTime getStop() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getStop();
    }

    public LocalTime getLunchStart() {
        return this.possibleDailySchedule.get(this.dailyScheduleId).getLunchStart();
    }

    public LocalTime getLunchStop() {
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
    
    @Override 
    public String toString(){
        System.out.println("started tostring");
        String returningString;
        String shiftName = this.shiftType;
        String startStopTime = getStart().toString() + " - " + getStop().toString() + " (" + Duration.between(getStart(), getStop()).toMinutes() + " minutes)";
        String lunchStartStopTime = getLunchStart().toString() + " - " + getLunchStop().toString() + " (" + Duration.between(getLunchStart(), getLunchStop()).toMinutes()+ " minutes)";
        returningString = shiftName + ": " + startStopTime + "; Lunch: " + lunchStartStopTime;
        return returningString;
    }
}
