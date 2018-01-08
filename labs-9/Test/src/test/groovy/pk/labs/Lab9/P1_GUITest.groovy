import spock.lang.*
import pk.labs.Lab9.LabDescriptor
import pk.labs.Lab9.ui.MainFrame
import org.fest.swing.fixture.*
import java.text.SimpleDateFormat
import org.fest.swing.data.TableCell

class P1_GUITest extends Specification {
    
    private FrameFixture window
    private String studentName
    private Date beginDate
    private Date endDate
    private int duration
    
    def setup() {
        studentName = 'Ambroży Kleks'
        beginDate = Date.parse('yyyy-MM-dd HH:mm', '2000-10-13 15:12')
        endDate = Date.parse('yyyy-MM-dd HH:mm', '2000-10-13 15:26')
        duration = 14
        
        def factory = LabDescriptor.consultationListFactory.newInstance();
        def consultationList = factory.create();
        def frame = new MainFrame()
        frame.consultationList = consultationList
        window = new FrameFixture(frame)
        window.show()
    }
    
    private def enterTestData() {
        window.panel("consultation").textBox("studentName").enterText(studentName)
        window.panel("consultation").panel("datePanel").component().date = beginDate
        window.panel("consultation").spinner("duration").enterText(duration.toString())
        window.button("addConsultation").click()
    }
    
    def "New consultation bean should be added to consultation list bean"() {
        when:
            enterTestData()
        then:
            def list = window.component().consultationList
            assert list.size == 1
            def con = list.consultation[0]
            assert con.student == studentName
            assert con.beginDate == beginDate
            assert con.endDate == endDate
    }
    
    def "New consultation bean should be added to table"() {
        given:
            def sdf = new SimpleDateFormat()
        when:
            enterTestData()
        then:
            def table = window.table('consultations')
            table.requireRowCount(1)
            def content = table.contents()
            assert content[0][0] == studentName
            assert content[0][1] == sdf.format(beginDate)
            assert content[0][2] == sdf.format(endDate)
    }
    
    def "Prolonging consultation should have an effect in consultation list bean"() {
        given:
            def sdf = new SimpleDateFormat()
            def endDate = Date.parse('yyyy-MM-dd HH:mm', '2000-10-13 15:41')
        when:
            enterTestData()
            window.table('consultations').cell(new TableCell(0, 3)).click()
            window.optionPane().okButton().click()
        then:
            def list = window.component().consultationList
            assert list.size == 1
            def con = list.consultation[0]
            assert con.student == studentName
            assert con.beginDate == beginDate
            assert con.endDate == endDate
    }
    
    def "Prolonging consultation should have an effect in table"() {
        given:
            def sdf = new SimpleDateFormat()
            def endDate = Date.parse('yyyy-MM-dd HH:mm', '2000-10-13 15:41')
        when:
            enterTestData()
            window.table('consultations').cell(new TableCell(0, 3)).click()
            window.optionPane().okButton().click()
        then:
            def table = window.table('consultations')
            table.requireRowCount(1)
            def content = table.contents()
            assert content[0][0] == studentName
            assert content[0][1] == sdf.format(beginDate)
            assert content[0][2] == sdf.format(endDate)
    }
    
    def cleanup() {
        window.cleanUp()
    }
	
}

