/**
 * Created by facundo on 1/10/15.
 */
var app = angular.module('acceptLesson', []);
app.controller('AcceptController', ['$scope', '$http', function ($scope, $http) {
    $http.get("/teacher-profile/get-lessons")
        .success(function (data) {
            $scope.teacherLessons = data;
        });
    $scope.decision = function (lessonId, answer) {
        $http.get("/teacher-profile/decision?answer=" + answer+"&stringLessonId="+lessonId)
            .success(function (){
                document.getElementById(lessonId).style.display = "none";
                document.getElementById('showModal').click();
            })
            .error(function (){
                //todo
            });
    };
}]);
