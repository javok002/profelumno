/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngTagsInput', 'flow'] );

app.controller('TeacherInfoController', ['$scope','$http',function($scope, $http){
    edit=this;
    edit.u='';

        $http.get('modify-teacher/user')
            .success(function (data, status, headers, config) {
                edit.u = data;
            }).
            error(function (data, status, headers, config) {
                // log error
            });

    $scope.subjects = [
        { text: 'Matematica' },
        { text: 'Lengua' },
        { text: 'Fisica' }
    ];//edit.u.user.materias

    edit.u.subjects=$scope.subjects;
    var verify = function() {
        var today = new Date();
        var birthday = edit.u.user.birthday;
        $scope.errors.teacherAge =(today.getYear() - birthday.getYear() < 6 ||(today.getYear() - birthday.getYear() == 6 && today.getMonth() < birthday.getMonth()));
        return !$scope.errors.incomplete && !$scope.errors.invalid && !$scope.errors.teacherAge;
    };

    $scope.loadTags = function(query) {
        return [{text: 'Matematica'},{text: 'Fisica'},{text: 'Algebra'},{text: 'Lengua'},{text: 'Programación'}]
    };

    $scope.submit = function () {
        if (!verify())
            return;
        if(!$scope.modifyForm.$valid) {
            errors.invalid = true;
            return;
        }
        $http.post('modify-teacher/teacher-modification-post', edit.u)
            .success(function (data) {
                $scope.errors = { incomplete:false, invalid: false, teacherAge: false };
                alert(JSON.stringify(data));
            })
            .error(function (data) {
                alert(data);
            });
    };

    $scope.uploadImage = function(files) {
        var fd = new FormData();
        fd.append("file", files[0]);

        $http.post('modify-teacher/img', fd, {
            withCredentials: true,
            headers: {'Content-Type': "image" },
            transformRequest: angular.identity
        }).success(
            alert("success")
        ).error(alert("error")/*function(data){alert("error"+data);}*/);

    };

    $scope.submitSubjects = function(){
        var myJsonString = JSON.stringify($scope.edit);
        $http.post('modify-teacher/subjects', myJsonString)
            .success(alert("subido con exito"))
            .error("falla al guardar")
    };
}]);

app.directive('googleplace', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, model) {
            var options = {
                types: [],
                componentRestrictions: {}
            };
            scope.gPlace = new google.maps.places.Autocomplete(element[0], options);

            google.maps.event.addListener(scope.gPlace, 'place_changed', function() {
                scope.$apply(function() {
                    model.$setViewValue(element.val());
                });
            });
        }
    };
});
