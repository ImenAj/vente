(function() {
    'use strict';

    angular
        .module('pfeApp')
        .factory('Mode_de_paimentSearch', Mode_de_paimentSearch);

    Mode_de_paimentSearch.$inject = ['$resource'];

    function Mode_de_paimentSearch($resource) {
        var resourceUrl =  'api/_search/mode-de-paiments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
