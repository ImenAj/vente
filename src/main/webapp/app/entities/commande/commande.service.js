(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Commande', Commande);

    Commande.$inject = ['$resource', 'DateUtils'];

    function Commande ($resource, DateUtils) {
        var resourceUrl =  'api/commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_commande = DateUtils.convertDateTimeFromServer(data.date_commande);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
