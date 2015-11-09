/**
 * Created by Paca on 9/19/15.
 */
angular.module('profLesson', [])

    .controller('HireCtrl',['$scope', '$http', function($scope, $http) {
        $scope.comment = '';
        $scope.lessonDate= '';
        $scope.selected= '';

        $scope.setSubjects = function (teacherId) {
            var subs = {};
            $http.get('/hire-lesson/get-subjects?teacherId='+teacherId)
                .success(function (data) {
                    subs = data;
                });
            return subs;
        };
        $scope.selected = {};
        $scope.postLesson = function(teacherId, dateTime) {
            data = {
                address:$scope.address,
                comment:$scope.comment,
                teacherId:teacherId,
                subjectId:$scope.selected,
                dateTime:dateTime
            };
            $http.post('/hire-lesson/new', data).then(successCallback, errorCallback);
            $("#loadingModal").modal("show");
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
                teacherSubs: '='
            },
            link : function(scope){
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

                    $http.get("/calendar/get-lessons")
                        .success(function (data) {
                            data.forEach(function(lesson) {
                                var teacher = lesson.teacher.user;
                                lesson.title = lesson.subject.text /*+ ' a ' + student.name + ' ' + student.surname*/;
                                lesson.date = new Date(lesson.dateTime);
                                lesson.end = angular.copy(lesson.date).addHours(lesson.duration.seconds / 3600);
                                lesson.lessonAcepted = true;
                            });

                            scope.data = data;

                            $('#calendar' + calendarIndex ).fullCalendar({
                                lang: 'es',
                                header: {
                                    left: 'today',
                                    center: 'prev,next',
                                    right: 'month,agendaWeek'
                                },
                                eventClick: function (event, jsEvent, view) {
                                    scope.dateTime = new Date(event.dateTime);
                                    if (!event.lessonAcepted) $('#datepicker' + scope.index).val(toDateTimeString(scope.dateTime));
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
                                    '<label for="selectSub">Seleccione una materia:</label>' +
                                    '<select class="form-control" id="selectSub" ng-model="selected">'+
                                       '<option ng-repeat="subject in teacherSubs" value="{{subject.id}}">{{subject.text}}</option>'+
                                        '</select>'+
                                    '<div class="form-group">' +
                                        '<div class="radio"> ' +
                                            '<label> ' +
                                                '<input type="radio" name="optionsRadios" id="optionsRadios1" value="teacher" ng-model="address" ng-required="!address"> ' +
                                                    'Tomar clase en el domicilio del profesor' +
                                            ' </label> ' +
                                        ' </div> ' +
                                        ' <div class="radio"> ' +
                                            ' <label> ' +
                                                ' <input type="radio" name="optionsRadios" id="optionsRadios2" value="student" ng-model="address" ng-required="!address"> ' +
                                                    'Tomar clases en mi domicilio' +
                                            ' </label> ' +
                                        ' </div> ' +
                                        ' <div class="radio"> ' +
                                            ' <label> ' +
                                                ' <input type="radio" name="optionsRadios" id="optionsRadios3" value="unknow" ng-model="address" ng-required="!address"> ' +
                                                    'El domicilio se pactará despues' +
                                            ' </label> ' +
                                        ' </div> ' +
                                    '</div> ' +
                                    '<div style="border-color: #ddd; border-style: solid; border-width: 1px; margin-bottom: 10px;">'+
                                        '<div class="box-body" id="calendar{{index}}" style="padding: 0px"></div><!-- /.box-body -->' +
                                    '</div>' +

                                    '<div class="input-group">' +
                                        '<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>' +
                                        '<input style="width: 100%;height: 28px;" id="datepicker{{index}}" ng-model="dateTime" value="">' +
                                    '</div>'+
                                        '<div class="form-group">' +
                                            '<label>Comentario</label>' +
                                            '<textarea class="form-control" rows="3" placeholder="Dejar comentario ..." ng-model="comment"></textarea>' +
                                        '</div>' +
                                    '</div><!-- /.box-body -->' +
                                    '<button ng-click="postLesson(teacherId, dateTime)" class="btn btn-primary" data-dismiss="modal">Enviar</button>' +
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