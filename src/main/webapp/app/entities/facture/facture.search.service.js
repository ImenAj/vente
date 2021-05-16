(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('FactureSearch', FactureSearch);

    FactureSearch.$inject = ['$resource'];

    function FactureSearch($resource) {
        var resourceUrl =  'api/_search/factures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
