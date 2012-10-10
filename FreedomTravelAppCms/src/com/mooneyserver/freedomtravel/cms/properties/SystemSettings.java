package com.mooneyserver.freedomtravel.cms.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mooneyserver.freedomtravel.cms.util.AppConstants;
import com.mooneyserver.freedomtravel.cms.util.StaticUtilityClass;

public enum SystemSettings {

	SETTINGS;
	
	private Properties prop = null;
	
	public void reloadProps() {		
		prop = new Properties();
		
		if (AppConstants.DEBUG) {
			try (InputStream in = getClass().getResourceAsStream("freedomtravel.props")) {
				prop.load(in);
			} catch (IOException e) {
				StaticUtilityClass.logSystemError("Error Trying to Load Properties", e);
			}
		} else {
			try (FileInputStream in = new FileInputStream("./freedomtravel.props")) {
				prop.load(in);
			} catch (IOException e) {
				StaticUtilityClass.logSystemError("Error Trying to Load Properties", e);
			}
		}
		 
	}
	
	public String getProp(String key) {
		if (prop == null)
			reloadProps();
		
		return prop.getProperty(key);
	}
}