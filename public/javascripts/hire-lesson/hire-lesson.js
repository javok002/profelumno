/**
 * Created by Paca on 9/19/15.
 */
angular.module('profLesson', [])

    .controller('HireCtrl',['$scope', '$http', function($scope, $http) {
        $scope.comment = '';
        $scope.lessonDate= '';
        $scope.selected= '';
        $scope.subjectBool = false;
        $scope.addressBool = false;
        $scope.dateBool = false;
        $scope.durationBool = false;

        $scope.setSubjects = function (teacherId) {
            var subs = {};
            $http.get('/hire-lesson/get-subjects?teacherId='+teacherId)
                .success(function (data) {
                    subs = data;
                });
            return subs;
        };
        $scope.selected = {};
        $scope.postLesson = function(teacherId, dateTime, duration, address, subject, index) {

            if(dateTime == undefined) {
                $scope.dateBool = true;
            } else {
                $scope.dateBool = false;
            }

            if(duration == undefined) {
                $scope.durationBool = true;
            } else {
                $scope.durationBool = false;
            }

            if(address == undefined){
                $scope.addressBool = true;
            } else {
                $scope.addressBool = false;
            }

            if(subject.$$hashKey != undefined){
                $scope.subjectBool = true;
            } else {
                $scope.subjectBool = false;
            }

            if (dateTime != undefined && duration != undefined && address != undefined && subject.$$hashKey == undefined ) {
                data = {
                    address: $scope.address,
                    comment: $scope.comment,
                    teacherId: teacherId,
                    subjectId: $scope.selected,
                    dateTime: dateTime.getTime(),
                    duration: duration
                };
                $('#hire-modal' + index).modal("hide");
                $http.post('/hire-lesson/new', data).then(successCallback, errorCallback);
                $("#loadingModal").modal("show");
            }
        };


        successCallback = function () {
            $("#succesModal").modal("show");
            $("#loadingModal").modal("hide");

        };

        errorCallback = function () {
            $("#errorModal").modal("show");
            $("#loadingModal").modal("hide");
        };

    }])

    .directive('hireLesson',['$http', function($http){
        return {
            restrict: 'E',
            scope : {
                teacherId : '=',
                index : '=',
                teacherSubs: '=',
                subjectBool: '=',
                addressBool: '=',
                dateBool: '=',
                durationBool: '='
            },
            link : function(scope){

                scope.$watch('selected', function(){
                    scope.subjectBool = false;
                });

                scope.$watch('address', function(){
                    scope.addressBool = false;
                });

                scope.$watch('dateTime', function(){
                    scope.dateBool = false;
                });

                scope.$watch('duration', function(){
                    scope.durationBool = false;
                });

                scope.date = 'date';

                spanishDays = ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];

                toDateTimeString = function (date) {
                    return [spanishDays[date.getDay()], date.getDate() + '/' + date.getMonth() + '/' + date.getFullYear(), 'a las', date.getHours() + ':' + date.getMinutes()].join(" ");
                };

                Date.prototype.addHours= function(h){
                    this.setHours(this.getHours()+h);
                    return this;
                };

                scope.init = function (calendarIndex) {

                    $http.get("/calendar/get-user-name")
                        .success(function (data) {
                            scope.userName = data;
                        });

                    $http.get("/calendar/get-teacher-time?teacherId=" + scope.teacherId)
                        .success(function (data) {
                            data[0].forEach(function(lesson) {
                                var teacher = lesson.teacher.user;
                                lesson.title = lesson.subject.text /*+ ' a ' + student.name + ' ' + student.surname*/;
                                lesson.date = new Date(lesson.dateTime);
                                lesson.end = angular.copy(lesson.date).addHours(lesson.duration.seconds / 3600);
                                lesson.lessonAcepted = true;
                            });

                            ranges = [];

                            scope.durations = [];

                            data[1].forEach(function(day) {
                                auxDate = new Date(day.day);
                                auxDate.setHours(0);
                                auxDate.setMinutes(0);
                                auxDate.setSeconds(0);

                                day.rangeList.forEach(function(range){
                                    for (var i = range.from; i < range.to; i++){
                                        ranges[ranges.length] = {
                                            title: '',
                                            start: angular.copy(auxDate).addHours(i),
                                            end: angular.copy(auxDate).addHours(i + 1),
                                            color: '#008d4c',
                                            dateTime: angular.copy(auxDate).addHours(i).getTime(),
                                            from: i,
                                            to: range.to
                                    };

                                    }
                                });
                            });

                            scope.data = data[0].concat(ranges);

                            $('#calendar' + calendarIndex ).fullCalendar({
                                lang: 'es',
                                header: {
                                    left: 'today',
                                    center: 'prev,next',
                                    right: 'agendaWeek'
                                },
                                eventClick: function (event, jsEvent, view) {
                                    if (!event.lessonAcepted) {
                                        scope.dateTime = new Date(event.dateTime);
                                        $('#datepicker' + scope.index).val(toDateTimeString(scope.dateTime));
                                        scope.durations = [];
                                        for(var j = event.from, i = 0; j < event.to; j++, i++){
                                            scope.durations[i] = event.to - j;
                                        }

                                        scope.$apply();
                                    }
                                },
                                events: scope.data
                            });

                        });
                    $('#hire-modal' + calendarIndex).on('shown.bs.modal', function(){
                        $('#calendar' + calendarIndex ).fullCalendar('render');
                    });

                    $('#hire-modal' + calendarIndex).modal('show');
                };
            },
            template:
            '<button type="button" class="btn btn-success" ng-click="init(index)">' +
                'Contratar' +
            '</button>' +

            '<!-- Modal -->' +
            '<div class="modal fade" id="hire-modal{{index}}" role="dialog">' +
                '<div class="modal-dialog">'+
                    '<!-- Modal content-->' +
                    '<div class="modal-content">' +
                        '<div class="modal-header">' +
                            '<button type="button" class="close" data-dismiss="modal">&times;</button>' +
                            '<h4 class="modal-title">Contratar Clase</h4>' +
                        '</div>' +
                        '<div class="modal-body">' +
                            '<div ng-controller="HireCtrl">' +
                                '<div class="box-body">' +

                                    '<label>Seleccione una materia:</label>' +
                                    '<select class="form-control" ng-model="selected">'+
                                       '<option ng-if="teacherSubs.length > 0" ng-repeat="subject in teacherSubs" value="{{subject.id}}">{{subject.text}}</option>'+
                                    '</select>'+

                                    '<div ng-show="subjectBool" class="callout callout-danger">' +
                                        '<p>Campo requerido</p>' +
                                    '</div>' +

                                    '<div class="form-group">' +
                                        '<div class="radio"> ' +
                                            '<label> ' +
                                                '<input type="radio" name="optionsRadios" id="optionsRadios1" value="teacher" ng-model="address"> ' +
                                                    'Tomar clase en el domicilio del profesor' +
                                            ' </label> ' +
                                        ' </div> ' +
                                        ' <div class="radio"> ' +
                                            ' <label> ' +
                                                ' <input type="radio" name="optionsRadios" id="optionsRadios2" value="student" ng-model="address"> ' +
                                                    'Tomar clases en mi domicilio' +
                                            ' </label> ' +
                                        ' </div> ' +
                                        ' <div class="radio"> ' +
                                            ' <label> ' +
                                                ' <input type="radio" name="optionsRadios" id="optionsRadios3" value="unknow" ng-model="address"> ' +
                                                    'El domicilio se pactará despues' +
                                            ' </label> ' +
                                        ' </div> ' +
                                    '</div> ' +
                                    '<div ng-show="addressBool" class="callout callout-danger">' +
                                        '<p>Campo requerido</p>' +
                                    '</div>' +

                                    '</div>' +
                                    '<div style="border-color: #ddd; border-style: solid; border-width: 1px; margin-bottom: 10px;">'+
                                        '<div class="box-body" id="calendar{{index}}" style="padding: 0px"></div><!-- /.box-body -->' +
                                    '</div>' +

                                    '<div class="input-group">' +
                                        '<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>' +
                                        '<input style="width: 100%;height: 28px;" id="datepicker{{index}}" readonly>' +
                                    '</div>'+
                                    '<div ng-show="dateBool" class="callout callout-danger"">' +
                                        '<p>Campo requerido</p>' +
                                    '</div>' +
                                    '<label>Duración (horas)</label>' +
                                    '<select class="form-control" ng-model="duration">'+
                                        '<option ng-repeat="i in durations track by $index" value="{{i}}">{{i}}</option>'+
                                    '</select>'+
                                    '<div ng-show="durationBool" class="callout callout-danger">' +
                                        '<p>Campo requerido</p>' +
                                    '</div>' +
                                    '<div>'+
                                        '<div class="form-group">' +
                                            '<label>Comentario</label>' +
                                            '<textarea class="form-control" rows="3" placeholder="Dejar comentario ..." ng-model="comment"></textarea>' +
                                        '</div>' +
                                    '</div><!-- /.box-body -->' +
                                    '<button ng-click="postLesson(teacherId, dateTime, duration, address, selected, index)" class="btn btn-primary">Enviar</button>' +
                            '</div><!-- /.box -->' +
                        '</div>' +
                        '<div class="modal-footer">' +
                            '<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>' +
                        '</div>' +
                    '</div>' +
                '</div>'+
            '</div>' +

            '<div class="modal" id="loadingModal">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-body">' +
            '<p>Espere unos segundos....</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-default pull-left" data-dismiss="modal">Cerrar</button>' +
            '</div>' +
            '</div><!-- /.modal-content -->' +
            '</div><!-- /.modal-dialog -->' +
            '</div>' +

            '<div class="modal" id="succesModal">' +
                '<div class="modal-dialog">' +
                    '<div class="modal-content">' +
                        '<div class="modal-header">' +
                            '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>' +
                            '<h4 class="modal-title">Clase en proceso!</h4>' +
                        '</div>' +
                        '<div class="modal-body">' +
                            '<p>Lo notificamos al profesor, te haremos saber si aceptó o rechazó la clase!</p>' +
                        '</div>' +
                        '<div class="modal-footer">' +
                            '<button type="button" class="btn btn-default pull-left" data-dismiss="modal">Cerrar</button>' +
                        '</div>' +
                    '</div><!-- /.modal-content -->' +
                '</div><!-- /.modal-dialog -->' +
            '</div>' +

            '<div class="modal" id="errorModal">' +
                '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>' +
            '<h4 class="modal-title">Ups!</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>No se pudo contratar la clase, volvé a intentarlo</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-default pull-left" data-dismiss="modal">Cerrar</button>' +
            '</div>' +
            '</div><!-- /.modal-content -->' +
            '</div><!-- /.modal-dialog -->' +
            '</div>'
        }
    }]);