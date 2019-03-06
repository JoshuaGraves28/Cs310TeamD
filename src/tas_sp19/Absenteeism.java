package tas_sp19;

import java.text.*;
import java.util.*;

/*
 * @author Jacob
 */

public class Absenteeism {
    private String badgeId;
    private long payPeriodTimestamp;
    private double absenteeismPercentage; 
    
    public Absenteeism(String badgeId, long originalTimestamp, double percentage) {
        this.badgeId = badgeId;
        this.payPeriodTimestamp = determineUpcomingSunday(originalTimestamp);
        this.absenteeismPercentage = percentage;
    }

    public void setAbsenteeismPercentage(double absenteeismPercentage) {
        this.absenteeismPercentage = absenteeismPercentage;
    }
    
    public String getBadgeId() {
        return badgeId;
    }

    public long getPayPeriodTimestamp() {
        return payPeriodTimestamp;
    }

    public double getAbsenteeismPercentage() {
        return absenteeismPercentage;
    }
    
    private long determineUpcomingSunday(long timestamp){
        GregorianCalendar dateOfPunch = new GregorianCalendar();
        dateOfPunch.setTimeInMillis(timestamp);

        int dayOfWeek = dateOfPunch.get(Calendar.DAY_OF_WEEK);
        int daysToIncreaseCalendar = 0;
        
        while (dayOfWeek <= 7){
            dayOfWeek++;
            daysToIncreaseCalendar++;
        }
        
        dateOfPunch.add(Calendar.DAY_OF_MONTH, daysToIncreaseCalendar);
        dateOfPunch.set(Calendar.AM_PM, 0);
        dateOfPunch.set(Calendar.HOUR_OF_DAY, 00);
        dateOfPunch.set(Calendar.MINUTE, 00);
        dateOfPunch.set(Calendar.SECOND, 00);
        dateOfPunch.set(Calendar.MILLISECOND, 00);
        
        return dateOfPunch.getTimeInMillis();
    }
    
    @Override
    public String toString(){
        String stringToBeReturned;
        GregorianCalendar calendarOfPunch = new GregorianCalendar();
        calendarOfPunch.setTimeInMillis(payPeriodTimestamp);
        
        calendarOfPunch.add(Calendar.DAY_OF_MONTH, -7);
        
        Date dateOfPunch = calendarOfPunch.getTime();
        
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String startingPayPeriod = formatter.format(dateOfPunch);
        
        NumberFormat formatterDec = new DecimalFormat("#0.00");
        String doubleFormatted = formatterDec.format(absenteeismPercentage);
        
        stringToBeReturned = "#" + getBadgeId() + " (Pay Period Starting " + startingPayPeriod + "): " + doubleFormatted + "%";
        
        return stringToBeReturned;
    }
}
