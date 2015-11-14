/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngRoute', 'ngTagsInput', 'flash', 'ngAnimate']);

app.controller('TeacherInfoController', ['$rootScope', '$scope', '$http', 'fileUpload', 'Flash', function ($rootScope, $scope, $http, fileUpload, Flash) {
    edit = this;
    edit.u = {};
    edit.u.user = {};
    $scope.radio = '';
    edit.u.allSubjects = [];
    edit.u.subjectsFiltered = [];
    $scope.imageUrl='';
    $scope.hasImage=true;

    $scope.successAlert = function () {
        var message = '<strong>¡Bien hecho! </strong> Actualizaste con éxito tus materias.';
        Flash.create('success', message);
    };

    $http.get('modify-teacher/user')
        .success(function (data, status, headers, config) {
            edit.u = data;
            $scope.search = edit.u.user.address+"";
            if($scope.search == "" || $scope.search == "null"){
                $scope.search = "";
                $scope.gotoCurrentLocation();
            }

            $scope.date=new Date(edit.u.user.birthday);
            $scope.renewalDate=new Date(edit.u.renewalDate);
            $scope.geoCode();
            if (edit.u.homeClasses) {
                $scope.radio = 'yes';
            } else {
                $scope.radio = 'no';
            }
        }).
        error(function (data, status, headers, config) {
            // log error
        });

    $http.get('modify-teacher/img')
        .success(function (data, status, headers, config) {
            $scope.imageUrl=data;
            $scope.hasImage=true;
        }).
        error(function (data, status, headers, config) {
            $scope.hasImage=false;
        });

    $http.get("modify-teacher/subjects").
     success(function(data, status, headers, config) {
            edit.u.allSubjects = data.length != 0 ? data : [{text: 'Lengua'},{text: 'Matematica'},{text: 'Fisica'},{text: 'Quimica'},{text: 'Algebra'}];
        }).
     error(function(data, status, headers, config) {
            console.log("error subjects")
     });

    $scope.loadTags = function(query) {
        return edit.u.subjectsFiltered.length == 0 ? edit.u.allSubjects : edit.u.subjectsFiltered;
    };

    $scope.sort = function(){
        edit.u.subjectsFiltered = [];
        if(typeof edit.u.allSubjects == 'undefined'){
            $http.get("modify-teacher/subjects").
                success(function(data, status, headers, config) {
                    edit.u.allSubjects = data.length != 0 ? data : [{text: 'Lengua'},{text: 'Matematica'},{text: 'Fisica'},{text: 'Quimica'},{text: 'Algebra'}];
                }).
                error(function(data, status, headers, config) {
                    console.log("error subjects")
                });
        }
        var currentTag = document.getElementsByName("currentTag")[0].value;
        for(var i = 0; i < edit.u.allSubjects.length; i++){
            if(edit.u.allSubjects[i].text.substring(0,currentTag.length).toLowerCase() == currentTag.toLowerCase()){
                edit.u.subjectsFiltered.push(edit.u.allSubjects[i]);
            }
        }
    };

    $scope.errors = {
        invalid: false,
        incomplete: false,
        teacherAge: false,
        taken:false
    };
    $scope.invalidFile=false;
    var verify = function () {
        var today = new Date();
        var birthday = $scope.date;
        $scope.errors.teacherAge = (today.getYear() - birthday.getYear() < 16 || (today.getYear() - birthday.getYear() == 16 && today.getMonth() < birthday.getMonth()));
        return !$scope.errors.incomplete && !$scope.errors.invalid && !$scope.errors.teacherAge;
    };

    $scope.back=function(){
        window.location.href = "/teacher-profile"
    };

    $scope.selectFile=function(){
        angular.element($('#inputFile')).trigger('click');
    };
    $scope.errLength=false;
    $scope.verifyDesc= function(){
        var text = angular.element($('#teacherDescription')).val();
        if (text.length<50) {
            $scope.errlength = true;
        }else{
            $scope.errlength=false;
        }
    };

    //MAPS
    $scope.gotoCurrentLocation = function () {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(function (position) {
                var c = position.coords;
                $scope.gotoLocation(c.latitude, c.longitude);
                if (!this.geocoder) this.geocoder = new google.maps.Geocoder();
                $scope.geocodeLatLng(this.geocoder, c.latitude, c.longitude);
            });
            return true;
        }
        return false;
    };

    $scope.geocodeLatLng = function(geocoder, lat, lon) {

        var latlng = {lat: lat, lng: lon};
        geocoder.geocode({'location': latlng}, function(results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                if (results[1]) {
                    $scope.search = results[1].formatted_address;
                    arrAddress = results[0].address_components;
                }
            }
        });
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
                    arrAddress = results[0].address_components;
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

    $scope.getCity = function () {
        for(var i=0;i<arrAddress.length;i++)
        {
            if (arrAddress[i].types[0] == "locality") {
                return arrAddress[i].long_name;
            }
        }
        return null;
    };

    $scope.getNeighbourhood = function () {
        for(var i=0;i<arrAddress.length;i++)
        {
            if (arrAddress[i].types.length>=2 && arrAddress[i].types[1] == "sublocality") {
                return arrAddress[i].long_name;
            }
        }
        return null;
    };

    //SUBMIT
    $scope.submit = function () {
        if (!verify())
            return;
        if (!$scope.modifyForm.$valid) {
            errors.invalid = true;
            return;
        }
        edit.u.user.birthday=$scope.date;
        edit.u.user.address=$scope.search;
        edit.u.homeClasses= $scope.radio=='yes';
        edit.u.user.latitude = $scope.loc.lat;
        edit.u.user.longitude = $scope.loc.lon;
        edit.u.user.city= $scope.getCity();
        edit.u.user.neighbourhood= $scope.getNeighbourhood();
        edit.u.renewalDate=$scope.renewalDate;
        edit.u.user.lastLogin = new Date(edit.u.user.lastLogin);


        $http.post('modify-teacher/teacher-modification-post', edit.u)
            .success(function (data) {
                $scope.errors = {incomplete: false, invalid: false, teacherAge: false, taken: false, length: false};
                window.location.href = "/teacher-profile";
                /*alert(JSON.stringify(data));*/
            })
            .error(function (data) {
                //alert(data);
            });
    };

    $scope.uploadFile = function(){
        var file = $scope.fileToUpload;
        var uploadUrl = "modify-teacher/img";
        fileUpload.uploadFileToUrl(file, uploadUrl, $scope);
    };

    $scope.submitSubjects = function () {
        if (!$scope.subjectsForm.$valid) {
            return;
        }
        //var myJsonString = JSON.stringify($scope.edit);
        var literalSubjects = [];
        for(var i = 0; i < edit.u.user.subjects.length; i++){
            literalSubjects.push(edit.u.user.subjects[i].text);
        }
        $http.post('modify-teacher/subjects', literalSubjects)
            .success(function(response){
                $scope.successAlert();
                //window.location.href = "/teacher-profile"
            })
            .error("falla al guardar")
    };
}]);

app.directive('googleplace', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attrs, model) {
            var options = {
                types: [],
                componentRestrictions: {}
            };
            scope.gPlace = new google.maps.places.Autocomplete(element[0], options);

            google.maps.event.addListener(scope.gPlace, 'place_changed', function () {
                scope.$apply(function () {
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
                window.location.href = "/modify-teacher";
                /*edit.u.user.profilePicture = data;
                scope.imageUrl=data;*/
                //window.location.href=data;
            })
            .error(function () {
                scope.invalidFile=true;
            });
    }
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

app.directive('customOnChange', function() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var onChangeHandler = scope.$eval(attrs.customOnChange);
            element.bind('change', onChangeHandler);
        }
    };
});

