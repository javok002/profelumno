/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngTagsInput']);

app.controller('TeacherInfoController', ['$scope', '$http', 'fileUpload', function ($scope, $http, fileUpload) {
    edit = this;
    edit.u = {};
    edit.u.user = {};
    $scope.radio = '';
    edit.u.user.subjects=[];

    $http.get('modify-teacher/user')
        .success(function (data, status, headers, config) {
            edit.u = data;
            $scope.date=new Date(edit.u.user.birthday);
            if (edit.u.homeClasses) {
                $scope.radio = 'yes';
            } else {
                $scope.radio = 'no';
            }

        }).
        error(function (data, status, headers, config) {
            // log error
        });

    $http.get('modify-teacher/img')
        .success(function (data, status, headers, config) {
            edit.u.user.profilePicture = data;
            alert(data);
        }).
        error(function (data, status, headers, config) {
            // log error
        });

    $http.get("modify-teacher/subjects").
        success(function(data, status, headers, config) {
            $scope.allSubjects= data;
        }).
        error(function(data, status, headers, config) {
            // log error
        });
    //TAGS
    edit.tags=$scope.tags;//meterias.json

    $scope.errors = {
        invalid: false,
        incomplete: false,
        teacherAge: false,
        taken:false
    };
    var verify = function () {
        var today = new Date();
        var birthday = $scope.date;
        $scope.errors.teacherAge =(today.getYear() - birthday.getYear() < 6 ||(today.getYear() - birthday.getYear() == 6 && today.getMonth() < birthday.getMonth()));
        return !$scope.errors.incomplete && !$scope.errors.invalid && !$scope.errors.teacherAge;
    };

    $scope.loadTags = function (query) {
        return $scope.allSubjects;
    };


    $scope.submit = function () {
        if (!verify())
            return;
        if (!$scope.modifyForm.$valid) {
            errors.invalid = true;
            return;
        }
        edit.u.user.birthday=$scope.date;
        edit.u.homeClasses= $scope.radio=='yes';
        $http.post('modify-teacher/teacher-modification-post', edit.u)
            .success(function (data) {
                $scope.errors = { incomplete:false, invalid: false, teacherAge: false };
                alert(JSON.stringify(data));
            })
            .error(function (data) {
                alert("error al guardar");
            });
    };

    $scope.uploadFile = function(){
        var file = $scope.fileToUpload;
        var uploadUrl = "modify-teacher/img";
        fileUpload.uploadFileToUrl(file, uploadUrl);;
    };

    $scope.submitSubjects = function () {
        var myJsonString = JSON.stringify($scope.edit);
        $http.post('modify-teacher/subjects', myJsonString)
            .success(alert("subido con exito"))
            .error(alert("falla al guardar subjects"))
    };
}]);

app.directive('googleplace', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attrs, model) {
            var options = {
                types: [],
                componentRestrictions: {}
            };
            scope.gPlace = new google.maps.places.Autocomplete(element[0], options);

            google.maps.event.addListener(scope.gPlace, 'place_changed', function () {
                scope.$apply(function () {
                    model.$setViewValue(element.val());
                });
            });
        }
    };
});

app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

app.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function (file, uploadUrl) {
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function (data, status, headers, config) {
                edit.u.user.profilePicture = data;
                alert(data);
            })
            .error(function () {
            });
    }
}]);