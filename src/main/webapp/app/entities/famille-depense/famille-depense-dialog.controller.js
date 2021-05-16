(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FamilleDepenseDialogController', FamilleDepenseDialogController);

    FamilleDepenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FamilleDepense'];

    function FamilleDepenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FamilleDepense) {
        var vm = this;

        vm.familleDepense = entity;
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
            if (vm.familleDepense.id !== null) {
                FamilleDepense.update(vm.familleDepense, onSaveSuccess, onSaveError);
            } else {
                FamilleDepense.save(vm.familleDepense, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:familleDepenseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
