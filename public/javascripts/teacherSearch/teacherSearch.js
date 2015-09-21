/**
 * Created by Nicolas Moreno on 14/09/2015
 */


var teacherSearchApp = angular.module("teacherSearchApp", ['ngTagsInput', 'angularUtils.directives.dirPagination']);
teacherSearchApp.controller("searchController", function($scope, $http) {
    $scope.subjects = [];
    //$scope.teachers = {};
    $scope.search = function search(){
        var literalSubjects = [];
        for(var i = 0; i < $scope.subjects.length; i++){
            literalSubjects.push($scope.subjects[i].text);
        }
        $scope.data = {subjects: literalSubjects , ranking: $scope.ranking, lessons: $scope.lessonsDictated, homeClasses: $scope.homeClasses};
        $http.post('/teacher-search/getTeachers', JSON.stringify($scope.data)).
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

