(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Facture', Facture);

    Facture.$inject = ['$resource', 'DateUtils'];

    function Facture ($resource, DateUtils) {
        var resourceUrl =  'api/factures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateFacture = DateUtils.convertLocalDateFromServer(data.dateFacture);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateFacture = DateUtils.convertLocalDateToServer(copy.dateFacture);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateFacture = DateUtils.convertLocalDateToServer(copy.dateFacture);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
