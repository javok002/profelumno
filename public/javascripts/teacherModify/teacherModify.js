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

    $scope.materias = [
        { text: 'Matematica' },
        { text: 'Fisica' }
    ];

    edit.materias=$scope.materias;

    $scope.loadTags = function(query) {
        return [{text: 'Matematica'},{text: 'Fisica'},{text: 'Algebra'},{text: 'Lengua'},{text: 'Programación'}]
    };

    $scope.uploadImage = function(files) {
        var fd = new FormData();
        fd.append("file", files[0]);

        $http.post('img', fd, {
            withCredentials: true,
            headers: {'Content-Type': "image" },
            transformRequest: angular.identity
        }).success(
            alert("success"+data)
        ).error(alert("error")/*function(data){alert("error"+data);}*/);

    };

    $scope.submitSubjects = function(){
        var myJsonString = JSON.stringify(edit);
        $http.post('/teacher-modify/subjects', myJsonString)
            .success(alert("subido con exito"))
            .error("falla al guardar")
    };

    function encodeImageFileAsURL(files) {
        var filesSelected = files;
        if (filesSelected.length > 0) {
            var fileToLoad = filesSelected[0];
            var fileReader = new FileReader();
            fileReader.onload = function (fileLoadedEvent) {
                var srcData = fileLoadedEvent.target.result; // <--- data: base64
                document.getElementById("hiddenBase64").value = srcData;
                document.getElementById("upload_form").submit();
            };
            fileReader.readAsDataURL(fileToLoad);
        }
    }
}]);
