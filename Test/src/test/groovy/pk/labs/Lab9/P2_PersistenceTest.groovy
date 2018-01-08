import spock.lang.*
import pk.labs.Lab9.LabDescriptor
import pk.labs.Lab9.ui.MainFrame
import org.fest.swing.fixture.*
import java.text.SimpleDateFormat
import java.awt.event.WindowEvent

class P2_PersistenceTest extends Specification {
    
    def "New window should contain no data"() {
        given:
            def window
            def frame = new MainFrame()
            def factory = LabDescriptor.consultationListFactory.newInstance();
            def consultationList = factory.create();
            frame.consultationList = consultationList
        when:
            window = new FrameFixture(frame)
            window.show()
        then:
            assert window.component().consultationList.size == 0
            window.table('consultations').requireRowCount(0)
        cleanup:
            window.cleanUp()
    }
    
    def "Window should contain deserialized data"() {
        given:
            def sdf = new SimpleDateFormat()
            def studentName = 'Ambroży Kleks'
            def beginDate = Date.parse('yyyy-MM-dd HH:mm', '2000-10-13 15:12')
            def endDate = Date.parse('yyyy-MM-dd HH:mm', '2000-10-13 15:26')
            def duration = 14
            def window
            def frame = new MainFrame()
            def factory = LabDescriptor.consultationListFactory.newInstance();
            def consultationList = factory.create();
            frame.consultationList = consultationList
        when:
            window = new FrameFixture(frame)
            window.component().setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE)
            window.show()
            window.panel("consultation").textBox("studentName").enterText(studentName)
            window.panel("consultation").panel("datePanel").component().date = beginDate
            window.panel("consultation").spinner("duration").enterText(duration.toString())
            window.button("addConsultation").click()
            window.close()
            factory.save(consultationList)
            window.cleanUp()
            consultationList = factory.create(true);
            frame.consultationList = consultationList
            window = new FrameFixture(frame)
            window.show()
        then:
            def list = window.component().consultationList
            assert list.size == 1
            def con = list.consultation[0]
            assert con.student == studentName
            assert con.beginDate == beginDate
            assert con.endDate == endDate
            def table = window.table('consultations')
            table.requireRowCount(1)
            def content = table.contents()
            assert content[0][0] == studentName
            assert content[0][1] == sdf.format(beginDate)
            assert content[0][2] == sdf.format(endDate)
        cleanup:
            window.cleanUp()
    }
	
}

