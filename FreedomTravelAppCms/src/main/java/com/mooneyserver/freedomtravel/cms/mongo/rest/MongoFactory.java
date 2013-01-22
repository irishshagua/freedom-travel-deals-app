package com.mooneyserver.freedomtravel.cms.mongo.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.mooneyserver.freedomtravel.cms.mongo.FreedomTravelDeal;
import com.mooneyserver.freedomtravel.cms.mongo.RestQueryCriteria;

import static com.mooneyserver.freedomtravel.cms.properties.SystemSettings.SETTINGS;

public class MongoFactory {
	
	/**
	 * Return a List of all the currently Live Deals
	 * 
	 * @return
	 * 	List of FreedomTravelDeal objects
	 * 
	 * @throws RestQueryFailedException
	 */
	public static List<FreedomTravelDeal> getAllLiveDeals() throws RestQueryFailedException {
		Gson gson = new Gson();
		
		Type collectionType = new TypeToken<List<FreedomTravelDeal>>(){}.getType();
		List<FreedomTravelDeal> deals = gson.fromJson(SimpleHttpRequest.getHttpCall(
				new RestQueryCriteria(SETTINGS.getProp("datastore.name"), 
						SETTINGS.getProp("datastore.collection.deals.name"))), 
				collectionType);
		
		return deals == null ? new ArrayList<FreedomTravelDeal>() : deals;
	}
	
	
	/**
	 * Post a NEW Deal or Update and Existing Deal
	 * 
	 * @param deal
	 * 
	 * @throws RestQueryFailedException
	 */
	public static void putDealLive(FreedomTravelDeal deal) throws RestQueryFailedException {
		RestQueryCriteria criteria = new RestQueryCriteria(
				SETTINGS.getProp("datastore.name"), 
				SETTINGS.getProp("datastore.collection.deals.name"));
		SimpleHttpRequest.postHttpCall(criteria, new Gson().toJson(deal));
	}
	
	
	/**
	 * Remove a Live Deal
	 * 
	 * @param deal
	 * 
	 * @throws RestQueryFailedException
	 */
	public static void deleteLiveDeal(FreedomTravelDeal deal) throws RestQueryFailedException {
		RestQueryCriteria criteria = new RestQueryCriteria(
				SETTINGS.getProp("datastore.name"), 
				SETTINGS.getProp("datastore.collection.deals.name"), 
				deal.getId());
		SimpleHttpRequest.deleteHttpCall(criteria);
	}
}