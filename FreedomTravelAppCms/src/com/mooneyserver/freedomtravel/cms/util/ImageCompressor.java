package com.mooneyserver.freedomtravel.cms.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImageCompressor {
	
	private static String TMP_IMG_FILE = "FT_TMP_SCALED_IMG";
	
	public static BufferedImage getScaledImage(File file) {

		try {
			if (file.length() > 51200L) { // 51200 is akin to 50KiB image size.
											// Parameterize this
				StaticUtilityClass
						.userNotification("File is excessively large ("
						+ humanReadableImageSize(file)
								+ "). Scaling will be performed!");
				return scaleImage(ImageIO.read(file));
			} else {
				return ImageIO.read(file);
			}

		} catch (IOException e1) {
			StaticUtilityClass.logSystemError(
					"Error Loading Image {" + file.getName() + "}", e1);
			return null;
		}
	}

	private static BufferedImage scaleImage(BufferedImage image) {
		
		BufferedImage scaledImg = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, 400, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
		File tmp;
		try {
			tmp = File.createTempFile(TMP_IMG_FILE, null);
			ImageIO.write(scaledImg, "jpeg", tmp);
			
			return ImageIO.read(tmp);
		} catch (IOException e) {
			StaticUtilityClass.logSystemError(
					"Image Compression Failed", e);
			return image;
		}
	}

	private static String humanReadableImageSize(File file) {
		long bytes = file.length();
		int unit = 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = ("KMGTPE").charAt(exp - 1) + ("i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}