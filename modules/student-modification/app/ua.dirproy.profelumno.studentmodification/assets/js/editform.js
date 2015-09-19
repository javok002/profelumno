/**
 * Created by tombatto on 14/09/15.
 */
var app=angular.module( 'EditForm', [''] );

app.controller("EditController", [function(){
    edit=this;
    $http.get('../user').
        success(function(data, status, headers, config) {
            edit.user = data;
        }).
        error(function(data, status, headers, config) {
            // log error
        });



}]);