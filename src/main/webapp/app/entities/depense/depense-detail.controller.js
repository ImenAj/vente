(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('DepenseDetailController', DepenseDetailController);

    DepenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Depense', 'Operation', 'Fournisseur', 'Compte', 'Mode_de_paiment'];

    function DepenseDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Depense, Operation, Fournisseur, Compte, Mode_de_paiment) {
        var vm = this;

        vm.depense = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pfeApp:depenseUpdate', function(event, result) {
            vm.depense = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
