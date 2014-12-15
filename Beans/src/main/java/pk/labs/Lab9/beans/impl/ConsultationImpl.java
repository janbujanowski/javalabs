package pk.labs.Lab9.beans.impl;

import pk.labs.Lab9.beans.Consultation;
import pk.labs.Lab9.beans.Term;

import java.beans.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Wojtek on 2014-12-14.
 */
public class ConsultationImpl implements Consultation, Serializable {
    public ConsultationImpl(){}
    private final VetoableChangeSupport vcs = new VetoableChangeSupport(this);
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String student;
    private Term term;

    @Override
    public String getStudent() {
        return this.student;
    }

    @Override
    public void setStudent(String student) {
        this.student=student;
    }

    @Override
    public Date getBeginDate() {
        return this.term.getBegin();
    }

    @Override
    public Date getEndDate() {
        return this.term.getEnd();
    }

    @Override
    public void setTerm(Term term) throws PropertyVetoException {
        Term oldTerm = this.term;
        vcs.fireVetoableChange("Term", oldTerm, term);
        this.term = term;
        pcs.firePropertyChange("Term", oldTerm, term);
    }

    public Term getTerm(){
        return this.term;
    }

    @Override
    public void prolong(int minutes) throws PropertyVetoException {
        if (minutes <= 0) minutes = 0;
            int oldDuration = this.term.getDuration();
            this.vcs.fireVetoableChange("Term", oldDuration, oldDuration + minutes);
            this.pcs.firePropertyChange("Term", oldDuration, oldDuration + minutes);
        this.term.setDuration(this.term.getDuration() + minutes);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        this.vcs.removeVetoableChangeListener(listener);
    }

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        this.vcs.addVetoableChangeListener(listener);
    }
}
