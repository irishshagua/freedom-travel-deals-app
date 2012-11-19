package com.mooneyserver.freedomtravel.cms.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class StaticUtilityClass {

	private static Logger log = Logger.getGlobal();
	
	public static void userNotification(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public static void logSystemError(String msg, Throwable e) {
		JOptionPane.showMessageDialog(null, 
				stackTraceToString(e), 
				msg, 
				JOptionPane.ERROR_MESSAGE);
		if (log.getHandlers().length < 1)
			try {
				log.addHandler(new FileHandler("errLog.log"));
			} catch (SecurityException | IOException e1) {
				JOptionPane.showMessageDialog(null, 
						stackTraceToString(e1), 
						"Error Setting up Logger", 
						JOptionPane.ERROR_MESSAGE);
			}
		
		log.log(Level.SEVERE, msg, e);
	}
    
	public static void copyToSystemClipboard(String symbol) {
		Clipboard clipboard = Toolkit.getDefaultToolkit()
				.getSystemClipboard();
		
		StringSelection data = new StringSelection(
				symbol == null ? "" : symbol);
		clipboard.setContents(data, data);
	}
	
    /*
     * Convert a Throwable's stack trace to a String
     */
	private static String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(e.getMessage() + "\n");
		
		for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    
	    return sb.toString();
	}
}