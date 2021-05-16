(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Document_article', Document_article);

    Document_article.$inject = ['$resource'];

    function Document_article ($resource) {
        var resourceUrl =  'api/document-articles/:id';

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
