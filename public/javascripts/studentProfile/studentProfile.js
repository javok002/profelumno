angular.module('studentProfile', [])

    .controller('StudentCtrl',['$scope', '$http', function($scope, $http) {
        $scope.data = '';
        $scope.classes = ['danger', 'info', 'warning', 'success'];

        $http.get('/student-profile/get-info', config).then(successCallback);

        succesCallback = function(response) {
            $scope.data = response.data;
        };
    }])

    .directive('subjects', function() {
        return {
            restrict: 'E',
            scope : {
                data : '=data'
            },
            link : function (scope){
               var i = 1;
            },
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
                                '<div ng-repeat="subject in data.subjects">' +
                                    '<div class="callout callout-{{classes[$index % 4]}}">' +
                                        '<h4>{{ subject.text }}</h4>' +
                                    '</div>' +
                                '</div>' +
                            '</div> <!-- /.box-body -->' +
                        '</div>' +
                    '</div>' +
                '</div> <!-- /.row -->' +
            '</section> <!-- /.content -->'

        }
    });
