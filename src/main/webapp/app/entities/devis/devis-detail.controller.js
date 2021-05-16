(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('DevisDetailController', DevisDetailController);

    DevisDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Devis', 'Client', 'Mode_de_paiment'];

    function DevisDetailController($scope, $rootScope, $stateParams, previousState, entity, Devis, Client, Mode_de_paiment) {
        var vm = this;

        vm.devis = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:devisUpdate', function(event, result) {
            vm.devis = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
