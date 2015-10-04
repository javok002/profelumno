angular.module('studentProfile', [])

    .controller('HireCtrl',['$scope', '$http', function($scope, $http) {
        var data;

        $http({
            method: 'GET',
            url: '/student-profile/get-info'
        }).then(function successCallback(response) {
            data = response.data;
        });

    }])

    .directive('subjects', function(){
        return {
            restrict: 'E',
            template:
                <!-- Main content -->
            '<section class="content">' +
                '<div class="row">' +
                    '<div class="col-md-4">' +
                        '<div class="box box-default">' +
                            '<div class="box-header with-border">' +
                                '<i class="fa fa-book"> </i>' +
                                '<h3 class="box-title"> Materias</h3>' +
                            '</div> <!-- /.box-header -->' +
                            '<div class="box-body">' +
                                '<div class="callout callout-danger">' +
                                    '<h4> I am a danger callout!</h4>' +
                                '</div>' +
                                '<div class="callout callout-info">' +
                                    '<h4> I am an info callout!</h4>' +
                                '</div>' +
                                '<div class="callout callout-warning">' +
                                    '<h4> I am a warning callout! </h4>' +
                                '</div>' +
                                '<div class="callout callout-success">' +
                                    '<h4> I am a success callout! </h4>' +
                                '</div>' +
                            '</div> <!-- /.box-body -->' +
                        '</div>' +
                    '</div>' +
                '</div> <!-- /.row -->' +
            '</section> <!-- /.content -->'

        }
    });
