(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('DepenseSearch', DepenseSearch);

    DepenseSearch.$inject = ['$resource'];

    function DepenseSearch($resource) {
        var resourceUrl =  'api/_search/depenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
