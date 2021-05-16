(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Sous_famille', Sous_famille);

    Sous_famille.$inject = ['$resource'];

    function Sous_famille ($resource) {
        var resourceUrl =  'api/sous-familles/:id';

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
