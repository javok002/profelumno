(function() {
    var formApp = angular.module('postApp', ['ngRoute']);

    formApp.controller('SubscriptionController', ['$http', '$scope', function ($http, $scope) {
        // create a blank object to handle form data.
        var control = this;
        control.card = {};
        control.succeded = false;
        control.error = false;
        control.successMessage = "Success!";
        control.errorMessage = "Error";
        // calling our submit function.
        control.submitForm = function () {
            $http.post('/subscription/validate', this.card).
                then(function(response) {
                    control.succeded = true;
                }, function(response) {
                    control.error = true;
                });
            //$http({
            //    method: 'POST',
            //    url: '/subscription/validate',
            //    data: this.card, //forms user object
            //    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            //}).success(function () {
            //        control.succeded = true;
            //    });
        };
    }]);
})();