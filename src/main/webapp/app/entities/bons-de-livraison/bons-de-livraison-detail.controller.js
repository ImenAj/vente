(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livraisonDetailController', Bons_de_livraisonDetailController);

    Bons_de_livraisonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Bons_de_livraison', 'Article', 'Commande'];

    function Bons_de_livraisonDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Bons_de_livraison, Article, Commande) {
        var vm = this;

        vm.bons_de_livraison = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pfeApp:bons_de_livraisonUpdate', function(event, result) {
            vm.bons_de_livraison = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
