/**
 * Created by Nicolás Burroni on 13/09/15.
 */
angular.module('register', [])
    .controller('RegisterController', ['$scope', '$http', function ($scope, $http) {

        $http.get('/register/secure-questions')
            .success(function (data) {
                $scope.secureQuestions = data;
            });

        $scope.errors = {
            invalid: false,
            taken: false,
            incomplete: false,
            teacherAge: false,
            studentAge: false
        };

        $scope.submit = function () {
            if (!verify())
                return;
            if(!$scope.registerForm.$valid) {
                errors.invalid = true;
                return;
            }
            $http.post('/register', $scope.user)
                .success(function (data) {
                    window.location.href = data;
                })
                .error(function (data) {
                    $scope.errors.invalid = data == 'invalid';
                    $scope.errors.taken = data == 'taken';
                });
        };

        var verify = function() {
            $scope.errors.incomplete = $scope.user["secureQuestion"] == undefined || $scope.user["secureQuestion"] == '';
            var today = new Date();
            var birthday = $scope.user.birthday;
            $scope.errors.teacherAge = $scope.user.role == 'teacher' &&
                (today.getYear() - birthday.getYear() < 16 ||
                (today.getYear() - birthday.getYear() == 16 && today.getMonth() < birthday.getMonth()));
            $scope.errors.studentAge = $scope.user.role == 'student' &&
                (today.getYear() - birthday.getYear() < 6 ||
                (today.getYear() - birthday.getYear() == 6 && today.getMonth() < birthday.getMonth()));
            return !$scope.errors.incomplete && !$scope.errors.invalid && !$scope.errors.studentAge && !$scope.errors.teacherAge;
        };

    }])
    .directive("compareTo", function () {
        return {
            require: "ngModel",
            scope: {
                otherModelValue: "=compareTo"
            },
            link: function (scope, element, attributes, ngModel) {

                ngModel.$validators.compareTo = function (modelValue) {
                    return modelValue == scope.otherModelValue;
                };

                scope.$watch("otherModelValue", function () {
                    ngModel.$validate();
                });
            }
        }
    });


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