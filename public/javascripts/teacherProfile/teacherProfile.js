/**
 * Created by Nicolás Burroni on 04/10/15.
 */
angular.module('app', [])
    .controller('DashboardController', ['$scope', '$http', function($scope, $http) {

        $scope.teacher = {
            ranking: 7.4,
            subscription: 'Premium',
            renewalDate: '22-Agosto-2016'
        };
        $scope.topSubjects = {
            "Algebra": 8.9,
            "Geografia": 7.5,
            "Biologia": 5.2
        };

        $scope.init = function() {
            /*$http.get('/teacher-profile/teacher')
                .success(function(data) {
                    $scope.teacher = data;
                })
                .error(function(data) {
                //    TODO warning message
                });
            $http.get('/teacher-profile/top-subjects')
                .success(function(data) {
                    $scope.topSubjects = data;
                })
                .error(function(data) {
                //    TODO warning message
                });
            $http.get('/teacher-profile/previous-lessons')
                .success(function(data) {
                    $scope.prevLessons = data;
                })
                .error(function(data) {
                //    TODO warning message
                });
            $http.get('/teacher-profile/next-lessons')
                .success(function(data) {
                    $scope.nextLessons = data;
                })
                .error(function(data) {
                //    TODO warning message
                });*/
        };

        $scope.init();

    }]);