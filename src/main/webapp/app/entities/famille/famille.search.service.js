(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('FamilleSearch', FamilleSearch);

    FamilleSearch.$inject = ['$resource'];

    function FamilleSearch($resource) {
        var resourceUrl =  'api/_search/familles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
