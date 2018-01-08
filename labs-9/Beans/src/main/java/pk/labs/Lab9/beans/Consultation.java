package pk.labs.Lab9.beans;

import java.beans.PropertyVetoException;
import java.util.Date;

public interface Consultation {

    public String getStudent();
    
    public void setStudent(String student);

    public Date getBeginDate();

    public Date getEndDate();
    
    public void setTerm(Term term) throws PropertyVetoException;
    
    public void prolong(int minutes) throws PropertyVetoException;
    
}
