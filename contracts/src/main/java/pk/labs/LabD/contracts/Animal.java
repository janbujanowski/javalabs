package pk.labs.LabD.contracts;

import java.beans.PropertyChangeListener;

public interface Animal {
	/**
	 * Gatunek
	 */
	String getSpecies();

	/**
	 * Nazwa
	 */
	String getName();

	/**
	 * Pobranie tego, co zwierzę robi w danej chwili
	 */
	String getStatus();

	/**
	 * Ustawienie tego, co zwierze robi w danej chwili.
	 */
	void setStatus(String status);

	void addPropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

}
