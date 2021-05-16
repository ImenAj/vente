(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Bons_de_livrisonSearch', Bons_de_livrisonSearch);

    Bons_de_livrisonSearch.$inject = ['$resource'];

    function Bons_de_livrisonSearch($resource) {
        var resourceUrl =  'api/_search/bons-de-livrisons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
