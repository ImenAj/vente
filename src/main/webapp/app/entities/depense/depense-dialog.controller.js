(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('DepenseDialogController', DepenseDialogController);

    DepenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Depense', 'Operation', 'Fournisseur', 'Compte', 'Mode_de_paiment'];

    function DepenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Depense, Operation, Fournisseur, Compte, Mode_de_paiment) {
        var vm = this;

        vm.depense = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.operations = Operation.query();
        vm.fournisseurs = Fournisseur.query();
        vm.comptes = Compte.query();
        vm.mode_de_paiments = Mode_de_paiment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.depense.id !== null) {
                Depense.update(vm.depense, onSaveSuccess, onSaveError);
            } else {
                Depense.save(vm.depense, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:depenseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date_depense = false;
        vm.datePickerOpenStatus.date_echeance = false;

        vm.setDocument = function ($file, depense) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        depense.document = base64Data;
                        depense.documentContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
