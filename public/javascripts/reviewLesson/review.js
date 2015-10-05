/**
 * Created by Gustavo on 28/9/15.
 */

(function(){
    var app = angular.module('lessonReview', []);


    app.controller('LessonReviewsController', function($scope, $http) {

        this.reviewedLessons = reviews.reviewed;
        this.nonReviewedLessons = reviews.nonReviewed;

        $http.post("/review-lesson/reviewed-lessons").success(function(response){
            $scope.listReviewedLessons(response);
        });

        $http.post("/review-lesson/non-reviewed-lessons").success(function(response){
            $scope.listNonReviewedLessons(response);
        });


        // Useful functions
        //Used to know which review the user want to comment/review.
        $scope.setIndex = function(index){
            currentIndex = index;
        };
        $scope.getIndex = function(){
            return currentIndex;
        };
        $scope.getCurrentEmail = function(){
            //return reviews.nonReviewed[currentIndex].userToReview;
            return nonReviewed[currentIndex].userToReview;
        };
        //List the reviewed lesson get it from the controller
        $scope.listReviewedLessons = function(rls){
            for(i = 0; i < rls.length; i++){
                var auxObj = {
                    userReviewed: rls[i].userReviewed,
                    reviewDate: rls[i].reviewDate,
                    reviewComment: rls[i].reviewComment,
                    reviewStars: rls[i].reviewStars,
                    index: i+1
                };
                reviews.reviewed.push(auxObj);
            }
        };
        //List the non reviewed lesson get it from the controller
        $scope.listNonReviewedLessons = function(nrls){
            for(i = 0; i < nrls.length; i++){
                var auxObj = {
                    teacherName: nrls[i].teacherName,
                    studentName: nrls[i].studentName,
                    lessonDate: nrls[i].lessonDate,
                    lessonDuration: nrls[i].lessonDuration,
                    lessonPrice: nrls[i].lessonPrice,
                    lessonId: nrls[i].lessonId,
                    userToReview: nrls[i].userToReview,
                    index: i+1
                };
                reviews.nonReviewed.push(auxObj);
            }
        };

        this.currentReview = {
            toEmail: this.nonReviewedLessons[currentIndex].userToReviewed,
            stars: 4,
            comment: null
        };
        /*$scope.currentReview = {
            stars: 4,
            comment: null
        };
*/
        $scope.postReview = function(){
            var comment = this.currentReview.comment;
            var stars = 4; //Default!! change it!!!
            var toEmail = "user.student@gmail.com"; //Default!! change it!!!
            var idLesson = 6; //Default!! change it!!!
            /*
            $http.post("/review-lesson/review?comment=" + comment + "&stars=" + stars +"&toEmail=" + toEmail + "&idLesson=" + idLesson)
                .success(function(response){
                    window.location.replace("/");
                });*/
            //alert("Comment: " + comment + "user: " + toEmail)
        };


    });


    var currentIndex = 0;

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
        ]
    };

})();
