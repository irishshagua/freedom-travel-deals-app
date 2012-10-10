package com.mooneyserver.freedomtravel.cms.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AppConstants {

	public static boolean DEBUG = false;
	
	public static BufferedImage loadLandingPageImgFromJar() {
		try {
			return ImageIO.read(AppConstants.class.getResource("landingPage.png"));
		} catch (IOException e) {
			return new BufferedImage(0, 0, 0);
		}
	}
	
	/*
	 * Here for Debug. Will be removed
	 */
	public static BufferedImage loadHolidayImgFromJar() {
		try {
			return ImageIO.read(AppConstants.class.getResource("smallHoliday.png"));
		} catch (IOException e) {
			return new BufferedImage(0, 0, 0);
		}
	}
}
