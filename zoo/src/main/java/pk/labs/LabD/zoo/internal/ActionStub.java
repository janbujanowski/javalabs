package pk.labs.LabD.zoo.internal;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import pk.labs.LabD.contracts.Animal;
import pk.labs.LabD.contracts.AnimalAction;

public class ActionStub {

    private ComponentContext context;
    private String refName;
    private ServiceReference ref;

    private String actionName;

    private AnimalAction instance;

    public ActionStub(ComponentContext context, String refName, ServiceReference ref) {
        this.context = context;
        this.refName = refName;
        this.ref  = ref;

        actionName = (String)ref.getProperty("name");
    }

    public String toString() {
        return actionName;
    }

    public AnimalAction get() {
        if (instance == null)
            instance = (AnimalAction)context.locateService(refName, ref);
        return instance;
    }

    public boolean execute(Animal animal) {
        return get().execute(animal);
    }
}