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
    
    private String badgeId;
    private int punchType;
    private int terminalId;
    private long originalTimeStamp;
    
    public Punch(String badgeId, int terminalId, int punchType, long originalTimeStamp){
        badgeId = this.badgeId;
        punchType = this.punchType;
        terminalId = this.terminalId;
        originalTimeStamp = this.originalTimeStamp;
    }
    
    public long printOriginalTimestamp() {
        return originalTimeStamp;
    }
}
