angular.module('studentProfile', [])

    .controller('StudentCtrl',['$scope', '$http', function($scope, $http) {
        $scope.classes = ['danger', 'info', 'warning', 'success'];
        $http.get('/student-profile/get-info').then(function(data) {
            $scope.data = data.data;
        });
    }])

    .directive('subjects', function() {
        return {
            restrict: 'E',
            template:
            '<div class="col-md-6">' +
                '<div class="box box-default">' +
                    '<div class="box-header with-border">' +
                        '<i class="fa fa-book"> </i>' +
                        '<h3 class="box-title"> Subjects</h3>' +
                    '</div> <!-- /.box-header -->' +
                    '<div class="box-body">' +
                        '<div ng-repeat="subject in data.Subjects">' +
                            '<div class="callout callout-{{classes[$index % 4]}}">' +
                                '<h4>{{ subject.text }}</h4>' +
                            '</div>' +
                        '</div>' +
                    '</div> <!-- /.box-body -->' +
                    '<div class="box-footer">' +
                        '<button class="btn-default"><a href="/modify-student/edit">Edit my subjects</a></button>' +
                    '</div>' +
                '</div>' +
            '</div>'
        }
    })

    .directive('teachers', function() {
        return {
            restrict: 'E',
            template:
            '<div class="col-md-6">' +
                '<div class="box box-default">' +
                    '<div class="box-header with-border">' +
                        '<i class="fa fa-user"> </i>' +
                        '<h3 class="box-title"> Teachers</h3>' +
                    '</div> <!-- /.box-header -->' +
                    '<div class="box-body">' +
                        '<div ng-repeat="teacher in data.Teachers">' +
                            '<div class="callout callout-{{classes[$index % 4]}}">' +
                                '<h4>{{ teacher }}</h4>' +
                            '</div>' +
                        '</div>' +
                    '</div> <!-- /.box-body -->' +
                '</div>' +
            '</div>'
        }
    });
