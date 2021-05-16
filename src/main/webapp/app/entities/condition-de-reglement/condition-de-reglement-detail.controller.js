(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Condition_de_reglementDetailController', Condition_de_reglementDetailController);

    Condition_de_reglementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Condition_de_reglement'];

    function Condition_de_reglementDetailController($scope, $rootScope, $stateParams, previousState, entity, Condition_de_reglement) {
        var vm = this;

        vm.condition_de_reglement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:condition_de_reglementUpdate', function(event, result) {
            vm.condition_de_reglement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
