(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('OperationDialogController', OperationDialogController);

    OperationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Operation', 'SousFamilleDepense'];

    function OperationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Operation, SousFamilleDepense) {
        var vm = this;

        vm.operation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sousfamilledepenses = SousFamilleDepense.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.operation.id !== null) {
                Operation.update(vm.operation, onSaveSuccess, onSaveError);
            } else {
                Operation.save(vm.operation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:operationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
