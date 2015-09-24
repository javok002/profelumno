/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngTagsInput', 'flow'] );


app.controller('TeacherInfoController', ['$scope','$http',function($scope, $http){
    edit=this;
    edit.u='';

        $http.get('modify-teacher/get-teacher')
            .success(function (data, status, headers, config) {
                edit.u = data;
            }).
            error(function (data, status, headers, config) {
                edit.u='juan';
                // log error
            });


    $scope.tags = [
        { text: 'Matematica' },
        { text: 'Fisica' }
    ];

    var verify = function() {
        var today = new Date();
        var birthday = edit.u.user.birthday;
        $scope.errors.teacherAge =(today.getYear() - birthday.getYear() < 6 ||(today.getYear() - birthday.getYear() == 6 && today.getMonth() < birthday.getMonth()));
        return !$scope.errors.incomplete && !$scope.errors.invalid && !$scope.errors.teacherAge;
    };

    edit.tags=$scope.tags;

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


}]);

app.controller('ImageController', function($scope, $http){

});