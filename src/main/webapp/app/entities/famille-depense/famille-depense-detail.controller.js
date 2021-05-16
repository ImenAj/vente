(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FamilleDepenseDetailController', FamilleDepenseDetailController);

    FamilleDepenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FamilleDepense'];

    function FamilleDepenseDetailController($scope, $rootScope, $stateParams, previousState, entity, FamilleDepense) {
        var vm = this;

        vm.familleDepense = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:familleDepenseUpdate', function(event, result) {
            vm.familleDepense = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
