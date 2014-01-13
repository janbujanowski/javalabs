package LabD

import org.apache.felix.scr.Component
import org.apache.felix.scr.ScrService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ops4j.pax.exam.Configuration
import org.ops4j.pax.exam.Option
import org.ops4j.pax.exam.junit.PaxExam
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy
import org.ops4j.pax.exam.spi.reactors.PerMethod
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext

import javax.inject.Inject

import static org.junit.Assert.assertEquals

import static LabD.Utils.allBundles
import static org.ops4j.pax.exam.CoreOptions.*

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class P2_ExtendedAnimalComponentsTest {

	@Inject
	BundleContext context

	@Inject
	ScrService service

	def zoo
	Bundle zooBundle
	Component zooComponent
	Collection<Bundle> animalBundles

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
		zooBundle = context.bundles.find {
			it.symbolicName == 'pk.labs.LabD.zoo'
		}
		zooComponent = service.getComponents(zooBundle).find {
			'pk.labs.LabD.zoo.Zoo' in it.services
		}

		zoo = zooComponent?.componentInstance?.instance

		animalBundles = context.bundles.findAll {
			it.symbolicName.startsWith 'pk.labs.LabD.animal'
		}
	}

	@Test
	void "Zoo component should exist"() {
		assert zooComponent
	}

	@Test
	void "Zoo should be activated"() {
		assert zooComponent.state == Component.STATE_ACTIVE
		assert zoo
	}

	@Test
	void "There should be 3 different animal components"() {
		// given
		def components = [] as Set
		def instances = [] as Set

		// when
		getAnimalComponents().each {
			components << it
			instances << it?.componentInstance?.instance
		}

		// then
		assertEquals 3, components.size()
		assertEquals 3, instances.size()
		assert !components.any { it == null}
		assert !instances.any { it == null}
	}

	@Test
	void "Animals should be correctly registered in zoo"() {
		// given
		def ref = zooComponent.references.find {
			it.serviceName == 'pk.labs.LabD.contracts.Animal'
		}

		// then
		assert ref
		assert ref.isOptional()
		assert ref.isMultiple()
	}

	@Test
	void "ZOO should contain all animals"() {
		// given
		def animals = [] as Set
		getAnimalComponents().each {
			animals << it?.componentInstance?.instance
		}

		// then
		assert zoo.animals.containsAll(animals)
	}

	@Test
	void "Empty zoo shouldn't contain any animal"() {
		// when
		getAnimalComponents().each { it.disable() }

		// then
		assertEquals 0, zoo.animals.size()
	}

	@Test
	void "There shouldn't be registered any animal component without bundles"() {
		// when
		animalBundles.each { it.stop() }

		// then
		assert !getAnimalComponents()
	}

	@Test
	void "Each animal bundle should register one more animal component"() {
		// when
		animalBundles.each { it.stop() }
		int len = 0

		// then
		animalBundles.each {
			it.start()
			assertEquals(++len, getAnimalComponents().size())
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
	void "All animal components should be instances of the same class"() {
		// given
		def classes = [] as Set
		getAnimalComponents().each {
			classes << it.componentInstance.instance.class
		}

		// then
		assertEquals 1, classes.size()
	}

	@Test
	void "Zoo bundle shouldn't depend on any animal bundle"() {
		// given
		def importPackages = zooBundle.headers.get('Import-Package')

		// then
		assert !importPackages.contains('animal')
		assert !importPackages.contains('common')
	}

	private Collection<Component> getAnimalComponents() {
		service.components.findAll {
			'pk.labs.LabD.contracts.Animal' in it.services
		}
	}
}
