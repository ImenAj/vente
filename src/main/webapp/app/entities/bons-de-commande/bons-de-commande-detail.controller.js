(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_commandeDetailController', Bons_de_commandeDetailController);

    Bons_de_commandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bons_de_commande', 'Fournisseur'];

    function Bons_de_commandeDetailController($scope, $rootScope, $stateParams, previousState, entity, Bons_de_commande, Fournisseur) {
        var vm = this;

        vm.bons_de_commande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:bons_de_commandeUpdate', function(event, result) {
            vm.bons_de_commande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
