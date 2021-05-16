(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_commandeDialogController', Bons_de_commandeDialogController);

    Bons_de_commandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bons_de_commande', 'Fournisseur'];

    function Bons_de_commandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bons_de_commande, Fournisseur) {
        var vm = this;

        vm.bons_de_commande = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.fournisseurs = Fournisseur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bons_de_commande.id !== null) {
                Bons_de_commande.update(vm.bons_de_commande, onSaveSuccess, onSaveError);
            } else {
                Bons_de_commande.save(vm.bons_de_commande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:bons_de_commandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date_commandeE = false;
        vm.datePickerOpenStatus.date_livraison_prevue = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
