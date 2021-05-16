(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('ReglementDialogController', ReglementDialogController);

    ReglementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Reglement', 'Depense', 'Mode_de_paiment', 'Condition_de_reglement', 'Article', 'Facture'];

    function ReglementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Reglement, Depense, Mode_de_paiment, Condition_de_reglement, Article, Facture) {
        var vm = this;

        vm.reglement = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.depenses = Depense.query();
        vm.mode_de_paiments = Mode_de_paiment.query();
        vm.condition_de_reglements = Condition_de_reglement.query();
        vm.articles = Article.query();
        vm.factures = Facture.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reglement.id !== null) {
                Reglement.update(vm.reglement, onSaveSuccess, onSaveError);
            } else {
                Reglement.save(vm.reglement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:reglementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date_Reglement = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
