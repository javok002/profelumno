(function() {
    var formApp = angular.module('postApp', ['ngRoute']);

    formApp.controller('SubscriptionController', ['$http', '$scope', function ($http) {
        var control = this;
        $http.get("/subscription/cardNumber").
            success(function (data, status, headers, config) {
                control.cardNumber=data;
            }).
            error(function (data, status, headers, config) {
                // log error
            });
        // create a blank object to handle form data.
        control.card = {};
        control.succeded = false;
        control.error = false;
        control.successMessage = "Success!";
        control.errorMessage = "Error";
        // calling our submit function.
        control.submitForm = function () {
            $http.post('/subscription/validate', this.card).
                then(function(response) {
                    window.location = response.data;
                }, function(response) {
                    document.getElementById('cardNumber').style.borderColor = "red";
                    document.getElementById('cardNumber').value = "";
                    document.getElementById('cardNumber').placeholder = "Número de tarjeta equivocado";
                });
        };
    }]);
})();