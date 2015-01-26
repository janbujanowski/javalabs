package LabD

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod
import org.osgi.framework.BundleContext
import org.apache.felix.scr.ScrService
import org.osgi.framework.Constants

import javax.inject.Inject

import static LabD.Utils.allBundles
import static org.ops4j.pax.exam.CoreOptions.*

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class P1_BasicAnimalComponentsTest {

	@Inject
	BundleContext context

	@Inject
	ScrService service

	def events

	def animalClassName = 'pk.labs.LabD.contracts.Animal'

	@Configuration
	Option[] configure() {
		[
			mavenBundle('org.apache.felix', 'org.apache.felix.scr', '1.8.0'),
			junitBundles(),
			mavenBundle('org.codehaus.groovy', 'groovy-all')
		] + allBundles('../bundles') as Option[]
	}

	@Before
	void setup() {
		def loggerService = context.getServiceReference('pk.labs.LabD.contracts.Logger')
		def logger = context.getService(loggerService)

		def contractsBundle = context.bundles.find {
			it.symbolicName == 'pk.labs.LabD.contracts'
		}
		//def loggerClass = loggerBundle.loadClass('pk.labs.LabD.logger.Logger')
		def logListenerClass = contractsBundle.loadClass('pk.labs.LabD.contracts.LogListener')
		events = []
		def listener = { evt -> events << evt }.asType(logListenerClass)
		logger.addLogListener(listener)
	}

	@Test
	void "Animal1 component should exist in its own bundle"() {
		testAnimalExistence 'pk.labs.LabD.animal1'
	}

	@Test
	void "Animal2 component should exist in its own bundle"() {
		testAnimalExistence 'pk.labs.LabD.animal2'
	}

	@Test
	void "Animal3 component should exist in its own bundle"() {
		testAnimalExistence 'pk.labs.LabD.animal3'
	}

	@Test
	void "Animal1 should inform about incoming and outgoing"() {
		testAnimalInOut 'pk.labs.LabD.animal1'
	}

	@Test
	void "Animal2 should inform about incoming and outgoing"() {
		testAnimalInOut 'pk.labs.LabD.animal2'
	}

	@Test
	void "Animal3 should inform about incoming and outgoing"() {
		testAnimalInOut 'pk.labs.LabD.animal3'
	}

	private void testAnimalInOut(String symbolicName) {
		// given
		def bundle = context.bundles.find {
			it.symbolicName == symbolicName
		}
		def component = service.getComponents(bundle).find {
			animalClassName in it.services
		}
		def ref = bundle.registeredServices.find { def prop = it.getProperty(Constants.OBJECTCLASS); prop == animalClassName || animalClassName in prop }
		context.getService(ref)

		// when
        component.disable()
		component.enable()
		component.disable()

		//then
		assert events.size() == 3
		Assert.assertEquals events[1].source, events[2].source
	}

	private void testAnimalExistence(String symbolicName) {
		// given
		def bundle = context.bundles.find {
			it.symbolicName == symbolicName
		}
		def component = service.getComponents(bundle).find {
			animalClassName in it.services
		}

		//then
		assert bundle
		assert component
	}
}
