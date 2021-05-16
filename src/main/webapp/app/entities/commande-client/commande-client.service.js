(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('CommandeClient', CommandeClient);

    CommandeClient.$inject = ['$resource', 'DateUtils'];

    function CommandeClient ($resource, DateUtils) {
        var resourceUrl =  'api/commande-clients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCommande = DateUtils.convertLocalDateFromServer(data.dateCommande);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateCommande = DateUtils.convertLocalDateToServer(copy.dateCommande);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateCommande = DateUtils.convertLocalDateToServer(copy.dateCommande);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
