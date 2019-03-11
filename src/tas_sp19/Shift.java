package tas_sp19;

import java.time.*;
import java.util.ArrayList;
/*
 * @author jdewi
 */
public class Shift {
    
    private int shiftId;
    private String shiftType;
    private DailySchedule shiftSchedule;
    private ArrayList<DailySchedule> payPeriodWeekSchedule;
    
    
    public Shift(int shiftId, String shiftType, DailySchedule shiftSchedule){
        this.shiftId = shiftId;
        this.shiftType = shiftType;
        this.shiftSchedule = shiftSchedule;
    }
    
    public Shift() {
        
    }
    
    public LocalTime getStart(){
        return this.shiftSchedule.getStart();
    }
    
    public LocalTime getStop() {
        return this.shiftSchedule.getStop();
    }

    public LocalTime getLunchStart() {
        return this.shiftSchedule.getLunchStart();
    }

    public LocalTime getLunchStop() {
        return this.shiftSchedule.getLunchStop();
    }
    
    public int getInterval() {
        return this.shiftSchedule.getInterval();
    }
    
    public int getGracePeriod() {
        return this.shiftSchedule.getGracePeriod();
    }
    
    public int getDock() {
        return this.shiftSchedule.getDock();
    }
    
    public int getLunchDeduct() {
        return this.shiftSchedule.getLunchDeduct();
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
