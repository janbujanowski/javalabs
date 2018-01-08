package pk.labs.Lab9.ui;

import java.text.DateFormat;
import javax.swing.table.DefaultTableCellRenderer;

public class DateRenderer extends DefaultTableCellRenderer {
    private DateFormat formatter;
    
    public DateRenderer() { } 
    
    public DateRenderer(DateFormat formatter) { 
        this.formatter = formatter;
    }

    @Override
    public void setValue(Object value) {
        if (formatter==null) {
            formatter = DateFormat.getDateInstance();
        }
        setText((value == null) ? "" : formatter.format(value));
    }
}
