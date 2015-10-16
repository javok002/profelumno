/**
 * Created by facundo on 1/10/15.
 */
var app = angular.module('acceptLesson', []);
function loadLessons($http, $scope) {
    $http.get("/teacher-profile/get-lessons")
        .success(function (data) {
            $scope.teacherLessons = data;
        });
}
app.controller('AcceptController', ['$scope', '$http', function ($scope, $http) {
    loadLessons($http, $scope);
    $scope.decision = function (lessonId, answer, button) {
        button.currentTarget.innerHTML = '<span><i class="fa fa-spinner fa-pulse"></i></span>';
        button.currentTarget.disabled = true;
        $http.get("/teacher-profile/decision?answer=" + answer+"&stringLessonId="+lessonId)
            .success(function (){
                loadLessons($http, $scope);
                document.getElementById('showModal').click();
            })
            .error(function (){
                //todo
            });
    };
}]);
