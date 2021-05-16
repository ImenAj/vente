(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Condition_de_reglementDialogController', Condition_de_reglementDialogController);

    Condition_de_reglementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Condition_de_reglement'];

    function Condition_de_reglementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Condition_de_reglement) {
        var vm = this;

        vm.condition_de_reglement = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.condition_de_reglement.id !== null) {
                Condition_de_reglement.update(vm.condition_de_reglement, onSaveSuccess, onSaveError);
            } else {
                Condition_de_reglement.save(vm.condition_de_reglement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:condition_de_reglementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
