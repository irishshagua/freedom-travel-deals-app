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
	private DealDetails details;
	private String body;
	private String image;
	
	public FreedomTravelDeal() {
		this._id = generateId();
	}
	
	public String getId() {return _id;}
	public void setId(String id) {this._id = id;}
	
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	public DealDetails getDetails() {return details;}
	public void setDetails(DealDetails details) { this.details = details;	}
	
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
	
	public class DealDetails {
		
		private String destination;
		private String price;
		private String date;
		private String duration;
		private String accomodation;
		private String basis;
		
		public String getDestination() { return destination == null ? "" : destination; }
		public void setDestination(String destination) { this.destination = destination; }
		
		public String getPrice() { return price == null ? "" : price; }
		public void setPrice(String price) { this.price = price; }
		
		public String getDate() { return date == null ? "" : date; }
		public void setDate(String date) { this.date = date; }
		
		public String getDuration() { return duration == null ? "" : duration; }
		public void setDuration(String duration) { this.duration = duration; }
		
		public String getAccomodation() { return accomodation == null ? "" : accomodation; }
		public void setAccomodation(String accomodation) { this.accomodation = accomodation;}
		
		public String getBasis() { return basis == null ? "" : basis;}
		public void setBasis(String basis) { this.basis = basis;}
	}
}
