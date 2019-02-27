package tas_fa18;

import java.time.*;
/*
 * @author jdewi
 */
public class Shift {
    
    private int shiftId;
    private String shiftType;
    private LocalTime start;
    private LocalTime stop;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    
    public Shift(int shiftId, String shiftType, LocalTime start, LocalTime stop, LocalTime lunchStart, LocalTime lunchStop){
        this.shiftId = shiftId;
        this.shiftType = shiftType;
        this.start = start;
        this.stop = stop;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
    }
    
    @Override 
    public String toString(){
        String returningString = "";
        String shiftName = this.shiftType;
        String startStopTime = this.start.toString() + " - " + this.stop.toString() + " (" + Duration.between(this.start, this.stop).toMinutes() + " minutes)";
        String lunchStartStopTime = this.lunchStart.toString() + " - " + this.lunchStop.toString() + " (" + Duration.between(this.lunchStart, this.lunchStop).toMinutes()+ " minutes)";
        returningString = shiftName + ": " + startStopTime + "; Lunch: " + lunchStartStopTime;
        return returningString;
    }
}
