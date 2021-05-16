(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Document_articleSearch', Document_articleSearch);

    Document_articleSearch.$inject = ['$resource'];

    function Document_articleSearch($resource) {
        var resourceUrl =  'api/_search/document-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
