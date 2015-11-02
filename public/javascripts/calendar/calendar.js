/**
 * Created by nico on 26/10/15.
 */

angular.module('app', [])
    .controller('CalendarController', ['$scope', '$http', function ($scope, $http) {

        $scope.init = function() {
            $http.get("/calendar/get-user-name")
                .success(function(data) {
                    $scope.userName = data;
                });

            $('#calendar').fullCalendar({
                lang: 'es',
                header: {
                    left:   'title',
                    center: 'today prev,next',
                    right:  'month,agendaWeek,agendaDay'
                },
                dayClick: function(date, jsEvent, view) {

                    date.format();


                    //alert('Current view: ' + view.name);
                    //$(this).css('background-color', 'red');

                },
                eventClick: function(event, jsEvent, view) {


                },
                events: [
                    {
                        title: 'Title',
                        start: new Date()

                    }
                ]
            });
        };



        $scope.init();

    }]);