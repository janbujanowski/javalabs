import spock.lang.*
import java.beans.*
import java.text.SimpleDateFormat
import pk.labs.Lab9.*
import pk.labs.Lab9.beans.*

class P1_BeansTest extends Specification {
    
    def "Term bean should implement Term interface"() {
        expect:
            Term.class.isAssignableFrom(LabDescriptor.termBean)
    }
    
    def "Term bean should contain 3 properties"() {
        expect:
            props(LabDescriptor.termBean).length == 1 + 3
    }
    
    def "Term bean should accept only positive duration"() {
        given:
            def term = LabDescriptor.termBean.newInstance()
            term.duration = 30
        when:
            term.duration = providedDuration
        then:
            assert term.duration == expectedDuration
        where:
            providedDuration | expectedDuration
                   15        |        15
                    0        |        30
                  -15        |        30
    }
    
    def "Term correctly calculate end date"() {
        given:
            def term = LabDescriptor.termBean.newInstance()
            def sdf = new SimpleDateFormat('yyyy-MM-dd HH:mm')
        when:
            term.begin = sdf.parse(beginDate)
            term.duration = duration
        then:
            assert term.end == sdf.parse(endDate)
        where:
                 beginDate     | duration |      endDate
            '2000-01-01 01:10' |    10    | '2000-01-01 01:20'
            '2000-01-01 01:55' |    10    | '2000-01-01 02:05'
            '2000-01-01 23:55' |    33    | '2000-01-02 00:28'
            '2000-12-31 23:55' |    33    | '2001-01-01 00:28'
    }
    
    def "Consultation bean should implement Consultation interface"() {
        expect:
            Consultation.class.isAssignableFrom(LabDescriptor.consultationBean)
    }
        
    def "Consultation bean should contain 4 properties"() {
        expect:
            props(LabDescriptor.consultationBean).length == 1 + 4
    }
    
    def "Consultation bean should contain Term bean property"() {
        expect:
            props(LabDescriptor.consultationBean).any {
                Term.class.isAssignableFrom(it.propertyType)
            }
    }
    
    def "Consultation bean should allow for prolonging term"() {
        given:
            def term = LabDescriptor.termBean.newInstance()
            term.begin = new Date()
            def consultation = LabDescriptor.consultationBean.newInstance()
            consultation.term = term
        when:
            term.duration = duration
            consultation.prolong minutes
        then:
            assert (consultation.endDate.time - consultation.beginDate.time) / 60000 == finalDuration
        where:
            duration | minutes | finalDuration
              30     |   15    |     45
              30     |    0    |     30
              30     |  -15    |     30
    }
    
    def "Consultation list bean should implement ConsultationList interface"() {
        expect:
            ConsultationList.class.isAssignableFrom(LabDescriptor.consultationListBean)
    }
    
    def "Consultation list bean should contain 2 properties"() {
        expect:
            props(LabDescriptor.consultationListBean).length == 1 + 2
    }
    
    def "Consulation list bean should contain 1 consultation after adding"() {
        given:
            def consultationList = LabDescriptor.consultationListBean.newInstance()
            def consultation = LabDescriptor.consultationBean.newInstance()
        when:
            consultationList.addConsultation(consultation)
        then:
            assert consultationList.size == old(consultationList.size) + 1
            assert consultationList.consultation[0] == consultation
    }
    
    def "Consulation list bean should propagate PCE after adding"() {
        given:
            def consultationList = LabDescriptor.consultationListBean.newInstance()
            def consultation = LabDescriptor.consultationBean.newInstance()
            def listener = Mock(PropertyChangeListener)
        when:
            consultationList.addPropertyChangeListener listener
            consultationList.addConsultation consultation
        then:
            1 * listener.propertyChange({ it.propertyName == 'consultation' })
    }
    
    BeanInfo bi(Class<?> cl) {
        Introspector.getBeanInfo cl
    }
    
    PropertyDescriptor[] props(Class<?> cl) {
        bi cl propertyDescriptors
    }
    
    MethodDescriptor[] methods(Class<?> cl) {
        bi cl methodDescriptors
    }
    
}

