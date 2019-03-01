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
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(this.originalTimeStamp);

        switch (this.punchType) {
            case CLOCK_OUT:

                break;
            case CLOCK_IN:

                break;
            default:
                break;
        }

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

        String originalTimestampString = "#" + this.employeeBadge.getId() + punchResults + " " + strDate.toUpperCase();
        return originalTimestampString;
    }
}
