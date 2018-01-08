package pk.labs.LabC

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod
import org.ops4j.pax.exam.spi.reactors.PerSuite
import org.osgi.framework.BundleContext

import javax.inject.Inject

import static org.junit.Assert.assertEquals
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.junitBundles
import static org.ops4j.pax.exam.CoreOptions.mavenBundle
import static pk.labs.LabC.Utils.allBundles

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
public class P2_AnimalServicesTest {

	@Inject
	BundleContext context

	def zoo
	def animalRefs

	@Configuration
	Option[] configure() {
		[
            junitBundles(),
            mavenBundle('org.codehaus.groovy', 'groovy-all')
		] + allBundles('../bundles') as Option[]
	}

	@Before
	void setup() {
		def zooBundle = context.bundles.find {
			it.symbolicName == 'pk.labs.LabC.zoo'
		}
		def zooClass = zooBundle.loadClass('pk.labs.LabC.zoo.Zoo')
		zoo = zooClass.get()
		animalRefs = context.getServiceReferences('pk.labs.LabC.contracts.Animal', null)
	}

	@Test
	void "There should be 3 different animal services"() {
		// given
		def services = [] as Set
		def names = [] as Set
		def species = [] as Set

		// when
		animalRefs.each {
			def service = context.getService(it)
			services << service
			names << service.name
			species << service.species
		}

		// then
		assertEquals 3, animalRefs.size()
		assertEquals 3, services.size()
		assertEquals 3, names.size()
		assertEquals 3, species.size()
	}

	@Test
	void "Animal1 should be registered as service in its own bundle"() {
		testAnimalService 'pk.labs.LabC.animal1'
	}

	@Test
	void "Animal2 should be registered as service in its own bundle"() {
		testAnimalService 'pk.labs.LabC.animal2'
	}

	@Test
	void "Animal3 should be registered as service in its own bundle"() {
		testAnimalService 'pk.labs.LabC.animal3'
	}

	private void testAnimalService(String symbolicName) {
		// given
		def ref = animalRefs.find {
			it.bundle.symbolicName == symbolicName
		}
		assert ref

		// when
		def service = context.getService(ref)

		// then
		assert service
	}

	@Test
	void "ZOO should contain all animals"() {
		// given
		def animals = [] as Set
		animalRefs.each {
			animals << context.getService(it)
		}

		// then
		assert zoo.animals.containsAll(animals)
	}
}
