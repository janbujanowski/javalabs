package pk.labs.LabA.ui;

import javax.swing.JPanel;

import pk.labs.LabA.Contracts.*;

public class MainFrame extends AbstractMainFrame {

	public MainFrame() {
        super();
        setTitle("Primary");
    }

    public void setDisplay(Display display) {
    	if (displayPanel != null)
    		getContentPane().remove(displayPanel);
    	displayPanel = display.getPanel();
    	getContentPane().add(displayPanel, java.awt.BorderLayout.NORTH);
    	pack();
    }
    
    public void setControlPanel(ControlPanel controlPanel) {
    	if (this.controlPanel != null)
    		getContentPane().remove(this.controlPanel);
    	this.controlPanel = controlPanel.getPanel();
    	getContentPane().add(this.controlPanel, java.awt.BorderLayout.CENTER);
    	pack();
    }
}
