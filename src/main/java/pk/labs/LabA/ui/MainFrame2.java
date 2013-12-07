package pk.labs.LabA.ui;

import javax.swing.JPanel;

public class MainFrame2 extends AbstractMainFrame {

	public MainFrame2() {
		super();
		setTitle("SecondHand");
	}
	
	public void setDisplayPanel(JPanel displayPanel) {
    	if (this.displayPanel != null)
    		getContentPane().remove(this.displayPanel);
    	this.displayPanel = displayPanel;
    	getContentPane().add(this.displayPanel, java.awt.BorderLayout.NORTH);
    	pack();
    }
    
    public void setControlPanelPanel(JPanel controlPanel) {
    	if (this.controlPanel != null)
    		getContentPane().remove(this.controlPanel);
    	this.controlPanel = controlPanel;
    	getContentPane().add(this.controlPanel, java.awt.BorderLayout.CENTER);
    	pack();
    }
}
