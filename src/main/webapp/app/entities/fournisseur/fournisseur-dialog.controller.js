(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FournisseurDialogController', FournisseurDialogController);

    FournisseurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fournisseur'];

    function FournisseurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fournisseur) {
        var vm = this;

        vm.fournisseur = entity;
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
            if (vm.fournisseur.id !== null) {
                Fournisseur.update(vm.fournisseur, onSaveSuccess, onSaveError);
            } else {
                Fournisseur.save(vm.fournisseur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:fournisseurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
