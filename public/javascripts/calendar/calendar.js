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

            $http.get("/calendar/get-calendar")
                .success(function (data) {
                    data.forEach(function (range) {
                        range.dayES = spanishDays[range.day];

                        range.fromHour = new Date(range.fromHour.split(" ART")[0]);
                        range.from = range.fromHour.getHours() + ':' + range.fromHour.getMinutes();
                        setTimeout(function () {
                            $('#' + range.day + 'from').timepicker({
                                showMeridian: false,
                                defaultTime: range.from
                            });
                        }, 500);

                        range.toHour = new Date(range.toHour.split(" ART")[0]);
                        range.to = range.toHour.getHours() + ':' + range.toHour.getMinutes();
                        setTimeout(function () {
                            $('#' + range.day + 'to').timepicker({
                                showMeridian: false,
                                defaultTime: range.to
                            });
                        }, 500);

                    });
                    $scope.availableHours = data;
                });

            $http.get("/calendar/get-lessons")
                .success(function (data) {
                    data.forEach(function (lesson) {
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
            //cableTimePickers();
        };

        $scope.updateHours = function() {
            $scope.availableHours.forEach(function(range) {
                var fromDate = getDateForHour(range.day, document.getElementById(range.day + 'from').value);
                var toDate = getDateForHour(range.day, document.getElementById(range.day + 'to').value);
                var data = {
                    date: serialize(fromDate),
                    fromHour: serialize(fromDate),
                    toHour: serialize(toDate)
                };
                $http.post("/calendar", data)
            });
        };

        $scope.openLessonModal = function (lessonEvent) {
            $scope.currentLesson = lessonEvent;
            $scope.$apply();
            $('#lessonModal').modal('show');
        };

        $scope.toFormatDate = function (time) {
            var date = new Date(time);
            return date.getDate() + "/" + (date.getMonth() + 1) + '/' + date.getFullYear();
        };

        var spanishDays = {
            "MONDAY": 'Lunes',
            "TUESDAY": 'Martes',
            "WEDNESDAY": 'Miércoles',
            "THURSDAY": 'Jueves',
            "FRIDAY": 'Viernes',
            "SATURDAY": 'Sábado',
            "SUNDAY": 'Domingo'
        };

        var getDateForHour = function(day, hour) {
            var date = new Date(correspondingDate[day]);
            var split = hour.split(':');
            date.setHours(split[0]);
            date.setMinutes(split[1]);
            return date;
        };

        var correspondingDate = {
            "MONDAY": '11/9/2015',
            "TUESDAY": '11/10/2015',
            "WEDNESDAY": '11/11/2015',
            "THURSDAY": '11/12/2015',
            "FRIDAY": '11/13/2015',
            "SATURDAY": '11/14/2015',
            "SUNDAY": '11/15/2015'
        };

        var serialize = function (date) {
            return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + twoDigits(date.getHours()) + ':' + twoDigits(date.getMinutes()) + ':' + twoDigits(date.getSeconds());
        };

        var twoDigits = function(number) {
            return ("0" + number).slice(-2);
        };

        $scope.init();

    }]);

var cableCalendar = function () {
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

var cableTimePickers = function () {
    $('.timePicker').timepicker({
        showMeridian: false,
        defaultTime: '9:00'
    });
};