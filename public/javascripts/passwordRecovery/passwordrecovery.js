var app = angular.module("PasswordRecovery", []);

app.controller('recoveryCtrl', function($scope, $http) {
    $scope.user = {mail: "", question: ""};
    $scope.validMail = false;
    $scope.secureAnswer = "";

    $scope.validate = function() {
        validating();
        if(this.user.mail.length != 0 && this.user.question.length == 0){
            $http.post("/password-recovery/validate-email?email=" + $scope.user.mail).success(function(response){
                validationFinished();
                $scope.user = response;
                if($scope.user.question.length != 0){
                    $scope.validMail = true;
                } else if ($("#warning").children().length == 0){
                    showWarning();
                }
            });
        } else if (this.user.mail.length != 0 && this.secureAnswer.length != 0){
            $http.post("/password-recovery/validate-personal-info?email=" + $scope.user.mail + "&answer=" + $scope.secureAnswer).success(function(response){
                validationFinished();
                if(response.ok == true){
                    $('#infoModal').modal('show');
                } else if ($("#warning").children().length == 0){
                    showWarning();
                }
            });
        } else if ($("#warning").children().length == 0){
            validationFinished();
            showWarning();
        }
    };

    $scope.close = function(){
        window.location.replace("/log/in");
    };
});

$(function(){
    $('input').iCheck({
        checkboxClass: 'icheckbox_square-blue',
        radioClass: 'iradio_square-blue',
        increaseArea: '20%' // optional
    });
});

var showWarning = function(){
    $("#warning").append("<div class='alert alert-warning alert-dismissible' role='alert'>" +
    "<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>" +
    "&times;</span></button><strong>Error!</strong> Información inválida</div>");
};

var validating = function () {
    var button = $('#next');
    button.disable(true);
    button.innerHTML = "Cargando ";

};

var validationFinished = function () {
    var button = $('#next');
    button.disable(false);
    button.innerHTML = "Continuar";
};
