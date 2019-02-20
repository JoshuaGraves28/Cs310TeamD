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
public class TASDatabase {
    public TASDatabase(){
        
    }
    
    public Badge getBadge(String badgeNumber) {
        
       Badge returningNull = new Badge();
       return returningNull;
    }
    
    public Punch getPunch(int punchTime){
        
        Punch returningNull = new Punch(0);
        return returningNull;
    }
    
    public Shift getShift(int badgenumber) {
        
        Shift returningNull = new Shift();
        return returningNull;
    }
    
    public Shift getShift(Badge testBadge) {
        
        Shift returningNull = new Shift();
        return returningNull;
    }
}
