package com.mooneyserver.freedomtravel.cms.mongo.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mooneyserver.freedomtravel.cms.mongo.RestQueryCriteria;

class SimpleHttpRequest {

	/*
	 * Internal enum to hold the added detail required for
	 * each request type
	 */
	enum HTTP_REQ_TYPE {
		GET			("Accept", "application/json"),
		POST		("Content-Type", "application/json"),
		DELETE		("Content-Type", "application/x-www-form-urlencoded");
		
		private String key, val;
		
		HTTP_REQ_TYPE(String key, String val) {
			this.key = key;
			this.val = val;
		}
		
		public String key() {return key;}
		public String val() {return val;}
	}
	
	
	/**
	 * GET request type
	 * 
	 * @param criteria
	 * 
	 * @return
	 * 	String of JSON data
	 * 
	 * @throws RestQueryFailedException 
	 */
	public static String getHttpCall(RestQueryCriteria criteria) 
			throws RestQueryFailedException {
		
		StringBuilder output = new StringBuilder();
		
		HttpURLConnection conn = null;
		try {
			conn = getConnectionForAction(criteria, HTTP_REQ_TYPE.GET);
			checkReturnCode(conn, criteria);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			
			String data;
			while ((data = br.readLine()) != null) {
				output.append(data);
			}
		} catch (IOException e) {
			throw new RestQueryFailedException("Generic Wrapped Error!", e);
		} finally {
			conn.disconnect();
		}
		
		return output.toString();
	}
	
	/**
	 * POST request type
	 * The JSON string that needs to be posted
	 * is passed as data param
	 * 
	 * @param criteria
	 * @param data
	 * @throws RestQueryFailedException 
	 */
	public static void postHttpCall(RestQueryCriteria criteria, String data) 
			throws RestQueryFailedException {
		
		HttpURLConnection conn = null;
		try {
			conn = getConnectionForAction(criteria, HTTP_REQ_TYPE.POST);
			conn.setDoOutput(true);
			
			OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
			os.write(data);
			os.close();
			
			checkReturnCode(conn, criteria);
			
		} catch (IOException e) {
			throw new RestQueryFailedException("Generic Wrapped Error!", e);
		} finally {
			conn.disconnect();
		}
	}
	
	
	/**
	 * DELETE request type
	 * 
	 * @param criteria
	 * @throws RestQueryFailedException 
	 */
	public static void deleteHttpCall(RestQueryCriteria criteria) 
			throws RestQueryFailedException {
		
		HttpURLConnection conn = null;
		try {
			conn = getConnectionForAction(criteria, HTTP_REQ_TYPE.DELETE);			
			checkReturnCode(conn, criteria);
		} catch (IOException e) {
			throw new RestQueryFailedException("Generic Wrapped Error!", e);
		} finally {
			conn.disconnect();
		}
	}
	
	/*
	 * Make the connection for the HTTP call. Common to all request types.
	 * IMPORTANT. Connection needs to be closed (disconnected) by the caller
	 * of this method
	 */
	private static HttpURLConnection getConnectionForAction(
			RestQueryCriteria criteria, HTTP_REQ_TYPE type) 
					throws RestQueryFailedException {
		
		try {
			URL url = new URL(criteria.buildCriteriaFromFilter());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(type.toString());
			conn.setRequestProperty(type.key(), type.val());
			
			return conn;
		} catch (IOException e) {
			throw new RestQueryFailedException("Generic Wrapped Error!", e);
		}
	}
	
	/*
	 * Check for a valid return code. All operation return 200 on success
	 */
	private static void checkReturnCode(HttpURLConnection conn, RestQueryCriteria criteria) 
			throws RestQueryFailedException, IOException {
		
		if (conn.getResponseCode() != 200) {
			throw new RestQueryFailedException("Failed : HTTP error code : "
					+ conn.getResponseCode() + ". Using Query {"+criteria.buildCriteriaFromFilter()+"}");
		}
	}
}
