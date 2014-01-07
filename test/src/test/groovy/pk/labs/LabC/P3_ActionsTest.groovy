package pk.labs.LabC

import junit.framework.Assert
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

import static org.junit.Assert.assertArrayEquals
import static org.junit.Assert.assertEquals
import static org.ops4j.pax.exam.CoreOptions.bundle
import static org.ops4j.pax.exam.CoreOptions.junitBundles
import static org.ops4j.pax.exam.CoreOptions.mavenBundle

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class P3_ActionsTest {

	@Inject
	BundleContext context

	def zooBundle
	def zoo
	def actionsBundle

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
		zooBundle = context.bundles.find {
			it.symbolicName == 'pk.labs.LabC.zoo'
		}
		def zooClass = zooBundle.loadClass('pk.labs.LabC.zoo.Zoo')
		zoo = zooClass.get()
		actionsBundle = context.bundles.find {
			it.symbolicName.startsWith 'pk.labs.LabC.actions'
		}
	}

	@Test
	void "There should be at least 3 actions"() {
		// then
		assert getActionServiceReferences().size() >= 3
	}

	@Test
	void "All actions should be achievable"() {
		// given
		def allActions = getActionServiceReferences() as Set
		def actions = [] as Set

		// when
		getAnimals().each {
			zoo.getActionsFor(it).each { actions << it }
		}

		// then
		assert actions.containsAll(allActions)
	}

	@Test
	void "All actions shouldn't be achievable for all animals"() {
		// given
		def allActionsCount = getActionServiceReferences().size()
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
    void "All actions should have name property"() {
        // then
        getActionServiceReferences().each {
            assert 'name' in it.propertyKeys
        }
    }

	@Test
	void "Some actions should have additional properties"() {
		// given
		def keys = [] as Set
		getActionServiceReferences().each { action ->
			action.propertyKeys.each { if (!it.equals('name')) keys << action }
		}

		// then
		assert keys.size() > 2
	}

	@Test
	void "Actions with additional properties shouldn't be achievable for all animals"() {
		// given
		def animals = getAnimals()
		def actions = [:]

		// when
		animals.each { animal ->
			zoo.getActionsFor(animal).each {
				if (!actions[it])
					actions[it] = [animal] as Set
				else
					actions[it] << animal
			}
		}

		// then
		getActionServiceReferences().each {
            if (it.propertyKeys.size() > 3) {
				assert !actions[it].containsAll(animals)
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

	private getActionServiceReferences() {
		context.getServiceReferences('pk.labs.LabC.contracts.AnimalAction', null)
	}

	private getAnimals() {
		context.getServiceReferences('pk.labs.LabC.contracts.Animal', null).collect {
			context.getService(it)
		}
	}
}
