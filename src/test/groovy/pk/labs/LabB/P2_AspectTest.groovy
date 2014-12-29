package pk.labs.LabB

import org.springframework.test.annotation.DirtiesContext

import static pk.labs.LabB.LabDescriptor.*

import javax.swing.JPanel

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import pk.labs.LabB.Contracts.*
import spock.lang.*

@ContextConfiguration(locations = '/META-INF/spring/beans.xml')
class P2_AspectTest extends Specification {

	Display display
	
	ControlPanel controlPanel
	
	@Autowired
	ApplicationContext context
	
	def mainComponent
	
	Logger logger
	
	private prepareMock() {
		logger = Mock(Logger)
		context.getBeanNamesForType(Logger).each {
			context.removeBeanDefinition(it)
			context.beanFactory.registerSingleton(it, logger)
		}
		//context.getBean(loggerAspectBeanName).logger = logger
	}

	def "Logger implementation sholud not be a logger aspect"() {
		expect:
		context.getBean(Logger) != context.getBean(loggerAspectBeanName)
	}

	@DirtiesContext
	def "Logger should be applied to display component"() {
		given:
		prepareMock()
		display = context.getBean(Display)

		when:
		display.setText("test")
		
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*setText.*/ }, ["test"])	
		then:
		1 * logger.logMethodExit({ it ==~ /.*setText.*/ }, null)
	}

	@DirtiesContext
	def "Logger should be applied to control panel component"() {
		given:
		prepareMock()
		controlPanel = context.getBean(ControlPanel)

		when:
		controlPanel.panel
		
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*getPanel.*/ }, [])
		then:
		1 * logger.logMethodExit({ it ==~ /.*getPanel.*/ }, _ as JPanel)
	}

	@DirtiesContext
	def "Logger should be applied to main component"() {
		given:
		prepareMock()
		mainComponent = context.getBean(mainComponentBeanName)

		when:
		mainComponent.invokeMethod(mainComponentMethodName, mainComponentMethodExampleParams)
		
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*${mainComponentMethodName}.*/ }, mainComponentMethodExampleParams)
		then:
		1 * logger.logMethodExit({ it ==~ /.*${mainComponentMethodName}.*/ }, _)
	}

	@DirtiesContext
	def "Logging on main component should involve logging on display"() {
		given:
		prepareMock()
		mainComponent = context.getBean(mainComponentBeanName)

		when:
		mainComponent.invokeMethod(mainComponentMethodName, mainComponentMethodExampleParams)
		
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*${mainComponentMethodName}.*/ }, mainComponentMethodExampleParams)
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*setText.*/ }, _)
		then:
		1 * logger.logMethodExit({ it ==~ /.*setText.*/ }, null)
		then:
		1 * logger.logMethodExit({ it ==~ /.*${mainComponentMethodName}.*/ }, _)
	}
}
