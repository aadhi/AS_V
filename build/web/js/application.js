$(document).ready(function() {
			
			//showRestaurantSearchOptions();
                        
            
    $('#restaurantName').click(function() {
        $('#restaurantName').val("");
    });
    $('#findRestaurantBtn').click(searchRestaurant);
    $('#findEventBtn').click(searchEvent);
});
/*
$(document.body).on('mousedown', '.pac-container', function(e) {
    $(".loader").css("display", "block");
    $('.restaurantsResult').css('display','none');
    $('.noResult').css('display','none');
    searchRestaurant();
});
 */
function showRestaurantSearchOptions() {
	 //$('#restaurantFind').css('display','block');

	//$('#findRestaurantBtn').click(searchRestaurant);
       
}
function searchRestaurantYelp() {

    var restaurantName = $('#restaurantName').val();
             console.log('restaurantName: '+restaurantName);

    // Perform GET request
     //$.getJSON('https://www.vegguide.org/search/by-address/'+restaurantName, function(data) {
     $.getJSON('http://localhost:8080/VegTox-SS/api/restaurant/getRestaurant/'+restaurantName, function(data) {
     // Empty old table entries, select all child rows of
     // tbody element
     console.log(data);

     $('#restaurantsResult tbody > tr').remove();
    var jsonData = data; 
    var l = jsonData.length;
     console.log('l: '+l);
     

     if (l==0) {
            $(".loader").css("display", "none");
            $('.restaurantsResult').css('display','none');
            $('.noResult').css('display','block');
     } 
     else {
         $(".loader").css("display", "none");
         $('.noResult').css('display','none');
         $('.restaurantsResult').css('display','block');
         $( ".restaurantsResult" ).empty();
         
         for (var j = 0; j < l; j++){
            console.log(jsonData);
            var short_desc = "";
            var website_url = "";
            var address = "";
            var veg_level_desc = "";

            if(typeof jsonData[j].url !== 'undefined') {
                    website_url = "<a href='" +jsonData[j].url+ "' target='_blank' class='btn btn-primary'> Visit website </a>";
             }


            if(typeof jsonData[j].address !== 'undefined') {
                    address = jsonData[j].address;
            }
            if(typeof jsonData[j].city !== 'undefined') {
                    if (address !== "") {
                        address = address + ", "+jsonData[j].city;
                    }
                    else {
                        address = address + jsonData[j].city;
                    }
            }
            if(typeof jsonData[j].postal_code !== 'undefined') {
                     if (address !== "") {
                        address = address + ", "+jsonData[j].postal_code;
                     }
                    else {
                        address = address + jsonData[j].postal_code;
                    }
            }
            if(typeof jsonData[j].veg_level_description !== 'undefined') {
                    veg_level_desc = jsonData[j].veg_level_description;
             }
            if(typeof jsonData[j].short_description !== 'undefined') {
                    short_desc = 
                     "<div class='row'>"
                    + "<div class='col-md-12 short_desc'>"
                    + jsonData[j].short_description
                    + "</div>"
                    + "</div>"

             }


            if(typeof jsonData[j].name !== 'undefined') {
             $('#restaurantsResult').append(
                "<div class='restaurantItem'>"
                + "<div class='row'>"
                + "<div class='col-md-4 name'>"
                + jsonData[j].name
                + "</div>"
                + "<div class='col-md-4 type'>"
                + veg_level_desc
                + "</div>"
                + "</div>"
                + short_desc
                + "<div class='row'>"
                + "<div class='col-md-12 city'>"
                + address
                + "</div>"
                + "</div>"
                + "<div class='row'>"
                + "<div class='col-md-4'>"
                + website_url
                + "</div>"
                + "<div class='col-md-4'>"
                + "<a href='" +jsonData[j].url + "' target='_blank' class='btn btn-primary'> See reviews </a>"
                + "</div>"
                + "</div>"
                + "</div>"
               );
             }
            }
        }

     });
}

function searchRestaurant() {
    searchEvent();
	 var restaurantName = $('#restaurantName').val();
	 //console.log('restaurantName: '+restaurantName);

// Perform GET request
 //$.getJSON('https://www.vegguide.org/search/by-address/'+restaurantName, function(data) {
 //$.getJSON('http://localhost:8080/VegTox-SS/api/restaurant/getRestaurant/'+restaurantName, function(data) {
 $.getJSON('/VegTox/api/restaurant/getRestaurant/'+restaurantName, function(data) {
 // Empty old table entries, select all child rows of
 // tbody element
 //console.log(data);
 
  $('#restaurantsResult tbody > tr').remove();
 var l = data.length;
 console.log('l: '+l);
 var jsonData;
 
 if (l==0) {
        $(".loader").css("display", "none");
	$('.restaurantsResult').css('display','none');
	$('.noResult').css('display','block');
 } 
 else {
     $(".loader").css("display", "none");
     $('.noResult').css('display','none');
     $('.restaurantsResult').css('display','block');
     $( ".restaurantsResult" ).empty();
     for (i = 0; i < l; i++) {
        jsonData = data[i];
        //console.log(data.entries[i]); 
        // Add Entries to restaurantsResult
        var short_desc = "";
        var website_url = "";
        var address = "";
        var veg_level_desc = "";
                
        if(jsonData.URLWeb) {
                website_url = "<a href='" +jsonData.URLWeb+ "' target='_blank' class='btn btn-primary'> Visit website </a>";
         }
         
        if(jsonData.Address) {
                address = jsonData.Address;
        }
        /*
        if(typeof jsonData.address2 !== 'undefined') {
                if (address !== "") {
                    address = address + ", "+jsonData.address2;
                }
                else {
                    address = address + jsonData.address2;
                }
        }
        if(typeof jsonData.city !== 'undefined') {
                if (address !== "") {
                    address = address + ", "+jsonData.city;
                }
                else {
                    address = address + jsonData.city;
                }
        }
        if(typeof jsonData.postal_code !== 'undefined') {
                 if (address !== "") {
                    address = address + ", "+jsonData.postal_code;
                 }
                else {
                    address = address + jsonData.postal_code;
                }
        }
        */
        if(jsonData.veg_level_description) {
                veg_level_desc = jsonData.veg_level_description;
         }
        if(jsonData.ShortDesc) {
                short_desc = 
                 "<div class='row'>"
                + "<div class='col-md-12 short_desc'>"
                + jsonData.ShortDesc
                + "</div>"
                + "</div>"
               
         }
         
         
        //if(jsonData.Name) {
         $('#restaurantsResult').append(
            "<div class='restaurantItem'>"
            + "<div class='row'>"
            + "<div class='col-md-4 name'>"
            + jsonData.Name
            + "</div>"
            + "<div class='col-md-4 type'>"
            + veg_level_desc
            + "</div>"
            + "</div>"
            + short_desc
            + "<div class='row'>"
            + "<div class='col-md-12 city'>"
            + address
            + "</div>"
            + "</div>"
            + "<div class='row'>"
            + "<div class='col-md-4'>"
            + website_url
            + "</div>"
            + "<div class='col-md-4'>"
            + "<a href='" +jsonData.URLReview + "' target='_blank' class='btn btn-primary'> See reviews </a>"
            + "</div>"
            + "</div>"
            + "</div>"
           );
        // }
        }
    }


 });

 }
 
 function searchEvent() {
	 //var restaurantName = $('#restaurantName').val();
	 //console.log('restaurantName: '+restaurantName);
var eventName = $('#eventName').val();
alert(eventName);
// Perform GET request
 //$.getJSON('https://www.vegguide.org/search/by-address/'+restaurantName, function(data) {
 //$.getJSON('http://localhost:8080/VegTox-SS/api/restaurant/getRestaurant/'+restaurantName, function(data) {
 $.getJSON('/web/api/event/getEvent/'+eventName, function(data) {
 // Empty old table entries, select all child rows of
 // tbody element
console.log(data);
 
  //$('#restaurantsResult tbody > tr').remove();
 var l = data.length;
 var limit = 5;
 
 console.log('l: '+l);
 var jsonData;
 
 if (l==0) {
        //$(".loader").css("display", "none");
	//$('.restaurantsResult').css('display','none');
	//$('.noResult').css('display','block');
 } 
 else {
     $(".loaderEvent").css("display", "none");
     $('.noResultEvent').css('display','none');
     $('.eventsResult').css('display','block');
    // $( ".eventResult" ).empty();
     for (i = 0; i <= limit; i++) {
        jsonData = data[i];
        //console.log(data.entries[i]); 
        // Add Entries to restaurantsResult
        var name = "";
        var desc = "";
        var startTime = "";
        var endTime = "";
        var venueId = "";
        var venueDetails = "";
        var venueAddress1 = "";
                
        
         
        if(jsonData.StartTime) {
                startTime = jsonData.StartTime;
        }
        if(jsonData.EndTime) {
                endTime = jsonData.EndTime;
        }
        if(jsonData.VenueId) {
                venueId = jsonData.VenueId;
        }
        if(jsonData.VenueDetails) {
                venueDetails = jsonData.VenueDetails;
        }
        
        if(jsonData.Desc) {
                desc = jsonData.Desc;
         }
         if(jsonData.VenueAddress1) {
                venueAddress1 = jsonData.VenueAddress1;
         }
        if(jsonData.ShortDesc) {
                desc = 
                 "<div class='row'>"
                + "<div class='col-md-12 short_desc'>"
                + jsonData.Desc
                + "</div>"
                + "</div>"
               
         }
         
         
        if(jsonData.Name) {
         $('.eventsResult').append(
            "<div class='eventItem'>"
            + "<div class='row'>"
            + "<div class='col-md-4 name'>"
            + jsonData.Name
            + "</div>"
            + "<div class='col-md-4 type'>"
            + desc
            + "</div>"
            + "</div>"
            + startTime
            + "<div class='row'>"
            + "<div class='col-md-12 city'>"
            + endTime
            + "</div>"
            + "</div>"
            + "<div class='row'>"
            + "<div class='col-md-4'>"
            + venueId + venueDetails + venueAddress1
            + "</div>"
            + "</div>"
            + "</div>"
           );
         }
        }
    }


 });

 }