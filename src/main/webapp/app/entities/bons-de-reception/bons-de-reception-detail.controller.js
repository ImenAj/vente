(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_ReceptionDetailController', Bons_de_ReceptionDetailController);

    Bons_de_ReceptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bons_de_Reception', 'Fournisseur', 'Condition_de_reglement'];

    function Bons_de_ReceptionDetailController($scope, $rootScope, $stateParams, previousState, entity, Bons_de_Reception, Fournisseur, Condition_de_reglement) {
        var vm = this;

        vm.bons_de_Reception = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:bons_de_ReceptionUpdate', function(event, result) {
            vm.bons_de_Reception = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
