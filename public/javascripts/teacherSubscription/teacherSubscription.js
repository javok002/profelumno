(function() {
    var formApp = angular.module('postApp', ['ngRoute']);

    formApp.controller('SubscriptionController', ['$http', '$scope', function ($http) {
        var control = this;
        $http.get("/subscription/cardNumber").
            success(function (data, status, headers, config) {
                control.cardNumber=data;
                document.getElementById('cancel').style.visibility = "visible";
            }).
            error(function (data, status, headers, config) {
                document.getElementById('cancel').style.visibility = "hidden";
                document.getElementById('logout').style.visibility = "visible";
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

function checkType() {
    var type=document.getElementById("type").value;
    //if(isNaN(type)){
    //    document.getElementById("cardNumberDiv").innerHTML = "<input type=\"text\" class=\"form-control\" name=\"cardNumber\" required=\"required\" id=\"cardNumber\" ng-model=\"control.card.number\" maxlength=\"20\">";
    //    document.getElementById("cardNumber").className += " input-error";
    //}
        if(type!="AMEX"){
            if(document.getElementById('cardNumber').value.length <= 16){
                document.getElementById("cardNumberDiv").innerHTML = "<input type=\"text\" class=\"form-control\" name=\"cardNumber\" required=\"required\" id=\"cardNumber\" ng-model=\"control.card.number\" maxlength=\"16\" value="+document.getElementById('cardNumber').value+">";
            }
            else{
                document.getElementById("cardNumberDiv").innerHTML = "<input type=\"text\" class=\"form-control\" name=\"cardNumber\" required=\"required\" id=\"cardNumber\" ng-model=\"control.card.number\" maxlength=\"16\">";
                document.getElementById("cardNumber").className += " input-error";

            }
            if(document.getElementById('secCode').value.length != 3){
                document.getElementById("secCodeDiv").innerHTML = "<input type=\"password\" class=\"form-control\" name=\"secCode\" id=\"secCode\" required=\"required\" maxlength=\"3\" >"
                document.getElementById("secCode").className += " input-error";

            }
        }
        else{
            if(document.getElementById('cardNumber').value.length <= 15){
                document.getElementById("cardNumberDiv").innerHTML = "<input type=\"text\" class=\"form-control\" name=\"cardNumber\" required=\"required\" id=\"cardNumber\" ng-model=\"control.card.number\" maxlength=\"15\" value="+document.getElementById('cardNumber').value+">";
            }
            else{
                document.getElementById("cardNumberDiv").innerHTML = "<input type=\"text\" class=\"form-control\" name=\"cardNumber\" required=\"required\" id=\"cardNumber\" ng-model=\"control.card.number\" maxlength=\"15\">";
                document.getElementById("cardNumber").className += " input-error";

            }
            if(document.getElementById('secCode').value.length != 4){
                document.getElementById("secCodeDiv").innerHTML = "<input type=\"password\" class=\"form-control\" name=\"secCode\" id=\"secCode\" required=\"required\" maxlength=\"4\" >"
                document.getElementById("secCode").className += " input-error";

            }
        }



}