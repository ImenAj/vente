(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('SousFamilleDepense', SousFamilleDepense);

    SousFamilleDepense.$inject = ['$resource'];

    function SousFamilleDepense ($resource) {
        var resourceUrl =  'api/sous-famille-depenses/:id';

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
