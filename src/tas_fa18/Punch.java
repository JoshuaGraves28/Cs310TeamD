/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_fa18;

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
        this.employeeBadge = employeeBadge;
        this.punchType = punchType;
        this.terminalId = terminalId;
        this.originalTimeStamp = originalTimeStamp;
    }
    
    public long printOriginalTimestamp() {
        return this.originalTimeStamp;
    }
}
