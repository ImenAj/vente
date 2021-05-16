(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Sous_familleDialogController', Sous_familleDialogController);

    Sous_familleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sous_famille', 'Famille'];

    function Sous_familleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sous_famille, Famille) {
        var vm = this;

        vm.sous_famille = entity;
        vm.clear = clear;
        vm.save = save;
        vm.familles = Famille.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sous_famille.id !== null) {
                Sous_famille.update(vm.sous_famille, onSaveSuccess, onSaveError);
            } else {
                Sous_famille.save(vm.sous_famille, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pfeApp:sous_familleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
