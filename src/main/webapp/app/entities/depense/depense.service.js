(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Depense', Depense);

    Depense.$inject = ['$resource', 'DateUtils'];

    function Depense ($resource, DateUtils) {
        var resourceUrl =  'api/depenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_depense = DateUtils.convertLocalDateFromServer(data.date_depense);
                        data.date_echeance = DateUtils.convertLocalDateFromServer(data.date_echeance);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_depense = DateUtils.convertLocalDateToServer(copy.date_depense);
                    copy.date_echeance = DateUtils.convertLocalDateToServer(copy.date_echeance);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_depense = DateUtils.convertLocalDateToServer(copy.date_depense);
                    copy.date_echeance = DateUtils.convertLocalDateToServer(copy.date_echeance);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
