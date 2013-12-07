package pk.labs.LabA;

import java.util.logging.Logger;

import javax.swing.JFrame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        Logger logger = Logger.getLogger(App.class.getName()) ;
    	
    	try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
        	logger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
        	logger.log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        	logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        final ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/beans.xml");
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame mainFrame = (JFrame) context.getBean("app-frame");
                mainFrame.setVisible(true);
            }
        });
    }
}
