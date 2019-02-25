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
    
    private int shiftType;
    private LocalTime start;
    private LocalTime stop;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    
    public Shift(int shiftType, LocalTime start, LocalTime stop, LocalTime lunchStart, LocalTime lunchStop){
        this.shiftType = shiftType;
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
        returningString = "Shift " + this.shiftType + ": " + this.start;
        return "test";
    }
}
