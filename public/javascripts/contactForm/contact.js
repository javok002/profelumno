/**
 * Created by yankee on 26/10/15.
 */
var app = angular.module('ContactForm', []);

app.controller("ContactController", ['$http', '$scope', function ($http, $scope) {

    $scope.sending = false;
    $scope.sent = false;
    $scope.disable = false;
    $scope.failed = false;

    $scope.submit = function () {
        $scope.failed = false;
        $scope.disable = true;
        $scope.sending = true;
        $http.post('contact-form/send-form', $scope.c)
            .success(function (data) {
                $scope.sending = false;
                $scope.sent = true;
            })
            .error(function (data) {
                $scope.sending = false;
                $scope.failed = true;
                $scope.disable = false;
            });
    };
}]);