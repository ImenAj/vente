(function() {
    'use strict';
    angular
        .module('pfeApp')
        .factory('Bons_de_Reception', Bons_de_Reception);

    Bons_de_Reception.$inject = ['$resource', 'DateUtils'];

    function Bons_de_Reception ($resource, DateUtils) {
        var resourceUrl =  'api/bons-de-receptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateReception = DateUtils.convertLocalDateFromServer(data.dateReception);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateReception = DateUtils.convertLocalDateToServer(copy.dateReception);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateReception = DateUtils.convertLocalDateToServer(copy.dateReception);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
