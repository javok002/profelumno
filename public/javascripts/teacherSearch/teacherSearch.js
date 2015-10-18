/**
 * Created by Nicolas Moreno on 14/09/2015
 */


var teacherSearchApp = angular.module("teacherSearchApp", ['ngTagsInput', 'angularUtils.directives.dirPagination', 'profLesson']);
teacherSearchApp.controller("searchController", function($scope, $http) {
    $scope.subjects = [];
    $scope.teachers = [];
    $scope.showResults = false;
    $scope.ranking = 0;
    $scope.lessonsDictated = 0;
    $scope.homeClasses = false;
    $scope.allSubjects = [];
    $scope.subjectsFiltered = [];
    $scope.search = function search(){
        var literalSubjects = [];
        for(var i = 0; i < $scope.subjects.length; i++){
            literalSubjects.push($scope.subjects[i].text);
        }
        if($scope.ranking == null){$scope.ranking = 0}
        if($scope.lessonsDictated == null){$scope.lessonsDictated = 0}
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
});

