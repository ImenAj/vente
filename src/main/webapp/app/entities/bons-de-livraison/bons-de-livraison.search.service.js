(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Bons_de_livraisonSearch', Bons_de_livraisonSearch);

    Bons_de_livraisonSearch.$inject = ['$resource'];

    function Bons_de_livraisonSearch($resource) {
        var resourceUrl =  'api/_search/bons-de-livraisons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
