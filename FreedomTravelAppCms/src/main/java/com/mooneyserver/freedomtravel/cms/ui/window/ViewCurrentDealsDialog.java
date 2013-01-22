package com.mooneyserver.freedomtravel.cms.ui.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.mooneyserver.freedomtravel.cms.mongo.FreedomTravelDeal;
import com.mooneyserver.freedomtravel.cms.mongo.rest.MongoFactory;
import com.mooneyserver.freedomtravel.cms.mongo.rest.RestQueryFailedException;
import com.mooneyserver.freedomtravel.cms.util.StaticUtilityClass;

public class ViewCurrentDealsDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private JTable table;

	/**
	 * Create the dialog.
	 */
	public ViewCurrentDealsDialog() {
		// Query the Live Deals to build the table
		List<FreedomTravelDeal> deals = new ArrayList<>();
		try {
			deals = MongoFactory.getAllLiveDeals();
		} catch (RestQueryFailedException e) {
			StaticUtilityClass.logSystemError("Error Querying Datastore", e);
		}
		
		Object[][] tableDeals = new Object[deals.size()][2];
		for (int i = 0; i < deals.size(); i++) {
			tableDeals[i][0] = deals.get(i).getTitle();
			tableDeals[i][1] = "\u2603";
		}
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Current Live Deals");
		setBounds(100, 100, 450, 300);
		{
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.setModel(new DefaultTableModel(
					tableDeals,
					new String[] {
						"Deal Heading", "View/Edit"
					}
				) {
					
					private static final long serialVersionUID = 1L;
					
					Class<?>[] columnTypes = new Class[] {
						String.class, JButton.class
					};
					public Class<?> getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
					boolean[] columnEditables = new boolean[] {
						false, true
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
				
				table.getColumnModel().getColumn(0).setPreferredWidth(150);
				table.getColumnModel().getColumn(0).setMinWidth(150);
				table.getColumnModel().getColumn(1).setPreferredWidth(100);
				table.getColumnModel().getColumn(1).setMinWidth(50);
				
				table.getColumn("View/Edit").setCellRenderer(new ButtonRenderer());
				table.getColumn("View/Edit").setCellEditor(
				        new ButtonEditor(new JCheckBox(), deals)
				        );
				
				scrollPane.setViewportView(table);
			}
		}
	}

}

class ButtonRenderer extends JButton implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public ButtonRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}
}

class ButtonEditor extends DefaultCellEditor {
	
	private static final long serialVersionUID = 1L;

	protected JButton button;

	private String label;
	private List<FreedomTravelDeal> deals;
	
	private int row = -1;

	private boolean isPushed;

	public ButtonEditor(JCheckBox checkBox, List<FreedomTravelDeal> deals) {
		super(checkBox);
		
		this.deals = deals;
		
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.row = row;
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	public Object getCellEditorValue() {
		if (isPushed) {
			try {
				new ViewEditSingleDeal(deals.get(row)).setVisible(true);
			} catch (IOException e) {
				StaticUtilityClass.logSystemError("Error Trying to load Deal To View", e);
			}
		}
		isPushed = false;
		return new String(label);
	}

	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}