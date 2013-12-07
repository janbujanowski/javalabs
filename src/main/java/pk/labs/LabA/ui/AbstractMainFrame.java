package pk.labs.LabA.ui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class AbstractMainFrame extends JFrame {

	protected JPanel displayPanel;
	protected JPanel controlPanel;

	public AbstractMainFrame() {
		initComponents();
	}

	protected void initComponents() {
	    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	    pack();
	}

	public JPanel getDisplayPanel() {
		return displayPanel;
	}

	public JPanel getControlPanelPanel() {
		return controlPanel;
	}

}