/**
 * Created by Nicolas Moreno on 14/09/2015
 */


var teacherSearchApp = angular.module("teacherSearchApp", ['ngRoute', 'ngTagsInput', 'angularUtils.directives.dirPagination', 'profLesson']);
//teacherSearchApp.config(['$routeProvider',
//    function($routeProvider) {
//        $routeProvider.
//        when('/', {
//            templateUrl: 'ua/dirproy/profelumno/teachersearch/views/teachersearch.scala.html',
//            controller: 'searchController'
//
//        })
//    }]);
teacherSearchApp.controller("searchController", function($scope, $http, $routeParams) {
    $scope.subjects = [];
    $scope.teachers = [];
    $scope.showResults = false;
    $scope.ranking = 0;
    $scope.lessonsDictated = 0;
    $scope.homeClasses = false;
    $scope.sortByDistance = false;
    $scope.allSubjects = [];
    $scope.subjectsFiltered = [];
    $scope.directions = [];
    $scope.currentPage = 1;
    $scope.studentAdress;
    function getQueryParams(qs) {
        qs = qs.split('+').join(' ');

        var params = {},
            tokens,
            re = /[?&]?([^=]+)=([^&]*)/g;

        while (tokens = re.exec(qs)) {
            params[decodeURIComponent(tokens[1])] = decodeURIComponent(tokens[2]);
        }

        return params;
    }
    $scope.search = function search(){
        var literalSubjects = [];
        for(var i = 0; i < $scope.subjects.length; i++){
            literalSubjects.push($scope.subjects[i].text);
        }
        if($scope.ranking == null){$scope.ranking = 0}
        if($scope.lessonsDictated == null){$scope.lessonsDictated = 0}
        $scope.data = {subjects: literalSubjects , ranking: $scope.ranking, lessons: $scope.lessonsDictated, homeClasses: $scope.homeClasses, sort: $scope.sortByDistance};
        $http.post('/teacher-search/getTeachers', $scope.data).
            then(function(response){
                //$scope.origin = new google.maps.LatLng(response.data[1], response.data[2]);
                $scope.studentAdress = response.data[1];
                $scope.teachers = response.data[0];
                $scope.teachers.forEach(function(teacher){
                    teacher.ranking = teacher.ranking.toFixed(2);
                });
                $scope.showResults = true;
                $scope.currentPage = 1;
                //$scope.teachers.sort(sortTeachersByDirection);
            }, function(response){
            })
    };
    (function() {  // init
        var queryParams = getQueryParams(document.location.search);
        if (queryParams.sort == "true") { // $routeParams.Id) {
            //var a = 0;
            //console.log("on init executed successfully" + a + $routeParams.paramId);
            //console.log($routeParams.bag);
            //console.log(queryParams);
            $scope.subjects.push({text: queryParams.subject.replace("-"," ")});
            $scope.sortByDistance = true;
            $scope.search();

        } else {
            //create a new object
        }
    })();

    //var sortTeachersByDirection = function (teacherA, teacherB) {
    //    if(teacherA.user.address == null || teacherA.user.address == undefined){
    //        if(teacherB.user.address == null || teacherB.user.address == undefined){
    //            return 0;
    //        }
    //        return 1;
    //    }
    //    if(teacherB.user.address == null || teacherB.user.address == undefined) {
    //        return -1;
    //    }
    //
    //    var location1;
    //    var location2;
    //    var originAddress;
    //    var distances = [];
    //    var geocoder = new google.maps.Geocoder();
    //    if (geocoder) {
    //        geocoder.geocode( { 'address': $scope.studentAdress}, function(results, status) {
    //            if (status == google.maps.GeocoderStatus.OK) {
    //                originAddress = results[0].geometry.location;
    //            }
    //        });
    //        geocoder.geocode( { 'address': teacherA.user.address}, function(results, status) {
    //            if (status == google.maps.GeocoderStatus.OK) {
    //                location1 = results[0].geometry.location;
    //            }
    //        });
    //        geocoder.geocode( { 'address': teacherB.user.address}, function(results, status) {
    //            if (status == google.maps.GeocoderStatus.OK) {
    //                location2 = results[0].geometry.location;
    //            }
    //        });
    //    }
    //    var directionsService = new google.maps.DirectionsService();
    //    var request = {
    //        origin: originAddress,
    //        destination: location1,
    //        travelMode: google.maps.DirectionsTravelMode.DRIVING
    //    };
    //    directionsService.route(request, function(response, status)
    //    {
    //        if (status == google.maps.DirectionsStatus.OK) {
    //            distances[0] = response.routes[0].legs[0].distance.text;
    //        }
    //    });
    //
    //    var request2 = {
    //        origin: originAddress,
    //        destination: location2,
    //        travelMode: google.maps.DirectionsTravelMode.DRIVING
    //    };
    //    directionsService.route(request2, function(response, status)
    //    {
    //        if (status == google.maps.DirectionsStatus.OK) {
    //            distances[1] = response.routes[0].legs[0].distance.text;
    //        }
    //    });
    //
    //    if(distances[0] == null || distances[0] == undefined){
    //        if(distances[1] == null || distances[1] == undefined){
    //            return 0;
    //        }
    //        return 1;
    //    }
    //    if(distances[1] == null || distances[1] == undefined) {
    //        return -1;
    //    }
    //    distances[0] = distances[0].replace(",",".");
    //    distances[1] = distances[1].replace(",",".");
    //    var distance1 = distances[0].split(" ");
    //    distances[0] = distance1[0] * (distance1[1] == "km" ? 1000 : 1);
    //
    //    var distance2 = distances[1].split(" ");
    //    distances[1] = distance2[0] * (distance2[1] == "km" ? 1000 : 1);
    //
    //
    //    return distances[0] - distances[1];
    //};

    $http.get("teacher-search/subjects").
        success(function(data, status, headers, config) {
            $scope.allSubjects = data.length != 0 ? data : [{text: 'Lengua'},{text: 'Matematica'},{text: 'Fisica'},{text: 'Quimica'},{text: 'Algebra'}];
        }).
        error(function(data, status, headers, config) {
            console.log("error subjects")
        });

    $scope.loadTags = function(query) {
        return $scope.subjectsFiltered.length == 0 ? $scope.allSubjects : $scope.subjectsFiltered;
    };

    $scope.sort = function(){
        $scope.subjectsFiltered = [];
        if(typeof $scope.allSubjects == 'undefined'){
            $http.get("teacher-search/subjects").
                success(function(data, status, headers, config) {
                    $scope.allSubjects = data.length != 0 ? data : [{text: 'Lengua'},{text: 'Matematica'},{text: 'Fisica'},{text: 'Quimica'},{text: 'Algebra'}];
                }).
                error(function(data, status, headers, config) {
                    console.log("error subjects")
                });
        }
        var currentTag = document.getElementsByName("currentTag")[0].value;
        for(var i = 0; i < $scope.allSubjects.length; i++){
            if($scope.allSubjects[i].text.substring(0,currentTag.length).toLowerCase() == currentTag.toLowerCase()){
                $scope.subjectsFiltered.push($scope.allSubjects[i]);
            }
        }
    };
})
;

