package com.mooneyserver.freedomtravel.cms.ui.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import static com.mooneyserver.freedomtravel.cms.properties.SystemSettings.SETTINGS;

public class DealImageFilter extends FileFilter {
	
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
            return true;
        }
 
		String allowedFileTypes = SETTINGS.getProp("filechooser.filterby");
		String[] arrAllowedFileTypes = allowedFileTypes.split(";");
        String extension = getExtension(f);
        
        if (extension != null) {
            for (String fileType : arrAllowedFileTypes) {
            	if (extension.equalsIgnoreCase(fileType))
            		return true;
            }            
        }
 
        return false;
	}

	@Override
	public String getDescription() {
		return "Only Image Files {" + SETTINGS.getProp("filechooser.filterby") + "} Allowed";
	}
	
    
 
    /*
     * Get the extension of a file.
     */
    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}