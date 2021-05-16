(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Reglement', Reglement);

    Reglement.$inject = ['$resource', 'DateUtils'];

    function Reglement ($resource, DateUtils) {
        var resourceUrl =  'api/reglements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_Reglement = DateUtils.convertLocalDateFromServer(data.date_Reglement);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_Reglement = DateUtils.convertLocalDateToServer(copy.date_Reglement);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_Reglement = DateUtils.convertLocalDateToServer(copy.date_Reglement);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
