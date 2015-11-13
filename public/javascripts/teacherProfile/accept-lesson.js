/**
 * Created by facundo on 1/10/15.
 */
var app = angular.module('acceptLesson', ['djds4rce.angular-socialshare']);
app.run(function($FB){
    $FB.init('1509250906034945');
});

function loadLessons($http, $scope) {
    $http.get("/teacher-profile/current-lessons")
        .success(function (data) {
            $scope.currentLessons = data;
        });
    $http.get('/teacher-profile/next-lessons')
        .success(function (data) {
            data.forEach(function (lesson) {
                var date = new Date(lesson.dateTime);
                lesson.date = dateToString(date);
            });
            $scope.nextLessons = data;
        })
        .error(function (data) {
            $scope.nextLessons = [];
        });
    $http.get('/teacher-profile/prev-lessons')
        .success(function (data) {
            $scope.prevLessons = data;
        })
        .error(function (data) {
            $scope.prevLessons = [];
        });
}
app.controller('AcceptController', ['$scope', '$http', function ($scope, $http) {
    loadLessons($http, $scope);
    $scope.toFormatDate = function (time) {

        var date = new Date(time);
        return date.getDate() + "/" + (date.getMonth() + 1) + '/' + date.getFullYear();
   };
    $scope.toFormatTime = function (time){
        var date = new Date(time);
        return date.getDate() + "/" + (date.getMonth() + 1) + '/' + date.getFullYear() + " " + date.getHours() +":" + date.getMinutes();
    };
    $scope.setLesson = function (lesson) {
        $scope.decisionLesson = lesson;
        document.getElementById('display-modal').click();

    };
    $scope.setDecision = function (lesson) {
        $scope.decisionLesson = lesson;
        $scope.succeeded = false;
        $scope.error = false;
        document.getElementById('display-decision').click();

    };
    $scope.decision = function (lessonId, answer) {
        document.getElementById('dismiss-modal').click();
        document.getElementById('showModal').click();
        $http.get("/teacher-profile/decision?answer=" + answer + "&stringLessonId=" + lessonId)
            .success(function () {
                loadLessons($http, $scope);
                $scope.succeeded = true;
            })
            .error(function () {
                $scope.error = true;
            });
    };
}]);

var dateToString = function (date) {
    return date.getDate() + "/" + (date.getMonth() + 1) + '/' + date.getFullYear();
};
