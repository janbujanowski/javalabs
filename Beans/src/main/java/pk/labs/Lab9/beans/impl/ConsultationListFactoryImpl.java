package pk.labs.Lab9.beans.impl;

import pk.labs.Lab9.beans.Consultation;
import pk.labs.Lab9.beans.ConsultationList;
import pk.labs.Lab9.beans.ConsultationListFactory;

import java.beans.PropertyVetoException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Wojtek on 2014-12-14.
 */
public class ConsultationListFactoryImpl implements ConsultationListFactory {
    @Override
    public ConsultationList create() {
        return new ConsultationListBeanImpl();
    }

    @Override
    public ConsultationList create(boolean deserialize) {
        ConsultationList lista = new ConsultationListBeanImpl();
        if(deserialize){
            try{
                XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("test.xml")));
                lista.addConsultation((Consultation)decoder.readObject());
                decoder.close();
            }
            catch(FileNotFoundException e) {
                System.out.print(e.getMessage());
            }   catch (PropertyVetoException ex) {
                Logger.getLogger(ConsultationListFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return lista;
        }
        else{
            return create();
        }
    }

    @Override
    public void save(ConsultationList consultationList) {
        try {
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("test.xml")));
            e.writeObject(consultationList.getConsultation()[0]);
            e.close();
        }
        catch (FileNotFoundException e)
        {
            Logger.getLogger(ConsultationListFactoryImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
