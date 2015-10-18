/**
 * Created by Gustavo on 28/9/15.
 */


var app = angular.module('lessonReview', []);


app.controller('LessonReviewsController', function ($scope, $http) {

    this.reviewedLessons = reviews.reviewed;
    this.nonReviewedLessons = reviews.nonReviewed;
    this.reviewedLessonsMade = reviews.reviewedMade;

    //Reviewed lessons
    $http.post("/review-lesson/reviewed-lessons").success(function (response) {
        $scope.listReviewedLessons(response);
    });

    //Non reviewed lessons
    $http.post("/review-lesson/non-reviewed-lessons").success(function (response) {
        $scope.listNonReviewedLessons(response);
    });

    //Reviewed by me
    $http.post("/review-lesson/reviewed-lessons-by-me").success(function (response) {
        $scope.listReviewedLessonsByMe(response);
    });


    // Useful functions
    //Used to know which review the user want to comment/review.
    $scope.setIndex = function (index) {
        currentIndex = index;
        $('textarea').val("").removeClass('form-control ng-dirty ng-valid-parse ng-valid ng-valid-required ng-touched').addClass('form-control ng-pristine ng-untouched ng-invalid ng-invalid-required');
        $('#stars-id').rating('reset');
    };
    $scope.getIndex = function () {
        return currentIndex;
    };
    $scope.getCurrentEmail = function () {
        //return reviews.nonReviewed[currentIndex].userToReview;
        return reviews.nonReviewed[currentIndex].userToReview;
    };
    $scope.getCurrentId = function () {
        return reviews.nonReviewed[currentIndex].lessonId;
    };
    $scope.setStars = function (s) {
        currentStars = s;
    };
    $scope.getCurrentStars = function () {
        return currentStars;
    };
    //List the reviewed lesson get it from the controller
    $scope.listReviewedLessons = function (rls) {
        for (i = 0; i < rls.length; i++) {
            var auxObj = {
                userReviewed: rls[i].userReviewed,
                reviewDate: rls[i].reviewDate,
                reviewComment: rls[i].reviewComment,
                reviewStars: rls[i].reviewStars,
                index: i
            };
            if (reviews.reviewed[0].userReviewed == null) {
                reviews.reviewed[0].userReviewed = auxObj.userReviewed;
                reviews.reviewed[0].reviewDate = auxObj.reviewDate;
                reviews.reviewed[0].reviewComment = auxObj.reviewComment;
                reviews.reviewed[0].reviewStars = auxObj.reviewStars;
                reviews.reviewed[0].index = auxObj.index;
            }
            else {
                reviews.reviewed.push(auxObj);
            }

        }
    };
    //List the non reviewed lesson get it from the controller
    $scope.listNonReviewedLessons = function (nrls) {
        for (i = 0; i < nrls.length; i++) {
            var auxObj = {
                teacherName: nrls[i].teacherName,
                studentName: nrls[i].studentName,
                lessonDate: nrls[i].lessonDate,
                lessonDuration: nrls[i].lessonDuration,
                lessonPrice: nrls[i].lessonPrice,
                lessonId: nrls[i].lessonId,
                userToReview: nrls[i].userToReview,
                index: i
            };
            if (reviews.nonReviewed[0].teacherName == null) {
                reviews.nonReviewed[0].teacherName = auxObj.teacherName;
                reviews.nonReviewed[0].studentName = auxObj.studentName;
                reviews.nonReviewed[0].lessonDate = auxObj.lessonDate;
                reviews.nonReviewed[0].lessonDuration = auxObj.lessonDuration;
                reviews.nonReviewed[0].lessonPrice = auxObj.lessonPrice;
                reviews.nonReviewed[0].lessonId = auxObj.lessonId;
                reviews.nonReviewed[0].userToReview = auxObj.userToReview;
                reviews.nonReviewed[0].index = auxObj.index;
            }
            else {
                reviews.nonReviewed.push(auxObj);
            }
        }
    };
    //List the reviewed lesson get it from the controller
    $scope.listReviewedLessonsByMe = function (rls) {
        for (i = 0; i < rls.length; i++) {
            var auxObj = {
                userReviewed: rls[i].userReviewed,
                reviewDate: rls[i].reviewDate,
                reviewComment: rls[i].reviewComment,
                reviewStars: rls[i].reviewStars,
                index: i
            };
            if (reviews.reviewedMade[0].userReviewed == null) {
                reviews.reviewedMade[0].userReviewed = auxObj.userReviewed;
                reviews.reviewedMade[0].reviewDate = auxObj.reviewDate;
                reviews.reviewedMade[0].reviewComment = auxObj.reviewComment;
                reviews.reviewedMade[0].reviewStars = auxObj.reviewStars;
                reviews.reviewedMade[0].index = auxObj.index;
            }
            else {
                reviews.reviewedMade.push(auxObj);
            }

        }
    };

    this.currentReview = {
        comment: null
    };

    $scope.postReview = function () {
        var comment = this.currentReview.comment;
        var stars = parseInt($('#stars-id').val());
        var toEmail = this.getCurrentEmail();
        var idLesson = this.getCurrentId();


        $http.post("/review-lesson/review?comment=" + comment + "&stars=" + stars + "&toEmail=" + toEmail + "&idLesson=" + idLesson)
            .success(function (response) {
                window.location.replace("/review-lesson");
            });


        //alert("Comment: " + comment + " Stars: " + stars + " ToEmail: " + toEmail + " LessonId: " + idLesson);
    };


});


var currentIndex = 0;
var currentStars = 0;

var reviews = {
    reviewed: [
        {
            userReviewed: null,
            reviewData: null,
            reviewComment: null,
            reviewStars: null,
            index: 0
        }
    ],
    nonReviewed: [
        {
            teacherName: null,
            studentName: null,
            lessonDate: null,
            lessonDuration: null,
            lessonPrice: null,
            lessonId: null,
            userToReviewed: null,
            index: 0
        }
    ],
    reviewedMade: [
    {
        userReviewed: null,
        reviewData: null,
        reviewComment: null,
        reviewStars: null,
        index: 0
    }
    ]
};
