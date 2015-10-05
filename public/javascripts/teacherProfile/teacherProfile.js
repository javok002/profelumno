/**
 * Created by Nicolás Burroni on 04/10/15.
 */
angular.module('app', [])
    .controller('DashboardController', ['$scope', '$http', function ($scope, $http) {

        /*$scope.teacher = {
         ranking: 7.4,
         subscription: 'Premium',
         renewalDate: '22-Agosto-2016'
         };
         $scope.topSubjects = {
         "Algebra": 8.9,
         "Geografia": 7.5,
         "Biologia": 5.2
         };
         $scope.nextLessons = [
         /!*{
         date: '13-Octubre-2015',
         subject: "Algebra"
         },
         {
         date: '16-Octubre-2015',
         subject: "Biologia"
         }*!/
         ];
         $scope.prevLessons = [
         {
         date: '02-Septiembre-2015',
         subject: "Geografia"
         },
         {
         date: '25-Septiembre-2015',
         subject: "Algebra"
         },
         {
         date: '28-Septiembre-2015',
         subject: "Algebra"
         }
         ];*/

        $scope.init = function () {
            $http.get('/teacher-profile/teacher')
                .success(function (data) {
                    var date = new Date(data.renewalDate);
                    data.renewalDate = date.getDay() + "/" + date.getMonth() + '/' + date.getFullYear();
                    $scope.teacher = data;
                })
                .error(function (data) {
                    //    TODO warning message
                });
            $http.get('/teacher-profile/top-subjects')
                .success(function (data) {
                    $scope.topSubjects = data;
                    $scope.noTopSubjects = Object.keys($scope.topSubjects).length == 0;
                })
                .error(function (data) {
                    //    TODO warning message
                });
            $http.get('/teacher-profile/previous-lessons')
                .success(function (data) {
                    data.forEach(function(lesson) {
                        var date = new Date(lesson.dateTime);
                        lesson.date = date.getDay() + "/" + date.getMonth() + '/' + date.getFullYear();
                    });
                    $scope.prevLessons = data;
                })
                .error(function (data) {
                    //    TODO warning message
                });
            $http.get('/teacher-profile/next-lessons')
                .success(function (data) {
                    data.forEach(function(lesson) {
                        var date = new Date(lesson.dateTime);
                        lesson.date = date.getDay() + "/" + date.getMonth() + '/' + date.getFullYear();
                    });
                    $scope.nextLessons = data;
                })
                .error(function (data) {
                    //    TODO warning message
                });
        };

        $scope.init();

    }]);