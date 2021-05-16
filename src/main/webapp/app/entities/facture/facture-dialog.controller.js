(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FactureDialogController', FactureDialogController);

    FactureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Facture', 'Client', 'Bons_de_livraison'];

    function FactureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Facture, Client, Bons_de_livraison) {
        var vm = this;

        vm.facture = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.clients = Client.query();
        vm.bons_de_livraisons = Bons_de_livraison.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.facture.id !== null) {
                Facture.update(vm.facture, onSaveSuccess, onSaveError);
            } else {
                Facture.save(vm.facture, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:factureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateFacture = false;

        vm.setPiece_jointe = function ($file, facture) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        facture.piece_jointe = base64Data;
                        facture.piece_jointeContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
