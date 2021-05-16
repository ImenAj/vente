(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('SousFamilleDepenseSearch', SousFamilleDepenseSearch);

    SousFamilleDepenseSearch.$inject = ['$resource'];

    function SousFamilleDepenseSearch($resource) {
        var resourceUrl =  'api/_search/sous-famille-depenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
