/**
 * Created by nico on 26/10/15.
 */

angular.module('app', [])
    .controller('CalendarController', ['$scope', '$http', function ($scope, $http) {

        $scope.init = function() {
            $http.get("/calendar/get-user-name")
                .success(function(data) {
                    $scope.userName = data;
                })
        };



        $scope.init();

    }]);

$(function() {
    $('#calendar').fullCalendar({
        lang: 'es'
    });
});