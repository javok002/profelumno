/**
 * Created by Nicolás Burroni on 13/09/15.
 */
angular.module('register', [])
    .controller('RegisterController', ['$scope', '$http', function ($scope, $http) {

        $scope.submit = function () {
            //$scope.$apply();
            $http.post('/register', $scope.user)
                .success(function (data) {
                    alert(JSON.stringify(data));
                })
                .error(function (data) {
                    alert(data);
                });
        };

    }]);

$(function () {
    //initDatepicker();
    initICheck();
});

function initDatepicker() {
    $('.datepicker').datepicker({
        endDate: '0d'
    });
}

function initICheck() {
    $('input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    });
}