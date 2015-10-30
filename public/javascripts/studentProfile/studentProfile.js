var currentIndex;

angular.module('studentProfile', [])
    .controller('StudentCtrl',['$scope', '$http', function($scope, $http) {
        $scope.classes = ['danger', 'info', 'warning', 'success'];
        $http.get('/student-profile/get-info').then(function(data) {
            $scope.data = data.data;
        });
        $http.get('/student-profile/get-student').success(function(user) {
            $scope.user = user;
        }).error(function(){});

        $scope.redirect = function(){

        };

        $scope.times = function(num) {
            return new Array(num);
        };
        $scope.setModal = function(index){
            var prevLesson = $scope.data.LessonsNoRating[index];
            currentIndex = index;
            $('#emailModal').val(prevLesson.teacher.user.email);
            $('#commentModal').val("");
            $('#starsModal').rating('reset');
        };

        $scope.review = function(){
            var prevLesson = $scope.data.LessonsNoRating[currentIndex];
            var comment = $('#commentModal').val();
            var stars = parseInt($('#starsModal').val());
            var toEmail =  $('#emailModal').val();
            var idLesson = prevLesson.id;

            $http.post("/review-lesson/review?comment=" + comment + "&stars=" + stars + "&toEmail=" + toEmail + "&idLesson=" + idLesson)
                .success(function (response) {
                    window.location.replace("/student-profile/student-dashboard");
                });
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
                          '<span ng-show="data.LessonsNoRating.length == 0">No hay información disponible</span>' +
                            '<div class="col-md-4 col-sm-6" ng-repeat="lesson in data.LessonsNoRating">' +
                                '<div class="info-box">' +
                                    '<!-- Apply any bg-* class to to the icon to color it -->' +
                                    '<span class="info-box-icon bg-olive"><i class="fa fa-calendar-check-o"></i></span>' +
                                    '<div class="info-box-content">' +
                                        '<span style="font-size: 12px" class="info-box-text">Clase con {{ lesson.teacher.user.name}} {{ lesson.teacher.user.surname }}</span>' +
                                        '<span style="font-size: 12px" class="info-box-number">{{ lesson.dateString }}</span>' +
                                        '<span style="font-size: 12px" class="info-box-text"></span>' +
                                        '<button type="button" data-toggle="modal" data-target="#makeReviewModal" ng-click="setModal($index)" class="btn btn-primary">Calificar alumno</button>' +
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
                            '<h3 class="box-title">Clases Pasadas</h3>' +
                            '<div class="box-tools pull-right">' +
                                '<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>' +
                            '</div><!-- /.box-tools -->' +
                        '</div><!-- /.box-header -->' +
                        '<div class="box-body bg-gray-light">' +
                            '<span ng-show="data.Lessons.length == 0">No hay información disponible</span>' +
                            '<div class="col-md-4 col-sm-6" ng-repeat="lesson in data.Lessons">' +
                                '<div class="info-box">' +
                                    '<!-- Apply any bg-* class to to the icon to color it -->' +
                                    '<span class="info-box-icon bg-yellow"><i class="fa fa-calendar-check-o"></i></span>' +
                                    '<div class="info-box-content">' +
                                        '<span style="font-size: 12px" class="info-box-text">Clase con {{ lesson.teacher.user.name}} {{ lesson.teacher.user.surname }}</span>' +
                                        '<span style="font-size: 12px" class="info-box-number">{{ lesson.dateString }}</span>' +
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
                            '<h3 class="box-title">Ultimos profesores contactados</h3>' +
                            '<div class="box-tools pull-right">' +
                                '<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>' +
                            '</div><!-- /.box-tools -->' +
                        '</div><!-- /.box-header -->' +
                        '<div class="box-body bg-gray-light">' +
                            '<span ng-show="data.Teachers.length == 0">No hay información disponible</span>' +
                            '<div class="col-md-4 col-sm-6" ng-repeat="teacher in data.Teachers">' +
                                '<div class="info-box">' +
                                    '<!-- Apply any bg-* class to to the icon to color it -->' +
                                    '<span class="info-box-icon bg-olive"><i class="fa fa-user"></i></span>' +
                                    '<div class="info-box-content">' +
                                        '<span class="info-box-text">{{ teacher.user.name }} {{ teacher.user.surname }}</span>' +
                                        '<span class="info-box-number">' +
                                            '<i style="color: gold" class="fa fa-star" ng-repeat="i in times(teacher.ranking) track by $index"></i>' +
                                            '<i style="color: #FFF3B4" class="fa fa-star" ng-repeat="i in times(5 - teacher.ranking) track by $index"></i>' +
                                            ' {{ teacher.ranking }}' +
                                        '</span>' +
                                    '</div><!-- /.info-box-content --> ' +
                                '</div><!-- /.info-box -->' +
                            '</div>' +
                        '</div><!-- /.box-body -->' +
                    '</div><!-- /.box -->' +
                '</div>' +
            '</div>'
        }
    });
