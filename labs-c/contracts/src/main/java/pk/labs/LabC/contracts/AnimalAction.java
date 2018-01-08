package pk.labs.LabC.contracts;

/**
 * Pewną czynność do wykonania na zwierzęciu, np. karmienie.
 */
public interface AnimalAction {

	/**
	 * Wykonuje daną czynność na konkretnym zwierzęciu
	 * używając metody {@link pk.labs.LabC.contracts.Animal#setStatus setStatus()}
	 * @param animal zwierzę poddane danej czynności
	 * @return informacja o tym, czy czynność się udała (np. zwierzę zostałao nakarmione i nie zdechło)
	 */
	boolean execute(Animal animal);
}
