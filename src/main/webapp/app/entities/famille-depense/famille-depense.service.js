(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('FamilleDepense', FamilleDepense);

    FamilleDepense.$inject = ['$resource'];

    function FamilleDepense ($resource) {
        var resourceUrl =  'api/famille-depenses/:id';

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
