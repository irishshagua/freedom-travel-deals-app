package com.mooneyserver.freedomtravel.cms.ui.widget;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.mooneyserver.freedomtravel.cms.ui.window.ImageViewer;
import com.mooneyserver.freedomtravel.cms.util.AppConstants;

public class ImagePanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private boolean respondToClick = true;

    public ImagePanel() {
       image = AppConstants.loadLandingPageImgFromJar();
    }
    
    public ImagePanel(BufferedImage image) {
        if (image != null)
        	this.image = image;
        else
        	this.image = AppConstants.loadHolidayImgFromJar();
        
        addMouseListener(this);
     }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
    
    public BufferedImage getImage() {
    	return this.image;
    }
    
    public void setImage(BufferedImage image) {
    	this.image = image;
    	this.repaint();
    }
    
    public void setRespondToClick(boolean bool) {
    	respondToClick = bool;
    }
    
    public boolean isRespondToClick() {
    	return respondToClick;
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		if (respondToClick)
			new ImageViewer(image).setVisible(true);
	}

	
	// Unimplemented Listeners
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}