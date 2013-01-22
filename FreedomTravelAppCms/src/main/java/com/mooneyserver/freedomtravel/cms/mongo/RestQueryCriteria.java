package com.mooneyserver.freedomtravel.cms.mongo;

import static com.mooneyserver.freedomtravel.cms.properties.SystemSettings.SETTINGS;

public class RestQueryCriteria {
	
	private String databaseName, collectionName, objectId;
	
	private String BASE_URL = SETTINGS.getProp("datastore.api.url");
	private String API_KEY = SETTINGS.getProp("datastore.api.key");
	
	
	/*
	 * CONSTRUCTORS
	 */
	public RestQueryCriteria(String dbName, String collName, String objId) {
		databaseName = dbName;
		collectionName = collName;
		objectId = objId;
	}
	
	public RestQueryCriteria(String dbName, String collName) {
		this(dbName, collName, null);
	}
	
	public RestQueryCriteria(String dbName) {
		this(dbName, null, null);
	}
	
	public RestQueryCriteria() {
		this(null, null, null);
	}

	/*
	 * MUTATORS
	 */
	public String getDatabaseName() { return databaseName; }
	public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }

	public String getCollectionName() { return collectionName; }
	public void setCollectionName(String collectionName) { this.collectionName = collectionName; }

	public String getObjectId() { return objectId; }
	public void setObjectId(String objectId) { this.objectId = objectId; }

	
	
	public String buildCriteriaFromFilter() {
		StringBuilder criteriaQuery = new StringBuilder();
		
		criteriaQuery.append("databases");
		if (databaseName == null) return formatMongolQuery(criteriaQuery.toString());
		
		criteriaQuery.append("/" + databaseName + "/collections");
		if (collectionName == null) return formatMongolQuery(criteriaQuery.toString());
		
		criteriaQuery.append("/" + collectionName);
		if (objectId == null) return formatMongolQuery(criteriaQuery.toString());
		
		
		return formatMongolQuery(criteriaQuery.append("/" + objectId).toString());
	}
	
	private String formatMongolQuery(String internalQuery) {
		return BASE_URL + internalQuery + API_KEY;
	}
}
