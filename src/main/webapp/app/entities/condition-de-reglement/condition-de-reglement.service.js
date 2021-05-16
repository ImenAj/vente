(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Condition_de_reglement', Condition_de_reglement);

    Condition_de_reglement.$inject = ['$resource'];

    function Condition_de_reglement ($resource) {
        var resourceUrl =  'api/condition-de-reglements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
