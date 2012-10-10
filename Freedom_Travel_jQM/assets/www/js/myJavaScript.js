/***************************
 * Swipe Functionality
 ***************************/ 
$(document).ready(function() {
    // Swipe from home page
	$('#Home').live("swipeleft", function(){
        $.mobile.changePage("#Weekly-Deals", "slide", false, true);
    });
	$('#Home').live("swiperight", function(){
        $.mobile.changePage("#Enquiries", "flip", false, true);
    });
    
    // Swipes from Weekly Deals
    $('#Weekly-Deals').live("swiperight", function(){
        $.mobile.changePage("#Home", "slide", true, true);
    });
    $('#Weekly-Deals').live("swipeleft", function(){
        $.mobile.changePage("#Enquiries", "slide", true, true);
    });
    
    // Swipe from Enquiries
    $('#Enquiries').live("swiperight", function(){
        $.mobile.changePage("#Weekly-Deals", "slide", false, true);
    });
    $('#Enquiries').live("swipeleft", function(){
        $.mobile.changePage("#Home", "flip", false, true);
    });
});


/******************************************
 * Weather Checking Stuff
 ******************************************/
//Global Constants for App interaction
var YAHOO_APP_ID = 'zHgnBS4m'; // TODO: Change to mine

function checkTheWeather() {
	navigator.notification.activityStart();
	
	// Find the WOEID
	var destination = $("#destinationInputField").val().trim().replace(/\s+/g, "+");
	var yahooGetWoeidWSCall = 'http://where.yahooapis.com/geocode?q='+destination+'&flags=J&appid='+YAHOO_APP_ID;
	
	// External callout for Woeid
	$.getJSON(yahooGetWoeidWSCall, function(json) {
		var woeid = json.ResultSet.Results[0].woeid;
		
		// Forming the query for Yahoo's weather forecasting API with YQL
	    // http://developer.yahoo.com/weather/
	    var weatherQuery = 'select * from weather.forecast where woeid='+woeid+' and u="c"';
	    var yahooGetWeatherWSCall = 'http://query.yahooapis.com/v1/public/yql?q='+encodeURIComponent(weatherQuery)+'&format=json';
	    
	    var weatherImg = 'http://l.yimg.com/a/i/us/we/52/XX.gif';
	    
	    // Issue a cross-domain AJAX request (CORS) to the GEO service.
	    // Not supported in Opera and IE.
	    $.getJSON(yahooGetWeatherWSCall, function(json){
	    	if(json.query){	
	    		// Select the forecast
	    		var todaysTemp = json.query.results.channel.item.condition.temp;
	    		var todaysOutlook = json.query.results.channel.item.condition.text;
	    		
	    		var city = json.query.results.channel.location.city;
	    		var country = json.query.results.channel.location.country;
	    		
	            var str1 = "";
	            str1 = str1.concat("Results Found for " + city + ", " + country + "<br/>");
	            str1 = str1.concat("<br/>Today , ["+todaysOutlook+"] ["+todaysTemp+"C]");
	                    
	            for (var i=0;i<2;i++){
	            	var item = json.query.results.channel.item.forecast[i];
	            	var imgCode	= item.code;
	                str1 = str1.concat("<br/><img src='"+weatherImg.replace('XX', imgCode)+"'/><br/>"+item.day+" {"+item.date+"}, Forecast: "+item.text+" [Lows: "+item.low+", Highs: "+item.high+"]");
	            }

	            $('#weatherDisplayArea').html("<p>"+str1+"</p>");
	    	}
	    });
	});
        
	navigator.notification.activityStop();
}