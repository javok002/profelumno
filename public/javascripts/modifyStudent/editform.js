/**
 * Created by tombatto on 14/09/15.
 */
var app=angular.module( 'EditForm', ['ngTagsInput'] );

app.controller("EditController", ['$http','$scope',function($http,$scope){
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
        { text: 'Tag1' },
        { text: 'Tag2' },
        { text: 'Tag3' }
    ];
    edit.tags=$scope.tags;
    $scope.loadTags = function(query) {
        return [{text: 'Tag1'},{text: 'Tag2'},{text: 'Tag3'},{text: 'Tag4'},{text: 'Tag5'}]
    };


}]);