package pk.labs.LabC

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod
import org.osgi.framework.BundleContext

import javax.inject.Inject

import static org.junit.Assert.assertEquals
import static org.ops4j.pax.exam.CoreOptions.allKnopflerfishVersions
import static org.ops4j.pax.exam.CoreOptions.junitBundles
import static org.ops4j.pax.exam.CoreOptions.mavenBundle
import static org.ops4j.pax.exam.CoreOptions.bundle

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class P3_DynamicAnimalsTest {

	@Inject
	BundleContext context

	def zooBundle
	def zoo
	def animalBundles

	@Configuration
	Option[] configure() {
        System.out.println(new java.io.File( "." ).getCanonicalPath());
		[
            junitBundles(),
            mavenBundle('org.codehaus.groovy', 'groovy-all'),
            bundle('file:../bundles/logger-1.0.0.jar'),
            bundle('file:../bundles/contracts-1.0.0.jar'),
            bundle('file:../bundles/animal1-1.0.0.jar'),
            bundle('file:../bundles/animal2-1.0.0.jar'),
            bundle('file:../bundles/animal3-1.0.0.jar'),
            bundle('file:../bundles/zoo-1.0.0.jar')
		] as Option[]
	}

	@Before
	void setup() {
		zooBundle = context.bundles.find {
			it.symbolicName == 'pk.labs.LabC.zoo'
		}
		def zooClass = zooBundle.loadClass('pk.labs.LabC.zoo.Zoo')
		zoo = zooClass.get()
		animalBundles = context.bundles.findAll {
			it.symbolicName.startsWith 'pk.labs.LabC.animal'
		}
	}

	@Test
	void "Empty zoo shouldn't contain any animal"() {
		// when
		animalBundles.each { it.stop() }

		// then
		assertEquals 0, zoo.animals.size()
	}

	@Test
	void "There shouldn't be registered any animal services without bundles"() {
		// when
		animalBundles.each { it.stop() }

		// then
		assert !getAnimalServiceReferences()
	}

	@Test
	void "Each animal bundle should register one more animal service"() {
		// when
		animalBundles.each { it.stop() }
		int len = 0

		// then
		animalBundles.each {
			it.start()
			assertEquals(++len, getAnimalServiceReferences().size())
		}
	}

	@Test
	void "Each animal bundle should register one more animal in zoo"() {
		// when
		animalBundles.each { it.stop() }
		int len = 0

		// then
		animalBundles.each {
			it.start()
			assertEquals(++len, zoo.animals.size())
		}
	}

	@Test
	void "Zoo bundle shouldn't depend on any animal bundle"() {
		// given
		def importPackages = zooBundle.headers.get('Import-Package')

		// then
		assert !importPackages.contains('animal')
	}

	private getAnimalServiceReferences() {
		context.getServiceReferences('pk.labs.LabC.contracts.Animal', null)
	}
}
