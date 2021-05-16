(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('CommandeClientDialogController', CommandeClientDialogController);

    CommandeClientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CommandeClient'];

    function CommandeClientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CommandeClient) {
        var vm = this;

        vm.commandeClient = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.commandeClient.id !== null) {
                CommandeClient.update(vm.commandeClient, onSaveSuccess, onSaveError);
            } else {
                CommandeClient.save(vm.commandeClient, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:commandeClientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCommande = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
