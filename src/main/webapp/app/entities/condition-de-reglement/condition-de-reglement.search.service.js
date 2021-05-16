(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Condition_de_reglementSearch', Condition_de_reglementSearch);

    Condition_de_reglementSearch.$inject = ['$resource'];

    function Condition_de_reglementSearch($resource) {
        var resourceUrl =  'api/_search/condition-de-reglements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
