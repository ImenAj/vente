(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livrisonDialogController', Bons_de_livrisonDialogController);

    Bons_de_livrisonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bons_de_livrison', 'Condition_de_reglement', 'Client', 'Mode_de_paiment'];

    function Bons_de_livrisonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bons_de_livrison, Condition_de_reglement, Client, Mode_de_paiment) {
        var vm = this;

        vm.bons_de_livrison = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.condition_de_reglements = Condition_de_reglement.query();
        vm.clients = Client.query();
        vm.mode_de_paiments = Mode_de_paiment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bons_de_livrison.id !== null) {
                Bons_de_livrison.update(vm.bons_de_livrison, onSaveSuccess, onSaveError);
            } else {
                Bons_de_livrison.save(vm.bons_de_livrison, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:bons_de_livrisonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date_livraison = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
