(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('OperationDetailController', OperationDetailController);

    OperationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Operation', 'SousFamilleDepense'];

    function OperationDetailController($scope, $rootScope, $stateParams, previousState, entity, Operation, SousFamilleDepense) {
        var vm = this;

        vm.operation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pfeApp:operationUpdate', function(event, result) {
            vm.operation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
