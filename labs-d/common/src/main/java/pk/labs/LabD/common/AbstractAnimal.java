package pk.labs.LabD.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import pk.labs.LabD.contracts.Animal;
import pk.labs.LabD.contracts.Logger;

public class AbstractAnimal implements Animal {
    // opis zwierzaka 
    String species;
    String name;
    String status;
    // logger
    private AtomicReference<Logger> logger = new AtomicReference<>();
    
    // powiadamiacz o zmianie zmiennej
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public AbstractAnimal() {
    }

    // funkcje do pobierania zmienny od zwierzaka
    @Override
    public String getSpecies() {
        return this.species;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        // powiadomienie ze jest zmieniany status
        this.pcs.firePropertyChange("status", this.status, status);
        this.status = status;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    
    // funckja uruchamiana jest gdy jest uruchamiany bundle
    public void activate(BundleContext context, ComponentContext ctx, Map dict) {
        // pobieram z xml'a wartosci opisujace zwierzaka
        this.name = (String) dict.get("name");
        this.species = (String) dict.get("species");
        this.status = (String) dict.get("status");
        // powiadam o przybyciu 
        this.logger.get().log(this, "activating");
    }

    // funkcja uruchamiana gdy jest wylaczany bundle
    public void deactivate() {
        // powiadamiam o odejsciu
        this.logger.get().log(this, "deactivating");
    }

    // zalaczamy naszego loggera
    public void bindLogger(Logger logger) {
        this.logger.set(logger);
    }

}
