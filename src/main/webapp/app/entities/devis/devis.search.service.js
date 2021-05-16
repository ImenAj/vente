(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('DevisSearch', DevisSearch);

    DevisSearch.$inject = ['$resource'];

    function DevisSearch($resource) {
        var resourceUrl =  'api/_search/devis/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
