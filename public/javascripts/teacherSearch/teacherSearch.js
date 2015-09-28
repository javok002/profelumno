/**
 * Created by Nicolas Moreno on 14/09/2015
 */


var teacherSearchApp = angular.module("teacherSearchApp", ['ngTagsInput', 'angularUtils.directives.dirPagination']);
teacherSearchApp.controller("searchController", function($scope, $http) {
    $scope.subjects = [];
    $scope.teachers = [];
    $scope.showResults = false;
    $scope.ranking = 0;
    $scope.lessonsDictated = 0;
    $scope.homeClasses = false;
    $scope.search = function search(){
        var literalSubjects = [];
        for(var i = 0; i < $scope.subjects.length; i++){
            literalSubjects.push($scope.subjects[i].text);
        }
        $scope.data = {subjects: literalSubjects , ranking: $scope.ranking, lessons: $scope.lessonsDictated, homeClasses: $scope.homeClasses};
        $http.post('/teacher-search/getTeachers', $scope.data).
            then(function(response){
                $scope.teachers = response.data;
                //$scope.teachers = [{name: "A", lessonsDictated: 1, ranking: 1}, {name: "B", lessonsDictated: 2, ranking: 2}, {name: "C", lessonsDictated: 3, ranking: 3}, {name: "C", lessonsDictated: 3, ranking: 3}]
                $scope.showResults = true;
            }, function(response){
                //TODO
            })
    };

    $scope.loadTags = function(query) {
        return [{text: 'Lengua'},{text: 'Matematica'},{text: 'Fisica'},{text: 'Quimica'},{text: 'Algebra'}]
    };
});

