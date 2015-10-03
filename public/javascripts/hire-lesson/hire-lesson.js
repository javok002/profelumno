/**
 * Created by Paca on 9/19/15.
 */
angular.module('profLesson', [])

    .controller('HireCtrl',['$scope', '$http', function($scope, $http) {
        $scope.comment = '';
        $scope.address= '';

        $scope.postLesson = function() {
            data = {
                address:$scope.address,
                comment:$scope.comment,
                teacherId:$scope.teacherId,
                subjectId:$scope.subjectId
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
                teacherId : '=teacherId', //todo quien se encargue de buscar profesores tiene que setear este atributo y la subject
                subjectId : '='
            },
            link : function(scope){
                scope.date = 'date';
            },
            template:
            '<button type="button" class="btn btn-success" data-toggle="modal" data-target="#hire-modal">' +
                'Hire' +
            '</button>' +

            '<!-- Modal -->' +
            '<div class="modal fade" id="hire-modal" role="dialog">' +
                '<div class="modal-dialog">'+
                    '<!-- Modal content-->' +
                    '<div class="modal-content">' +
                        '<div class="modal-header">' +
                            '<button type="button" class="close" data-dismiss="modal">&times;</button>' +
                            '<h4 class="modal-title">Hire lesson</h4>' +
                        '</div>' +
                        '<div class="modal-body">' +
                            '<div ng-controller="HireCtrl">' +
                                '<div class="box-body">' +
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
                                    '<!-- Calendar --> ' +
                                    '<div class="box box-solid bg-blue-gradient"> ' +
                                        '<div class="box-header">' +
                                            '<i class="fa fa-calendar"></i> ' +
                                            '<h3 class="box-title">Calendar</h3> ' +
                                            '<!-- tools box --> ' +
                                            '<div class="pull-right box-tools"> ' +
                                                '<!-- button with a dropdown --> ' +
                                                '<button class="btn btn-success btn-sm bg-blue-gradient" data-widget="collapse"><i class="fa fa-minus"></i></button> ' +
                                            '</div><!-- /. tools --> ' +
                                        '</div><!-- /.box-header --> ' +
                                        '<div class="box-body no-padding"> ' +
                                            '<!--The calendar --> ' +
                                            '<div id="calendar" style="width: 100%">' +
                                            '</div> ' +
                                        '</div><!-- /.box-body --> ' +
                                    '</div><!-- /.box --> ' +


                                        '<div class="input-group">' +
                                            '<span class="input-group-addon"><i class="fa fa-calendar"></i></span>' +
                                            '<input type="email" class="form-control" placeholder="Fecha y horario" required="">' +
                                        '</div>' +
                                        '<div class="form-group">' +
                                            '<label>Textarea</label>' +
                                            '<textarea class="form-control" rows="3" placeholder="Enter ..." ng-model="comment"></textarea>' +
                                        '</div>' +
                                    '</div><!-- /.box-body -->' +
                                    '<button ng-click="postLesson()" class="btn btn-primary" data-dismiss="modal">Submit</button>' +
                            '</div><!-- /.box -->' +
                        '</div>' +
                        '<div class="modal-footer">' +
                            '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>' +
                        '</div>' +
                    '</div>' +
                '</div>'+
            '</div>'
        }
    });