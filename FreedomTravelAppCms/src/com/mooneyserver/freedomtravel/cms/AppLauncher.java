package com.mooneyserver.freedomtravel.cms;

import com.mooneyserver.freedomtravel.cms.ui.window.MainCmsFrame;
import com.mooneyserver.freedomtravel.cms.util.StaticUtilityClass;

public class AppLauncher {

	public static void main(String... args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				StaticUtilityClass.logSystemError("Generic System Error has occurred", e);
			}
		});

		MainCmsFrame frame = new MainCmsFrame();
		frame.setVisible(true);
	}
}