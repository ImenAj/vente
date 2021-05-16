(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('SousFamilleDepenseDialogController', SousFamilleDepenseDialogController);

    SousFamilleDepenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SousFamilleDepense', 'FamilleDepense'];

    function SousFamilleDepenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SousFamilleDepense, FamilleDepense) {
        var vm = this;

        vm.sousFamilleDepense = entity;
        vm.clear = clear;
        vm.save = save;
        vm.familledepenses = FamilleDepense.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sousFamilleDepense.id !== null) {
                SousFamilleDepense.update(vm.sousFamilleDepense, onSaveSuccess, onSaveError);
            } else {
                SousFamilleDepense.save(vm.sousFamilleDepense, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:sousFamilleDepenseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
