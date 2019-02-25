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
public class Badge {
    private String employeeName;
    private String id;
    
    public Badge(String employeeName, String id){
        this.employeeName = employeeName;
        this.id = id;
    }
    
    @Override
    public String toString(){
        String info = "#" + this.id + " (" + this.employeeName + ")";
        return info;
    }
}
