package pk.labs.LabA

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration

import pk.labs.LabA.Contracts.*
import pk.labs.LabA.ui.MainFrame2
import pk.labs.LabA.ui.UpsideDownDecorator;
import pk.labs.LabA.utils.BeanUtils;
import spock.lang.*

import static LabDescriptor.*

@ContextConfiguration(locations = '/META-INF/spring/beans.xml')
class P3_ExtendedConfigurationTest extends Specification {

	@Autowired
	ApplicationContext context
	
	def "MainFrame2 should not be accessible"() {
		when:
		context.getBean(MainFrame2.class)
		
		then:
		thrown(NoSuchBeanDefinitionException)
	}
	
	def "Silly frame should be configured"() {
		expect:
		context.getBean(sillyFrameBeanName) != null
	}
	
	def "Silly frame should be build by UpsideDownFactory"() {
		when:
		def frame = context.getBean(sillyFrameBeanName)
		
		then:
		assert frame.originalFrame != null
		assert frame.title.endsWith('UpsideDown')
	}
	
	def "Silly frame should be build from MainFrame2"() {
		given:
		def mf2 = new MainFrame2()
		
		when:
		def frame = context.getBean(sillyFrameBeanName)
		mf2.displayPanel = frame.originalFrame.displayPanel
		mf2.controlPanelPanel = frame.originalFrame.controlPanelPanel
		def udFrame = UpsideDownDecorator.apply(mf2)
		
		then:
		assert frame.originalFrame.class == MainFrame2.class
		assert BeanUtils.compare(frame.originalFrame, mf2)
		assert BeanUtils.compare(frame, udFrame)
	}
	
	def "Silly frame should not be a singleton"() {
		when:
		def frame1 = context.getBean(sillyFrameBeanName)
		def frame2 = context.getBean(sillyFrameBeanName)
		
		then:
		assert !frame1.is(frame2)
	}
	
	def "MainFrame2 should have changed title"() {
		when:
		def frame = context.getBean(sillyFrameBeanName).originalFrame
		
		then:
		assert frame.title != 'SecondHand'
	}
	
}
