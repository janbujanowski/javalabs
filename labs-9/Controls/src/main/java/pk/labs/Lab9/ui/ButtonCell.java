package pk.labs.Lab9.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ButtonCell implements TableCellRenderer, TableCellEditor {
    private int selectedRow;
    private int selectedColumn;
    private Object selectedValue;
  
    private Component buildComponent(JTable table, Object value,
                     boolean isSelected, boolean hasFocus, int row, int column) {
        JButton button = (JButton)value;
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else{
            button.setForeground(table.getForeground());
            button.setBackground(UIManager.getColor("Button.background"));
        }
        return button;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                     boolean isSelected, boolean hasFocus, int row, int column) {
        return buildComponent(table, value, isSelected, hasFocus, row, column);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedRow = row;
        selectedColumn = column;
        selectedValue = value;
        return buildComponent(table, value, isSelected, true, row, column);
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
    }

}
