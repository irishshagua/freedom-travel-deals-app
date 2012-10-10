package com.mooneyserver.freedomtravel.cms.ui.window;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Action;

import com.mooneyserver.freedomtravel.cms.ui.widget.ImagePanel;
import com.mooneyserver.freedomtravel.cms.util.StaticUtilityClass;

public class MainCmsFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private final Action launchViewDialog = new SwingAction();
	private final Action closeApp = new SwingAction_1();
	private final Action addNewDeal = new AddNewDealAction();

	/**
	 * Create the frame.
	 */
	public MainCmsFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setResizable(false);
		setTitle("Freedom Travel Deals");
		setBounds(100, 100, 388, 358);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.setAction(closeApp);
		mnView.add(mntmClose);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenuItem mntmView = new JMenuItem("View/Edit");
		mntmView.setAction(launchViewDialog);
		mnOptions.add(mntmView);
		
		JMenuItem mntmAdd = new JMenuItem("Add");
		mntmAdd.setAction(addNewDeal);
		mnOptions.add(mntmAdd);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new ImagePanel();
		contentPane.add(panel, BorderLayout.CENTER);
	}

	private class SwingAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public SwingAction() {
			putValue(NAME, "View/Edit");
			putValue(SHORT_DESCRIPTION, "Launch Edit/View Dialog");
		}
		
		public void actionPerformed(ActionEvent e) {
			JDialog viewDeals = new ViewCurrentDealsDialog();
			viewDeals.setModalityType(ModalityType.APPLICATION_MODAL);
			viewDeals.setVisible(true);
		}
	}
	
	private class SwingAction_1 extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public SwingAction_1() {
			putValue(NAME, "Close");
			putValue(SHORT_DESCRIPTION, "Exit The Application");
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	private class AddNewDealAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		public AddNewDealAction() {
			putValue(NAME, "Add");
			putValue(SHORT_DESCRIPTION, "Add a New Deal to the Datastore");
		}
		public void actionPerformed(ActionEvent e) {
			try {
				new ViewEditSingleDeal().setVisible(true);
			} catch (IOException e1) {
				StaticUtilityClass.logSystemError("Error Trying to load Deal To View", e1);
			}
		}
	}
}
