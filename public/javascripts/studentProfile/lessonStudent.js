/**
 * Created by facundo on 18/10/15.
 */
var app = angular.module('acceptLesson', []);
function loadLessons($http, $scope) {
    $http.get("/student-profile/get-lessons")
        .success(function (data) {
            $scope.studentLessons = data;
        });
}
app.controller('AcceptController', ['$scope', '$http', function ($scope, $http) {
    loadLessons($http, $scope);
    $scope.toFormatDate = function (time) {
        return new Date(time);
    };
}]);