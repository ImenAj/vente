(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('FamilleDepenseDeleteController',FamilleDepenseDeleteController);

    FamilleDepenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'FamilleDepense'];

    function FamilleDepenseDeleteController($uibModalInstance, entity, FamilleDepense) {
        var vm = this;

        vm.familleDepense = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FamilleDepense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
