/**
 * Created by rudy on 19/09/15.
 */
var app = angular.module('teacherModify', ['ngTagsInput']);
app.controller('TeacherInfoController', ['$http','$scope',function($scope, $http){
    edit=this;
    edit.u={};
    $http.get("user").
        success(function(data, status, headers, config) {
            edit.u= data;
        }).
        error(function(data, status, headers, config) {
            // log error
        });
    $scope.tags = [
        { text: 'Tag1' },
        { text: 'Tag2' },
        { text: 'Tag3' }
    ];
    edit.tags=$scope.tags;
    $scope.loadTags = function(query) {
        return [{text: 'Tag1'},{text: 'Tag2'},{text: 'Tag3'},{text: 'Tag4'},{text: 'Tag5'}]
    };


}]);

app.controller('ImageController', function($scope, $http){

    $scope.uploadFile = function (input) {
        alert("entro");
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {

                //Sets the Old Image to new New Image
                $('#photo-id').attr('src', e.target.result);

                //Create a canvas and draw image on Client Side to get the byte[] equivalent
                var canvas = document.createElement("canvas");
                var imageElement = document.createElement("img");

                imageElement.setAttribute('src', e.target.result);
                canvas.width = imageElement.width;
                canvas.height = imageElement.height;
                var context = canvas.getContext("2d");
                context.drawImage(imageElement, 0, 0);
                var base64Image = canvas.toDataURL("image/jpeg");

                //Removes the Data Type Prefix
                //And set the view model to the new value
                $scope.data.UserPhoto = base64Image.replace(/data:image\/jpeg;base64,/g, '');
            };

            //Renders Image on Page
            reader.readAsDataURL(input.files[0]);
        }
    };

});