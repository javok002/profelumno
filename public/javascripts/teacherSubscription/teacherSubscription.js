(function() {
    var formApp = angular.module('postApp', ['ngRoute']);

    formApp.controller('SubscriptionController', ['$http', '$scope', function ($http) {
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
                    document.getElementById('cardNumber').style.borderColor = "red";
                    document.getElementById('cardNumber').value = "";
                    document.getElementById('cardNumber').placeholder = "Número de tarjeta equivocado";
                });
        };
    }]);
})();