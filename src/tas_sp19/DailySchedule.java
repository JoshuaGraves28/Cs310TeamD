/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_sp19;

import java.time.LocalTime;

/**
 *
 * @author jdewi
 */
public class DailySchedule {
    
    private int id;
    private LocalTime start;
    private LocalTime stop;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    private int interval;
    private int gracePeriod;
    private int dock;
    private int lunchDeduct;
    
    public DailySchedule(int id, LocalTime start, LocalTime stop, LocalTime lunchStart, LocalTime lunchStop, int interval, int gracePeriod, int dock, int lunchDeduct){
        this.id = id;
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
}
