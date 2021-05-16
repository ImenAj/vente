(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Famille', Famille);

    Famille.$inject = ['$resource'];

    function Famille ($resource) {
        var resourceUrl =  'api/familles/:id';

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
