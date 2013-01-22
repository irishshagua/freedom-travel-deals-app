package com.mooneyserver.freedomtravel.cms.ui.window;

import java.io.IOException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import com.mooneyserver.freedomtravel.cms.mongo.FreedomTravelDeal;
import com.mooneyserver.freedomtravel.cms.mongo.FreedomTravelDeal.DealDetails;
import com.mooneyserver.freedomtravel.cms.mongo.rest.MongoFactory;
import com.mooneyserver.freedomtravel.cms.mongo.rest.RestQueryFailedException;
import com.mooneyserver.freedomtravel.cms.ui.filechooser.DealImageFilter;
import com.mooneyserver.freedomtravel.cms.ui.widget.ImagePanel;
import com.mooneyserver.freedomtravel.cms.util.AppConstants;
import com.mooneyserver.freedomtravel.cms.util.ImageCompressor;
import com.mooneyserver.freedomtravel.cms.util.StaticUtilityClass;

public class ViewEditSingleDeal extends JDialog implements ChangeListener,
		ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField headingTextField;
	private JToggleButton setEditableTglBtn;
	private JButton changeImgBtn;
	private JButton charCopyDropdownBtn;
	private final JPopupMenu popupMenu;
	private JTextArea mainContentTextArea;
	private JScrollPane scrollMainTextArea;
	private JButton saveCloseBtn;
	private ImagePanel imgPanel;
	private final Action saveCloseTheContentViewDialog = new SaveCloseAction();
	private final Action Remove = new RemoveAction();
	private FreedomTravelDeal deal;
	private JLabel lblDestination;
	private JLabel lblPrice;
	private JLabel lblDate;
	private JLabel lblDuration;
	private JLabel lblAccomodation;
	private JLabel lblBasis;
	private JTextField destinationTextField;
	private JTextField priceTextField;
	private JTextField dateTextField;
	private JTextField durationTextField;
	private JTextField accomTextField;
	private JTextField basisTextField;

	/*
	 * Constructors
	 */
	public ViewEditSingleDeal() throws IOException { this(null); }

	public ViewEditSingleDeal(FreedomTravelDeal deal) throws IOException {

		this.deal = deal;

		((SaveCloseAction) saveCloseTheContentViewDialog).setParentDialog(this);
		((RemoveAction) Remove).setParentDialog(this);

		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 424, 655);

		getContentPane().setLayout(null);

		setEditableTglBtn = new JToggleButton("Switch to Edit Mode");
		setEditableTglBtn.setBounds(237, 11, 169, 23);

		setEditableTglBtn.addChangeListener(this);

		popupMenu = new JPopupMenu();
		CopyToClipboard copyListener = new CopyToClipboard();
		
		JMenuItem mntmEuroSign = new JMenuItem(AppConstants.EURO_SIGN);
		mntmEuroSign.addActionListener(copyListener);
		popupMenu.add(mntmEuroSign);

		JMenuItem mntmPoundSign = new JMenuItem(AppConstants.POUND_SIGN);
		mntmPoundSign.addActionListener(copyListener);
		popupMenu.add(mntmPoundSign);

		JMenuItem mntmLessThan = new JMenuItem(AppConstants.LESS_THAN_SIGN);
		mntmLessThan.addActionListener(copyListener);
		popupMenu.add(mntmLessThan);

		JMenuItem mntmGreaterThan = new JMenuItem(
				AppConstants.GREATER_THAN_SIGN);
		mntmGreaterThan.addActionListener(copyListener);
		popupMenu.add(mntmGreaterThan);

		JMenuItem mntmCopyright = new JMenuItem(AppConstants.COPYRIGHT_SIGN);
		mntmCopyright.addActionListener(copyListener);
		popupMenu.add(mntmCopyright);
		
		JMenuItem mntmAmpersand = new JMenuItem(AppConstants.AMPERSAND_SIGN);
		mntmAmpersand.addActionListener(copyListener);
		popupMenu.add(mntmAmpersand);

		getContentPane().add(setEditableTglBtn);

		saveCloseBtn = new JButton("Close");
		saveCloseBtn.setAction(saveCloseTheContentViewDialog);
		saveCloseBtn.setBounds(10, 590, 132, 23);
		getContentPane().add(saveCloseBtn);

		JLabel lblNewLabel = new JLabel("Heading");
		lblNewLabel.setBounds(28, 49, 46, 14);
		getContentPane().add(lblNewLabel);

		headingTextField = new JTextField();
		headingTextField.setBounds(28, 74, 378, 20);
		getContentPane().add(headingTextField);
		headingTextField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Image");
		lblNewLabel_1.setBounds(28, 105, 46, 14);
		getContentPane().add(lblNewLabel_1);

		if (deal == null)
			imgPanel = new ImagePanel(null);
		else
			imgPanel = new ImagePanel(deal.getImage());
		
		imgPanel.setBounds(28, 130, 125, 125);
		getContentPane().add(imgPanel);

		changeImgBtn = new JButton("Load New Image");
		changeImgBtn.setBounds(162, 225, 149, 23);


		changeImgBtn.addActionListener(this);
		getContentPane().add(changeImgBtn);

		JLabel lblNewLabel_2 = new JLabel("Content");
		lblNewLabel_2.setBounds(28, 409, 46, 14);
		getContentPane().add(lblNewLabel_2);

		
		mainContentTextArea = new JTextArea();
		mainContentTextArea.setWrapStyleWord(true);
		mainContentTextArea.setBounds(28, 434, 378, 145);
		mainContentTextArea.setLineWrap(true);
		
		scrollMainTextArea = new JScrollPane(mainContentTextArea);
		scrollMainTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollMainTextArea.setBounds(28, 434, 378, 145);
		getContentPane().add(scrollMainTextArea);
		
		JButton btnNewButton = new JButton("Remove");
		btnNewButton.setAction(Remove);
		btnNewButton.setBounds(293, 589, 117, 25);


		getContentPane().add(btnNewButton);

		lblDestination = new JLabel("Destination");
		lblDestination.setBounds(28, 259, 67, 14);
		getContentPane().add(lblDestination);

		lblPrice = new JLabel("Price");
		lblPrice.setBounds(28, 284, 46, 14);
		getContentPane().add(lblPrice);

		lblDate = new JLabel("Date");
		lblDate.setBounds(28, 308, 46, 14);
		getContentPane().add(lblDate);

		lblDuration = new JLabel("Duration");
		lblDuration.setBounds(28, 333, 46, 14);
		getContentPane().add(lblDuration);
		
		lblAccomodation = new JLabel("Accomodation");
		lblAccomodation.setBounds(28, 358, 89, 14);
		getContentPane().add(lblAccomodation);
		
		lblBasis = new JLabel("Basis");
		lblBasis.setBounds(28, 383, 46, 14);
		getContentPane().add(lblBasis);

		destinationTextField = new JTextField();
		destinationTextField.setBounds(115, 256, 291, 20);
		destinationTextField.setColumns(10);
		getContentPane().add(destinationTextField);
		
		priceTextField = new JTextField();
		priceTextField.setColumns(10);
		priceTextField.setBounds(115, 281, 291, 20);
		getContentPane().add(priceTextField);

		dateTextField = new JTextField();
		dateTextField.setColumns(10);
		dateTextField.setBounds(115, 305, 291, 20);
		getContentPane().add(dateTextField);

		durationTextField = new JTextField();
		durationTextField.setColumns(10);
		durationTextField.setBounds(115, 330, 291, 20);
		getContentPane().add(durationTextField);

		accomTextField = new JTextField();
		accomTextField.setColumns(10);
		accomTextField.setBounds(115, 355, 291, 20);
		getContentPane().add(accomTextField);

		basisTextField = new JTextField();
		basisTextField.setColumns(10);
		basisTextField.setBounds(115, 380, 291, 20);
		getContentPane().add(basisTextField);

		charCopyDropdownBtn = new JButton("");
		charCopyDropdownBtn.setIcon(new ImageIcon
				(ViewEditSingleDeal.class.getResource
						("/com/mooneyserver/freedomtravel/cms/util/rsc/dropdown_icon.gif")));
		charCopyDropdownBtn.setBounds(10, 11, 27, 23);
		charCopyDropdownBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		getContentPane().add(charCopyDropdownBtn);		
		
		
		if (deal == null) {
			setEditableTglBtn.setEnabled(false);
			setEditableTglBtn.setText("Switch to View Mode");
			
			
			headingTextField.setEditable(true);
			mainContentTextArea.setEditable(true);
			changeImgBtn.setEnabled(true);
			
			saveCloseBtn.setText("Save");
			
			btnNewButton.setEnabled(false);
		} else {
			headingTextField.setEditable(false);
			headingTextField.setText(deal.getTitle());
			
			changeImgBtn.setEnabled(false);
			
			mainContentTextArea.setEditable(false);
			mainContentTextArea.setText(deal.getBody());
			
			if (deal.getDetails() != null) {
				destinationTextField.setText(deal.getDetails().getDestination());
				priceTextField.setText(deal.getDetails().getPrice());
				dateTextField.setText(deal.getDetails().getDate());
				durationTextField.setText(deal.getDetails().getDuration());
				accomTextField.setText(deal.getDetails().getAccomodation());
				basisTextField.setText(deal.getDetails().getBasis());
			}
		}
	}

	public FreedomTravelDeal getDeal() {
		return deal;
	}

	public String getHeading() {
		return headingTextField.getText();
	}
	
	public String getBody() {
		return mainContentTextArea.getText();
	}

	public String getDetailsDestination() {
		return destinationTextField.getText();
	}
	
	public String getDetailsPrice() {
		return priceTextField.getText();
	}
	
	public String getDetailsDate() {
		return dateTextField.getText();
	}
	
	public String getDetailsDuration() {
		return durationTextField.getText();
	}
	
	public String getDetailsAccomodation() {
		return accomTextField.getText();
	}
	
	public String getDetailsBasis() {
		return basisTextField.getText();
	}

	public BufferedImage getImage() {
		return ((ImagePanel) imgPanel).getImage();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (setEditableTglBtn.getText().equalsIgnoreCase("Switch to Edit Mode")) {
			setEditableTglBtn.setText("Switch to View Mode");
			headingTextField.setEditable(true);
			changeImgBtn.setEnabled(true);
			mainContentTextArea.setEditable(true);
			saveCloseBtn.setText("Save Changes");
		} else {
			setEditableTglBtn.setText("Switch to Edit Mode");
			headingTextField.setEditable(false);
			changeImgBtn.setEnabled(false);
			mainContentTextArea.setEditable(false);
			saveCloseBtn.setText("Close");
		}
	}

	private class CopyToClipboard implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String symbol = null;
			switch (e.getActionCommand()) {
				case AppConstants.AMPERSAND_SIGN:
					symbol = AppConstants.HTML_AMPERSAND_SIGN;
					break;
				case AppConstants.EURO_SIGN:
					symbol = AppConstants.HTML_EURO_SIGN;
					break;
				case AppConstants.POUND_SIGN:
					symbol = AppConstants.HTML_POUND_SIGN;
					break;
				case AppConstants.LESS_THAN_SIGN:
					symbol = AppConstants.HTML_LESS_THAN_SIGN;
					break;
				case AppConstants.GREATER_THAN_SIGN:
					symbol = AppConstants.HTML_GREATER_THAN_SIGN;
					break;
				case AppConstants.COPYRIGHT_SIGN:
					symbol = AppConstants.HTML_COPYRIGHT_SIGN;
					break;
			}

			StaticUtilityClass.copyToSystemClipboard(symbol);
		}
	}

	private class SaveCloseAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		ViewEditSingleDeal parent = null;
		public SaveCloseAction() {
			putValue(NAME, "Close");
			putValue(SHORT_DESCRIPTION, "Save or Update the Deal Content");
		}

		public void actionPerformed(ActionEvent e) {
			if (setEditableTglBtn.getText().equalsIgnoreCase(
					"Switch to Edit Mode")) {
				parent.dispose();
			} else {
				FreedomTravelDeal deal = parent.getDeal() == null ?
				new FreedomTravelDeal() : parent.getDeal();
				
				deal.setTitle(parent.getHeading());
				deal.setBody(parent.getBody());
				
				DealDetails details = deal.new DealDetails();
				details.setDestination(parent.getDetailsDestination());
				details.setPrice(parent.getDetailsPrice());
				details.setDate(parent.getDetailsDate());
				details.setDuration(parent.getDetailsDuration());
				details.setAccomodation(parent.getDetailsAccomodation());
				details.setBasis(parent.getDetailsBasis());
				
				deal.setDetails(details);
				
				try {
					deal.setImage(parent.getImage());
					MongoFactory.putDealLive(deal);
					parent.dispose();
				} catch (RestQueryFailedException e1) {
					StaticUtilityClass.logSystemError(
							"Error Trying to persist deal to datastore", e1);
				} catch (IOException e1) {
					StaticUtilityClass.logSystemError(
							"Error Serializing Deal Image", e1);
				}
			}
		}

		public void setParentDialog(ViewEditSingleDeal dialog) {
			parent = dialog;
		}
	}

	private class RemoveAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		JDialog parent = null;
		public RemoveAction() {
			putValue(NAME, "RemoveDeal");
			putValue(SHORT_DESCRIPTION, "Remove the deal from the datastore");
		}

		public void actionPerformed(ActionEvent e) {
			try {
				MongoFactory.deleteLiveDeal(deal);
			} catch (RestQueryFailedException e1) {
				StaticUtilityClass.logSystemError("Error Deleting Deal", e1);
			}

			parent.dispose();
		}

		public void setParentDialog(JDialog dialog) {
			parent = dialog;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser imgSelect = new JFileChooser();
		imgSelect.setAcceptAllFileFilterUsed(false);
		imgSelect.addChoosableFileFilter(new DealImageFilter());
		int returnVal = imgSelect.showOpenDialog(this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.imgPanel.setImage(ImageCompressor.getScaledImage(imgSelect.getSelectedFile()));
		}
	}
}