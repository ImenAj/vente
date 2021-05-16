(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Mode_de_paiment', Mode_de_paiment);

    Mode_de_paiment.$inject = ['$resource'];

    function Mode_de_paiment ($resource) {
        var resourceUrl =  'api/mode-de-paiments/:id';

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
