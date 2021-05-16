(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('SousFamilleDepenseDeleteController',SousFamilleDepenseDeleteController);

    SousFamilleDepenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'SousFamilleDepense'];

    function SousFamilleDepenseDeleteController($uibModalInstance, entity, SousFamilleDepense) {
        var vm = this;

        vm.sousFamilleDepense = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SousFamilleDepense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
