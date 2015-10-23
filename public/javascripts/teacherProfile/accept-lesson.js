/**
 * Created by facundo on 1/10/15.
 */
var app = angular.module('acceptLesson', []);
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
    $scope.setLesson = function (lesson) {
        $scope.decisionLesson = lesson;
        document.getElementById('display-modal').click();

    };
    $scope.setDecision = function (lesson) {
        $scope.decisionLesson = lesson;
        document.getElementById('display-decision').click();

    };
    $scope.decision = function (lessonId, answer) {
        if (answer == true) {
            document.getElementById("acceptLesson").innerHTML = '<span><i class="fa fa-spinner fa-pulse"></i></span>';
        } else {
            document.getElementById("rejectLesson").innerHTML = '<span><i class="fa fa-spinner fa-pulse"></i></span>';

        }
        document.getElementById("acceptLesson").disabled = true;
        document.getElementById("rejectLesson").disabled = true;
        $http.get("/teacher-profile/decision?answer=" + answer + "&stringLessonId=" + lessonId)
            .success(function () {
                loadLessons($http, $scope);
                document.getElementById('dismiss-modal').click();
                document.getElementById('showModal').click();
                document.getElementById("acceptLesson").innerHTML = 'Aceptar';
                document.getElementById("rejectLesson").innerHTML = 'Rechazar';
                document.getElementById("acceptLesson").disabled = false;
                document.getElementById("rejectLesson").disabled = false;
            })
            .error(function () {
                //todo
            });
    };
}]);

var dateToString = function (date) {
    return date.getDate() + "/" + (date.getMonth() + 1) + '/' + date.getFullYear();
};
