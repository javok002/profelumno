/**
 * Created by tombatto on 14/09/15.
 */
var app=angular.module( 'EditForm', ['ngTagsInput'] );

app.controller("EditController", ['$http','$scope',function($http,$scope){
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

app.directive('googleplace', function() {
    return {
        require : 'ngModel',
        link : function(scope, element, attrs, model) {
            var options = {
                types : [],
            };
            scope.gPlace = new google.maps.places.Autocomplete(element[0],
                options);

            google.maps.event.addListener(scope.gPlace, 'place_changed',
                function() {
                    scope.$apply(function() {
                        model.$setViewValue(element.val());
                    });
                });
        }
    };
});

app.directive('googleplace', [ function() {
    return {
        require: 'ngModel',
        scope: {
            ngModel: '=',
            details: '=?'
        },
        link: function(scope, element, attrs, model) {
            var options = {
                types: ['(cities)'],
                componentRestrictions: {}
            };

            scope.gPlace = new google.maps.places.Autocomplete(element[0], options);

            google.maps.event.addListener(scope.gPlace, 'place_changed', function() {
                var geoComponents = scope.gPlace.getPlace();
                var latitude = geoComponents.geometry.location.A;
                var longitude = geoComponents.geometry.location.F
                var addressComponents = geoComponents.address_components;

                addressComponents = addressComponents.filter(function(component){
                    switch (component.types[0]) {
                        case "locality": // city
                            return true;
                        case "administrative_area_level_1": // state
                            return true;
                        case "country": // country
                            return true;
                        default:
                            return false;
                    }
                }).map(function(obj) {
                    return obj.long_name;
                });

                addressComponents.push(latitude, longitude);

                scope.$apply(function() {
                    scope.details = addressComponents; // array containing each location component
                    model.$setViewValue(element.val());
                });
            });
        }
    };
}]);
