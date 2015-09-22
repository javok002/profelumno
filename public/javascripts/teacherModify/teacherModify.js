/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngTagsInput', 'flow'] );

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


}]);
app.controller('ImageController', function($scope, $http){

});