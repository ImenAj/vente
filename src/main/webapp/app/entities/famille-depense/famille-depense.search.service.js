(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('FamilleDepenseSearch', FamilleDepenseSearch);

    FamilleDepenseSearch.$inject = ['$resource'];

    function FamilleDepenseSearch($resource) {
        var resourceUrl =  'api/_search/famille-depenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
