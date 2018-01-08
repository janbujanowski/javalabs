package pk.labs.LabC.animal1.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import pk.labs.LabC.contracts.Animal;
import pk.labs.LabC.logger.Logger;

/**
 * Created by Jan on 2015-01-19.
 */
public class Activator implements BundleActivator {
    private static BundleContext context;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.registerService(Animal.class.getName(), new animal1(), null);
        context = bundleContext;
        Logger.get().log(this, "jest");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
        Logger.get().log(this, "nie ma");
    }
}
