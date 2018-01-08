package pk.labs.LabB.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

public class Utils {
	
	public void negateComponent(Component component) {
		component.setBackground(inverseColor(component.getBackground()));
		component.setForeground(inverseColor(component.getForeground()));
		if (component instanceof Container)
			for (Component c : ((Container)component).getComponents())
				negateComponent(c);
	}
	
	public Color inverseColor(Color color) {
		float[] components = color.getComponents(null);
		for (int i = 0; i < components.length - 1; ++i)
			components[i] = 1 - components[i];
		return new Color(components[0], components[1], components[2], components[3]);
	}
}
