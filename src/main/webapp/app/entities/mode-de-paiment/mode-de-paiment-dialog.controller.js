(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Mode_de_paimentDialogController', Mode_de_paimentDialogController);

    Mode_de_paimentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Mode_de_paiment'];

    function Mode_de_paimentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Mode_de_paiment) {
        var vm = this;

        vm.mode_de_paiment = entity;
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
            if (vm.mode_de_paiment.id !== null) {
                Mode_de_paiment.update(vm.mode_de_paiment, onSaveSuccess, onSaveError);
            } else {
                Mode_de_paiment.save(vm.mode_de_paiment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:mode_de_paimentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
