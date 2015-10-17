(function() {
    var formApp = angular.module('postApp', ['ngRoute']);

    formApp.controller('SubscriptionController', ['$http', '$scope', function ($http) {
        var control = this;
        $http.get("/subscription/cardNumber").
            success(function (data, status, headers, config) {
                control.cardNumber=data;
                document.getElementById('cancel').hidden = false;
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
            var yearSelect = document.getElementById('year');
            var monthSelect = document.getElementById('month');
            var typeSelect = document.getElementById('type');


            if(yearSelect.value == "" || monthSelect.value == ""){
                document.getElementById('venError').hidden = false;
                document.getElementById('typeError').hidden = true;
            }
            else if(typeSelect.value == ""){
                document.getElementById('venError').hidden = true;
                document.getElementById('typeError').hidden = false;
            }
            else {
                document.getElementById('typeError').hidden = true;
                document.getElementById('venError').hidden = true;

                $http.post('/subscription/validate', this.card).
                    then(function (response) {
                        window.location = response.data;
                    }, function (response) {
                        document.getElementById('cardNumber').style.borderColor = "red";
                        document.getElementById('cardNumber').value = "";
                        document.getElementById('cardNumber').placeholder = "Número de tarjeta equivocado";
                    });
            }
        };
    }]);
})();