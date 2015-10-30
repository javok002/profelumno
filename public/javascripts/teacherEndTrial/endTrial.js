/**
 * Created by yankee on 19/10/15.
 */
(function() {
    var formApp = angular.module('postApp', ['ngRoute']);

    formApp.controller('EndTrialController', ['$http', '$scope', function ($http) {
        var control = this;
        $http.get("/subscription/cardNumber").
            success(function (data, status, headers, config) {
                control.cardNumber=data;
                document.getElementById('cancel').style.visibility = "visible";
            }).
            error(function (data, status, headers, config) {
                document.getElementById('cancel').style.visibility = "hidden";

                // log error
            });
        // calling our submit function.
        control.submitForm = function () {
            $http.post('/subscription/charge').
                then(function (response) {
                    window.location = response.data;
                }, function (response) {

                });
        };
    }]);
})();