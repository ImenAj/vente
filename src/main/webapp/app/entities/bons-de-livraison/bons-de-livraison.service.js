(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Bons_de_livraison', Bons_de_livraison);

    Bons_de_livraison.$inject = ['$resource', 'DateUtils'];

    function Bons_de_livraison ($resource, DateUtils) {
        var resourceUrl =  'api/bons-de-livraisons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_livraison = DateUtils.convertLocalDateFromServer(data.date_livraison);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_livraison = DateUtils.convertLocalDateToServer(copy.date_livraison);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_livraison = DateUtils.convertLocalDateToServer(copy.date_livraison);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
