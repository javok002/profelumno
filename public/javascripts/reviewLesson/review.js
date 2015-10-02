/**
 * Created by Gustavo on 28/9/15.
 */

(function(){
    var app = angular.module('lessonReview', []);

    app.controller('LessonReviewController', function($scope) {
        this.reviewedLessons = reviewedLessons;
        this.nonReviewedLessons = nonReviewedLessons;

    });

    app.controller('MakeReviewController', function($scope){
        $scope.revEmail = "";
        $scope.revStars = 3; //DEFAULT VALUE IT MUST BE CHANGE!!!
        $scope.revComment = "";

        $scope.postReview = function(){
            //Posteamos la data


        };
    });

    app.controller('LessonReviewNotificationController', function($scope) {
        this.reviewedLessons = reviewedLessons;
        this.nonReviewedLessons = nonReviewedLessons;

    });

    var reviewedLessons = [
        {   user: 'Here will appear the user name',
            date: "12/2/15",
            stars: 4,
            comment: 'Here will appear the comment'},

        {   user: 'Here will appear the user name',
            date: "15/2/15",
            stars: 2,
            comment: 'Here will appear the comment'}
    ];
    var nonReviewedLessons = [
        {   user: 'Here will appear the user name',
            date: "12/2/15",
            stars: "",
            comment: ""},

        {   user: 'Here will appear the user name',
            date: "15/2/15",
            stars: "",
            comment: ""}
    ];

})();
