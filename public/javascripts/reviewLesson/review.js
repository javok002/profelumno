/**
 * Created by Gustavo on 28/9/15.
 */

(function(){
    var app = angular.module('lessonReview', []);

    app.controller('LessonReviewsController', function($scope, $http) {

        this.reviewedLessons = reviews.reviewed;
        this.nonReviewedLessons = reviews.nonReviewed;

        //Get the reviewed lessons
        $http.post("/review-lesson/reviewed-lessons").success(function(response){
            $scope.listReviewedLessons(response);
        });

            //Get the non reviewed lessons
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
            return reviews.nonReviewed[currentIndex].userToReview;
        };
        //List the reviewed lesson get it from the controller
        $scope.listReviewedLessons = function(rls){
            for(i = 0; i < rls.length; i++){
                reviews.reviewed[i].userReviewed = rls[i].userReviewed;
                reviews.reviewed[i].reviewDate = rls[i].reviewDate;
                reviews.reviewed[i].reviewComment = rls[i].reviewComment;
                reviews.reviewed[i].reviewStars = rls[i].reviewStars;
                reviews.reviewed[i].index = i;
            }
        };
        //List the non reviewed lesson get it from the controller
        $scope.listNonReviewedLessons = function(nrls){
            for(i = 0; i < nrls.length; i++){
                reviews.nonReviewed[i].teacherName = nrls[i].teacherName;
                reviews.nonReviewed[i].studentName = nrls[i].studentName;
                reviews.nonReviewed[i].lessonDate = nrls[i].lessonDate;
                reviews.nonReviewed[i].lessonDuration = nrls[i].lessonDuration;
                reviews.nonReviewed[i].lessonPrice = nrls[i].lessonPrice;
                reviews.nonReviewed[i].lessonId = nrls[i].lessonId;
                reviews.nonReviewed[i].userToReview = nrls[i].userToReview;
                reviews.nonReviewed[i].index = i;
            }
        };

        $scope.currentReview = {
            email: reviews.nonReviewed[currentIndex].userToReview,
            stars: 4,
            comment: null
        };

        $scope.postReview = function(){
            /*$http.post("/review-lesson/reviewed?comment=" + review.comment + "&stars=" + review.stars +"&toEmail=" + review.email + "&idLesson=" + review.id)
                .success(function(response){
                    window.location.replace("/");
                });*/
            alert("Email: " + this.currentReview.email + " Stars: " + this.currentReview.stars + " Comment: " + this.currentReview.comment);
        };

    });



    var countReviewed = 0;
    var countNonReviewed = 0;

    var reviews = {
        reviewed: [
            {   userReviewed: 'Here will appear the user name',
                reviewDate: "12/2/15",
                reviewComment:'Here will appear the comment' ,
                reviewStars: 3,
                index: countReviewed++
            },
            {   userReviewed: 'Here will appear the user name',
                reviewDate: "12/2/15",
                reviewComment:'Here will appear the comment' ,
                reviewStars: 3,
                index: countReviewed++
            }
        ],
        nonReviewed: [
            {   teacherName: 'user1@mail',
                studentName: 'user2@mail',
                lessonDate: '12/2/15',
                lessonDuration: '2',
                lessonPrice: '$23',
                lessonId: 'id',
                userToReview: 'userToRev@f',
                index: countNonReviewed++
            },
            {   teacherName: 'user1@mail',
                studentName: 'user2@mail',
                lessonDate: '12/2/15',
                lessonDuration: '2',
                lessonPrice: '$23',
                lessonId: 'id',
                userToReview: 'userToRev@f',
                index: countNonReviewed++
            }
        ]
    };

    var currentIndex = 0;

})();
