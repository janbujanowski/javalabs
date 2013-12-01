package pk.labs.Lab9.beans;

import java.util.Date;

public interface Term {

    Date getBegin();

    void setBegin(Date begin);
    
    /**
     * Getting duration in minutes
     * @param duration 
     */
    int getDuration();
    
    /**
     * Setting duration in minutes
     * @param duration 
     */
    void setDuration(int duration);
    
    
    Date getEnd();
    
}
