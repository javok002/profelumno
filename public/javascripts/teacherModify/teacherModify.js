/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngTagsInput']);

app.controller('TeacherInfoController', ['$scope', '$http', 'fileUpload', function ($scope, $http, fileUpload) {
    edit = this;
    edit.u = {};
    edit.u.user = {};
    $scope.radio = '';
    edit.u.subjects=[];
    $scope.imageUrl='';

    $http.get('modify-teacher/user')
        .success(function (data, status, headers, config) {
            edit.u = data;
            //for(var i = 0; i < edit.u.user.subjects; i++){
            //    edit.u.subjects.push({"text": edit.u.user.subjects[i]});
            //}
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
            $scope.imageUrl=data;
        }).
        error(function (data, status, headers, config) {
            // log error
        });

    /*$http.get("modify-teacher/subjects").
        success(function(data, status, headers, config) {
            $scope.allSubjects= data;
        }).
        error(function(data, status, headers, config) {
            // log error
        });
    //TAGS
    edit.tags=$scope.tags;//meterias.json*/

    $scope.loadTags = function(query) {
        return [{text: 'Lengua'},{text: 'Matematica'},{text: 'Fisica'},{text: 'Quimica'},{text: 'Algebra'}]
    };

    $scope.errors = {
        invalid: false,
        incomplete: false,
        teacherAge: false,
        taken:false
    };
    var verify = function () {
        var today = new Date();
        var birthday = $scope.date;
        $scope.errors.teacherAge = (today.getYear() - birthday.getYear() < 16 || (today.getYear() - birthday.getYear() == 16 && today.getMonth() < birthday.getMonth()));
        return !$scope.errors.incomplete && !$scope.errors.invalid && !$scope.errors.teacherAge;
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
                $scope.errors = {incomplete: false, invalid: false, teacherAge: false, taken: false, length: false};
                window.location.href = data;
                /*alert(JSON.stringify(data));*/
            })
            .error(function (data) {
                //alert(data);
            });
    };

    $scope.uploadFile = function(){
        var file = $scope.fileToUpload;
        var uploadUrl = "modify-teacher/img";
        fileUpload.uploadFileToUrl(file, uploadUrl, $scope);
    };

    $scope.submitSubjects = function () {
        if (!$scope.subjectsForm.$valid) {
            return;
        }
        //var myJsonString = JSON.stringify($scope.edit);
        var literalSubjects = [];
        for(var i = 0; i < edit.u.user.subjects.length; i++){
            literalSubjects.push(edit.u.user.subjects[i].text);
        }
        $http.post('modify-teacher/subjects', literalSubjects)
            .success(function(response){
                window.location.href = "/teacher-profile"
            })
            .error("falla al guardar")
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
    this.uploadFileToUrl = function (file, uploadUrl, scope) {
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function (data, status, headers, config) {
                edit.u.user.profilePicture = data;
                scope.imageUrl=data;
                  //window.location.href=data;
            })
            .error(function () {
            });
    }
}]);