(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Facture_achatDialogController', Facture_achatDialogController);

    Facture_achatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Facture_achat'];

    function Facture_achatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Facture_achat) {
        var vm = this;

        vm.facture_achat = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.facture_achat.id !== null) {
                Facture_achat.update(vm.facture_achat, onSaveSuccess, onSaveError);
            } else {
                Facture_achat.save(vm.facture_achat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:facture_achatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date_facture = false;
        vm.datePickerOpenStatus.echeance = false;

        vm.setPiece_jointe = function ($file, facture_achat) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        facture_achat.piece_jointe = base64Data;
                        facture_achat.piece_jointeContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
