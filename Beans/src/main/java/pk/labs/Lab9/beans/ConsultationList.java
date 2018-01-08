package pk.labs.Lab9.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

public interface ConsultationList {

    public int getSize();

    public Consultation[] getConsultation();
    
    public Consultation getConsultation(int index);
    
    public void addConsultation(Consultation consultation) throws PropertyVetoException;

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(PropertyChangeListener listener);
    
}
