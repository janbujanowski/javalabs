package pk.labs.Lab9;

import pk.labs.Lab9.beans.*;
import pk.labs.Lab9.beans.impl.ConsultationImpl;
import pk.labs.Lab9.beans.impl.ConsultationListBeanImpl;
import pk.labs.Lab9.beans.impl.ConsultationListFactoryImpl;
import pk.labs.Lab9.beans.impl.TermImpl;

public class LabDescriptor {
    public static Class<? extends Term> termBean = TermImpl.class;
    public static Class<? extends Consultation> consultationBean = ConsultationImpl.class;
    public static Class<? extends ConsultationList> consultationListBean = ConsultationListBeanImpl.class;
    public static Class<? extends ConsultationListFactory> consultationListFactory = ConsultationListFactoryImpl.class;
    
}
