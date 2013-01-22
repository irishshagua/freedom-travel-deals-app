package com.mooneyserver.freedomtravel.cms.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AppConstants {

	public static boolean DEBUG = false;
	
	public static BufferedImage loadLandingPageImgFromJar() {
		try {
			return ImageIO.read(AppConstants.class.getResource("rsc/landingPage.png"));
		} catch (IOException e) {
			return new BufferedImage(0, 0, 0);
		}
	}
	
	/*
	 * Here for Debug. Will be removed
	 */
	public static BufferedImage loadHolidayImgFromJar() {
		try {
			return ImageIO.read(AppConstants.class.getResource("rsc/smallHoliday.png"));
		} catch (IOException e) {
			return new BufferedImage(0, 0, 0);
		}
	}
	
	// Special HTML Characters
    public static final String EURO_SIGN = "\u20ac";
    public static final String POUND_SIGN = "\u00a3";
    public static final String LESS_THAN_SIGN = "<";
    public static final String GREATER_THAN_SIGN = ">";
    public static final String COPYRIGHT_SIGN = "\u00a9";
    public static final String AMPERSAND_SIGN = "&";   

    public static final String HTML_EURO_SIGN = "&euro;";
    public static final String HTML_POUND_SIGN = "&pound;";
    public static final String HTML_LESS_THAN_SIGN = "&lt;";
    public static final String HTML_GREATER_THAN_SIGN = "&gt;";
    public static final String HTML_COPYRIGHT_SIGN = "&copy;";
    public static final String HTML_AMPERSAND_SIGN = "&amp;";
}