(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('AvoirsSearch', AvoirsSearch);

    AvoirsSearch.$inject = ['$resource'];

    function AvoirsSearch($resource) {
        var resourceUrl =  'api/_search/avoirs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
