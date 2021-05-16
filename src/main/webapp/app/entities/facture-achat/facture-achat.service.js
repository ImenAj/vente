(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Facture_achat', Facture_achat);

    Facture_achat.$inject = ['$resource', 'DateUtils'];

    function Facture_achat ($resource, DateUtils) {
        var resourceUrl =  'api/facture-achats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_facture = DateUtils.convertLocalDateFromServer(data.date_facture);
                        data.echeance = DateUtils.convertLocalDateFromServer(data.echeance);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_facture = DateUtils.convertLocalDateToServer(copy.date_facture);
                    copy.echeance = DateUtils.convertLocalDateToServer(copy.echeance);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_facture = DateUtils.convertLocalDateToServer(copy.date_facture);
                    copy.echeance = DateUtils.convertLocalDateToServer(copy.echeance);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
