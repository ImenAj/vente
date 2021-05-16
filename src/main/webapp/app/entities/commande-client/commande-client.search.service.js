(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('CommandeClientSearch', CommandeClientSearch);

    CommandeClientSearch.$inject = ['$resource'];

    function CommandeClientSearch($resource) {
        var resourceUrl =  'api/_search/commande-clients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
