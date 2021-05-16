(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Avoirs', Avoirs);

    Avoirs.$inject = ['$resource'];

    function Avoirs ($resource) {
        var resourceUrl =  'api/avoirs/:id';

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
