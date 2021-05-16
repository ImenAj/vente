(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Facture_achatDetailController', Facture_achatDetailController);

    Facture_achatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Facture_achat'];

    function Facture_achatDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Facture_achat) {
        var vm = this;

        vm.facture_achat = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pfeApp:facture_achatUpdate', function(event, result) {
            vm.facture_achat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
