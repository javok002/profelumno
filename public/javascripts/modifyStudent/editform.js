/**
 * Created by tombatto on 14/09/15.
 */
var app=angular.module( 'EditForm', ['ngTagsInput'] );

app.controller("EditController", ['$http','$scope', 'fileUpload',function($http,$scope, fileUpload){
    edit=this;
    edit.u={};
    edit.u.user={};
    edit.u.user.subjects=[];
    $scope.imageUrl='';

    $http.get("user").
        success(function(data, status, headers, config) {
            edit.u= data;
            $scope.search = edit.u.user.address+"";
            $scope.date=new Date(edit.u.user.birthday);
            $scope.geoCode();
        }).
        error(function(data, status, headers, config) {
            // log error
        });

    $http.get("subjects").
        success(function(data, status, headers, config) {
            $scope.allSubjects= data;
        }).
        error(function(data, status, headers, config) {
            // log error
        });

    $http.get('img')
        .success(function (data, status, headers, config) {
            $scope.imageUrl=data;
        }).
        error(function (data, status, headers, config) {
            // log error
        });

    //TAGS
    edit.tags=$scope.tags;//meterias.json

    $scope.loadTags = function(query) {
        return $scope.allSubjects;
    };

    $scope.compare=function(array1,value2){
        for(var i=0;i<array1.length;i++){
            if(array1[i].name==value2){
                return array1[i];
            }
        }
        return {};
    };

    //MAPS
    $scope.gotoCurrentLocation = function () {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(function (position) {
                var c = position.coords;
                $scope.gotoLocation(c.latitude, c.longitude);
            });
            return true;
        }
        return false;
    };

    $scope.gotoLocation = function (lat, lon) {
        if ($scope.lat != lat || $scope.lon != lon) {
            $scope.loc = { lat: lat, lon: lon };
            $scope.marker={lat:$scope.loc.lat,lon:$scope.loc.lon};
            $scope.$apply("marker");
            if (!$scope.$$phase) $scope.$apply("loc");
        }
    };
    $scope.geoCode = function () {
        if ($scope.search && $scope.search.length > 0) {
            if (!this.geocoder) this.geocoder = new google.maps.Geocoder();
            this.geocoder.geocode({ 'address': $scope.search }, function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    var loc = results[0].geometry.location;
                    $scope.search = results[0].formatted_address;
                    $scope.gotoLocation(loc.lat(), loc.lng());
                    return loc;
                } else {
                    alert("Sorry, this search produced no results.");
                }
            });
        }
    };
    $scope.loc =$scope.geoCode();

    //IMAGE
    $scope.uploadFile = function(){
        var file = $scope.fileToUpload;
        var uploadUrl = "img";
        fileUpload.uploadFileToUrl(file, uploadUrl, $scope);;
    };
    //SUBMIT
    $scope.errors = {
        invalid: false,
        incomplete: false,
        studentAge: false,
        taken:false
    };

    var verify = function() {
        var today = new Date();
        var birthday = $scope.date;
        $scope.errors.studentAge =(today.getYear() - birthday.getYear() < 6 ||(today.getYear() - birthday.getYear() == 6 && today.getMonth() < birthday.getMonth()));
        return !$scope.errors.incomplete && !$scope.errors.invalid && !$scope.errors.studentAge && !$scope.errors.teacherAge;
    };

    $scope.submit = function () {
        if (!verify())
            return;
        if(!$scope.modifyForm.$valid&&$scope.modifyForm.actualPassword.$valid) {
            $scope.errors.invalid = true;
            return;
        }
        edit.u.user.password=$scope.password;
        edit.u.user.address=$scope.search;
        edit.u.user.birthday=$scope.date;
        edit.u.user.latitude = $scope.loc.lat;
        edit.u.user.longitude = $scope.loc.lon;

        //edit.u.user.subjects;
        $http.post('student-modification', edit.u)
            .success(function (data) {
                $scope.errors = { invalid: false, incomplete: false, teacherAge: false, studentAge: false,take:false};
                //alert(JSON.stringify(data));
                window.location.href = data;
            })
            .error(function (data) {
                $scope.errors.taken=true;
            });
    };

}]);

app.directive("appMap", function () {
    return {
        restrict: "E",
        replace: true,
        template: "<div></div>",
        scope: {
            center: "=",        // Center point on the map (e.g. <code>{ latitude: 10, longitude: 10 }</code>).
            markers: "=",       // Array of map markers (e.g. <code>[{ lat: 10, lon: 10, name: "hello" }]</code>).
            width: "@",         // Map width in pixels.
            height: "@",        // Map height in pixels.
            zoom: "@",          // Zoom level (one is totally zoomed out, 25 is very much zoomed in).
            mapTypeId: "@",     // Type of tile to show on the map (roadmap, satellite, hybrid, terrain).
            panControl: "@",    // Whether to show a pan control on the map.
            zoomControl: "@",   // Whether to show a zoom control on the map.
            scaleControl: "@"   // Whether to show scale control on the map.
        },
        link: function (scope, element, attrs) {
            var toResize, toCenter;
            var map;
            var currentMarkers;

            // listen to changes in scope variables and update the control
            var arr = ["width", "height", "markers", "mapTypeId", "panControl", "zoomControl", "scaleControl"];
            for (var i = 0, cnt = arr.length; i < arr.length; i++) {
                scope.$watch(arr[i], function () {
                    cnt--;
                    if (cnt <= 0) {
                        updateControl();
                    }
                });
            }

            // update zoom and center without re-creating the map
            scope.$watch("zoom", function () {
                if (map && scope.zoom)
                    map.setZoom(scope.zoom * 1);
            });
            scope.$watch("center", function () {
                if (map && scope.center)
                    map.setCenter(getLocation(scope.center));
            });

            // update the control
            function updateControl() {

                // update size
                if (scope.width) element.width(scope.width);
                if (scope.height) element.height(scope.height);

                // get map options
                var options =
                {
                    center: new google.maps.LatLng(40, -73),
                    zoom: 6,
                    mapTypeId: "roadmap"
                };
                if (scope.center) options.center = getLocation(scope.center);
                if (scope.zoom) options.zoom = scope.zoom * 1;
                if (scope.mapTypeId) options.mapTypeId = scope.mapTypeId;
                if (scope.panControl) options.panControl = scope.panControl;
                if (scope.zoomControl) options.zoomControl = scope.zoomControl;
                if (scope.scaleControl) options.scaleControl = scope.scaleControl;

                // create the map
                map = new google.maps.Map(element[0], options);

                // update markers
                updateMarkers();

                // listen to changes in the center property and update the scope
                google.maps.event.addListener(map, 'center_changed', function () {

                    // do not update while the user pans or zooms
                    if (toCenter) clearTimeout(toCenter);
                    toCenter = setTimeout(function () {
                        if (scope.center) {

                            // check if the center has really changed
                            if (map.center.lat() != scope.center.lat ||
                                map.center.lng() != scope.center.lon) {

                                // update the scope and apply the change
                                scope.center = { lat: map.center.lat(), lon: map.center.lng() };
                                if (!scope.$$phase) scope.$apply("center");
                            }
                        }
                    }, 500);
                });
            }

            // update map markers to match scope marker collection
            function updateMarkers() {
                if (map && scope.markers) {

                    // clear old markers
                    if (currentMarkers != null) {
                        for (var i = 0; i < currentMarkers.length; i++) {
                            //currentMarkers[i] = m.setMap(null);
                        }
                    }

                    // create new markers
                    currentMarkers = [];
                    var markers = scope.markers;
                    if (angular.isString(markers)) markers = scope.$eval(scope.markers);
                    for (var i = 0; i < markers.length; i++) {
                        var m = markers[i];
                        if(m!=null) {
                            var loc = new google.maps.LatLng(m.lat, m.lon);
                            var mm = new google.maps.Marker({position: loc, map: map, title: m.name});
                            currentMarkers.push(mm);
                        }
                    }
                }
            }

            // convert current location to Google maps location
            function getLocation(loc) {
                if (loc == null) return new google.maps.LatLng(40, -73);
                if (angular.isString(loc)) loc = scope.$eval(loc);
                return new google.maps.LatLng(loc.lat, loc.lon);
            }
        }
    };
});

app.directive('googleplace', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, model) {
            var options = {
                types: [],
                componentRestrictions: {}
            };
            scope.gPlace = new google.maps.places.Autocomplete(element[0], options);

            google.maps.event.addListener(scope.gPlace, 'place_changed', function() {
                scope.$apply(function() {
                    model.$setViewValue(element.val());
                });
            });
        }
    };
});

app.directive('ngEnter', function () {
    return function (scope, elements, attrs) {
        elements.bind('keypress', function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter);
                });
                event.preventDefault();
            }
        });
    };
});

app.directive("compareTo", function () {
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function (scope, element, attributes, ngModel) {

            ngModel.$validators.compareTo = function (modelValue) {
                return modelValue == scope.otherModelValue;
            };

            scope.$watch("otherModelValue", function () {
                ngModel.$validate();
            });
        }
    }
});

app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

app.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function (file, uploadUrl, scope) {
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function (data, status, headers, config) {
                edit.u.user.profilePicture = data;
                scope.imageUrl=data;
            })
            .error(function () {
            });
    }
}]);