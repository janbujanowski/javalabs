package pk.labs.LabB

import spock.lang.*

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

import pk.labs.LabB.Contracts.*
import pk.labs.LabB.ui.MainFrame;

import static LabDescriptor.*

@ContextConfiguration(locations = '/META-INF/spring/beans.xml')
class P1_ConfigurationTest extends Specification {
	
	@Autowired
	Display display
	
	@Autowired
	ControlPanel controlPanel
	
	@Autowired
	@Qualifier("main-frame")
	MainFrame mainFrame
	
	@Autowired
	ApplicationContext context
	
	def "Display implementation should be configured"() {
		expect:
		display != null
	}
	
	def "ControlPanel implementation should be configured"() {
		expect:
		controlPanel != null
	}
	
	def "Main component implementation should be configured"() {
		expect:
		context.getBean(mainComponentBeanName) != null
	}
	
	def "MainFrame should be configured"() {
		expect:
		mainFrame != null
	}
	
	def "MainFrame should have injected display panel"() {
		when:
		def panel = mainFrame.displayPanel
		
		then:
		assert panel != null
		assert panel.is(display.panel)
	}
	
	def "MainFrame should have injected control panel"() {
		when:
		def panel = mainFrame.controlPanelPanel
		
		then:
		assert panel != null
		assert panel.is(controlPanel.panel)
	}
	
	def "Beans.xml should not contain explicite bean definitions"() {
		given:
		def beansURL = MainFrame.class.getResource('/META-INF/spring/beans.xml')
		
		when:
		def beans = new XmlSlurper().parse(beansURL.toString())
		
		then:
		assert beans.bean.size() == 0
	}
	
	@DirtiesContext
	def "Display component should not depend on other components"() {
		given:
		def beanName = context.getBeanNamesForType(ControlPanel.class)[0]
		
		when:
		context.removeBeanDefinition(beanName)
		context.removeBeanDefinition(mainComponentBeanName)
		def bean = context.getBean(Display.class)
		
		then:
		notThrown(Exception)
		assert bean != null
	}
	
	@DirtiesContext
	def "Main component should depend on display component"() {
		given:
		def beanName = context.getBeanNamesForType(Display.class)[0]
		
		when:
		context.removeBeanDefinition(beanName)
		context.getBean(mainComponentBeanName)
		
		then:
		thrown(NoSuchBeanDefinitionException)
	}
	
	@DirtiesContext
	def "Control panel component should depend on main component"() {
		when:
		context.removeBeanDefinition(mainComponentBeanName)
		context.getBean(ControlPanel.class)
		
		then:
		thrown(NoSuchBeanDefinitionException)
	}
}
