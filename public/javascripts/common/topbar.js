/**
 * Created by Nicolás Burroni on 04/10/15.
 */
angular.module('img', [])
    .controller('ImageController', ['$scope', '$http', function($scope, $http) {
        $http.get('/common/userImage')
            .success(function(data) {
                $scope.profileImage = data;
            })
            .error(function() {

            });
    }]);
$(function() {
    angular.bootstrap(document.getElementById("img-app"),['img']);
});