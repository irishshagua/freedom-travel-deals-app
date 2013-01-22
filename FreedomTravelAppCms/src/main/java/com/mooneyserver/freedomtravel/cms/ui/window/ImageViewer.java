package com.mooneyserver.freedomtravel.cms.ui.window;

import javax.swing.JDialog;

import java.awt.image.BufferedImage;

import com.mooneyserver.freedomtravel.cms.ui.widget.ImagePanel;

public class ImageViewer extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private ImagePanel imgPanel;

	/*
	 * Constructors
	 */
	public ImageViewer(BufferedImage img) {
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setBounds(100, 100, img.getWidth() + 30, img.getHeight() + 50);
		

		imgPanel = new ImagePanel(img);
		imgPanel.setBounds(10, 10, img.getWidth(), img.getHeight());
		imgPanel.setRespondToClick(false);
		
		getContentPane().add(imgPanel);
	}	
}