(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('AvoirsDialogController', AvoirsDialogController);

    AvoirsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Avoirs', 'Facture'];

    function AvoirsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Avoirs, Facture) {
        var vm = this;

        vm.avoirs = entity;
        vm.clear = clear;
        vm.save = save;
        vm.factures = Facture.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.avoirs.id !== null) {
                Avoirs.update(vm.avoirs, onSaveSuccess, onSaveError);
            } else {
                Avoirs.save(vm.avoirs, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:avoirsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
