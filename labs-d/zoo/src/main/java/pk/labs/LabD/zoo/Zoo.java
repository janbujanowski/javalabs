package pk.labs.LabD.zoo;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import pk.labs.LabD.contracts.Animal;
import pk.labs.LabD.zoo.internal.ActionStub;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Zoo {

    private static Zoo instance;

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private Set<Animal> animals = new HashSet<>();

	private ComponentContext componentContext;


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
            stubs.add(new ActionStub(componentContext, "action", ref));
        return stubs;
    }

    /**
     * Wyszukuje czynności dostępne dla danego zwięrzęcia
     * na podstawie metadanych czynności.
     * @param animal zwierzę, dla którego są pobierane czynności
     * @return zbiór referencji na dozwolone czynności
     */
	public Set<ServiceReference> getActionsFor(Animal animal) {
        throw new UnsupportedOperationException("Do implementacji!");
	}

    /**
     * Dodaje czynność zwierzęcia
     * @param ref referencja na nową usługę czynności
     */
    public void addAction(ServiceReference ref) {
        // Do implementacji!
    }

    /**
     * Usuwa nieaktualną czynność zwierzęcia
     * @param ref referencja do usuwanej usługi czynności
     */
    void removeAction(ServiceReference ref) {
        // Do implementacji!
    }

}
