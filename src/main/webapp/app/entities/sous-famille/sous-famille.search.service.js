(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Sous_familleSearch', Sous_familleSearch);

    Sous_familleSearch.$inject = ['$resource'];

    function Sous_familleSearch($resource) {
        var resourceUrl =  'api/_search/sous-familles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
