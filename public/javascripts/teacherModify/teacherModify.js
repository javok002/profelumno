/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngTagsInput'] );

app.controller('TeacherInfoController', ['$scope','$http',function($scope, $http){
    edit=this;
    edit.u={};

    $http.get("user").
        success(function(data, status, headers, config) {
            edit.u= data;
        }).
        error(function(data, status, headers, config) {
            // log error
        });

    $scope.tags = [
        { text: 'Matematica' },
        { text: 'Fisica' }
    ];

    edit.tags=$scope.tags;

    $scope.loadTags = function(query) {
        return [{text: 'Matematica'},{text: 'Fisica'},{text: 'Algebra'},{text: 'Lengua'},{text: 'Programación'}]
    };

    $scope.uploadImage = function(files) {
        var fd = new FormData();
        fd.append("file", files[0]);

        $http.post('teacher-modify/img', fd, {
            withCredentials: true,
            headers: {'Content-Type': "image" },
            transformRequest: angular.identity
        }).success(
            alert("success"+data)
        ).error(alert("error")/*function(data){alert("error"+data);}*/);

    };

    $scope.submitSubjects = function(){
        var myJsonString = JSON.stringify(edit.tags);
        $http.post('teacher-modify/subjects', myJsonString)
            .success(alert("subido con exito"))
            .error("falla al guardar")
    };
}]);
