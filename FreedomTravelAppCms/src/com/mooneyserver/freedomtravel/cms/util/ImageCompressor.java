package com.mooneyserver.freedomtravel.cms.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

public class ImageCompressor {
	
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
		double scale = ((double) image.getHeight() / (double) image.getWidth());
		int newW = 400;
		int newH = (int) (400D * scale);
		BufferedImage resizedImage = new BufferedImage(newW, newH,
				image.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, newW, newH, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		
		return compressTest(resizedImage);
	}

	private static BufferedImage compressTest(BufferedImage image) {

		try {
			Iterator<?> iter = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer = (ImageWriter) iter.next();
			File f = File.createTempFile("tmpScaleImg", "JPG");
			ImageOutputStream ios = ImageIO.createImageOutputStream(f);
			writer.setOutput(ios);
			ImageWriteParam iwparam = new JPEGImageWriteParam(
					Locale.getDefault());
			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwparam.setCompressionQuality(1.0F);
			writer.write(null, new IIOImage(image, null, null), iwparam);
			ios.flush();
			writer.dispose();
			ios.close();
			return ImageIO.read(f);

		} catch (Exception e) {
			StaticUtilityClass.logSystemError("Image Compression failed", e);
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
