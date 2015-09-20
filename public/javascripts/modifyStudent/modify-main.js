/**
 * Created by tombatto on 19/09/15.
 */
$(function(){
    initDatepicker();
});

$(function(){
    google.maps.event.addDomListener(window, 'load', initialize);
});

function initDatepicker() {
    $('.datepicker').datepicker({
        endDate: '0d',
        language: 'es',
        format: 'dd/mm/yyyy'
    });
}

function initialize() {
    var mapCanvas = document.getElementById('map-canvas');
    var myLatLng=new google.maps.LatLng(-34.443625, -58.879069);
    var mapOptions = {
        center:myLatLng ,
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        disableDefaultUI: true
    }
    var map = new google.maps.Map(mapCanvas, mapOptions)
    var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';
    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        icon: iconBase + 'schools_maps.png'
    });
}

