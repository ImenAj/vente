(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livraisonDialogController', Bons_de_livraisonDialogController);

    Bons_de_livraisonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Bons_de_livraison', 'Article', 'Commande'];

    function Bons_de_livraisonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Bons_de_livraison, Article, Commande) {
        var vm = this;

        vm.bons_de_livraison = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.articles = Article.query();
        vm.commandes = Commande.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bons_de_livraison.id !== null) {
                Bons_de_livraison.update(vm.bons_de_livraison, onSaveSuccess, onSaveError);
            } else {
                Bons_de_livraison.save(vm.bons_de_livraison, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:bons_de_livraisonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date_livraison = false;

        vm.setPiece_jointe = function ($file, bons_de_livraison) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        bons_de_livraison.piece_jointe = base64Data;
                        bons_de_livraison.piece_jointeContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
