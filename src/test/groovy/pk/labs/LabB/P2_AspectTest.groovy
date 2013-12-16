package pk.labs.LabB

import static pk.labs.LabB.LabDescriptor.*

import javax.swing.JPanel

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import pk.labs.LabB.Contracts.*
import spock.lang.*

@ContextConfiguration(locations = '/META-INF/spring/beans.xml')
class P2_AspectTest extends Specification {
	@Autowired
	Display display
	
	@Autowired
	ControlPanel controlPanel
	
	@Autowired
	ApplicationContext context
	
	def mainComponent
	
	Logger logger
	
	def setup() {
		logger = Mock(Logger)
		context.getBean(loggerAspectBeanName).logger = logger
		mainComponent = context.getBean(mainComponentBeanName)
	}
	
	def "Logger should be applied to display component"() {
		when:
		display.setText("test")
		
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*setText.*/ }, ["test"])	
		then:
		1 * logger.logMethodExit({ it ==~ /.*setText.*/ }, null)
	}
	
	def "Logger should be applied to control panel component"() {
		when:
		controlPanel.panel
		
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*getPanel.*/ }, [])
		then:
		1 * logger.logMethodExit({ it ==~ /.*getPanel.*/ }, _ as JPanel)
	}
	
	def "Logger should be applied to main component"() {
		when:
		mainComponent.invokeMethod(mainComponentMethodName, mainComponentMethodExampleParams)
		
		then:
		1 * logger.logMethodEntrance({ it ==~ /.*${mainComponentMethodName}.*/ }, mainComponentMethodExampleParams)
		then:
		1 * logger.logMethodExit({ it ==~ /.*${mainComponentMethodName}.*/ }, _)
	}
	
	def "Logging on main component should involve logging on display"() {
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
