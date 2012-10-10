package com.mooneyserver.freedomtravel.cms.mongo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class FreedomTravelDeal {
	
	private String _id;
	private String title;
	private String body;
	private String image;
	
	public FreedomTravelDeal() {
		this._id = generateId();
	}
	
	public String getId() {return _id;}
	public void setId(String id) {this._id = id;}
	
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	
	public String getBody() {return body;}
	public void setBody(String body) {this.body = body;}
	
	
	public BufferedImage getImage() throws IOException {
		InputStream in = new ByteArrayInputStream(Base64.decodeBase64(image.getBytes()));
		return ImageIO.read(in);
	}
	public void setImage(BufferedImage image) throws IOException {
		byte[] imageInByte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		imageInByte = baos.toByteArray();
		baos.close();
		
		this.image = new String(Base64.encodeBase64(imageInByte));
	}

	@Override
	public String toString() {
		return title + "\n" + body;
	}
	
	private String generateId() {
		return UUID.randomUUID().toString();
	}
}
