package pk.labs.LabB

import java.lang.reflect.Modifier

import java.awt.Component

import spock.lang.*

import pk.labs.LabB.Contracts.*


class P1_ComponentTest extends Specification {

    def "Component implementation should implement apropriate interface"() {
        expect:
        spec.isAssignableFrom Class.forName(impl)

        where:
        spec                                                    |            impl
        Display.class                                           | LabDescriptor.displayImplClassName
        ControlPanel.class                                      | LabDescriptor.controlPanelImplClassName
        Class.forName(LabDescriptor.mainComponentSpecClassName) | LabDescriptor.mainComponentImplClassName
    }

    def "Component specification and implementation should be in aproprate jar file"() {
        expect:
        Utils.getJarFile(clazz).endsWith(fileName)

        where:
        clazz                         |   fileName
        Display.class                            | 'interfaces.jar'
        ControlPanel.class                       | 'interfaces.jar'
        LabDescriptor.mainComponentSpecClassName | 'interfaces.jar'
        LabDescriptor.displayImplClassName       | 'display.jar'
        LabDescriptor.controlPanelImplClassName  | 'controlpanel.jar'
        LabDescriptor.mainComponentImplClassName | 'main.jar'
    }

    def "Component should not contain other public classes"() {
        when:
        def classes = Utils.getClassesInPackage(impl)

        then:
        classes.each {
            if (it != Class.forName(impl))
                assert !Modifier.isPublic(it.modifiers)
        }

        where:
        impl << [ LabDescriptor.displayImplClassName, LabDescriptor.controlPanelImplClassName, LabDescriptor.mainComponentImplClassName ]
    }
    
    def "Component implementation should not directly inherit from java Component"() {
        expect:
        !Component.isAssignableFrom(Class.forName(impl))

        where:
        impl << [ LabDescriptor.displayImplClassName, LabDescriptor.controlPanelImplClassName, LabDescriptor.mainComponentImplClassName ]
    }
}