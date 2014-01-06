package pk.labs.LabC.zoo;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import pk.labs.LabC.contracts.Animal;
import pk.labs.LabC.contracts.AnimalAction;
import pk.labs.LabC.zoo.internal.ActionStub;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Zoo {

    private static Zoo instance;

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private Set<Animal> animals = new CopyOnWriteArraySet<>();

	private BundleContext context;

    public static Zoo get() {
        return instance;
    }

    public static Zoo create(BundleContext bc) {
        instance = new Zoo(bc);
        return instance;
    }

	private Zoo(BundleContext bc) {
        context = bc;
	}

    public void addAnimal(Animal animal) {
		Set<Animal> oldAnimals = animals;
		animals = new HashSet<>();
		animals.addAll(oldAnimals);
		if (animals.add(animal))
			pcs.firePropertyChange("animals", oldAnimals, animals);
	}

	public void removeAnimal(Animal animal) {
		Set<Animal> oldAnimals = animals;
		animals = new HashSet<>();
		animals.addAll(oldAnimals);
		if (animals.remove(animal))
            pcs.firePropertyChange("animals", oldAnimals, animals);
	}

	public Set<Animal> getAnimals() {
		return Collections.unmodifiableSet(animals);
	}

	public int getAnimalsCount() {
		return animals.size();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

    public Set<ActionStub> getActionsFor(Collection<Animal> animals) {
        boolean first = true;
        Set<ServiceReference> actions = new HashSet<>();
        for (Animal animal : animals) {
            if (first) {
                actions.addAll(getActionsFor(animal));
                first = false;
            } else
                actions.retainAll(getActionsFor(animal));
        }
        Set<ActionStub> stubs = new HashSet<>();
        for (ServiceReference ref : actions)
            stubs.add(new ActionStub(context, ref));
        return stubs;
    }


    // Do zmiany na P3
    /**
     * Wyszukuje czynności dostępne dla danego zwięrzęcia
     * na podstawie metadanych czynności.
     * @param animal zwierzę, dla którego są pobierane czynności
     * @return zbiór referencji na dozwolone czynności
     */
	public Set<ServiceReference> getActionsFor(Animal animal) {
		Set<ServiceReference> actions = new HashSet<>();
		try {
			ServiceReference[] refs = context.getServiceReferences(AnimalAction.class.getName(), "(|(species="+animal.getSpecies()+")(!(species=*)))");
			for (ServiceReference ref : refs)
				actions.add(ref);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return actions;
	}

    public void open() {
    }

    public void close() {
    }
}
