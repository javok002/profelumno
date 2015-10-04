/**
 * Created by Nicolás Burroni on 04/10/15.
 */
angular.module('app', [])
    .controller('DashboardController', ['$scope', '$http', function($scope, $http) {

        $scope.ranking = 0;

        $scope.init = function() {
            $http.get('/teacher-profile/ranking')
                .success(function(data) {
                    $scope.ranking = data;
                })
                .error(function(data) {
                //    TODO warning message
                });
        };

        $scope.init();

    }]);