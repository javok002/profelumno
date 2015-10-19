angular.module('studentProfile', [])

    .controller('StudentCtrl',['$scope', '$http', function($scope, $http) {
        $scope.classes = ['danger', 'info', 'warning', 'success'];
        $http.get('/student-profile/get-info').then(function(data) {
            $scope.data = data.data;
        });

        $scope.redirect = function(){

        };
    }])

    .directive('subjects', function() {
        return {
            restrict: 'E',
            template:
            '<div class="row">' +
                '<div class="col-sm-12">' +
                    '<div class="box box-solid box-success">' +
                        '<div class="box-header">' +
                            '<h3 class="box-title">Materias</h3>' +
                            '<div class="box-tools pull-right">' +
                                '<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>' +
                            '</div><!-- /.box-tools -->' +
                        '</div><!-- /.box-header --> ' +
                        '<div class="box-body bg-gray-light"> ' +
                            '<span ng-show="data.Subjects.length == 0">No hay información disponible</span>' +
                            '<div class="col-md-4 col-sm-6" ng-repeat="subject in data.Subjects">' +
                                '<div class="info-box">' +
                                    '<!-- Apply any bg-* class to to the icon to color it -->' +
                                    '<span class="info-box-icon bg-aqua"><i class="fa fa-book"></i></span>'+
                                    '<div class="info-box-content">' +
                                        '<span class="info-box-text">{{ subject.text }}</span> ' +
                                    '</div><!-- /.info-box-content -->' +
                                '</div><!-- /.info-box -->' +
                            '</div>' +
                        '</div><!-- /.box-body -->' +
                    '</div><!-- /.box -->' +
                '</div>' +
            '</div>'
        }
    })

    .directive('lessonsToRate', function() {
        return {
            restrict: 'E',
            template:
            '<div class="row">' +
                '<div class="col-sm-12">' +
                    '<div class="box box-solid box-primary">' +
                        '<div class="box-header">' +
                            '<h3 class="box-title">Clases a calificar</h3>' +
                            '<div class="box-tools pull-right">' +
                                '<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>' +
                            '</div><!-- /.box-tools -->' +
                        '</div><!-- /.box-header -->' +
                        '<div class="box-body bg-gray-light">' +
                          '<span ng-show="Teacher.length == 0">No hay información disponible</span>' +
                            '<div class="col-md-4 col-sm-6" ng-repeat="lesson in data.LessonsNoRating">' +
                                '<div class="info-box">' +
                                    '<!-- Apply any bg-* class to to the icon to color it -->' +
                                    '<span class="info-box-icon bg-olive"><i class="fa fa-user"></i></span>' +
                                    '<div class="info-box-content">' +
                                        '<span class="info-box-text">Clase con {{ lesson.teacher.user.name}}</span>' +
                                        '<span class="info-box-text"></span>' +
                                        '<a href="/review-lesson">'+
                                            '<button type="button" class="btn btn-primary">Calificar</button>' +
                                        '</a>' +
                                    '</div><!-- /.info-box-content --> ' +
                                '</div><!--  /.info-box -->' +
                            '</div>' +
                        '</div><!-- /.box-body -->' +
                    '</div><!-- /.box -->' +
                '</div>' +
            '</div>'
        }
    })

    .directive('lessons', function() {
        return {
            restrict: 'E',
            template:
            '<div class="row">' +
                '<div class="col-sm-12">' +
                    '<div class="box box-solid box-primary">' +
                        '<div class="box-header">' +
                            '<h3 class="box-title">Clases pasadas</h3>' +
                            '<div class="box-tools pull-right">' +
                                '<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>' +
                            '</div><!-- /.box-tools -->' +
                        '</div><!-- /.box-header -->' +
                        '<div class="box-body bg-gray-light">' +
                            '<span ng-show="Teacher.length == 0">No hay información disponible</span>' +
                            '<div class="col-md-4 col-sm-6" ng-repeat="lesson in data.Lessons">' +
                                '<div class="info-box">' +
                                    '<!-- Apply any bg-* class to to the icon to color it -->' +
                                    '<span class="info-box-icon bg-olive"><i class="fa fa-user"></i></span>' +
                                    '<div class="info-box-content">' +
                                        '<span class="info-box-text">Clase con {{ lesson.teacher.user.name}}</span>' +
                                        '<span class="info-box-text">el {{ lesson.dateTime | date}}</span>' +
                                        '<span class="info-box-text">Comentario del profesor:</span>' +
                                        '<span class="info-box-text">{{ lesson.studentReview.comment }}</span>' +
                                    '</div><!-- /.info-box-content --> ' +
                                '</div><!--  /.info-box -->' +
                            '</div>' +
                        '</div><!-- /.box-body -->' +
                    '</div><!-- /.box -->' +
                '</div>' +
            '</div>'
        }
    })

    .directive('teachers', function() {
        return {
            restrict: 'E',
            template:
            '<div class="row">' +
                '<div class="col-sm-12">' +
                    '<div class="box box-solid box-primary">' +
                        '<div class="box-header">' +
                            '<h3 class="box-title">Profesores</h3>' +
                            '<div class="box-tools pull-right">' +
                                '<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>' +
                            '</div><!-- /.box-tools -->' +
                        '</div><!-- /.box-header -->' +
                        '<div class="box-body bg-gray-light">' +
                            '<span ng-show="Teacher.length == 0">No hay información disponible</span>' +
                            '<div class="col-md-4 col-sm-6" ng-repeat="teacher in data.Teachers">' +
                                '<div class="info-box">' +
                                    '<!-- Apply any bg-* class to to the icon to color it -->' +
                                    '<span class="info-box-icon bg-olive"><i class="fa fa-user"></i></span>' +
                                    '<div class="info-box-content">' +
                                        '<span class="info-box-text">{{ teacher.user.name}}</span>' +
                                        '<span class="info-box-text"></span>' +
                                    '</div><!-- /.info-box-content --> ' +
                                '</div><!-- /.info-box -->' +
                            '</div>' +
                        '</div><!-- /.box-body -->' +
                    '</div><!-- /.box -->' +
                '</div>' +
            '</div>'
        }
    });
