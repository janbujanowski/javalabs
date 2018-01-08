package pk.labs.Lab9.beans.impl;

import pk.labs.Lab9.beans.Consultation;
import pk.labs.Lab9.beans.ConsultationList;
import pk.labs.Lab9.beans.Term;

import java.beans.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Wojtek on 2014-12-14.
 */
public class ConsultationListBeanImpl implements ConsultationList, Serializable {
    protected Consultation[] list;

    public ConsultationListBeanImpl()
    {
        this.list = new Consultation[]{};
    }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override
    public int getSize() {
        return this.list.length;
    }

    @Override
    public Consultation[] getConsultation() {
        return this.list;
    }

    @Override
    public Consultation getConsultation(int index) {
        return this.list[index];
    }

    @Override
    public void addConsultation(Consultation consultation) throws PropertyVetoException {
        ((ConsultationImpl)consultation).addVetoableChangeListener(new VetoableChangeListener() {
                                                                       @Override
                                                                       public void vetoableChange(final PropertyChangeEvent evt) throws PropertyVetoException {
                                                                           int newValue=-1;
                                                                           try {
                                                                               newValue = ((Integer)evt.getNewValue()).intValue();
                                                                           }
                                                                           catch(NullPointerException e){
                                                                           }
                                                                           String name = evt.getPropertyName();
                                                                           if (name == "Term" && list.length > 0 && newValue > -1) {
                                                                               Consultation o = (Consultation) evt.getSource();
                                                                               Date d = new Date(o.getBeginDate().getTime() + (newValue * 60000));
                                                                               for (int i = 0; i < list.length; i++) {
                                                                                   if (list[i] != o) {
                                                                                       if ((d.after(list[i].getBeginDate()) && o.getBeginDate().before(list[i].getBeginDate()))
                                                                                               || (o.getBeginDate().before(list[i].getEndDate()) && d.after(list[i].getBeginDate())) || (o.getBeginDate() == list[i].getBeginDate()) || (d == list[i].getEndDate())) {
                                                                                           throw new PropertyVetoException("Term PropertyVetoException", null);
                                                                                       }
                                                                                   }
                                                                               }
                                                                           }
                                                                       }
                                                                   }
        );
        for (int i = 0; i < this.list.length; i++) {
            if ((consultation.getEndDate().after(this.list[i].getBeginDate()) && consultation.getBeginDate().before(this.list[i].getBeginDate()))
                    || (consultation.getBeginDate().before(this.list[i].getEndDate()) && consultation.getEndDate().after(this.list[i].getBeginDate())) || (consultation.getBeginDate() == this.list[i].getBeginDate() || (consultation.getEndDate() == this.list[i].getEndDate()))) {
                throw new PropertyVetoException("ConsultationListBean PropertyVetoException", null);
            }
        }
        Consultation[] newList = Arrays.copyOf(this.list, this.list.length + 1);
        newList[newList.length-1] = consultation;
        Consultation[] oldList = this.list;
        this.list = newList;
        pcs.firePropertyChange("consultation", oldList, newList);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
}
