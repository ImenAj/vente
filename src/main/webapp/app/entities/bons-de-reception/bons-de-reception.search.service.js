(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Bons_de_ReceptionSearch', Bons_de_ReceptionSearch);

    Bons_de_ReceptionSearch.$inject = ['$resource'];

    function Bons_de_ReceptionSearch($resource) {
        var resourceUrl =  'api/_search/bons-de-receptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
