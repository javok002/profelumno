(function() {
    var formApp = angular.module('postApp', ['ngRoute']);

    formApp.controller('SubscriptionController', ['$http', function ($scope, $http) {
        // create a blank object to handle form data.
        $scope.card = {};
        // calling our submit function.
        $scope.submitForm = function () {
            // Posting data to php file
            $http({
                method: 'POST',
                url: '/validate',
                data: $scope.card, //forms user object
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            })
                .success(function () {
                    $scope.successMessage = "success";
                });
        };
    }]);
})();