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
import org.osgi.framework.ServiceReference

import javax.inject.Inject

import static LabD.Utils.allBundles
import static org.ops4j.pax.exam.CoreOptions.*

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class P3_ActionsTest {

	@Inject
	BundleContext context

	@Inject
	ScrService service

	Bundle zooBundle
	Component zooComponent
	def zoo
	def actionsBundle

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

		actionsBundle = context.bundles.find {
			it.symbolicName.startsWith 'pk.labs.LabD.actions'
		}
	}

	@Test
	void "There should be at least 3 actions"() {
		// then
		assert getActionComponents().size() >= 3
	}

	@Test
	void "All action components should be instances of different classes"() {
		// given
		def components = getActionComponents()
		def classes = [] as Set
		components.each {
			classes << it?.componentInstance?.instance.class
		}

		// then
		org.junit.Assert.assertEquals components.size(), classes.size()
	}

	@Test
	void "Actions should be correctly registered in zoo"() {
		// given
		def ref = zooComponent.references.find {
			it.serviceName == 'pk.labs.LabD.contracts.AnimalAction'
		}

		// then
		assert ref
		assert ref.isOptional()
		assert ref.isMultiple()
	}

	@Test
	void "Actions should be registered in zoo as service references"() {
		// given
		def ref = zooComponent.references.find {
			it.serviceName == 'pk.labs.LabD.contracts.AnimalAction'
		}
		println ' === '
		println ref.bindMethodName
		zoo.class.methods.each { println it.name }
		def method = zoo.class.methods.find { it.name == ref.bindMethodName }
		def params = method?.parameterTypes

		// then
		assert method
		assert params.size() == 1
		assert params[0].name == ServiceReference.name
	}

	@Test
	void "All actions should be achievable"() {
		// given
		def allActions = [] as Set
		getActionComponents().each { allActions << it?.componentInstance?.instance }
		def actions = [] as Set

		// when
		getAnimals().each {
			zoo.getActionsFor(it).each { actions << context.getService(it) }
		}

		// then
		assert actions.containsAll(allActions)
	}

	@Test
	void "All actions shouldn't be achievable for all animals"() {
		// given
		def allActionsCount = getActionComponents().size()
		def animals = [] as Set

		// when
		getAnimals().each {
			if (zoo.getActionsFor(it).size() < allActionsCount)
				animals << it
		}

		// then
		assert animals
	}

	@Test
	void "All actions should have at least name property"() {
		// given
		def keys = [] as Set

		// then
		getActionComponents().each {
			assert it.properties['name']
		}
	}

	@Test
	void "Some actions should have more properties"() {
		// given
		def components = getActionComponents()

		// than
		assert components.any {
			it.properties.size() > 3
		}
	}

	@Test
	void "Actions with properties shouldn't be achievable for all animals"() {
		// given
		def animals = getAnimals()
		def actions = [:]

		// when
		animals.each { animal ->
			zoo.getActionsFor(animal).each {
				def action = context.getService(it)
				if (!actions[action])
					actions[action] = [animal] as Set
				else
					actions[action] << animal
			}
		}

		// then
		getActionComponents().each {
			if (it.properties.size() > 3) {
				def action = it.componentInstance.instance
				println '==='
				println it.properties
				println '==='
				assert !actions[action].containsAll(animals)
			}
		}
	}

	@Test
	void "Zoo bundle shouldn't depend on actions bundle"() {
		// given
		def importPackages = zooBundle.headers.get('Import-Package')

		// then
		assert !importPackages.contains('actions')
	}

	private getActionComponents() {
		def actionCLasssName = 'pk.labs.LabD.contracts.AnimalAction'
		context.getServiceReferences(actionCLasssName, null).each {
			context.getService(it)
		}
		service.getComponents(actionsBundle).findAll {
			actionCLasssName in it.services
		}
	}

	private getAnimals() {
		service.components.findAll {
			'pk.labs.LabD.contracts.Animal' in it.services
		}.collect {
			it?.componentInstance?.instance
		}
	}
}
