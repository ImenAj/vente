(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livrisonDetailController', Bons_de_livrisonDetailController);

    Bons_de_livrisonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bons_de_livrison', 'Condition_de_reglement', 'Client', 'Mode_de_paiment'];

    function Bons_de_livrisonDetailController($scope, $rootScope, $stateParams, previousState, entity, Bons_de_livrison, Condition_de_reglement, Client, Mode_de_paiment) {
        var vm = this;

        vm.bons_de_livrison = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:bons_de_livrisonUpdate', function(event, result) {
            vm.bons_de_livrison = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
