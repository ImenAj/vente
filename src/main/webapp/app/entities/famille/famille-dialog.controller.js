(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FamilleDialogController', FamilleDialogController);

    FamilleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Famille'];

    function FamilleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Famille) {
        var vm = this;

        vm.famille = entity;
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
            if (vm.famille.id !== null) {
                Famille.update(vm.famille, onSaveSuccess, onSaveError);
            } else {
                Famille.save(vm.famille, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:familleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
