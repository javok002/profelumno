/**
 * Created by Nicolas Moreno on 14/09/2015.
 */


var teacherSearch = angular.module("teacherSearch", []);
teacherSearch.controller("Search", function($scope, $http) {
    $http.get('/getTeacher').
        then(function(response){
            $scope.teachers = response;
        //TODO
    }, function(response){
        //TODO
        })
});
