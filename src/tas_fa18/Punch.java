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
    private int punchType;
    private int terminalId;
    private long originalTimeStamp;
    private long adjustedTimeStamp;
    private String typeOfAdjustment;

    public Punch(Badge employeeBadges, int terminalId, int punchType, long originalTimeStamp) {
        this.employeeBadge = employeeBadges;
        this.punchType = punchType;
        this.terminalId = terminalId;
        this.originalTimeStamp = originalTimeStamp;
    }

    public Punch(Badge employeeBadges, int terminalId, int punchType) {
        this.employeeBadge = employeeBadges;
        this.punchType = punchType;
        this.terminalId = terminalId;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.getTime();
        this.originalTimeStamp = calendar.getTimeInMillis();
    }

    public String getBadgeid() {
        String returningString = (String) employeeBadge.getId();
        return returningString;
    }

    public int getTerminalid() {
        int returningInt = this.terminalId;
        return returningInt;
    }

    public int getPunchtypeid() {
        int returningInt = this.punchType;
        return returningInt;
    }

    public long getOriginaltimestamp() {
        long returningLong = this.originalTimeStamp;
        return returningLong;
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
        LocalTime betweenLunchTime = lunchStart.plusMinutes(Duration.between(lunchStart, lunchStop).toMinutes());
        
        int dayItIs = clockTime.get(Calendar.DAY_OF_WEEK);
        
        /*round to interval*/
        int minuteRoundChange = thisTimeLocalTime.getMinute();
        int moduloOfMinute = minuteRoundChange % interval;
        long roundingToLong;
        
        if (moduloOfMinute >= (interval/2)) {
            roundingToLong = new Long(interval - moduloOfMinute);
            thisTimeLocalTime = thisTimeLocalTime.plusMinutes(roundingToLong);
        } else {
            roundingToLong = new Long(moduloOfMinute);
            thisTimeLocalTime = thisTimeLocalTime.minusMinutes(roundingToLong);
        }
        /*Sets rounded minute*/
        this.typeOfAdjustment = "(Interval Round)";
        clockTime.set(Calendar.HOUR_OF_DAY, thisTimeLocalTime.get(ChronoField.HOUR_OF_DAY));
        clockTime.set(Calendar.MINUTE, thisTimeLocalTime.get(ChronoField.MINUTE_OF_HOUR));
        clockTime.set(Calendar.SECOND, 00);
        
        /*
        if (dayItIs != 1 && dayItIs != 7) {
            
            if (thisTimeLocalTime.isAfter(startIntervalTime) && thisTimeLocalTime.isBefore(start)) {
                System.out.print(clockTime.getTime());
                clockTime.set(Calendar.HOUR_OF_DAY, start.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, start.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, start.get(ChronoField.SECOND_OF_MINUTE));
                System.out.println(" Time adjusted to: " + clockTime.getTime() + " Because interval to start");
                this.typeOfAdjustment = "(Shift Start)";
            } else if (thisTimeLocalTime.isAfter(start) && thisTimeLocalTime.isBefore(startGraceTime)) {
                System.out.print(clockTime.getTime());
                clockTime.set(Calendar.HOUR_OF_DAY, start.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, start.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, start.get(ChronoField.SECOND_OF_MINUTE));
                System.out.println(" Time adjusted to: " + clockTime.getTime() + " Because grace to start");
                this.typeOfAdjustment = "(Shift Start)";
            } else if (thisTimeLocalTime.equals(start)){
                clockTime.set(Calendar.HOUR_OF_DAY, start.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, start.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, start.get(ChronoField.SECOND_OF_MINUTE));
                this.typeOfAdjustment = "(None)";
            } else if (thisTimeLocalTime.isAfter(startGraceTime) && thisTimeLocalTime.isBefore(startDockTime)){
                clockTime.set(Calendar.HOUR_OF_DAY, startDockTime.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, startDockTime.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, startDockTime.get(ChronoField.SECOND_OF_MINUTE));
            } else if (thisTimeLocalTime.isAfter(stopDockTime) && thisTimeLocalTime.isBefore(stopGraceTime)) {
                clockTime.set(Calendar.HOUR_OF_DAY, stopDockTime.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, stopDockTime.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, stopDockTime.get(ChronoField.SECOND_OF_MINUTE));
            } else if (thisTimeLocalTime.isAfter(stopGraceTime) && thisTimeLocalTime.isBefore(stop)) {
                clockTime.set(Calendar.HOUR_OF_DAY, stop.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, stop.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, stop.get(ChronoField.SECOND_OF_MINUTE));
                this.typeOfAdjustment = "(None)";
            } else if (thisTimeLocalTime.isAfter(stop) && thisTimeLocalTime.isBefore(stopIntervalTime)){
                clockTime.set(Calendar.HOUR_OF_DAY, stop.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, stop.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, stop.get(ChronoField.SECOND_OF_MINUTE));
                this.typeOfAdjustment = "(Shift Stop)";
            } else if (thisTimeLocalTime.isAfter(lunchStart) && thisTimeLocalTime.isBefore(betweenLunchTime)) {
                clockTime.set(Calendar.HOUR_OF_DAY, lunchStart.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, lunchStart.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, lunchStart.get(ChronoField.SECOND_OF_MINUTE));
            } else if (thisTimeLocalTime.isAfter(betweenLunchTime) && thisTimeLocalTime.isBefore(lunchStop)) {
                clockTime.set(Calendar.HOUR_OF_DAY, lunchStop.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, lunchStop.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, lunchStop.get(ChronoField.SECOND_OF_MINUTE));
            } else {
                
            }
            
        } else {
            
        }
        
        switch (this.punchType) {
            
            case CLOCK_OUT:
                testClock1 = s.getLunchStart();
                testClock2 = s.getStop();
                
                distance1 = Math.abs(Duration.between(testClock1, thisTimeLocalTime).toMinutes());
                distance2 = Math.abs(Duration.between(testClock2, thisTimeLocalTime).toMinutes());
                
                if (distance1 < distance2) {
                    System.out.println(distance1);
                    clockTime.set(Calendar.HOUR_OF_DAY, testClock1.get(ChronoField.HOUR_OF_DAY));
                    clockTime.set(Calendar.MINUTE, testClock1.get(ChronoField.MINUTE_OF_HOUR));
                    clockTime.set(Calendar.SECOND, testClock1.get(ChronoField.SECOND_OF_MINUTE));
                    this.typeOfAdjustment = "(Interval Round1)";
                } else {
                    System.out.println(distance2);
                    clockTime.set(Calendar.HOUR_OF_DAY, testClock2.get(ChronoField.HOUR_OF_DAY));
                    clockTime.set(Calendar.MINUTE, testClock2.get(ChronoField.MINUTE_OF_HOUR));
                    clockTime.set(Calendar.SECOND, testClock2.get(ChronoField.SECOND_OF_MINUTE));
                    this.typeOfAdjustment = "(Shift Start2)";
                }
                break;
            case CLOCK_IN:
                testClock1 = s.getLunchStop();
                testClock2 = s.getStart();
                
                distance1 = Math.abs(Duration.between(testClock1, thisTimeLocalTime).toMinutes());
                distance2 = Math.abs(Duration.between(testClock2, thisTimeLocalTime).toMinutes());
                
                if (distance1 < distance2) {
                    System.out.println(distance1);
                    clockTime.set(Calendar.HOUR_OF_DAY, testClock1.get(ChronoField.HOUR_OF_DAY));
                    clockTime.set(Calendar.MINUTE, testClock1.get(ChronoField.MINUTE_OF_HOUR));
                    clockTime.set(Calendar.SECOND, testClock1.get(ChronoField.SECOND_OF_MINUTE));
                    this.typeOfAdjustment = "(Interval Round3)";
                } else {
                    System.out.println(distance2);
                    clockTime.set(Calendar.HOUR_OF_DAY, testClock2.get(ChronoField.HOUR_OF_DAY));
                    clockTime.set(Calendar.MINUTE, testClock2.get(ChronoField.MINUTE_OF_HOUR));
                    clockTime.set(Calendar.SECOND, testClock2.get(ChronoField.SECOND_OF_MINUTE));
                    this.typeOfAdjustment = "(Shift Start4)";
                }
                break;
            default:
                System.out.println("ERROR");
                break;
        }
        */
        
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

        String adjustedTimestampString = "#" + this.employeeBadge.getId() + punchResults + " " + strDate.toUpperCase() + " " + this.typeOfAdjustment;
        return adjustedTimestampString;
    }
}
