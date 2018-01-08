import spock.lang.*
import java.beans.*
import pk.labs.Lab9.*
import pk.labs.Lab9.beans.*

class P3_ConstraintsTest extends Specification {
    
    private def dateFormat = 'yyyy-MM-dd HH:mm'
    private def consultationListBean
    
    def setup() {
        consultationListBean = LabDescriptor.consultationListFactory.newInstance().create()
    }
    
    def "Should be able to add 2 not overlapping consultation beans"() {
        given:
            def con1 = LabDescriptor.consultationBean.newInstance()
            con1.term = LabDescriptor.termBean.newInstance()
            con1.term.begin = Date.parse(dateFormat, date1)
            con1.term.duration = duration1
            
            def con2 = LabDescriptor.consultationBean.newInstance()
            con2.term = LabDescriptor.termBean.newInstance()
            con2.term.begin = Date.parse(dateFormat, date2)
            con2.term.duration = duration2
        when:
            consultationListBean.addConsultation con1
            consultationListBean.addConsultation con2
        then:
            notThrown(PropertyVetoException)
            assert consultationListBean.size == 2
        where:
                  date1        | duration1 |      date2         | duration2
            '2000-10-13 15:12' |     10    | '2000-10-13 15:30' |     30
            '2000-10-13 15:30' |     30    | '2000-10-13 15:12' |     10
    }
    
    def "Should not be able to add 2 overlapping consultation beans"() {
        given:
            def con1 = LabDescriptor.consultationBean.newInstance()
            con1.term = LabDescriptor.termBean.newInstance()
            con1.term.begin = Date.parse(dateFormat, date1)
            con1.term.duration = duration1
            
            def con2 = LabDescriptor.consultationBean.newInstance()
            con2.term = LabDescriptor.termBean.newInstance()
            con2.term.begin = Date.parse(dateFormat, date2)
            con2.term.duration = duration2
        when:
            consultationListBean.addConsultation con1
            consultationListBean.addConsultation con2
        then:
            def e = thrown(PropertyVetoException)
        where:
                  date1        | duration1 |      date2         | duration2
            '2000-10-13 15:12' |     30    | '2000-10-13 15:12' |     30
            '2000-10-13 15:12' |     30    | '2000-10-13 15:30' |     30
            '2000-10-13 15:30' |     30    | '2000-10-13 15:12' |     30
            '2000-10-13 15:12' |     30    | '2000-10-13 15:30' |     10
            '2000-10-13 15:30' |     10    | '2000-10-13 15:12' |     30
    }
    
    def "Should be able to prolong consultation duration when not overlapping after"() {
        given:
            def con1 = LabDescriptor.consultationBean.newInstance()
            con1.term = LabDescriptor.termBean.newInstance()
            con1.term.begin = Date.parse(dateFormat, date1)
            con1.term.duration = duration1
            
            def con2 = LabDescriptor.consultationBean.newInstance()
            con2.term = LabDescriptor.termBean.newInstance()
            con2.term.begin = Date.parse(dateFormat, date2)
            con2.term.duration = duration2
        when:
            consultationListBean.addConsultation con1
            consultationListBean.addConsultation con2
            assert consultationListBean.size == 2    
            con2.prolong duration3
        then:
            notThrown(PropertyVetoException)
            assert con2.term.duration == duration2 + duration3
        where:
                  date1        | duration1 |      date2         | duration2 | duration3
            '2000-10-13 15:12' |     10    | '2000-10-13 15:30' |     30    |     10
            '2000-10-13 15:30' |     30    | '2000-10-13 15:12' |     10    |      5
    }
    
    def "Should not be able to prolong consultation duration when overlapping after"() {
        given:
            def con1 = LabDescriptor.consultationBean.newInstance()
            con1.term = LabDescriptor.termBean.newInstance()
            con1.term.begin = Date.parse(dateFormat, date1)
            con1.term.duration = duration1
            
            def con2 = LabDescriptor.consultationBean.newInstance()
            con2.term = LabDescriptor.termBean.newInstance()
            con2.term.begin = Date.parse(dateFormat, date2)
            con2.term.duration = duration2
        when:
            consultationListBean.addConsultation con1
            consultationListBean.addConsultation con2
            assert consultationListBean.size == 2
            con2.prolong duration3
        then:
            def e = thrown(PropertyVetoException)
            assert con2.term.duration == duration2
        where:
                  date1        | duration1 |      date2         | duration2 | duration3
            '2000-10-13 15:30' |     10    | '2000-10-13 15:12' |     10    |     10 
            '2000-10-13 15:30' |     10    | '2000-10-13 15:12' |     10    |     20
    }
}

