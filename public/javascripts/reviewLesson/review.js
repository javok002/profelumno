/**
 * Created by Gustavo on 28/9/15.
 */

(function(){
    var app = angular.module('lessonReview', []);

    app.controller('LessonReviewsController', function($scope) {
        this.reviewedLessons = reviews.reviewed;
        this.nonReviewedLessons = reviews.nonReviewed;


        /* Useful functions */
        //Used to know which review the user want to comment/review.
        $scope.setIndex = function(index){
            currentIndex = index;
        }
    });

    app.controller('MakeReviewController', function($scope){
        this.nonReviewedLessons = reviews.nonReviewed;
        this.currentStars ='';
        $scope.review = {
            email: this.nonReviewedLessons[currentIndex].user,
            stars: 3, //Default value, It must be change!!!
            comment: ""
        };

        $scope.postReview = function(){

            /*
            $http.post("/URL" + "Params").success(function(response){
               //Do something with th response!!!
            });
            */

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
                stars: 4,
                comment: 'Here will appear the comment',
                index: countReviewed++
            },
            {   user: 'Here will appear the user name',
                date: "12/2/15",
                stars: 4,
                comment: 'Here will appear the comment',
                index: countReviewed++
            }
        ],
        nonReviewed: [
            {   user: 'user1@mail',
                date: "12/2/15",
                stars: '',
                comment: '',
                index: countNonReviewed ++
            },
            {   user: 'user2@mail',
                date: "12/2/15",
                stars: '',
                comment: '',
                index: countNonReviewed++
            }
        ]
    };

    var currentIndex = 0;


})();
