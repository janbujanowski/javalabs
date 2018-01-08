package pk.labs.LabC.animal1.internal;

import pk.labs.LabC.contracts.Animal;

import java.beans.PropertyChangeListener;

/**
 * Created by Jan on 2015-01-19.
 */
public class animal1 implements Animal {


    @Override
    public String getSpecies() {
        return "co za ssak";
    }

    @Override
    public String getName() {
        return "jestę jednorożcę";
    }

    @Override
    public String getStatus() {
        return "rzygam tęczo";
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
