/**
 * Created by Nicolas Moreno on 14/09/2015
 */


var teacherSearchApp = angular.module("teacherSearchApp", ['ngTagsInput', 'angularUtils.directives.dirPagination']);
teacherSearchApp.controller("searchController", function($scope, $http) {
    $scope.subjects = [];
    $scope.teachers = {};
    $scope.search = function search(){
        $http.get('/getTeachers', $scope.subjects, $scope.ranking, $scope.lessons, $scope.home).
            then(function(response){
                $scope.teachers = response;
            }, function(response){
                //TODO
            })
    };

    $scope.loadTags = function(query) {
        return [{text: 'Lengua'},{text: 'Matematica'},{text: 'Fisica'},{text: 'Quimica'},{text: 'Algebra'}]
    };
});

