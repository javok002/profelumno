/**
 * Created by Paca on 9/19/15.
 */
angular.module('profLesson', [])

    .controller('HireCtrl',['$scope', '$http', function($scope, $http) {
        $scope.comment = '';
        $scope.address= '';
        $scope.lessonDate= '';
        $scope.setSubjects = function (teacherId) {
            var subs = {};
            $http.get('/hire-lesson/get-subjects?teacherId='+teacherId)
                .success(function (data) {
                    subs = data;
                });
            return subs;
        };
        $scope.selected = {};
        $scope.postLesson = function(teacherId) {
            data = {
                address:$scope.address,
                comment:$scope.comment,
                teacherId:teacherId,
                subjectId:$scope.selected,
                dateTime:$scope.dateTime
            };
            $http.post('/hire-lesson/new', data).then(successCallback);
        };

        successCallback = function () {
            console.log('success!');
        };
    }])

    .directive('hireLesson', function(){
        return {
            restrict: 'E',
            scope : {
                teacherId : '=', //todo quien se encargue de buscar profesores tiene que setear este atributo y la subject
                index : '=',
                teacherSubs: '='
            },
            link : function(scope){
                scope.date = 'date';
            },
            template:
            '<button type="button" class="btn btn-success" data-toggle="modal" data-target="#hire-modal{{index}}">' +
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
                                    ' </div> ' +

                                    '<div class="input-group">' +
                                        '<!--<input type="text" class="form-control datepicker" datepicker="user.birthday" placeholder="Fecha de Nacimiento" name="birthday" required ng-model="user.birthday">' +
                                        '<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>--!>' +
                                        '<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>' +
                                        '<input ng-model="dateTime" type="date" class="form-control" name="birthday" required ng-model="user.birthday" ng-class="{&#39;default-option&#39;: user.birthday == undefined}">' +
                                    '</div>'+

                                        '<div class="input-group">' +
                                            '<span class="input-group-addon"><i class="fa fa-calendar"></i></span>' +
                                            '<input type="email" class="form-control" placeholder="Fecha y horario" required="">' +
                                        '</div>' +
                                        '<div class="form-group">' +
                                            '<label>Comentario</label>' +
                                            '<textarea class="form-control" rows="3" placeholder="Dejar comentario ..." ng-model="comment"></textarea>' +
                                        '</div>' +
                                    '</div><!-- /.box-body -->' +
                                    '<button ng-click="postLesson(teacherId)" class="btn btn-primary" data-dismiss="modal">Enviar</button>' +
                            '</div><!-- /.box -->' +
                        '</div>' +
                        '<div class="modal-footer">' +
                            '<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>' +
                        '</div>' +
                    '</div>' +
                '</div>'+
            '</div>'
        }
    });