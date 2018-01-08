package pk.labs.LabB.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pk.labs.LabB.Contracts.*;
@Component("main-frame")
public class MainFrame extends AbstractMainFrame {

	private Display display;
	private ControlPanel control;
	
	public MainFrame() {
        super();
        setTitle("Primary");
    }
 @Autowired
    public void setDisplay(Display display) {
		this.display = display;
    	if (displayPanel != null)
    		getContentPane().remove(displayPanel);
    	displayPanel = display.getPanel();
    	getContentPane().add(displayPanel, java.awt.BorderLayout.NORTH);
    	pack();
    }
	@Autowired
    public void setControlPanel(ControlPanel controlPanel) {
		this.control = controlPanel;
    	if (this.controlPanel != null)
    		getContentPane().remove(this.controlPanel);
    	this.controlPanel = controlPanel.getPanel();
    	getContentPane().add(this.controlPanel, java.awt.BorderLayout.CENTER);
    	pack();
    }
	
	public void negative() {
		if (display != null && display instanceof Negativeable)
			((Negativeable)display).negative();
		if (control != null && control instanceof Negativeable)
			((Negativeable)control).negative();
	}
}
