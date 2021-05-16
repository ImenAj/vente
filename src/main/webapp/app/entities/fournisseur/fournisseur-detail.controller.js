(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FournisseurDetailController', FournisseurDetailController);

    FournisseurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Fournisseur'];

    function FournisseurDetailController($scope, $rootScope, $stateParams, previousState, entity, Fournisseur) {
        var vm = this;

        vm.fournisseur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:fournisseurUpdate', function(event, result) {
            vm.fournisseur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
