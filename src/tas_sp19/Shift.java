package tas_sp19;

import java.time.*;
import java.time.temporal.ChronoField;
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
    private int interval;
    private int gracePeriod;
    private int dock;
    private int lunchDeduct;
    
    public Shift(int shiftId, String shiftType, LocalTime start, LocalTime stop, LocalTime lunchStart, LocalTime lunchStop, int interval, int gracePeriod, int dock, int lunchDeduct){
        this.shiftId = shiftId;
        this.shiftType = shiftType;
        this.start = start;
        this.stop = stop;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dock = dock;
        this.lunchDeduct = lunchDeduct;
    }
    
    public LocalTime getStart(){
        return this.start;
    }
    
    public LocalTime getStop() {
        return this.stop;
    }

    public LocalTime getLunchStart() {
        return this.lunchStart;
    }

    public LocalTime getLunchStop() {
        return this.lunchStop;
    }
    
    public int getInterval() {
        return this.interval;
    }
    
    public int getGracePeriod() {
        return this.gracePeriod;
    }
    
    public int getDock() {
        return this.dock;
    }
    
    public int getLunchDeduct() {
        return this.lunchDeduct;
    }
    
    @Override 
    public String toString(){
        String returningString;
        String shiftName = this.shiftType;
        String startStopTime = this.start.toString() + " - " + this.stop.toString() + " (" + Duration.between(this.start, this.stop).toMinutes() + " minutes)";
        String lunchStartStopTime = this.lunchStart.toString() + " - " + this.lunchStop.toString() + " (" + Duration.between(this.lunchStart, this.lunchStop).toMinutes()+ " minutes)";
        returningString = shiftName + ": " + startStopTime + "; Lunch: " + lunchStartStopTime;
        return returningString;
    }
}
