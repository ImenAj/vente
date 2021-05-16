(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Bons_de_commandeSearch', Bons_de_commandeSearch);

    Bons_de_commandeSearch.$inject = ['$resource'];

    function Bons_de_commandeSearch($resource) {
        var resourceUrl =  'api/_search/bons-de-commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
