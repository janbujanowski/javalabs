package pk.labs.LabC.animal3.internal;

import pk.labs.LabC.contracts.Animal;

import java.beans.PropertyChangeListener;

/**
 * Created by Jan on 2015-01-19.
 */
public class animal3 implements Animal {


    @Override
    public String getSpecies() {
        return "ryba";
    }

    @Override
    public String getName() {
        return "łoś";
    }

    @Override
    public String getStatus() {
        return "udaje żem łoś";
    }

    @Override
    public void setStatus(String status) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
