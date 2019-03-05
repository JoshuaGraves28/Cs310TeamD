package tas_fa18;

import java.time.*;
import java.util.*;
import org.json.simple.*;

/*
 * @author jdewi
 */
public class TASLogic {
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailyPunchList, Shift shift){
        
        ArrayList<Integer> punchListPunchType = new ArrayList();
        Boolean clockOutForLunch = false;
        int totalMinutes = 0;
        
        for (Punch p: dailyPunchList) {
            punchListPunchType.add(p.getPunchtypeid());
        }
        
        if (punchListPunchType.size() > 1) {
            for (int i = 0; i < punchListPunchType.size(); i++){
                if (i%2 == 0){
                    if (punchListPunchType.get(i) == 1 && punchListPunchType.get(i+1) == 0) {
                        GregorianCalendar ofClockIn = new GregorianCalendar();
                        GregorianCalendar ofClockOut = new GregorianCalendar();

                        ofClockIn.setTimeInMillis(dailyPunchList.get(i).getAdjustedtimestamp());
                        ofClockOut.setTimeInMillis(dailyPunchList.get(i+1).getAdjustedtimestamp());

                        Date dateOfClockIn = ofClockIn.getTime();
                        Date dateOfClockOut = ofClockOut.getTime();

                        long minutesBetweenMillis = dateOfClockOut.getTime() - dateOfClockIn.getTime();

                        long minutesBetweenLong = minutesBetweenMillis / (60 * 1000);
                        
                        totalMinutes += Math.toIntExact(minutesBetweenLong);
                    }
                    
                }
                if (i == 3) {
                    clockOutForLunch = true;
                }
            }
            
        }
        if ((totalMinutes > shift.getLunchDeduct()) && !clockOutForLunch) {
            
            long amountOfLunchToDeductLong = Duration.between(shift.getLunchStart(), shift.getLunchStop()).toMinutes();
            int amountOfLunchToDeduct = Math.toIntExact(amountOfLunchToDeductLong);
            
            totalMinutes -= amountOfLunchToDeduct;
        }
        return totalMinutes;
    }
    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        String returningJSONString;

        ArrayList<HashMap<String, String>> jsonData = new ArrayList();
        
        for (Punch p : dailypunchlist) {
            HashMap<String, String> punchData = new HashMap<>();
            punchData.put("id", Integer.toString(p.getPunchId()));
            punchData.put("badgeid", p.getBadgeid());
            punchData.put("terminalid", Integer.toString(p.getTerminalid()));
            punchData.put("punchtypeid", Integer.toString(p.getPunchtypeid()));
            punchData.put("punchdata", p.getPunchData());
            punchData.put("originaltimestamp", Long.toString(p.getOriginaltimestamp()));
            punchData.put("adjustedtimestamp", Long.toString(p.getAdjustedtimestamp()));
            
            jsonData.add(punchData);
        }

        returningJSONString = JSONValue.toJSONString(jsonData);
        
        return returningJSONString;
    }
    
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift){
        
        ArrayList<Punch> day1 = new ArrayList();
        ArrayList<Punch> day2 = new ArrayList();
        ArrayList<Punch> day3 = new ArrayList();
        ArrayList<Punch> day4 = new ArrayList();
        ArrayList<Punch> day5 = new ArrayList();
        ArrayList<Punch> day6 = new ArrayList();
        ArrayList<Punch> day7 = new ArrayList();
        
        long timeSupposedToWorkInAWeekLong = ((Duration.between(shift.getStart(), shift.getLunchStart()).toMinutes() + (Duration.between(shift.getStop(), shift.getLunchStop()).toMinutes())) * 5);
        int minutesScheduledInAWeek = (int)timeSupposedToWorkInAWeekLong;
        int minutesActuallyWorked = 0;
        
        for (Punch p : punchlist) {
            GregorianCalendar currentPunchCalendar = new GregorianCalendar();
            currentPunchCalendar.setTimeInMillis(p.getAdjustedtimestamp());
            
            switch (currentPunchCalendar.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    day1.add(p);
                    break;
                case 2:
                    day2.add(p);
                    break;
                case 3:
                    day3.add(p);
                    break;
                case 4:
                    day4.add(p);
                    break;
                case 5:
                    day5.add(p);
                    break;
                case 6:
                    day6.add(p);
                    break;
                case 7:
                    day7.add(p);
                    break;
            }    
        }
        
        if (!day1.isEmpty()) { minutesActuallyWorked += calculateTotalMinutes(day1, shift); }
        if (!day2.isEmpty()) { minutesActuallyWorked += calculateTotalMinutes(day2, shift); }
        if (!day3.isEmpty()) { minutesActuallyWorked += calculateTotalMinutes(day3, shift); }
        if (!day4.isEmpty()) { minutesActuallyWorked += calculateTotalMinutes(day4, shift); }
        if (!day5.isEmpty()) { minutesActuallyWorked += calculateTotalMinutes(day5, shift); }
        if (!day6.isEmpty()) { minutesActuallyWorked += calculateTotalMinutes(day6, shift); }
        if (!day7.isEmpty()) { minutesActuallyWorked += calculateTotalMinutes(day7, shift); }
        
        double minutesScheduledInAWeekDouble = new Double(minutesScheduledInAWeek);
        double minutesActuallyWorkedDouble = new Double(minutesActuallyWorked);
        double absenteeismPercentage = 1 - (minutesActuallyWorkedDouble/minutesScheduledInAWeekDouble);
        
        return absenteeismPercentage;
    }   
}
