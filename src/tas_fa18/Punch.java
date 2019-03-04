package tas_fa18;

import java.text.*;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 *
 * @author jdewi
 */
public class Punch {

    public static final int CLOCK_OUT = 0;
    public static final int CLOCK_IN = 1;
    public static final int TIMED_OUT = 2;

    private Badge employeeBadge;
    private int punchId;
    private int punchType;
    private int terminalId;
    private long originalTimeStamp;
    private long adjustedTimeStamp = 0;
    private String typeOfAdjustment = "";

    public Punch(Badge employeeBadges, int punchId, int terminalId, int punchType, long originalTimeStamp) {
        this.employeeBadge = employeeBadges;
        this.punchId = punchId;
        this.punchType = punchType;
        this.terminalId = terminalId;
        this.originalTimeStamp = originalTimeStamp;
    }

    public Punch(Badge employeeBadges, int terminalId, int punchType) {
        this.employeeBadge = employeeBadges;
        this.punchId = punchId;
        this.punchType = punchType;
        this.terminalId = terminalId;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.getTime();
        this.originalTimeStamp = calendar.getTimeInMillis();
    }
    
    public void addPunchId(int punchId){
        this.punchId = punchId;
    }
    
    public String getBadgeid() {
        return employeeBadge.getId();
    }
    
    public int getPunchId(){
        return punchId;
    }
    
    public int getTerminalid() {
        return terminalId;
    }

    public int getPunchtypeid() {
        return punchType;
    }

    public String getPunchData() {
        return typeOfAdjustment;
    }
    
    public long getOriginaltimestamp() {
        return originalTimeStamp;
    }

    public long getAdjustedtimestamp() {
        return adjustedTimeStamp;
    }
    
    public String printOriginalTimestamp() {
        String punchResults = "";
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(this.originalTimeStamp);
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("E MM/dd/yyyy HH:mm:ss");
        String strDate = formatter.format(date);
        
        switch (this.punchType) {
            case 0:
                punchResults = " CLOCKED OUT:";
                break;
            case 1:
                punchResults = " CLOCKED IN:";
                break;
            case 2:
                punchResults = " TIMED OUT:";
                break;
            default:
                System.out.println("Error");
        }

        String originalTimestampString = "#" + this.employeeBadge.getId() + punchResults + " " + strDate.toUpperCase();
        return originalTimestampString;
    }

    public void adjust(Shift s) {
        GregorianCalendar clockTime = new GregorianCalendar();
        clockTime.setTimeInMillis(this.originalTimeStamp);
        
        int interval = s.getInterval();
        int gracePeriod = s.getGracePeriod();
        int dock = s.getDock();
        
        LocalTime thisTimeLocalTime = LocalTime.of((int)clockTime.get(Calendar.HOUR_OF_DAY), (int)clockTime.get(Calendar.MINUTE));
        
        LocalTime start = s.getStart();
        LocalTime startIntervalTime = start.minusMinutes(interval);
        LocalTime startGraceTime = start.plusMinutes(gracePeriod);
        LocalTime startDockTime = start.plusMinutes(dock);
        
        LocalTime stop = s.getStop();
        LocalTime stopIntervalTime = stop.plusMinutes(interval);
        LocalTime stopGraceTime = stop.minusMinutes(gracePeriod);
        LocalTime stopDockTime = stop.minusMinutes(dock);
        
        LocalTime lunchStart = s.getLunchStart();
        LocalTime lunchStop = s.getLunchStop();

        LocalTime betweenLunchTime = lunchStart.plusMinutes((Duration.between(lunchStart, lunchStop).toMinutes())/2);
        
        int dayItIs = clockTime.get(Calendar.DAY_OF_WEEK);

        if (dayItIs != 1 && dayItIs != 7) {
            if ((thisTimeLocalTime.equals(startIntervalTime) || thisTimeLocalTime.isAfter(startIntervalTime)) && (thisTimeLocalTime.isBefore(start) || thisTimeLocalTime.equals(start))) {
                thisTimeLocalTime = start;
                this.typeOfAdjustment = "Shift Start";
            } else if (thisTimeLocalTime.isAfter(start) && (thisTimeLocalTime.isBefore(startGraceTime) || thisTimeLocalTime.equals(startGraceTime))) {
                thisTimeLocalTime = start;
                this.typeOfAdjustment = "Shift Start";
            } else if (thisTimeLocalTime.isAfter(startGraceTime) && (thisTimeLocalTime.isBefore(startDockTime) || thisTimeLocalTime.equals(startDockTime))) {
                thisTimeLocalTime = startDockTime;
                this.typeOfAdjustment = "Shift Dock";
            } else if ((thisTimeLocalTime.isAfter(stopDockTime) || thisTimeLocalTime.equals(stopDockTime)) && thisTimeLocalTime.isBefore(stopGraceTime)) {
                thisTimeLocalTime = stopDockTime;
                this.typeOfAdjustment = "Shift Dock";                   
            } else if (thisTimeLocalTime.isAfter(stopGraceTime) && (thisTimeLocalTime.isBefore(stop) || thisTimeLocalTime.equals(stop))) {
                thisTimeLocalTime = stop;
                this.typeOfAdjustment = "Shift Stop";
            } else if (thisTimeLocalTime.isAfter(stop) && thisTimeLocalTime.isBefore(stopIntervalTime)){
                thisTimeLocalTime = stop;
                this.typeOfAdjustment = "Shift Stop";
            } else if ((thisTimeLocalTime.equals(lunchStart)|| thisTimeLocalTime.isAfter(lunchStart)) && thisTimeLocalTime.isBefore(betweenLunchTime)) {
                thisTimeLocalTime = lunchStart;
                this.typeOfAdjustment = "Lunch Start";
            } else if (thisTimeLocalTime.isAfter(betweenLunchTime) && (thisTimeLocalTime.isBefore(lunchStop) || thisTimeLocalTime.equals(lunchStop))) {
                thisTimeLocalTime = lunchStop;
                this.typeOfAdjustment = "Lunch Stop";
            }
        }
        
        /*round to interval*/
        int minuteRoundChange = thisTimeLocalTime.getMinute();
        int moduloOfMinute = minuteRoundChange % interval;
        long roundingToLong;
        
        if (moduloOfMinute != 0) {
            if (moduloOfMinute >= (interval/2)) {
                roundingToLong = new Long(interval - moduloOfMinute);
                thisTimeLocalTime = thisTimeLocalTime.plusMinutes(roundingToLong);
            } else {
                roundingToLong = new Long(moduloOfMinute);
                thisTimeLocalTime = thisTimeLocalTime.minusMinutes(roundingToLong);
            }
            
            /*Sets rounded minute*/
            this.typeOfAdjustment = "Interval Round";
            clockTime.set(Calendar.HOUR_OF_DAY, thisTimeLocalTime.get(ChronoField.HOUR_OF_DAY));
            clockTime.set(Calendar.MINUTE, thisTimeLocalTime.get(ChronoField.MINUTE_OF_HOUR));
            clockTime.set(Calendar.SECOND, 00);
        } else if (moduloOfMinute == 0 && this.typeOfAdjustment.equals("")) {
            clockTime.set(Calendar.SECOND, 00);
            this.typeOfAdjustment = "None";
        } else {
            clockTime.set(Calendar.HOUR_OF_DAY, thisTimeLocalTime.get(ChronoField.HOUR_OF_DAY));
            clockTime.set(Calendar.MINUTE, thisTimeLocalTime.get(ChronoField.MINUTE_OF_HOUR));
            clockTime.set(Calendar.SECOND, 00);
        }
        
        this.adjustedTimeStamp = clockTime.getTimeInMillis();
        
    }

    public String printAdjustedTimestamp() {
        String punchResults = "";
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(this.adjustedTimeStamp);
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("E MM/dd/yyyy HH:mm:ss");
        String strDate = formatter.format(date);
        switch (this.punchType) {
            case 0:
                punchResults = " CLOCKED OUT:";
                break;
            case 1:
                punchResults = " CLOCKED IN:";
                break;
            case 2:
                punchResults = " TIMED OUT:";
                break;
            default:
                System.out.println("Error");
        }

        String adjustedTimestampString = "#" + this.employeeBadge.getId() + punchResults + " " + strDate.toUpperCase() + " (" + this.typeOfAdjustment + ")";
        return adjustedTimestampString;
    }
}
