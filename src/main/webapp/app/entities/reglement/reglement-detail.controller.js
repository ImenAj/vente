(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('ReglementDetailController', ReglementDetailController);

    ReglementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Reglement', 'Depense', 'Mode_de_paiment', 'Condition_de_reglement', 'Article', 'Facture'];

    function ReglementDetailController($scope, $rootScope, $stateParams, previousState, entity, Reglement, Depense, Mode_de_paiment, Condition_de_reglement, Article, Facture) {
        var vm = this;

        vm.reglement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:reglementUpdate', function(event, result) {
            vm.reglement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
