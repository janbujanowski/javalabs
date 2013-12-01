package pk.labs.Lab9.beans;

/**
 *
 * @author mikus
 */
public interface ConsultationListFactory {
    
    ConsultationList create();
    ConsultationList create(boolean deserialize);
    void save(ConsultationList consultationList);
}
