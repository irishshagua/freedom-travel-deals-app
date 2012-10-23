package com.mooneyserver.freedomtravel.cms.ui.window;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.AbstractAction;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.Action;

import com.mooneyserver.freedomtravel.cms.mongo.FreedomTravelDeal;
import com.mooneyserver.freedomtravel.cms.mongo.rest.MongoFactory;
import com.mooneyserver.freedomtravel.cms.mongo.rest.RestQueryFailedException;
import com.mooneyserver.freedomtravel.cms.ui.filechooser.DealImageFilter;
import com.mooneyserver.freedomtravel.cms.ui.widget.ImagePanel;
import com.mooneyserver.freedomtravel.cms.util.StaticUtilityClass;

public class ViewEditSingleDeal extends JDialog implements ChangeListener, ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JTextField headingTextField;
	private JToggleButton setEditableTglBtn;
	private JButton changeImgBtn;
	private JTextArea mainContentTextArea;
	private JButton saveCloseBtn;
	private ImagePanel imgPanel;
	private final Action saveCloseTheContentViewDialog = new SwingAction();
	private final Action Remove = new SwingAction_1();
	private FreedomTravelDeal deal;

	/*
	 * Constructors
	 */
	
	public ViewEditSingleDeal() throws IOException {
		this(null);
	}
	
	public ViewEditSingleDeal(FreedomTravelDeal deal) throws IOException {
		this.deal = deal;
		
		((SwingAction)saveCloseTheContentViewDialog).setParentDialog(this);
		((SwingAction_1)Remove).setParentDialog(this);
		
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 424, 655);
		getContentPane().setLayout(null);
		
		setEditableTglBtn = new JToggleButton("Switch to Edit Mode");
		setEditableTglBtn.setBounds(237, 11, 169, 23);
		if (this.deal == null) {
			setEditableTglBtn.setText("Switch to View Mode");
			setEditableTglBtn.setEnabled(false);
		}
		setEditableTglBtn.addChangeListener(this);
		
		
		getContentPane().add(setEditableTglBtn);
		
		saveCloseBtn = new JButton("Close");
		saveCloseBtn.setAction(saveCloseTheContentViewDialog);
		saveCloseBtn.setBounds(10, 590, 132, 23);
		if (this.deal == null) {
			saveCloseBtn.setText("Save");
		}
		getContentPane().add(saveCloseBtn);
		
		JLabel lblNewLabel = new JLabel("Heading");
		lblNewLabel.setBounds(28, 49, 46, 14);
		getContentPane().add(lblNewLabel);
		
		headingTextField = new JTextField();
		headingTextField.setBounds(28, 74, 378, 20);
		getContentPane().add(headingTextField);
		headingTextField.setColumns(10);
		if (this.deal != null) {
			headingTextField.setEditable(false);
			headingTextField.setText(deal.getTitle());
		}
		
		JLabel lblNewLabel_1 = new JLabel("Image");
		lblNewLabel_1.setBounds(28, 105, 46, 14);
		getContentPane().add(lblNewLabel_1);
		
		
		if (deal != null)
			imgPanel = new ImagePanel(deal.getImage());
		else
			imgPanel = new ImagePanel(null);
		
		imgPanel.setBounds(28, 130, 125, 125);
		getContentPane().add(imgPanel);
		
		changeImgBtn = new JButton("Load New Image");
		changeImgBtn.setBounds(163, 232, 149, 23);
		if (this.deal != null)
			changeImgBtn.setEnabled(false);
		changeImgBtn.addActionListener(this);
		getContentPane().add(changeImgBtn);
		
		
		JLabel lblNewLabel_2 = new JLabel("Content");
		lblNewLabel_2.setBounds(28, 266, 46, 14);
		getContentPane().add(lblNewLabel_2);
		
		mainContentTextArea = new JTextArea();
		mainContentTextArea.setWrapStyleWord(true);
		mainContentTextArea.setBounds(28, 291, 378, 288);
		mainContentTextArea.setLineWrap(true);
		if (this.deal != null) {
			mainContentTextArea.setEditable(false);
			mainContentTextArea.setText(deal.getBody());
		}
		
		getContentPane().add(mainContentTextArea);
		
		JButton btnNewButton = new JButton("Remove");
		btnNewButton.setAction(Remove);
		btnNewButton.setBounds(293, 589, 117, 25);
		if (this.deal == null)
			btnNewButton.setEnabled(false);
		
		getContentPane().add(btnNewButton);
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
	
	public BufferedImage getImage() {
		return ((ImagePanel)imgPanel).getImage();
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
	
	private class SwingAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		ViewEditSingleDeal parent = null;
		
		public SwingAction() {
			putValue(NAME, "Close");
			putValue(SHORT_DESCRIPTION, "Save or Update the Deal Content");
		}
		
		public void actionPerformed(ActionEvent e) {
			if (setEditableTglBtn.getText().equalsIgnoreCase("Switch to Edit Mode")) {
				parent.dispose();
			} else {
				FreedomTravelDeal deal = parent.getDeal() == null ? 
						new FreedomTravelDeal() : parent.getDeal();
				
				deal.setTitle(parent.getHeading());
				deal.setBody(parent.getBody());
				try {
					deal.setImage(parent.getImage());
				
					MongoFactory.putDealLive(deal);
					parent.dispose();
				} catch (RestQueryFailedException e1) {
					StaticUtilityClass.logSystemError("Error Trying to persist deal to datastore", e1);
				} catch (IOException e1) {
					StaticUtilityClass.logSystemError("Error Serializing Deal Image", e1);
				}
			}
		}
		
		public void setParentDialog(ViewEditSingleDeal dialog) {
			parent = dialog;
		}
	}
	
	
	private class SwingAction_1 extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		JDialog parent = null;
		
		public SwingAction_1() {
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
			this.imgPanel.setImage(getScaledImage(imgSelect.getSelectedFile()));
        }
	}
	
	private BufferedImage getScaledImage(File file) {
		try {
			if (file.length() > 51200L) { // 51200 is akin to 50KiB image size. Parameterize this
				StaticUtilityClass.userNotification("File is excessively large ("
						+humanReadableImageSize(file)+"). Scaling will be performed!");
				return scaleImage(ImageIO.read(file));
			} else {
				return ImageIO.read(file);
			}
		} catch (IOException e1) {
			StaticUtilityClass.logSystemError("Error Loading Image {"+file.getName()+"}", e1);
			return null;
		}
	}
	
	private BufferedImage scaleImage(BufferedImage image) {
		
		
		double scale = ((double) image.getHeight() / (double) image.getWidth());
		
		int newW = 400;
		int newH = (int)(400D * scale);
		
		BufferedImage resizedImage = new BufferedImage(newW, newH, image.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, newW, newH, null);
		g.dispose();
		
		g.setComposite(AlphaComposite.Src);
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		return compressTest(resizedImage);
	}
	
	public BufferedImage compressTest(BufferedImage image) {
		try {
			Iterator<?> iter = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer = (ImageWriter) iter.next();

			File f = File.createTempFile("tmpScaleImg", "JPG");
			ImageOutputStream ios = ImageIO.createImageOutputStream(f);
			writer.setOutput(ios);

			ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());

			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwparam.setCompressionQuality(1.0F);

			writer.write(null, new IIOImage(image, null, null), iwparam);

			ios.flush();
			writer.dispose();
			ios.close();
			
			return ImageIO.read(f);
		} catch (Exception e) {
			StaticUtilityClass.logSystemError("Image Compression failed", e);
			return image;
		}
    }

	
	private String humanReadableImageSize(File file) {
		long bytes = file.length();

		int unit = 1024;
		if (bytes < unit) 
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = ("KMGTPE").charAt(exp-1) + ("i");
		
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}