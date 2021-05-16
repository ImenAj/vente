(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FactureDetailController', FactureDetailController);

    FactureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Facture', 'Client', 'Bons_de_livraison'];

    function FactureDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Facture, Client, Bons_de_livraison) {
        var vm = this;

        vm.facture = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pfeApp:factureUpdate', function(event, result) {
            vm.facture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
