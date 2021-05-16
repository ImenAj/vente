(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('DepenseDeleteController',DepenseDeleteController);

    DepenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Depense'];

    function DepenseDeleteController($uibModalInstance, entity, Depense) {
        var vm = this;

        vm.depense = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Depense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
