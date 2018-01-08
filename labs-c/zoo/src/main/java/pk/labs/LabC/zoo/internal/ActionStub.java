package pk.labs.LabC.zoo.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import pk.labs.LabC.contracts.Animal;
import pk.labs.LabC.contracts.AnimalAction;

public class ActionStub {

    private BundleContext context;
    private ServiceReference ref;

    private String actionName;

    private AnimalAction instance;

    public ActionStub(BundleContext context, ServiceReference ref) {
        this.context = context;
        this.ref  = ref;

        actionName = (String)ref.getProperty("name");
    }

    public String toString() {
        return actionName;
    }

    public AnimalAction get() {
        if (instance == null)
            instance = (AnimalAction)context.getService(ref);
        return instance;
    }

    public boolean execute(Animal animal) {
        return get().execute(animal);
    }
}