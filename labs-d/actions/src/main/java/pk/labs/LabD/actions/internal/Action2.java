package pk.labs.LabD.actions.internal;

import pk.labs.LabD.contracts.Animal;

public class Action2 implements pk.labs.LabD.contracts.AnimalAction {
    // implementacja interfejsu
    @Override
    public boolean execute(Animal animal) {
        if (animal != null) {
            // zmiana statusu gdy jest uruchamiana akcja
            animal.setStatus("jem");
            return true;
        }
        return false;
    }
}
