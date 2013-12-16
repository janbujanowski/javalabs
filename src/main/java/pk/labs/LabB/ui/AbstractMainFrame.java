package pk.labs.LabB.ui;

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