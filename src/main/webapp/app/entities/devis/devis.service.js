(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Devis', Devis);

    Devis.$inject = ['$resource', 'DateUtils'];

    function Devis ($resource, DateUtils) {
        var resourceUrl =  'api/devis/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_Devis = DateUtils.convertLocalDateFromServer(data.date_Devis);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_Devis = DateUtils.convertLocalDateToServer(copy.date_Devis);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_Devis = DateUtils.convertLocalDateToServer(copy.date_Devis);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
