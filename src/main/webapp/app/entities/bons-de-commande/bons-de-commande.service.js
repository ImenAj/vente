(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Bons_de_commande', Bons_de_commande);

    Bons_de_commande.$inject = ['$resource', 'DateUtils'];

    function Bons_de_commande ($resource, DateUtils) {
        var resourceUrl =  'api/bons-de-commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_commandeE = DateUtils.convertLocalDateFromServer(data.date_commandeE);
                        data.date_livraison_prevue = DateUtils.convertLocalDateFromServer(data.date_livraison_prevue);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_commandeE = DateUtils.convertLocalDateToServer(copy.date_commandeE);
                    copy.date_livraison_prevue = DateUtils.convertLocalDateToServer(copy.date_livraison_prevue);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_commandeE = DateUtils.convertLocalDateToServer(copy.date_commandeE);
                    copy.date_livraison_prevue = DateUtils.convertLocalDateToServer(copy.date_livraison_prevue);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
