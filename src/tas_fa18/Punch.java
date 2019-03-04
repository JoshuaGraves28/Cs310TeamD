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
    private String typeOfAdjustment = "";

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

        LocalTime betweenLunchTime = lunchStart.plusMinutes((Duration.between(lunchStart, lunchStop).toMinutes())/2);
        
        int dayItIs = clockTime.get(Calendar.DAY_OF_WEEK);

        if (dayItIs != 1 && dayItIs != 7) {
            if ((thisTimeLocalTime.equals(startIntervalTime) || thisTimeLocalTime.isAfter(startIntervalTime)) && thisTimeLocalTime.isBefore(start)) {
                clockTime.set(Calendar.HOUR_OF_DAY, start.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, start.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, start.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = start;
                this.typeOfAdjustment = "(Shift Start)";
            } else if (thisTimeLocalTime.equals(start)){
                clockTime.set(Calendar.HOUR_OF_DAY, start.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, start.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, start.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = start;
                this.typeOfAdjustment = "(Shift Start)";
            } else if (thisTimeLocalTime.isAfter(start) && (thisTimeLocalTime.isBefore(startGraceTime) || thisTimeLocalTime.equals(startGraceTime))) {
                clockTime.set(Calendar.HOUR_OF_DAY, start.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, start.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, start.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = start;
                this.typeOfAdjustment = "(Shift Start)";
            } else if (thisTimeLocalTime.isAfter(startGraceTime) && (thisTimeLocalTime.isBefore(startDockTime) || thisTimeLocalTime.equals(startDockTime))) {
                clockTime.set(Calendar.HOUR_OF_DAY, startDockTime.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, startDockTime.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, startDockTime.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = startDockTime;
                this.typeOfAdjustment = "(Shift Dock)";
            } else if ((thisTimeLocalTime.isAfter(stopDockTime) || thisTimeLocalTime.equals(stopDockTime)) && thisTimeLocalTime.isBefore(stopGraceTime)) {
                clockTime.set(Calendar.HOUR_OF_DAY, stopDockTime.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, stopDockTime.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, stopDockTime.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = stopDockTime;
                this.typeOfAdjustment = "(Shift Dock)";                   
            } else if (thisTimeLocalTime.isAfter(stopGraceTime) && (thisTimeLocalTime.isBefore(stop) || thisTimeLocalTime.equals(stop))) {
                clockTime.set(Calendar.HOUR_OF_DAY, stop.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, stop.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, stop.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = stop;
                this.typeOfAdjustment = "(Shift Stop)";
            } else if (thisTimeLocalTime.isAfter(stop) && thisTimeLocalTime.isBefore(stopIntervalTime)){
                clockTime.set(Calendar.HOUR_OF_DAY, stop.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, stop.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, stop.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = stop;
                this.typeOfAdjustment = "(Shift Stop)";
            } else if ((thisTimeLocalTime.equals(lunchStart)|| thisTimeLocalTime.isAfter(lunchStart)) && thisTimeLocalTime.isBefore(betweenLunchTime)) {
                System.out.println("lunchStart");
                clockTime.set(Calendar.HOUR_OF_DAY, lunchStart.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, lunchStart.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, lunchStart.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = lunchStart;
                this.typeOfAdjustment = "(Lunch Start)";
            } else if (thisTimeLocalTime.isAfter(betweenLunchTime) && (thisTimeLocalTime.isBefore(lunchStop) || thisTimeLocalTime.equals(lunchStop))) {
                clockTime.set(Calendar.HOUR_OF_DAY, lunchStop.get(ChronoField.HOUR_OF_DAY));
                clockTime.set(Calendar.MINUTE, lunchStop.get(ChronoField.MINUTE_OF_HOUR));
                clockTime.set(Calendar.SECOND, lunchStop.get(ChronoField.SECOND_OF_MINUTE));
                thisTimeLocalTime = lunchStop;
                this.typeOfAdjustment = "(Lunch Stop)";
            }
        }
        
        /*round to interval*/
        int minuteRoundChange = thisTimeLocalTime.getMinute();
        int moduloOfMinute = minuteRoundChange % interval;
        long roundingToLong;
        if (moduloOfMinute != 0) {
            if (moduloOfMinute >= (interval/2)) {
                roundingToLong = new Long(interval - moduloOfMinute);
                System.out.println("Time before: " + thisTimeLocalTime.getHour() + ":" + thisTimeLocalTime.getMinute());
                thisTimeLocalTime = thisTimeLocalTime.plusMinutes(roundingToLong);
                System.out.println("Time after: " + thisTimeLocalTime.getHour() + ":" + thisTimeLocalTime.getMinute());
            } else {
                roundingToLong = new Long(moduloOfMinute);
                System.out.println("Time before: " + thisTimeLocalTime.getHour() + ":" + thisTimeLocalTime.getMinute());
                thisTimeLocalTime = thisTimeLocalTime.minusMinutes(roundingToLong);
                System.out.println("Time after: " + thisTimeLocalTime.getHour() + ":" + thisTimeLocalTime.getMinute());
            }
            
            /*Sets rounded minute*/
            this.typeOfAdjustment = "(Interval Round)";
            clockTime.set(Calendar.HOUR_OF_DAY, thisTimeLocalTime.get(ChronoField.HOUR_OF_DAY));
            clockTime.set(Calendar.MINUTE, thisTimeLocalTime.get(ChronoField.MINUTE_OF_HOUR));
            clockTime.set(Calendar.SECOND, 00);
        } else if (moduloOfMinute == 0 && this.typeOfAdjustment.equals("")) {
            clockTime.set(Calendar.SECOND, 00);
            this.typeOfAdjustment = "(None)";
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

        String adjustedTimestampString = "#" + this.employeeBadge.getId() + punchResults + " " + strDate.toUpperCase() + " " + this.typeOfAdjustment;
        return adjustedTimestampString;
    }
}
