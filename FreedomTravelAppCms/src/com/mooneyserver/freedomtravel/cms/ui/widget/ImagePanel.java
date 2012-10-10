package com.mooneyserver.freedomtravel.cms.ui.widget;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.mooneyserver.freedomtravel.cms.util.AppConstants;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

    public ImagePanel() {
       image = AppConstants.loadLandingPageImgFromJar();
    }
    
    public ImagePanel(BufferedImage image) {
        if (image != null)
        	this.image = image;
        else
        	this.image = AppConstants.loadHolidayImgFromJar();
     }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);            
    }
    
    public BufferedImage getImage() {
    	return this.image;
    }
    
    public void setImage(BufferedImage image) {
    	this.image = image;
    	this.repaint();
    }
}