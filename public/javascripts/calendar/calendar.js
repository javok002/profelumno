/**
 * Created by nico on 26/10/15.
 */

angular.module('app', [])
    .controller('CalendarController', ['$scope', '$http', function ($scope, $http) {

        $scope.init = function () {
            $http.get("/calendar/get-user-name")
                .success(function (data) {
                    $scope.userName = data;
                });

            $http.get("/calendar/get-lessons")
                .success(function (data) {
                    data.forEach(function(lesson) {
                        var student = lesson.student.user;
                        lesson.title = lesson.subject.text /*+ ' a ' + student.name + ' ' + student.surname*/;
                        lesson.date = new Date(lesson.dateTime);
                    });

                    $scope.data = data;

                    $('#calendar').fullCalendar({
                        lang: 'es',
                        header: {
                            left: 'title',
                            center: 'today prev,next',
                            right: 'month,agendaWeek,agendaDay'
                        },
                        eventClick: function (event, jsEvent, view) {
                            $scope.openLessonModal(event);
                        },
                        events: $scope.data
                    });
                });
            /*cableCalendar();*/
        };

        $scope.openLessonModal = function (lessonEvent) {
            $scope.currentLesson = lessonEvent;
            $scope.$apply();
            $('#lessonModal').modal('show');
        };

        $scope.init();

    }]);

var cableCalendar = function() {
    $('#calendar').fullCalendar({
        lang: 'es',
        header: {
            left: 'title',
            center: 'today prev,next',
            right: 'month,agendaWeek,agendaDay'
        },
        eventClick: function (event, jsEvent, view) {
            $scope.openLessonModal(event);
        },
        events: [
            {
                title: 'Title',
                date: new Date(),
                address: 'Fake Street 123',
                comment: 'Lorem ipsum dolor sit amet',
                lessonState: 2,
                student: {
                    user: {
                        name: 'Pepe',
                        surname: 'Pepierrez'
                    }
                },
                subject: {
                    text: 'Math'
                }
            },
            {
                title: 'Title',
                date: new Date(),
                address: 'Fake Street 123',
                comment: 'Lorem ipsum dolor sit amet',
                lessonState: 2,
                student: {
                    user: {
                        name: 'Pepe',
                        surname: 'Pepierrez'
                    }
                },
                subject: {
                    text: 'Math'
                }
            }
        ]
    });
};