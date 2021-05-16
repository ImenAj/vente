(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('CommandeDetailController', CommandeDetailController);

    CommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Commande', 'Mode_de_paiment'];

    function CommandeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Commande, Mode_de_paiment) {
        var vm = this;

        vm.commande = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pfeApp:commandeUpdate', function(event, result) {
            vm.commande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
