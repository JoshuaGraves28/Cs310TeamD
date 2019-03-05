package tas_fa18;

import java.text.*;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;

/*
 * @author Jacob
 */

public class Absenteeism {
    private String badgeId = "";
    private long payPeriodTimestamp;
    private double absenteeismPercentage; 
    
    public Absenteeism(String badgeId, long originalTimeStamp, double percentage) {
        this.badgeId = badgeId;
        
        GregorianCalendar dateOfPunch = new GregorianCalendar();
        dateOfPunch.setTimeInMillis(originalTimeStamp);
        
        
    }
}
