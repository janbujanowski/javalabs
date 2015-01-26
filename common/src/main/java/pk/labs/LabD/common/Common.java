package pk.labs.LabD.common;

import org.osgi.service.component.ComponentContext;
import pk.labs.LabD.contracts.Animal;
import pk.labs.LabD.contracts.AnimalAction;
import pk.labs.LabD.contracts.Logger;

import java.beans.PropertyChangeListener;

public class Common implements Animal
{
    private Logger logger;
    private AnimalAction AnimalAction;
    String name;
    String species;
    String status;
    String action;

    @Override
    public String getSpecies()
    {
        return species;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getStatus()
    {
        return status;
    }

    @Override
    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {

    }

    void setLogger(Logger logger)
    {
        this.logger=logger;
    }

    void activate(ComponentContext context)
    {
        name = (String) context.getProperties().get("name");
        species = (String) context.getProperties().get("species");
        logger.log(this, "activate "+this.name);
        System.out.println("activate");
    }


    void deactivate(ComponentContext context)
    {
        name = (String) context.getProperties().get("name");
        species = (String) context.getProperties().get("species");
        logger.log(this, "deactivate "+this.name);
        System.out.println("deactivate");
    }

    boolean execute(AnimalAction AnimalAction)
    {
        AnimalAction= this.AnimalAction;
        return true;
    }
}
