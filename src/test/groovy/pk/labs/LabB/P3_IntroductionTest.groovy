package pk.labs.LabB

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import pk.labs.LabB.Contracts.*
import pk.labs.LabB.ui.*
import spock.lang.*

@ContextConfiguration(locations = '/META-INF/spring/beans.xml')
class P3_IntroductionTest extends Specification {

	@Autowired
	Display display
	
	@Autowired
	ControlPanel controlPanel
	
	@Autowired
	@Qualifier("main-frame")
	MainFrame mainFrame
	
	@Autowired
	ApplicationContext context
	
	def "Negativeable should be introduced to visual components"() {
		expect:
		display instanceof Negativeable
		
		and:
		controlPanel instanceof Negativeable
	}
	
	def "Invoke negative on main frame should change visual components colors"() {
		when:
		mainFrame.negative()
		
		then:
		assert display.panel.background != old(display.panel.background)
		assert display.panel.foreground != old(display.panel.foreground)
		assert controlPanel.panel.background != old(controlPanel.panel.background)
		assert controlPanel.panel.foreground != old(controlPanel.panel.foreground)
	}
	
	def "Double invoke negative on main frame shouldn't change visual components colors"() {
		when:
		mainFrame.negative()
		mainFrame.negative()
		
		then:
		assert display.panel.background == old(display.panel.background)
		assert display.panel.foreground == old(display.panel.foreground)
		assert controlPanel.panel.background == old(controlPanel.panel.background)
		assert controlPanel.panel.foreground == old(controlPanel.panel.foreground)
	}
}
