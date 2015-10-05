/**
 * Created by tombatto on 21/09/15.
 */
var app=angular.module( 'EditForm', [] );

app.controller("EditController", ['$http','$scope',function($http,$scope){
    edit=this;
    edit.u={};
    edit.u.user={};

    $http.get("user").
        success(function(data, status, headers, config) {
            edit.u= data;
            $scope.date=new Date(edit.u.user.birthday);
        }).
        error(function(data, status, headers, config) {
            // log error
        });


    //SUBMIT
    $scope.errors = {
        invalid:false,
        invalidactual: false,
        invalidnew: false,
        invalidconfirm: false
    };

    $scope.submit = function () {
        if(!$scope.modifyForm.actualPassword.$valid) {
            $scope.errors.invalidactual = true;
            return;
        }
        if(!$scope.modifyForm.password.$valid) {
            $scope.errors.invalidnew = true;
            $scope.errors.invalidactual = false;
            return;
        }
        if(!$scope.modifyForm.confirm.$valid) {
            $scope.errors.invalidconfirm = true;
            $scope.errors.invalidactual = false;
            return;
        }
        edit.u.user.birthday=$scope.date;
        edit.u.user.password=$scope.password;
        $http.post('save-pass', edit.u)
            .success(function (data) {
                $scope.errors = { invalid: false, incomplete: false, teacherAge: false, studentAge: false };
                //alert(JSON.stringify(data));
                window.location.href = data;
            })
            .error(function (data) {
                alert(data);
            });
    };

}]);

app.directive("compareTo", function () {
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