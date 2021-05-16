(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_ReceptionDialogController', Bons_de_ReceptionDialogController);

    Bons_de_ReceptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bons_de_Reception', 'Fournisseur', 'Condition_de_reglement'];

    function Bons_de_ReceptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bons_de_Reception, Fournisseur, Condition_de_reglement) {
        var vm = this;

        vm.bons_de_Reception = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.fournisseurs = Fournisseur.query();
        vm.condition_de_reglements = Condition_de_reglement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bons_de_Reception.id !== null) {
                Bons_de_Reception.update(vm.bons_de_Reception, onSaveSuccess, onSaveError);
            } else {
                Bons_de_Reception.save(vm.bons_de_Reception, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:bons_de_ReceptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateReception = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
