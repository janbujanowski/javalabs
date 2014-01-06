package pk.labs.LabC

import org.junit.*
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod
import org.osgi.framework.BundleContext

import static org.junit.Assert.*

import javax.inject.Inject

import static org.ops4j.pax.exam.CoreOptions.*

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class P1_InAndOutTest {

	@Inject
	BundleContext context

    def events

	@Configuration
	Option[] configure() {
		[
            junitBundles(),
            mavenBundle('org.codehaus.groovy', 'groovy-all'),
            bundle('file:../bundles/logger-1.0.0.jar'),
            bundle('file:../bundles/contracts-1.0.0.jar'),
            bundle('file:../bundles/animal1-1.0.0.jar'),
            bundle('file:../bundles/animal2-1.0.0.jar'),
            bundle('file:../bundles/animal3-1.0.0.jar'),
            bundle('file:../bundles/actions-1.0.0.jar'),
            bundle('file:../bundles/zoo-1.0.0.jar')
		] as Option[]
	}

	@Before
	void setup() {
		def loggerBundle = context.bundles.find {
			it.symbolicName == 'pk.labs.LabC.logger'
		}
		def loggerClass = loggerBundle.loadClass('pk.labs.LabC.logger.Logger')
		def logListenerClass = loggerBundle.loadClass('pk.labs.LabC.logger.LogListener')
        events = []
		def listener = { evt -> events << evt }.asType(logListenerClass)
		def logger = loggerClass.get()
		logger.addLogListener(listener)
	}

	@Test
	void "Animal1 should inform about incoming and outgoing"() {
		testBundleInOut 'pk.labs.LabC.animal1'
	}

	@Test
	void "Animal2 should inform about incoming and outgoing"() {
		testBundleInOut 'pk.labs.LabC.animal2'
	}

	@Test
	void "Animal3 should inform about incoming and outgoing"() {
		testBundleInOut 'pk.labs.LabC.animal3'
	}

	private void testBundleInOut(String symbolicName) {
		// given
		def bundle = context.bundles.find {
			it.symbolicName == symbolicName
		}

		// when
		bundle.stop()
		bundle.start()
		bundle.stop()

		//then
		assert events.size() == 3
		assertEquals events[1].source, events[2].source
	}
}
