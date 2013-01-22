package com.mooneyserver.freedomtravel.cms.mongo.rest;

public class RestQueryFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public RestQueryFailedException(String string) {
		super(string);
	}
	
	public RestQueryFailedException(String string, Throwable e) {
		super(string, e);
	}

}
