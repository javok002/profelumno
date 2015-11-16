/**
 * Created by Nicolás Burroni on 04/10/15.
 */
angular.module('app', [])
    .controller('DashboardController', ['$scope', '$http', function ($scope, $http) {

        $scope.times = function(number) { return number <= 0 ? new Array(0) : new Array(number) };

        $scope.init = function () {
            $http.get('/teacher-profile/teacher')
                .success(function (data) {
                    var date = new Date(data.renewalDate);
                    data.renewalDate = dateToString(date);
                    data.ranking = data.ranking.toFixed(2);
                    data.subscription = data.subscription.substring(data.subscription.length - 4);
                    $scope.teacher = data;
                })
                .error(function (data) {
                    $scope.teacher = {};
                });
            $http.get('/teacher-profile/previous-lessons')
                .success(function (data) {
                    data.forEach(function(lesson) {
                        var split = lesson.date.split('/');
                        lesson.dateTime = new Date(split[1], split[0], split[2]);
                    });
                    $scope.prevLessons = data;
                })
                .error(function (data) {
                    $scope.prevLessons = [];
                });
            $http.get('/teacher-profile/next-lessons')
                .success(function (data) {
                    data.forEach(function(lesson) {
                        var date = new Date(lesson.dateTime);
                        lesson.date = dateToString(new Date(date));
                    });
                    $scope.nextLessons = data;
                })
                .error(function (data) {
                    $scope.nextLessons = [];
                });
            $http.get('/teacher-profile/top-subjects')
                .success(function (data) {
                    $scope.topSubjects = data;
                    $scope.noTopSubjects = $scope.topSubjects.length == 0;
                })
                .error(function (data) {
                    $scope.noTopSubjects = true;
                });
        };

        $scope.setModal = function(index){
            var prevLesson = $scope.prevLessons[index];
            currentIndex = index;
            $('#emailModal').val(prevLesson.studentEmail);
            $('#commentModal').val("");
            $('#starsModal').rating('reset');
        };

        $scope.review = function(){
            var prevLesson = $scope.prevLessons[currentIndex];
            var comment = $('#commentModal').val();
            var stars = parseInt($('#starsModal').val());
            var toEmail =  $('#emailModal').val();
            var idLesson = prevLesson.idLesson;

            $http.post("/review-lesson/review?comment=" + comment + "&stars=" + stars + "&toEmail=" + toEmail + "&idLesson=" + idLesson)
                .success(function (response) {
                    window.location.replace("/teacher-profile");
                });
        };

        $scope.init();

    }]);

var currentIndex;

var dateToString = function (date) {
    return date.getDate() + "/" + (date.getMonth() + 1) + '/' + date.getFullYear();
};