/***************************
 * Swipe Functionality
 ***************************/ 
$(document).ready(function() {
    // Swipe from home page
	$('#Home').live("swipeleft", function(){
        $.mobile.changePage("#Weekly-Deals", "fade", false, true);
    });
	$('#Home').live("swiperight", function(){
        $.mobile.changePage("#Enquiries", "flip", false, true);
    });
    
    // Swipes from Weekly Deals
    $('#Weekly-Deals').live("swiperight", function(){
        $.mobile.changePage("#Home", "fade", true, true);
    });
    $('#Weekly-Deals').live("swipeleft", function(){
        $.mobile.changePage("#Enquiries", "fade", true, true);
    });
    
    // Swipe from Enquiries
    $('#Enquiries').live("swiperight", function(){
        $.mobile.changePage("#Weekly-Deals", "fade", false, true);
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


/******************************************
 * Mongo Query Stuff
 ******************************************/
var baseUrl='https://api.mongolab.com/api/1'
var generatedPageMap = new Object

// Add page open event listener
$('#Weekly-Deals').live('pageshow',function(event, ui){
	// Display loading notification
	navigator.notification.activityStart();
	
	performMongoDBQueries();
});

function performMongoDBQueries() {
	   
	var apiKey = '?apiKey=5072b532e4b088be4c29ea5a';
	var dbName = '/freedom-travel-deals';
	var collectionName = '/deals-of-the-week';
	   
	var queryString = baseUrl + '/databases' +
	                       dbName + '/collections' +
	                       collectionName;

	$.ajaxSetup({
	  error: AjaxError,
	  cache: false
	});


	$.get(queryString + apiKey, function (response) {   
		$('#Weekly-Deals_list').empty();
		
		$.each(response, function() {
			var newPage = $('<div data-role="page" id="'+this.id+'" style="background: -webkit-gradient(linear, left bottom, left top, color-stop(0, #FFFFFF), color-stop(1, #00A3EF));"><div data-role="header"><h1>Hot Deals</h1></div><div data-role="content"><h1>'+this.title+'</h1><p><img src="data:image/png;base64,'+this.image+
	    			'" style="float: left; width: 70%; margin-right: 2%"/>'+this.body+'</p></div></div>');
	    	newPage.appendTo( $.mobile.pageContainer );
	    	generatedPageMap[this.id] = newPage
	    	
	    	$('#Weekly-Deals_list').append('<li id="'+this.id+'"><h3><a onclick="alert(\"Clicked\");">'+
	    			this.title+'</a></h3><img src="data:image/png;base64,'+this.image+
	    			'" style="float: left;"/><p>'+this.body+'</p></li>');
	    	
//	    	$('#'+this.id).click(function(e) {
//	    		$.mobile.changePage(generatedPageMap[this.id]);
//	    	});
	    });
	    
	    $('#Weekly-Deals_list').listview('refresh');
	    navigator.notification.activityStop();
	});
}

function AjaxError(x, e) {
	if (x.status == 0) {
		alert(' Check Your Network.');
	} else if (x.status == 404) {
		alert('Requested URL not found.');
	} else if (x.status == 500) {
	    alert('Internel Server Error.');
	}  else {
	    alert('Unknow Error.\n' + x.responseText);
	}
}