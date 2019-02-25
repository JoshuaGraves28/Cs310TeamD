/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_fa18;
import java.util.*;
/**
 *
 * @author jdewi
 */
public class Punch {
    
    private Badge employeeBadge;
    private int punchType;
    private int terminalId;
    private long originalTimeStamp;
    
    public Punch(Badge employeeBadges, int terminalId, int punchType, long originalTimeStamp){
        this.employeeBadge = employeeBadges;
        this.punchType = punchType;
        this.terminalId = terminalId;
        this.originalTimeStamp = originalTimeStamp;
    }
    
    public String printOriginalTimestamp() {
        String punchResults = "";
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = new Date();
        calendar.setTimeInMillis(originalTimeStamp);
        date.setTime(originalTimeStamp);
        switch(this.punchType){
            case 0:
                punchResults = " CLOCKED IN:";
                break;
            case 1:
                punchResults = " CLOCKED OUT:";
                break;
            case 2:
                punchResults = " TIMED OUT:";
                break;
            default:
                System.out.println("Error");
        }
        String originalTimestampString = "#" + this.employeeBadge.getId() + punchResults;
        return originalTimestampString;
    }
}
