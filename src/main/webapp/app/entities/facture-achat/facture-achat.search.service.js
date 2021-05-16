(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Facture_achatSearch', Facture_achatSearch);

    Facture_achatSearch.$inject = ['$resource'];

    function Facture_achatSearch($resource) {
        var resourceUrl =  'api/_search/facture-achats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
