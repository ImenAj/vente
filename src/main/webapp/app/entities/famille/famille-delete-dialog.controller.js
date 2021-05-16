(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FamilleDeleteController',FamilleDeleteController);

    FamilleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Famille'];

    function FamilleDeleteController($uibModalInstance, entity, Famille) {
        var vm = this;

        vm.famille = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Famille.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
