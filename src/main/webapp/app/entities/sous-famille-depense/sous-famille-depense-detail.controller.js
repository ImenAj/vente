(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('SousFamilleDepenseDetailController', SousFamilleDepenseDetailController);

    SousFamilleDepenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SousFamilleDepense', 'FamilleDepense'];

    function SousFamilleDepenseDetailController($scope, $rootScope, $stateParams, previousState, entity, SousFamilleDepense, FamilleDepense) {
        var vm = this;

        vm.sousFamilleDepense = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:sousFamilleDepenseUpdate', function(event, result) {
            vm.sousFamilleDepense = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
