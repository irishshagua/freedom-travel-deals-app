/***************************
 * Page Flow Functionality
 ***************************/
$(document).ready(function() {
	// Swipe from home page
	$('#Home').live("swipeleft", function(){
        $.mobile.changePage("#Weekly-Deals", "fade", false, true);
    });
	$('#Home').live("swiperight", function(){
        $.mobile.changePage("#Enquiries", "flip", false, true);
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
var YAHOO_APP_ID = 'vwqpJrvV34Gi8gUvpcLReuscuHqYORnXSmADO4uXnQ8GXQOIuxZD1Zxz1BbqpDYuBxQVU6y1RvbS5F1gvDt_ssW45J9o9BA-';

function checkTheWeather() {
	navigator.notification.activityStart("Checking Weather", "Weather detail provided by Yahoo");
	
	// Find the WOEID
	var destination = $("#destinationInputField").val().trim().replace(/\s+/g, "+");
	var yahooGetWoeidWSCall = 'http://where.yahooapis.com/geocode?q='+destination+'&flags=J&appid='+YAHOO_APP_ID;
	
	// External callout for Woeid
	$.getJSON(yahooGetWoeidWSCall, function(json) {
		if (typeof json.ResultSet.Results == 'undefined') {
			navigator.notification.activityStop();
			alert('No weather found for ' + destination);
			return;
		}
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
	            str1 = str1.concat("Weather for " + city + ", " + country + "<br/>");
	            str1 = str1.concat("<br/>Today , "+todaysOutlook+" "+todaysTemp+"C");
	                    
	            for (var i=0;i<2;i++){
	            	var item = json.query.results.channel.item.forecast[i];
	            	var imgCode	= item.code;
	                str1 = str1.concat("<br/><img src='"+weatherImg.replace('XX', imgCode)+"'/><br/>"+item.day+", "+item.text+" [Lows: "+item.low+", Highs: "+item.high+"]");
	            }

	            $('#weatherDisplayArea').html("<p>"+str1+"</p>");
	    	}
	    	
	    	navigator.notification.activityStop();
	    });
	});
}


/******************************************
 * Mongo Query Stuff
 ******************************************/
var baseUrl='https://api.mongolab.com/api/1';
var _selectIndex = 0;
var apiKey = '?apiKey=5072b532e4b088be4c29ea5a';
var dbName = '/freedom-travel-deals';
var collectionName = '/deals-of-the-week';
   
var queryString = baseUrl + '/databases' +
                       dbName + '/collections' +
                       collectionName;


$(function() {
	$("#dealsReloadBtn").click(function() {
		navigator.notification.activityStart("Loading Deals", "Accessing Deals from Internet");
		performMongoDBQueries();
	});
});

$.ajaxSetup({
	  error: AjaxError,
	  cache: false
	});

//Add page open event listener
$( function() {
    document.addEventListener("deviceready", performMongoDBQueries, false);
});

$('#Weekly-Deals').live('pageshow',function(event, ui){
	if ($('#Weekly-Deals_list').hasClass('ui-listview')) {
        //this listview has already been initialized, so refresh it
		$('#Weekly-Deals_list').listview('refresh');
    }
	
	if ($('#Weekly-Deals_list li').size() > 0) {
		return;
	}
	
	// Display loading notification
	navigator.notification.activityStart("Loading Deals", "Accessing Deals from Internet");
});

function performMongoDBQueries() {
	$.get(queryString + apiKey, function (response) {   
		$('#Weekly-Deals_list').empty();
		
		$.each(response, function() {
			var detailsTable = '<div class="deal-details"><table>'+
							   '<tr><td><b>Dest:</b></td><td>'+this.details.destination+'</td></tr>'+
							   '<tr><td><b>Price:</b></td><td>'+this.details.price+'</td></tr>'+
							   '<tr><td><b>Date:</b></td><td>'+this.details.date+'</td></tr>'+
							   '<tr><td><b>Length:</b></td><td>'+this.details.duration+'</td></tr>'+
							   '<tr><td><b>Accom:</b></td><td>'+this.details.accomodation+'</td></tr>'+
							   '<tr><td><b>Basis:</b></td><td>'+this.details.basis+'</td></tr>'+
							   '</table></div>';
			var newPageId = "page_" + _selectIndex++;
			var newLiId = "li_" + _selectIndex++;
			var newPage = $('<div data-role="page" id="'+newPageId+'" class="blue-grad-backg">'+
					'<div data-role="header">'+
						'<a href="#Weekly-Deals" data-icon="star">Deals</a>'+
						'<h1>Hot Deals</h1>'+
					'</div>'+
					'<div data-role="content">'+
						'<div class="deal-header"><center><h1>'+this.title+'</h1></center></div>'+
						'<img src="data:image/png;base64,'+this.image+'" class="img-shadow"/>'+
						detailsTable +
						'<p>' + this.body + '</p>'+
						'<br/><br/>'+
						'<center><a href="mailto:sales@freedomtravel.ie?subject=Enquiry: '+this.title+'" class="no-underline">'+
							'<img src="images/Mail-icon.png"/><br/>'+
							'Deal Enquiry'+
						'</a></center>'+
					'</div></div>');
	    	
	    	$('#Weekly-Deals_list').append('<li id="'+newLiId+'"><h3><a href="#'+newPageId+'">'+
	    			this.title+'</a></h3><img src="data:image/png;base64,'+this.image+
	    			'" class="list-img"/>'+detailsTable+'</li>');
	    	
	    	$('#'+newLiId).live('click', function() {
	    		$.mobile.changePage($('#'+newPageId));
	    	});
	    	
	    	newPage.appendTo( $.mobile.pageContainer);
	    });
	    
		var $weeklyDealList = $('#Weekly-Deals_list');
	    if ($weeklyDealList.hasClass('ui-listview')) {
	        //this listview has already been initialized, so refresh it
	    	$weeklyDealList.listview('refresh');
	    }
	    
	    navigator.notification.activityStop();
	});
}

function AjaxError(x, e) {
	navigator.notification.activityStop();
	
	if (x.status == 0) {
		alert('Check Your Network.');
	} else if (x.status == 404) {
		alert('Requested URL not found.');
	} else if (x.status == 500) {
	    alert('Internel Server Error.');
	}  else {
	    alert('Unknow Error');
	}
}