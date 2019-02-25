/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_fa18;

import java.time.*;

/**
 *
 * @author jdewi
 */
public class Shift {
    
    private int shiftId;
    private LocalTime start;
    private LocalTime stop;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    
    public Shift(int shiftId, LocalTime start, LocalTime stop, LocalTime lunchStart, LocalTime lunchStop){
        this.shiftId = shiftId;
        this.start = start;
        this.stop = stop;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
    }
    
    public Shift(){
        
    }
    
    @Override 
    public String toString(){
        String returningString = "";
        String shiftType = "";
        String startStopTime = this.start.toString() + " - " + this.stop.toString() + " (" + Duration.between(this.start, this.stop).toMinutes();
        String lunchStartStopTime = this.lunchStart.toString() + " - " + this.lunchStop.toString() + " (" + Duration.between(this.lunchStart, this.lunchStop).toMinutes();
        returningString = "Shift " + this.shiftId + ": " + startStopTime + " minutes); Lunch: ";
        System.out.println(returningString);
        return returningString;
    }
}
