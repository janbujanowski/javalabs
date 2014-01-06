package pk.labs.LabC.zoo.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import pk.labs.LabC.zoo.Zoo;
import pk.labs.LabC.zoo.ZooFrame;

/**
 * Extension of the default OSGi bundle activator
 */
public final class Activator implements BundleActivator, Runnable {

	private BundleContext context;
	private Zoo zoo;
    private ZooFrame frame;

	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(BundleContext bc) throws Exception {
		System.out.println("Otwieram ZOO");

		context = bc;
		zoo = Zoo.create(context);
		zoo.open();

		frame = ZooFrame.create(zoo, bc);
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(BundleContext bc) throws Exception {
		System.out.println("Zamykam ZOO");

        zoo.close();

        frame.setVisible(false);
        frame.dispose();
	}

	@Override
	public void run() {

	}
}

