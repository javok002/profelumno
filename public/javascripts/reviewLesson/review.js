/**
 * Created by Gustavo on 28/9/15.
 */

(function(){
    var app = angular.module('lessonReview', []);

    app.controller('LessonReviewsController', function($scope, $http) {
        //Here a post is need in order to get the users reviews...
        //Get the reviewed lessons
        $http.post("/review-lesson/reviewed-lessons").success(function(response){
            $scope.reviewedLessonsAux = response;
            listReviewedLessons(response);
            
        });
        //Get the non reviewed lessons
        $http.post("/review-lesson/non-reviewed-lessons").success(function(response){
            listNonReviewedLessons(response);
        });

        this.reviewedLessons = reviews.reviewed;
        this.nonReviewedLessons = reviews.nonReviewed;

        /* Useful functions */
        //Used to know which review the user want to comment/review.
        $scope.setIndex = function(index){
            currentIndex = index;
        };
        //List the reviewed lesson get it from the controller
        $scope.listReviewedLessons = function(rls){
            for(i = 0; i < rls.length; i++){
               reviews.reviewed[i].user = rls[i].userReviewed; 
               reviews.reviewed[i].date = rls[i].reviewDate; 
               reviews.reviewed[i].comment = rls[i].reviewComment; 
               reviews.reviewed[i].stars = rls[i].reviewStars; 
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
        }
    });

    app.controller('MakeReviewController', function($scope, $http){
        this.nonReviewedLessons = reviews.nonReviewed;
        this.currentStars ='';

        $scope.review = {
            comment: "",
            stars: 3, //Default value, It must be change!!!
            email: this.nonReviewedLessons[currentIndex].user,
            id: this.nonReviewedLessons[currentIndex].lessonId
        };

        $scope.postReview = function(){
            $http.post("/review-lesson/reviewed?comment=" + review.comment + "&stars=" + review.stars +"&toEmail=" + review.email + "&idLesson=" + review.id)
                .success(function(response){
                    window.location.replace("/");
            });
        };

        /* Useful functions */

        $scope.setStars = function(stars){
            $scope.review.stars = stars;
        }
    });

    var countReviewed = 0;
    var countNonReviewed = 0;

    var reviews = {
        reviewed: [
            {   user: 'Here will appear the user name',
                date: "12/2/15",
                comment:'Here will appear the comment' ,
                stars: 3,
                index: countReviewed++
            },
            {   user: 'Here will appear the user name',
                date: "12/2/15",
                comment:'Here will appear the comment' ,
                stars: 3,
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
                userToReview: 'userToRev',
                index: countNonReviewed++
            },
            {   teacherName: 'user1@mail',
                studentName: 'user2@mail',
                lessonDate: '12/2/15',
                lessonDuration: '2',
                lessonPrice: '$23',
                lessonId: 'id',
                userToReview: 'userToRev',
                index: countNonReviewed++
            }
        ]
    };

    var currentIndex = 0;


})();
