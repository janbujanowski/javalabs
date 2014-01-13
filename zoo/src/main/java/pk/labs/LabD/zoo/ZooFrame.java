package pk.labs.LabD.zoo;


import pk.labs.LabD.contracts.Animal;
import pk.labs.LabD.contracts.Logger;
import pk.labs.LabD.contracts.LogEvent;
import pk.labs.LabD.contracts.LogListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import pk.labs.LabD.zoo.internal.ActionStub;

public class ZooFrame extends JFrame {

    private JTable table;
    private JComboBox<ActionStub> cb;

    private AtomicReference<Logger> logger = new AtomicReference<>();

	public void initComponents(final Zoo zoo) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("ZOO");
		setLayout(new BorderLayout());
		table = new JTable(new ZooTableModel(zoo));
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Set<Animal> selectedAnimals = new HashSet<>();
				for (int row : table.getSelectedRows()) {
					selectedAnimals.add(((ZooTableModel) table.getModel()).getAnimalForRow(row));
				}
				cb.setModel(new AnimalActionsModel(zoo.getActionsFor(selectedAnimals)));
			}
		});
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(table);
		getContentPane().add(pane, BorderLayout.CENTER);
		pane = new JScrollPane();
		pane.setAutoscrolls(true);
        final JTextArea console = new JTextArea();
		console.setRows(10);
		pane.setViewportView(console);
		getContentPane().add(pane, BorderLayout.SOUTH);
		JPanel sidepanel = new JPanel();
		getContentPane().add(sidepanel, BorderLayout.EAST);
		sidepanel.setLayout(new BorderLayout());
		JPanel actionpanel = new JPanel();
		sidepanel.add(actionpanel, BorderLayout.NORTH);
		actionpanel.setLayout(new GridLayout(2, 1, 0, 5));
		cb = new JComboBox<>();
		actionpanel.setBorder(BorderFactory.createTitledBorder("Czynność"));
		actionpanel.add(cb);
		JButton button = new JButton("Wykonaj");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionStub action = (ActionStub) cb.getSelectedItem();
				if (action != null) {
					Animal[] animals = zoo.getAnimals().toArray(new Animal[0]);
					for (int row : table.getSelectedRows())
						action.execute(animals[row]);
				}
			}
		});
		actionpanel.add(button);
		pack();

		logger.get().addLogListener(new LogListener() {
			@Override
			public void performLogEvent(LogEvent evt) {
				console.append(String.format("[%s] %s\n", evt.getSource(), evt.getMessage()));
			}
		});
	}

	private class ZooTableModel extends AbstractTableModel {

		private Zoo zoo;
		private AnimalListener listener;

		public ZooTableModel(Zoo zoo) {
			this.zoo = zoo;
			listener = new AnimalListener();
			for (Animal animal : zoo.getAnimals()) {
				animal.addPropertyChangeListener(listener);
			}
			zoo.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("animals")) {
						for (Animal animal : (Set<Animal>) evt.getOldValue()) {
							animal.removePropertyChangeListener(listener);
						}
						for (Animal animal : (Set<Animal>) evt.getNewValue()) {
							animal.addPropertyChangeListener(listener);
						}
					}
					fireTableDataChanged();
				}
			});
		}

		@Override
		public int getRowCount() {
			return zoo.getAnimalsCount();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
				case 0:
					return "Gatunek";
				case 1:
					return "Nazwa";
				case 2:
					return "Status";
			}
			return null;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Animal animal = getAnimalForRow(rowIndex);
            if (animal != null)
                switch (columnIndex) {
                    case 0:
                        return animal.getSpecies();
                    case 1:
                        return animal.getName();
                    case 2:
                        return animal.getStatus();
                }
			return null;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			throw new UnsupportedOperationException();
		}

		public Animal getAnimalForRow(int rowIndex) {
            Animal[] animals = zoo.getAnimals().toArray(new Animal[0]);
            if (animals.length > rowIndex)
			    return animals[rowIndex];
            return null;
		}

		private class AnimalListener implements PropertyChangeListener {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				fireTableDataChanged();
			}
		}
	}

    private class AnimalActionsModel extends DefaultComboBoxModel<ActionStub> {

        private Zoo zoo;

        public AnimalActionsModel(Collection<ActionStub> actions) {
            for (ActionStub action : actions)
                addElement(action);
        }
    }

    private void activate(final BundleContext context) {
        Runnable creator = new Runnable() {
            @Override
            public void run() {
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent evt) {
                        try {
                            context.getBundle(0).stop();
                        } catch (BundleException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                setVisible(true);
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            creator.run();
        } else {
            try {
                javax.swing.SwingUtilities.invokeAndWait(creator);
            } catch (Exception ex) {
                System.err.println("Nie udało się wystartować");
                ex.printStackTrace();
            }
        }
    }

    private void deactivate() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setVisible(false);
                    dispose();
                }
            });
        } catch (Exception ex) {
            System.err.println("Nie udało się zatrzymać");
            ex.printStackTrace();
        }
    }

    private void bindZoo(final Zoo zoo) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    initComponents(zoo);
                }
            });
        } catch (Exception ex) {

        }
    }

    private void bindLogger(Logger logger) {
        this.logger.set(logger);
    }

    private void unbindLogger(Logger logger) {
        this.logger.set(null);
    }
}
