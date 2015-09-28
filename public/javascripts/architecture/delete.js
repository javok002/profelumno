/**
 * Created by Nicolás Burroni on 28/09/15.
 */
angular.module('app', [])
    .controller('DeleteController', ['$scope', '$http', function($scope, $http) {

        $scope.delete = function() {
            $http.post('/delete').success(function(data) {
                window.location.href = '/';
            });
        };

    }]);